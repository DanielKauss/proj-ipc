/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package app.controllers;

import static app.Controller.changeScene;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import upv.ipc.sportlib.Session;
import upv.ipc.sportlib.SportActivityApp;

/**
 * FXML Controller class
 *
 * @author Daniel
 */
public class HistorialSesionesController implements Initializable {

    @FXML
    private Button buttonCancel;
    @FXML
    private ListView<Session> HistorialObservable;
    
    SportActivityApp app = SportActivityApp.getInstance();
    
    ObservableList<Session> Historial = null;
    
    private List<Session> datos = app.getSessionsByUser(app.getCurrentUser());

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         
        Historial = FXCollections.observableArrayList(datos);
        
        HistorialObservable.setItems(Historial);
    }    

    @FXML
    private void actionCancel(ActionEvent event) {
        changeScene("PaginaPrincipal");
    }
    
}
