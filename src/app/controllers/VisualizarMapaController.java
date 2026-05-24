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
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import upv.ipc.sportlib.MapRegion;

/**
 * FXML Controller class
 *
 * @author apere
 */
public class VisualizarMapaController extends Controller {

    @FXML
    private Slider zoom_slider;
    @FXML
    private Label mousePosition;
    @FXML
    private SplitPane splitPane;
    @FXML
    private Label latMin;
    @FXML
    private Label latMax;
    @FXML
    private Label longMin;
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Label longMax;
    @FXML
    private Text nombreMapa;
    
    private Group zoomGroup;

    private Pane mapPane;

    private MapRegion mapa;
    
    public void initMapa(MapRegion m){
        mapa = m;
        buildMap(new File(mapa.getImagePath()));
        latMin.setText(String.valueOf(mapa.getLatMin()));
        latMax.setText(String.valueOf(mapa.getLatMax()));
        longMin.setText(String.valueOf(mapa.getLonMin()));
        longMax.setText(String.valueOf(mapa.getLonMax()));
        nombreMapa.setText(mapa.getName());
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        zoom_slider.setMin(0.5);
        zoom_slider.setMax(1.5);
        zoom_slider.setValue(1.0);
        
        zoom_slider.valueProperty().addListener(
            (observable, oldVal, newVal) -> zoom(newVal.doubleValue())
        );  
    }    

    @FXML
    private void zoomOut(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal - 0.1);
    }

    @FXML
    private void zoomIn(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + 0.1);
    }
    
    private void zoom(double scaleValue) {
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();

        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);

        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }

    @FXML
    private void salir(ActionEvent event) {
        changeScene("GestionMapas");
    }

    @FXML
    private void showPosition(MouseEvent event) {
        mousePosition.setText(
            "sceneX: " + (int) event.getSceneX() +
            ", sceneY: " + (int) event.getSceneY() + "\n" +
            "         X: " + (int) event.getX() +
            ",          Y: " + (int) event.getY()
        );
    }
    
    private void buildMap(File imgFile) {
        if (!imgFile.exists()) {
            map_scrollpane.setContent(new Label("Imagen no encontrada: " + imgFile.getPath()));
            return;
        }

        Image img = new Image(imgFile.toURI().toString());
        double W = img.getWidth();
        double H = img.getHeight();

        mapPane = new Pane();
        mapPane.setPrefSize(W, H);
        mapPane.setMinSize(W, H);
        mapPane.setMaxSize(W, H);

        ImageView iv = new ImageView(img);
        iv.setFitWidth(W);
        iv.setFitHeight(H);
        mapPane.getChildren().add(iv);

        mapPane.setOnMouseMoved(this::showPosition);

        zoomGroup = new Group();
        Group contentGroup = new Group();
        
        zoomGroup.getChildren().add(mapPane);
        contentGroup.getChildren().add(zoomGroup);

        double initialZoom = zoom_slider.getValue();
        zoomGroup.setScaleX(initialZoom);
        zoomGroup.setScaleY(initialZoom);

        map_scrollpane.setContent(contentGroup);
    }
}
