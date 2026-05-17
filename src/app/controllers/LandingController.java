/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controllers;

import app.Controller;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author daniel
 */
public class LandingController extends Controller {
    @FXML
    private Button loginBtn;
    private Button RegisterBtn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    public void loginBtnClick(MouseEvent event) throws IOException {
        System.out.println("btn clicked!!");
        changeScene("LogIn");
    }

    @FXML
    public void registerBtnClick(MouseEvent event) throws IOException {
        System.out.println("btn clicked!!");
        changeScene("Registro");
    }
    
}
