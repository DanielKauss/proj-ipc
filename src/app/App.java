/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import upv.ipc.sportlib.SportActivityApp;

/**
 *
 * @author daniel
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //System.out.println(SportActivityApp.getInstance().login("juan_23", "juan_23!"));
        //Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/PaginaPrincipal.fxml"));
        Font.loadFont(
                getClass().getResourceAsStream(
                        "/fonts/MervaleScrpit-Regular.ttf"
                ),
                80
        );
        Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/LandingPage.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/logo.png")));
        Scene scene = new Scene(root);
        stage.setTitle("Running La Safor");
        stage.setScene(scene);
        stage.show();

        Controller.stage = stage;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
