/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package app.controllers;

import app.Controller;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import upv.ipc.sportlib.MapRegion;
import upv.ipc.sportlib.SportActivityApp;


public class GestionMapasController extends Controller {

    @FXML
    private ListView<MapRegion> listView;
    @FXML
    private Button bVisualizar;
    @FXML
    private Button bEliminar;
    @FXML
    private Button bAñadir;

    SportActivityApp app = SportActivityApp.getInstance();
    
    ObservableList<MapRegion> mapasObs;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<MapRegion> mapas = app.getMapRegions();
        mapasObs = FXCollections.observableArrayList(mapas);
        
        listView.setCellFactory(c-> new MapListCell());
        
        listView.setItems(mapasObs);
        
        bVisualizar.disableProperty().bind(Bindings.equal(-1, listView.getSelectionModel().selectedIndexProperty()));
        bEliminar.disableProperty().bind(Bindings.equal(-1, listView.getSelectionModel().selectedIndexProperty()));
    }    

    @FXML
    private void visualizar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                LandingController.class.getResource(
                        "/resources/fxml/VisualizarMapa.fxml"
                )
        );
        try {
            Parent root = loader.load();

            VisualizarMapaController controller = loader.getController();
            controller.initMapa(listView.getSelectionModel().getSelectedItem());

            stage.getScene().setRoot(root);
            stage.show();
        } catch (Exception ex) {
            System.out.println(ex.fillInStackTrace());
        }   
    }

    @FXML
    private void eliminar(ActionEvent event) throws IOException {
        MapRegion mapaSeleccionado = listView.getSelectionModel().getSelectedItem();
        
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/resources/fxml/EliminarMapa.fxml"));
        Parent root = miCargador.load();
        
        EliminarMapaController controlador2 = miCargador.getController();
        
        Scene scene = new Scene(root,500,200);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Eliminar Mapa");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
        
        if (controlador2.getEliminarMapa()) {
            boolean mapaBorrado = app.removeMapRegion(mapaSeleccionado);
            
            if (mapaBorrado) {
                listView.getSelectionModel().clearSelection();
                mapasObs.remove(mapaSeleccionado);
            } else {
                FXMLLoader miCargador2 = new FXMLLoader(getClass().getResource("/resources/fxml/AvisoMapa.fxml"));
                Parent root2 = miCargador2.load();
                Scene scene2 = new Scene(root2, 408, 170);
                Stage stage2 = new Stage();
                stage2.setScene(scene2);
                stage2.setTitle("Aviso");
                stage2.initModality(Modality.APPLICATION_MODAL);
                stage2.setResizable(false);
                stage2.showAndWait();
            }
        }
    }

    @FXML
    private void volver(ActionEvent event) {
        changeScene("PaginaPrincipal");
    }

    @FXML
    private void añadir(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/resources/fxml/AñadirMapa.fxml"));
        Parent root = miCargador.load();
        
        AñadirMapaController controlador2 = miCargador.getController();
        
        Scene scene = new Scene(root,500,550);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Añadir Mapa");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
        
        if (controlador2.isOKPressed()) {
            mapasObs.add(controlador2.getMapa());
        }
    }
}

class MapListCell extends ListCell<MapRegion> {
    
    @Override
    protected void updateItem(MapRegion item, boolean empty) {
        super.updateItem(item, empty);
        
        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item.getName());
        }
    }
}

