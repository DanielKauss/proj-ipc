package app.controllers;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author daniel
 */

import app.Controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import upv.ipc.sportlib.AnnotationType;


public class AñadirAnotacionController extends Controller {

    @FXML private TextField textField;
    @FXML private ColorPicker colorPicker;

    private boolean isConfirmed = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colorPicker.setValue(Color.web("#E74C3C"));
    }

    @FXML
    private void add() {
        isConfirmed = true;
        close();
    }

    @FXML
    private void cancel() {
        isConfirmed = false;
        close();
    }

    private void close() {
        Stage stage = (Stage) textField.getScene().getWindow();
        stage.close();
    }
    
    public boolean isConfirmed() { return isConfirmed; }
    
    public String getText() { return textField.getText(); }
    
    public String getHexColor() {
        Color c = colorPicker.getValue();
        return String.format("#%02X%02X%02X", 
            (int)(c.getRed() * 255), 
            (int)(c.getGreen() * 255), 
            (int)(c.getBlue() * 255));
    }
}
