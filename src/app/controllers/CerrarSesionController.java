/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package app.controllers;

import app.Controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import upv.ipc.sportlib.SportActivityApp;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class CerrarSesionController extends Controller {

    @FXML
    private Button volver;
    @FXML
    private Button permanecer;
    @FXML
    private Button salir;
    SportActivityApp app = SportActivityApp.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void volver(ActionEvent event) {
        changeScene("PaginaPrincipal");
    }

    @FXML
    private void salirse(ActionEvent event) {
        app.logout();
        changeScene("landingPage");
    }
    
}
