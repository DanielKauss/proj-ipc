/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package app.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import upv.ipc.sportlib.MapRegion;
import upv.ipc.sportlib.SportActivityApp;

/**
 * FXML Controller class
 *
 * @author apere
 */
public class AñadirMapaController implements Initializable {

    @FXML
    private TextField campoLatMin;
    @FXML
    private TextField campoNombre;
    @FXML
    private TextField campoLatMax;
    @FXML
    private TextField campoLongMin;
    @FXML
    private TextField campoLongMax;
    @FXML
    private Button bCancelar;
    @FXML
    private Button bAñadir;
    
    @FXML
    private Label errorMapa;
    
    private MapRegion mapa;
    
    private boolean pulsadoOK;
    
    private final ObjectProperty<File> mapFile = new SimpleObjectProperty<>(null);
    
    SportActivityApp app = SportActivityApp.getInstance();
    

    /**
     * Initializes the controller class.
     */
    
    
    public boolean isOKPressed( )
    {
        return pulsadoOK;
    }
    public MapRegion getMapa( )
    {
        return mapa;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        pulsadoOK = false;
        
        campoLatMin.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d*\\.?\\d*")) {
                campoLatMin.setText(oldValue);
        }});
        campoLatMax.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d*\\.?\\d*")) {
                campoLatMax.setText(oldValue);
        }});
        campoLongMin.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d*\\.?\\d*")) {
                campoLongMin.setText(oldValue);
        }});
        campoLongMax.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d*\\.?\\d*")) {
                campoLongMax.setText(oldValue);
        }});
        
        bAñadir.disableProperty().bind(Bindings.or(
                campoNombre.textProperty().isEmpty(), campoLatMin.textProperty().isEmpty()).or(
                        campoLatMax.textProperty().isEmpty()).or(campoLongMin.textProperty().isEmpty()).or(
                                campoLongMax.textProperty().isEmpty()).or(mapFile.isNull()));
        
        errorMapa.setVisible(false);
    }    


    @FXML
    private void cancelar(ActionEvent event) {
        bAñadir.getScene().getWindow().hide();
    }

    @FXML
    private void subirMapa(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir fichero");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Imágenes", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            mapFile.set(selectedFile);
        } 
    }

    @FXML
    private void añadir(ActionEvent event) {
        MapRegion nuevoMapa = app.addMapRegion(campoNombre.getText(), mapFile.getValue(), 
                    Double.parseDouble(campoLatMin.getText()), Double.parseDouble(campoLatMax.getText()),
                        Double.parseDouble(campoLongMin.getText()), Double.parseDouble(campoLongMax.getText()));
        if(nuevoMapa == null){
            errorMapa.setVisible(true);
            campoLatMin.clear();
            campoLatMax.clear();
            campoLongMin.clear();
            campoLongMax.clear();
        }else{
            pulsadoOK = true;
            mapa = nuevoMapa;
            bAñadir.getScene().getWindow().hide();
        }
        
    }
    
}
