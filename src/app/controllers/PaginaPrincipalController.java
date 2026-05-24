package app.controllers;

import app.Controller;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import upv.ipc.sportlib.Activity;
import upv.ipc.sportlib.SportActivityApp;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class PaginaPrincipalController extends Controller implements Initializable {

    @FXML
    private Button añadir;
    @FXML
    private Button mapas;
    @FXML
    private Button historial;
    @FXML
    private Button cerrar;
    @FXML
    private ListView<Activity> lista;

    SportActivityApp app = SportActivityApp.getInstance();

    private ObservableList<Activity> datos = null;
    @FXML
    private ImageView avatarImage;
    @FXML
    private Circle avatarCircle;
    @FXML
    private Label labelTiempoTotal;
    @FXML
    private Label labelDistanciaAcumulada;
    @FXML
    private Label labelMetrosAscenso;
    @FXML
    private Label labelMetrosDescenso;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        List<Activity> actividades = app.getUserActivities();

        datos = FXCollections.observableList(actividades);

        lista.setItems(datos);
        double distanciaTotal = 0;
        double ascensoTotal = 0;
        double descensoTotal = 0;
        int minutosTotales = 0;

        for (Activity a : datos) {
            distanciaTotal += a.getTotalDistance();
            ascensoTotal += a.getElevationGain();
            descensoTotal += a.getElevationLoss();
            minutosTotales += a.getDuration().toMinutes();
        }

        labelTiempoTotal.setText(minutosTotales + "");

        labelMetrosAscenso.setText(
                Math.round(ascensoTotal) + ""
        );

        labelDistanciaAcumulada.setText(
                Math.round(distanciaTotal) + ""
        );

        labelMetrosDescenso.setText(
                Math.round(descensoTotal) + ""
        );

        lista.setCellFactory(c -> new ListCell<Activity>() {

            private final Label nombre = new Label();
            private final Label duracion = new Label();
            private final Label distancia = new Label();
            private final Label velocidad = new Label();
            private final Label mapa = new Label();

            private final GridPane root = new GridPane();

            {
                root.setPadding(new Insets(20));
                root.setHgap(40);

                ColumnConstraints c1 = new ColumnConstraints();
                c1.setPercentWidth(25);
                c1.setHgrow(Priority.ALWAYS);

                nombre.setWrapText(true);
                nombre.setMaxWidth(Double.MAX_VALUE);

                ColumnConstraints c2 = new ColumnConstraints();
                c2.setPercentWidth(35);

                ColumnConstraints c3 = new ColumnConstraints();
                c3.setPercentWidth(40);

                root.getColumnConstraints().addAll(c1, c2, c3);

                VBox centro = new VBox(5, duracion, distancia);

                VBox derecha = new VBox(5, velocidad, mapa);

                root.add(nombre, 0, 0);
                root.add(centro, 1, 0);
                root.add(derecha, 2, 0);
            }

            @Override
            protected void updateItem(Activity item, boolean empty) {

                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    return;
                }

                nombre.setText(item.getName());

                long minutos = item.getDuration().toMinutes();
                long segundos = item.getDuration().toSeconds() % 60;

                duracion.setText(
                        "Duración: " + minutos + " min " + segundos + " sec"
                );

                distancia.setText(
                        "Distancia: "
                        + Math.round(item.getTotalDistance()) + " m"
                );

                velocidad.setText(
                        "Velocidad: "
                        + Math.round(item.getAverageSpeed() * 100) / 100 + " km/h"
                );

                mapa.setText(item.getSuggestedMap().getName());

                setGraphic(root);
            }

        });

        lista.setOnMouseClicked((e) -> {
            if(lista.getSelectionModel().getSelectedItem() == null) return;
            FXMLLoader loader = new FXMLLoader(
                    LandingController.class.getResource(
                            "/resources/fxml/VistaActividad.fxml"
                    )
            );
            try {
                Parent root = loader.load();

                VistaActividadController controller = loader.getController();
                controller.updateData(lista.getSelectionModel().getSelectedItem());
                stage.getScene().setRoot(root);
                stage.show();
            } catch (Exception ex) {
                System.out.println(ex.fillInStackTrace());
            }

        });

        if (app.getCurrentUser().getAvatar() != null) {

            avatarImage.setImage(app.getCurrentUser().getAvatar());
            Circle clip = new Circle(55);
            clip.setCenterX(55);
            clip.setCenterY(55);

            avatarImage.setClip(clip);
        }
    }

    @FXML
    private void mapas(ActionEvent event) {
        changeScene("GestionMapas");
    }

    @FXML
    private void historialSesiones(ActionEvent event) {
        changeScene("HistorialSesiones");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/resources/fxml/CerrarSesion.fxml"));
        Parent root = miCargador.load();

        CerrarSesionController controlador2 = miCargador.getController();

        Scene scene = new Scene(root, 600, 400);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setTitle("Cerrar Sesion");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();

    }

    @FXML
    private void actividadNueva(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Seleccionar archivo GPX");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Archivos GPX",
                        "*.gpx"
                )
        );

        File fichero = fileChooser.showOpenDialog(
                lista.getScene().getWindow()
        );
        if (fichero == null) {
            return;
        }

        try {

            Activity actividad
                    = app.importActivity(fichero);

            datos.add(actividad);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Actividad añadida");
            alert.setHeaderText(null);

            alert.setContentText(
                    "La actividad se añadió correctamente."
            );

            alert.showAndWait();

        } catch (Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Error");
            alert.setHeaderText("No se pudo importar el GPX");

            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }

    @FXML
    private void actionPerfil(MouseEvent event) {
        changeScene("ModificarPerfil");
    }
}
