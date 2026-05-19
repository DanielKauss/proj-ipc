/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package app.controllers;

import static app.Controller.changeScene;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import upv.ipc.sportlib.SportActivityApp;

/**
 * FXML Controller class
 *
 * @author Daniel
 */
public class LogInController implements Initializable {

    @FXML
    private TextField campoCorreo;
    @FXML
    private TextField campoContra;
    @FXML
    private Button buttonLogIn;
    
    @FXML
    private Label errorContra;
    
    SportActivityApp app = SportActivityApp.getInstance();
    @FXML
    private Button cancel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorContra.setVisible(false);
        
        buttonLogIn.disableProperty().bind(Bindings.or
        (Bindings.lessThan(campoCorreo.lengthProperty(),1) ,(Bindings.lessThan(campoContra.lengthProperty(), 1))));
        
    }    

    @FXML
    private void actionLogIn(ActionEvent event) {
        
       if(app.login(campoCorreo.getText(),campoContra.getText())){ 
        changeScene("MapaDemo");
       }else{errorContra.setVisible(true);}
    }

    @FXML
    private void actionCancel(ActionEvent event) {
        changeScene("LandingPage");
    }
    
}
