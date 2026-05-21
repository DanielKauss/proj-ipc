/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package app.controllers;

import app.Controller;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import upv.ipc.sportlib.SportActivityApp;
import upv.ipc.sportlib.User;

/**
 * FXML Controller class
 *
 * @author apere
 */
public class RegistroController extends Controller  {

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
    
    boolean visiblePassword = false;
    
    SportActivityApp app = SportActivityApp.getInstance();
    @FXML
    private Circle avatarCircle;
    @FXML
    private ImageView avatarImage;
    @FXML
    private Button buttonEliminarAvatar;
    @FXML
    private TextField campoContrVisible;
    @FXML
    private Button buttonVisible;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorNombre.setVisible(false);
        errorCorreo.setVisible(false);
        errorContr.setVisible(false);
        errorFecha.setVisible(false);
        
        campoContrVisible.textProperty()
        .bindBidirectional(
                campoContr.textProperty()
        );
    }    

    @FXML
    private void salir(ActionEvent event) {
        changeScene("LandingPage");
    }

    @FXML
    private void registrarse(ActionEvent event) {
        boolean datosValidos = true;
    
    if (!User.checkNickName(campoNombre.getText())) {
        errorNombre.setText("El nombre de usuario debe ser entre 6 y 15 caracteres, solo letras, dígitos, guión o subguión");
        errorNombre.setVisible(true);
        datosValidos = false;
    } else if (app.nickNameExists(campoNombre.getText())) {
        errorNombre.setText("Este nombre de usuario ya está en uso");
        errorNombre.setVisible(true);
        datosValidos = false;
    } else {
        errorNombre.setVisible(false);
    }
    
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
        app.registerUser(campoNombre.getText(), campoCorreo.getText(),
                campoContr.getText(), campoFecha.getValue(), avatarPath);
        
        app.login(campoNombre.getText(), campoContr.getText()); 

        campoContrVisible.textProperty().unbindBidirectional(campoContr.textProperty());
        changeScene("PaginaPrincipal");
    }
}

    @FXML
    private void subirAvatar(MouseEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar foto de avatar");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Imágenes", "*.jpg", "*.jpeg", "*.png"
                )
        );

        File archivo = fileChooser.showOpenDialog(null);

        if (archivo != null) {

            avatarPath = archivo.getAbsolutePath();

            Image image =
                    new Image(archivo.toURI().toString());

            avatarImage.setImage(image);

            Circle clip = new Circle(55);
            clip.setCenterX(55);
            clip.setCenterY(55);

            avatarImage.setClip(clip);
        }
    }

    @FXML
    private void actionEliminarAvatar(ActionEvent event) {
        
        avatarPath = null;
        avatarImage.setImage(null);
    }

    @FXML
    private void mostrarContr(ActionEvent event) {
        
        visiblePassword = !visiblePassword;

        campoContr.setVisible(!visiblePassword);
        campoContr.setManaged(!visiblePassword);

        campoContrVisible.setVisible(visiblePassword);
        campoContrVisible.setManaged(visiblePassword);
    }
}
