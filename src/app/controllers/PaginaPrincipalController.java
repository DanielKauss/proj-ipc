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
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
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
    private Button perfil;
    @FXML
    private Button historial;
    @FXML
    private Button cerrar;
    @FXML
    private ListView<Activity> lista;
    
    SportActivityApp app = SportActivityApp.getInstance();
     
    private List<Activity> actividades = app.getUserActivities();
    
    private ObservableList<Activity> datos = null;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datos = FXCollections.observableList(actividades);
        lista.setItems(datos);
    }    


    @FXML
    private void mapas(ActionEvent event) {
         changeScene("GestionMapas");
    }

    @FXML
    private void perfil(ActionEvent event) {
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
    
}
