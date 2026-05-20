/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package app.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author apere
 */
public class EliminarMapaController implements Initializable {

    /**
     * Initializes the controller class.
     */
   
    @FXML
    private Button bEliminar;
    
    
    private boolean eliminarMapa;
     
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        eliminarMapa = false;
    }    
    
    public boolean getEliminarMapa() {
        return eliminarMapa;
    }

    @FXML
    private void eliminar(ActionEvent event) {
        eliminarMapa = true;
        bEliminar.getScene().getWindow().hide();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        bEliminar.getScene().getWindow().hide();
    }
}
