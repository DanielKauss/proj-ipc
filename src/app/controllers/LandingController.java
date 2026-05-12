/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controllers;

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
public class LandingController implements Initializable {
    @FXML
    private Button loginBtn;
    private Button RegisterBtn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    public void loginBtnClick(MouseEvent event) throws IOException {
        System.out.println("btn clicked!!");
        FXMLLoader loader = new FXMLLoader(
            LandingController.class.getResource(
                "/resources/fxml/MapaDemo.fxml"
            )
        );

        Parent root = loader.load();

        // aqui se pueden pasar valores creo?
        MapaDemoController controller = loader.getController();
        Stage stage = (Stage) loginBtn.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
