/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package app.controllers;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import upv.ipc.sportlib.Activity;
import upv.ipc.sportlib.MapProjection;
import upv.ipc.sportlib.MapRegion;
import upv.ipc.sportlib.SportActivityApp;
import upv.ipc.sportlib.TrackPoint;

/**
 * FXML Controller class
 *
 * @author daniel
 */
public class VistaActividadController implements Initializable {
    
    @FXML
    private VBox leftVBox;
    @FXML
    private Label nameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label durationLabel;
    @FXML
    private Label distanceLabel;
    @FXML
    private Label heightDiffLabel;
    @FXML
    private Label speedLabel;
    @FXML
    private Button changeMapBtn;
    @FXML
    private ImageView mapImageView;
    @FXML
    private ScrollPane mapScrollPane;

    SportActivityApp app = SportActivityApp.getInstance();

    private static final double MIN_SCALE = 0.5;
    private static final double MAX_SCALE = 5.0;
    
    // Class-level variables
    private Group zoomGroup;
    private Polyline routeLine;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(app.login("juan_23", "juan_23!"));
        Activity activity = app.getCurrentUser().getActivities().get(1);
        
        File mapPath = new File(activity.getSuggestedMap().getImagePath());
        Image map = new Image(mapPath.toURI().toString());
        mapImageView.setImage(map);
        
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm"); 
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        dateLabel.setText(activity.getStartTime().format(dateTimeFormatter));
        Duration duration = activity.getDuration();
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        String durationText = String.format("%dh %02dm %02ds", hours, minutes, seconds);
        if (hours == 0) {
            durationText = String.format("%dm %02ds", minutes, seconds);
        }
        durationLabel.setText(durationText);

        nameLabel.setText(activity.getName());
        distanceLabel.setText(String.format("%.2f m", activity.getTotalDistance()));
        heightDiffLabel.setText(String.format("%.2f m", activity.getMaxElevation() - activity.getMinElevation()));   
        
        zoomGroup = new Group(mapImageView);
        mapScrollPane.setContent(zoomGroup);
        
        drawRoute(activity, activity.getSuggestedMap());
        
        zoomGroup.setOnScroll((ScrollEvent event) -> {
            event.consume();
            if (event.getDeltaY() == 0) { return; }
            double scrollH = mapScrollPane.getHvalue();
            double scrollV = mapScrollPane.getVvalue();
            double zoomFactor = (event.getDeltaY() > 0) ? 1.1 : 0.9;
            double newScale = zoomGroup.getScaleX() * zoomFactor;
            
            if (newScale >= MIN_SCALE && newScale <= MAX_SCALE) {
                zoomGroup.setScaleX(newScale);
                zoomGroup.setScaleY(newScale);
            }
            
            mapScrollPane.setHvalue(scrollH);
            mapScrollPane.setVvalue(scrollV);
        });

        ContextMenu contextMenu = createContextMenu();
        
        mapScrollPane.setOnContextMenuRequested(event -> {
            contextMenu.show(mapScrollPane, event.getScreenX(), event.getScreenY());
        });
        
        mapScrollPane.setOnMouseClicked(event -> {
             if (event.getButton() == MouseButton.PRIMARY && contextMenu.isShowing()) {
                 contextMenu.hide();
             }
        });
    }    

    public void drawRoute(Activity activity, MapRegion region) {
        double imgWidth = mapImageView.getImage().getWidth();
        double imgHeight = mapImageView.getImage().getHeight();
        MapProjection projection = new MapProjection(region, imgWidth, imgHeight);

        List<Point2D> pixelPoints = projection.projectActivity(activity);
        List<TrackPoint> trackPoints = activity.getTrackPoints();

        double minSpeed = Double.MAX_VALUE;
        double maxSpeed = Double.MIN_VALUE;

        for (int i = 0; i < trackPoints.size() - 1; i++) {
            double speed = trackPoints.get(i).speedTo(trackPoints.get(i + 1));
            if (speed < minSpeed) minSpeed = speed;
            if (speed > maxSpeed) maxSpeed = speed;
        }

        for (int i = 0; i < pixelPoints.size() - 1; i++) {
            Point2D p1 = pixelPoints.get(i);
            Point2D p2 = pixelPoints.get(i + 1);

            TrackPoint tp1 = trackPoints.get(i);
            TrackPoint tp2 = trackPoints.get(i + 1);
            double speed = tp1.speedTo(tp2);

            Line segment = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            segment.setStrokeWidth(4.0);
            segment.setStrokeLineCap(StrokeLineCap.ROUND);

            segment.setStroke(getColorForSpeed(speed, minSpeed, maxSpeed));

            zoomGroup.getChildren().add(segment);
        }

        populateChart(trackPoints);populateChart(trackPoints);
    }

    private Color getColorForSpeed(double speed, double minSpeed, double maxSpeed) {
        Color accentColor = Color.web("#0685BC");
        if (maxSpeed == minSpeed) return Color.GREEN;

        double ratio = (speed - minSpeed) / (maxSpeed - minSpeed);

        double brig = ratio * accentColor.getBrightness(); 

        return Color.hsb(accentColor.getHue(), 1.0, brig);
    }
    
    private void populateChart(List<TrackPoint> trackPoints) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Kilometros");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Km/h");

        LineChart lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Velocidad");

        XYChart.Series data = new XYChart.Series<Number, Number>();
        // data.setName("2022");

        double totalDistanceKm = 0.0;

        if (!trackPoints.isEmpty()) {
            data.getData().add(new XYChart.Data<>(0.0, 0.0));
        }

        for (int i = 0; i < trackPoints.size() - 1; i++) {
            TrackPoint current = trackPoints.get(i);
            TrackPoint next = trackPoints.get(i + 1);

            double distanceStepKm = current.distanceTo(next) / 1000.0;
            totalDistanceKm += distanceStepKm;

            double speed = current.speedTo(next);

            data.getData().add(new XYChart.Data<>(totalDistanceKm, speed));
        }

        lineChart.getData().add(data);
        lineChart.setLegendVisible(false);
        lineChart.setMinHeight(300);
        
        leftVBox.getChildren().add(lineChart);


        // altitudeChart.getData().add(series);
    }
    
    private ContextMenu createContextMenu() {
        ContextMenu menu = new ContextMenu();
        
        MenuItem item1 = new MenuItem("Anyadir anotacion");
        item1.setOnAction(e -> System.out.println("anotacion"));
        
        MenuItem item2 = new MenuItem("Reset Zoom/Pan");
        item2.setOnAction(e -> {
            zoomGroup.setScaleX(1.0);
            zoomGroup.setScaleY(1.0);
            
            mapScrollPane.setHvalue(0.5);
            mapScrollPane.setVvalue(0.5);
        });

        menu.getItems().addAll(item1, item2);
        return menu;
    }
}
