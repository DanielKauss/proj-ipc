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
    
    private boolean nickexiste = false;
    @FXML
    private TextField campoContraVisible;
    @FXML
    private Button buttonVisible;

    boolean visiblePassword = false;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorContra.setVisible(false);
        
        buttonLogIn.disableProperty().bind(Bindings.or
        (Bindings.lessThan(campoCorreo.lengthProperty(),1) ,(Bindings.lessThan(campoContra.lengthProperty(), 1))));
        
         campoContraVisible.textProperty()
        .bindBidirectional(
                campoContra.textProperty()
        );
    }    

    @FXML
    private void actionLogIn(ActionEvent event) {
        
        
        
       if(app.login(campoCorreo.getText(),campoContra.getText())){ 
        changeScene("PaginaPrincipal");
       }else{errorContra.setVisible(true);
       System.out.println(campoCorreo.getText());
        System.out.println(campoContra.getText());}
    }

    @FXML
    private void actionCancel(ActionEvent event) {
        changeScene("LandingPage");
    }

    @FXML
    private void mostrarContr(ActionEvent event) {
         
        visiblePassword = !visiblePassword;

        campoContra.setVisible(!visiblePassword);
        campoContra.setManaged(!visiblePassword);

        campoContraVisible.setVisible(visiblePassword);
        campoContraVisible.setManaged(visiblePassword);
    }
    
}
