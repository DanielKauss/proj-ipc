/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import app.controllers.LandingController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author daniel
 */
public abstract class Controller implements Initializable {
    static public Stage stage;
    
    public static void changeScene(String name) {
        FXMLLoader loader = new FXMLLoader(
            LandingController.class.getResource(
                "/resources/fxml/" + name + ".fxml"
            )
        );
        try {
            Parent root = loader.load();

            // aqui se pueden pasar valores creo?
            // Controller controller = loader.getController();
            // Stage stage = (Stage) loginBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
