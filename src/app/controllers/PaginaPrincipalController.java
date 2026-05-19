  package app.controllers;

import app.Controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

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
    private ListView<?> lista;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    


    @FXML
    private void mapas(ActionEvent event) {
         changeScene("MapaDemo");
    }

    @FXML
    private void perfil(ActionEvent event) {
    }

    @FXML
    private void historialSesiones(ActionEvent event) {
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
         changeScene("CerrarSesion");
    }

    @FXML
    private void actividadNueva(ActionEvent event) {
    }
    
}
