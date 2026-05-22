  package app.controllers;

import app.Controller;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import upv.ipc.sportlib.Activity;
import upv.ipc.sportlib.SportActivityApp;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class PaginaPrincipalController extends Controller {

    @FXML
    private Button añadir;
    @FXML
    private Button mapas;
    @FXML
    private Button historial;
    @FXML
    private Button cerrar;
    @FXML
    private ListView<Activity> lista;
    
    SportActivityApp app = SportActivityApp.getInstance();
     
    
    private ObservableList<Activity> datos = null;
    @FXML
    private ImageView avatarImage;
    @FXML
    private Circle avatarCircle;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Activity> actividades = app.getUserActivities();
        datos = FXCollections.observableList(actividades);
        
        lista.setItems(datos);
        lista.setCellFactory(c-> new ActivityListCell());
        
         if (app.getCurrentUser().getAvatar() != null) {

                avatarImage.setImage(app.getCurrentUser().getAvatar());
                Circle clip = new Circle(55);
                clip.setCenterX(55);
                clip.setCenterY(55);

            avatarImage.setClip(clip);
            }
    }    


    @FXML
    private void mapas(ActionEvent event) {
         changeScene("GestionMapas");
    }

    @FXML
    private void historialSesiones(ActionEvent event) {
        changeScene("HistorialSesiones");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) throws IOException{
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/resources/fxml/CerrarSesion.fxml"));
        Parent root = miCargador.load();
        
        CerrarSesionController controlador2 = miCargador.getController();
        
        Scene scene = new Scene(root,600,400);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setTitle("Cerrar Sesion");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
        
    }

    @FXML
    private void actividadNueva(ActionEvent event) {
    }

    @FXML
    private void actionPerfil(MouseEvent event) {
        changeScene("ModificarPerfil");
    }
    class ActivityListCell extends ListCell<Activity> {
    
         @Override
         protected void updateItem(Activity item, boolean empty) {
            super.updateItem(item, empty);
            if(item==null||empty)setText(null);
            else{ 
                setText(item.getName() + ": Tiempo: " + item.getDuration().toString() + " Distancia: " + Math.round(item.getTotalDistance())+"m");
            }
        }
    }   
}
