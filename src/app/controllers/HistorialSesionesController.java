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

import java.time.format.DateTimeFormatter;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

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
    
  private ObservableList<Session> historial;

@Override
public void initialize(URL url, ResourceBundle rb) {

    List<Session> datos =
            app.getSessionsByUser(app.getCurrentUser());

    historial = FXCollections.observableArrayList(datos);

    HistorialObservable.setItems(historial);

    HistorialObservable.setCellFactory(param ->
            new ListCell<Session>() {

        private final Label fecha = new Label();

        private final Label duracion = new Label();
        private final Label actividades = new Label();

        private final Label vistas = new Label();
        private final Label anotaciones = new Label();

        private final GridPane root = new GridPane();

        {
            root.setPadding(new Insets(20));
            root.setHgap(40);

            ColumnConstraints c1 = new ColumnConstraints();
            c1.setPercentWidth(25);

            ColumnConstraints c2 = new ColumnConstraints();
            c2.setPercentWidth(35);

            ColumnConstraints c3 = new ColumnConstraints();
            c3.setPercentWidth(40);

            root.getColumnConstraints()
                    .addAll(c1, c2, c3);

            VBox centro =
                    new VBox(5, duracion, actividades);

            VBox derecha =
                    new VBox(5, vistas, anotaciones);

            root.add(fecha, 0, 0);
            root.add(centro, 1, 0);
            root.add(derecha, 2, 0);

            fecha.getStyleClass().add("fecha");

            duracion.getStyleClass().add("info");
            actividades.getStyleClass().add("info");
            vistas.getStyleClass().add("info");
            anotaciones.getStyleClass().add("info");

            root.getStyleClass().add("session-row");
        }

        @Override
        protected void updateItem(Session item,
                                  boolean empty) {

            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
                return;
            }

            fecha.setText(
                    item.getStartTime()
                            .toLocalDate()
                            .format(
                                    DateTimeFormatter
                                    .ofPattern("dd/MM/yyyy")
                            )
            );

            long minutos =
                    item.getDuration().toMinutes();

            duracion.setText(minutos + " min.");

            actividades.setText(
                    item.getImportedActivities()
                    + " actividades importadas"
            );

            vistas.setText(
                    item.getViewedActivities()
                    + " vistas"
            );

            anotaciones.setText(
                    item.getAnnotationsCreated()
                    + " anotaciones creadas"
            );

            setGraphic(root);
        }
    });
}

    @FXML
    private void actionCancel(ActionEvent event) {
        changeScene("PaginaPrincipal");
    }
    
}
