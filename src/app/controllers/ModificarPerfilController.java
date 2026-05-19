/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package app.controllers;

import app.Controller;
import static app.Controller.changeScene;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import upv.ipc.sportlib.SportActivityApp;
import upv.ipc.sportlib.User;

/**
 * FXML Controller class
 *
 * @author apere
 */
public class ModificarPerfilController extends Controller  {

    @FXML
    private TextField campoNombre;
    @FXML
    private Label errorNombre;
    @FXML
    private Label errorContr;
    @FXML
    private Label errorCorreo;
    @FXML
    private Label errorFecha;
    @FXML
    private TextField campoCorreo;
    @FXML
    private DatePicker campoFecha;
    @FXML
    private PasswordField campoContr;
    
    String avatarPath = null;
    boolean visible = false;
    SportActivityApp app = SportActivityApp.getInstance();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorNombre.setVisible(false);
        errorCorreo.setVisible(false);
        errorContr.setVisible(false);
        errorFecha.setVisible(false);
        
        campoNombre.setText(app.getCurrentUser().getNickName());
        campoNombre.setEditable(false);
        
        campoContr.setText(app.getCurrentUser().getPassword());
        
        campoCorreo.setText(app.getCurrentUser().getEmail());
        
        campoFecha.setValue(app.getCurrentUser().getBirthDate());
        
        
    }    

    @FXML
    private void salir(ActionEvent event) {
        changeScene("LandingPage");
    }

    @FXML
    private void actionModificar (ActionEvent event) {
        boolean datosValidos = true;
        
        if (!User.checkEmail(campoCorreo.getText())) {
        errorCorreo.setVisible(true);
        datosValidos = false;
        } else {
            errorCorreo.setVisible(false);
        }

        if (!User.checkPassword(campoContr.getText())) {
            errorContr.setVisible(true);
            datosValidos = false;
        } else {
            errorContr.setVisible(false);
        }

        if (!User.isOlderThan(campoFecha.getValue(), 12)) {
            errorFecha.setVisible(true);
            datosValidos = false;
        } else {
            errorFecha.setVisible(false);
        }

        if (datosValidos) {
            app.updateCurrentUser(campoCorreo.getText(),
                    campoContr.getText(), campoFecha.getValue(), avatarPath);

            changeScene("PaginaPrincipal");
        }

        }

    @FXML
    private void subirAvatar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar foto de avatar");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Imágenes JPG y PNG","*.jpg","*.jpeg","*.png")
        );
        File archivo = fileChooser.showOpenDialog(null);

        if (archivo != null) {
            avatarPath = archivo.getAbsolutePath();
        }
    }

 
   
}