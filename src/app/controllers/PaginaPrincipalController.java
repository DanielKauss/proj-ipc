  package app.controllers;

import app.Controller;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import upv.ipc.sportlib.Activity;
import upv.ipc.sportlib.SportActivityApp;
import upv.ipc.sportlib.User;

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
        lista.setCellFactory(c-> new ActivityListCell());
        lista.setItems(datos);
        
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
    private void cerrarSesion(ActionEvent event) {
         changeScene("CerrarSesion");
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
            setText(item.getName() + " " + item.getDuration().toString() + " " + item.getEndTime().toString() + " " + item.getTotalDistance());
    }
}
  
    
}
