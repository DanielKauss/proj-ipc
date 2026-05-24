/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package app.controllers;

import app.Controller;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import upv.ipc.sportlib.Activity;
import upv.ipc.sportlib.Annotation;
import upv.ipc.sportlib.AnnotationType;
import upv.ipc.sportlib.GeoPoint;
import upv.ipc.sportlib.MapProjection;
import upv.ipc.sportlib.MapRegion;
import upv.ipc.sportlib.SportActivityApp;
import upv.ipc.sportlib.TrackPoint;

/**
 * FXML Controller class
 *
 * @author daniel
 */
public class VistaActividadController extends Controller {
    
    @FXML
    private VBox chartContainer;
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
    private Label anotationLabel;
    @FXML
    private Button backBtn;
    @FXML
    private Button zoomInBtn;
    @FXML
    private Button zoomOutBtn;
    @FXML
    private Button changeMapBtn;
    @FXML
    private Button deleteActivityBtn;
    @FXML
    private ImageView mapImageView;
    @FXML
    private ScrollPane mapScrollPane;
    @FXML
    private TextField nameTextField;

    SportActivityApp app = SportActivityApp.getInstance();
    
    private static final double MIN_SCALE = 0.5;
    private static final double MAX_SCALE = 5.0;
    
    private List<Line> routeSegments = new ArrayList<>();
    private List<Color> routeSegmentColors = new ArrayList<>();
    
    private Activity activity;
    private MapProjection projection;
    
    private Point2D mousePos = new Point2D(0, 0);
    
    private Group zoomGroup;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (zoomGroup == null) {
            zoomGroup = new Group(mapImageView);
            mapScrollPane.setContent(zoomGroup);
        }
        
        anotationLabel.setVisible(false);
        
        zoomInBtn.setOnAction(event -> zoom(1.1));
        zoomOutBtn.setOnAction(event -> zoom(0.9));
        
        zoomGroup.setOnScroll((ScrollEvent event) -> {
            event.consume();
            if (event.getDeltaY() == 0) { return; }
            double zoomFactor = (event.getDeltaY() > 0) ? 1.1 : 0.9;
            
            zoom(zoomFactor);
        });

        ContextMenu contextMenu = createContextMenu();
        
        mapScrollPane.setOnContextMenuRequested(event -> {
            if (firstAnnotationPoint != null) {
                secondAnnotationPoint = projection.unproject(mousePos.getX(), mousePos.getY());
                openAnnotationPopup();
            }
            else {
                firstAnnotationPoint = projection.unproject(mousePos.getX(), mousePos.getY());
                contextMenu.show(mapScrollPane, event.getScreenX(), event.getScreenY());
            }
        });
        
        nameTextField.textProperty().addListener((obs, oldv, newv) -> {
            System.out.println("changed name: " + newv);
            app.renameActivity(activity, newv);
        });
        
        mapImageView.setOnMouseMoved(event -> {
            mousePos = new Point2D(event.getX(), event.getY());
        });
        
        mapScrollPane.setOnMouseClicked(event -> {
             if (event.getButton() == MouseButton.PRIMARY && contextMenu.isShowing()) {
                 firstAnnotationPoint = null;
                 anotationLabel.setVisible(false);
                 contextMenu.hide();
             }
        });
        
        backBtn.setOnAction((e) -> {
            changeScene("PaginaPrincipal");
        });
        
        deleteActivityBtn.setOnAction(e -> {
            try {
                FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/resources/fxml/EliminarMapa.fxml"));
                Parent root = miCargador.load();

                EliminarMapaController controlador2 = miCargador.getController();

                Scene scene = new Scene(root,500,200);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Eliminar Actividad");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.showAndWait();

                if (controlador2.getEliminarMapa()) {
                    app.removeActivity(activity);
                    changeScene("PaginaPrincipal");
                }
            } catch (IOException ex) {
                    
            }
        });
    }    
    
    public void updateData(Activity activity) {
        this.activity = activity;

        zoomGroup = new Group(mapImageView);
        mapScrollPane.setContent(zoomGroup);

        File mapPath = new File(activity.getSuggestedMap().getImagePath());
        Image map = new Image(mapPath.toURI().toString());
        mapImageView.setImage(map);
        
        double imgWidth = mapImageView.getImage().getWidth();
        double imgHeight = mapImageView.getImage().getHeight();
        projection = new MapProjection(activity.getSuggestedMap(), imgWidth, imgHeight);

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

        nameTextField.setText(activity.getName());
        distanceLabel.setText(String.format("%.2f m", activity.getTotalDistance()));
        heightDiffLabel.setText(String.format("%.2f m", activity.getMaxElevation() - activity.getMinElevation()));   
        speedLabel.setText(String.format("%.2f min/km", activity.getAveragePace()));
        drawRoute();
    }
    
    private void zoom(double factor) {
        double scrollH = mapScrollPane.getHvalue();
        double scrollV = mapScrollPane.getVvalue();

        double newScale = zoomGroup.getScaleX() * factor;

        if (newScale >= MIN_SCALE && newScale <= MAX_SCALE) {
            zoomGroup.setScaleX(newScale);
            zoomGroup.setScaleY(newScale);
        }

        mapScrollPane.setHvalue(scrollH);
        mapScrollPane.setVvalue(scrollV);
    }

    private void drawRoute() {
        List<Point2D> pixelPoints = projection.projectActivity(activity);
        List<TrackPoint> trackPoints = activity.getTrackPoints();

        double minSpeed = Double.MAX_VALUE;
        double maxSpeed = Double.MIN_VALUE;

        for (int i = 0; i < trackPoints.size() - 1; i++) {
            double speed = trackPoints.get(i).speedTo(trackPoints.get(i + 1));
            if (speed < minSpeed) minSpeed = speed;
            if (speed > maxSpeed) maxSpeed = speed;
        }
        
        if (pixelPoints.getFirst() != null) {
            mapScrollPane.setHvalue(pixelPoints.getFirst().getX() / mapImageView.getImage().getWidth());
            mapScrollPane.setVvalue(pixelPoints.getFirst().getY() / mapImageView.getImage().getHeight());
        }

        routeSegments.clear();
        routeSegmentColors.clear();

        for (int i = 0; i < pixelPoints.size() - 1; i++) {
            Point2D p1 = pixelPoints.get(i);
            Point2D p2 = pixelPoints.get(i + 1);

            TrackPoint tp1 = trackPoints.get(i);
            TrackPoint tp2 = trackPoints.get(i + 1);
            double speed = tp1.speedTo(tp2);

            Line segment = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            segment.setStrokeWidth(4.0);
            segment.setStrokeLineCap(StrokeLineCap.ROUND);

            Color baseColor = getColorForSpeed(speed, minSpeed, maxSpeed);
            segment.setStroke(baseColor);

            routeSegments.add(segment);
            routeSegmentColors.add(baseColor);

            zoomGroup.getChildren().add(segment);
        }
        
        drawAnnotations();
        createChartSpeed(trackPoints);
        createChartElevation(trackPoints);
    }

    private void drawAnnotations() {
        List<Annotation> annotations = activity.getAnnotations();
        for (int i = 0; i < annotations.size(); i++) {
            Annotation curr = annotations.get(i);

            Color color = Color.web(curr.getColor());

            switch (curr.getType()) {
                case TEXT: {
                    Text annotLabel = new Text(curr.getText());
                    annotLabel.setFill(color);

                    Point2D p = projection.project(curr.getGeoPoints().getFirst());
                    annotLabel.setX(p.getX());
                    annotLabel.setY(p.getY());

                    if (curr.getText() != null && !curr.getText().trim().isEmpty()) {
                        zoomGroup.getChildren().add(annotLabel);
                    }
                    break;
                }
                case POINT: {
                    Point2D p = projection.project(curr.getGeoPoints().getFirst());

                    Circle pointMarker = new Circle(p.getX(), p.getY(), 5);
                    pointMarker.setFill(color);
                    zoomGroup.getChildren().add(pointMarker);


                    Text annotLabel = new Text(curr.getText());
                    annotLabel.setFill(color);
                    annotLabel.setX(p.getX() + 8);
                    annotLabel.setY(p.getY() + 4);

                    if (curr.getText() != null && !curr.getText().trim().isEmpty()) {
                        zoomGroup.getChildren().add(annotLabel);
                    }
                    break;
                }
                case LINE: {
                    Point2D start = projection.project(curr.getGeoPoints().get(0));
                    Point2D end = projection.project(curr.getGeoPoints().get(1));
                    
                    Point2D mid = start.midpoint(end);
                    
                    Text annotLabel = new Text(curr.getText());
                    annotLabel.setFill(color);
                    annotLabel.setX(mid.getX() + 8);
                    annotLabel.setY(mid.getY() + 4);

                    Line line = new Line(start.getX(), start.getY(), end.getX(), end.getY());
                    line.setStroke(color);
                    line.setStrokeWidth(curr.getStrokeWidth());

                    zoomGroup.getChildren().add(line);
                    zoomGroup.getChildren().add(annotLabel);
                    break;
                }
                case CIRCLE: {
                    Point2D center = projection.project(curr.getGeoPoints().get(0));
                    Point2D edge = projection.project(curr.getGeoPoints().get(1));

                    Text annotLabel = new Text(curr.getText());
                    annotLabel.setFill(color);
                    annotLabel.setX(center.getX() + 8);
                    annotLabel.setY(center.getY() + 4);
                    
                    double radius = center.distance(edge);

                    Circle mapCircle = new Circle(center.getX(), center.getY(), radius);
                    mapCircle.setFill(Color.TRANSPARENT);
                    mapCircle.setStroke(color);
                    mapCircle.setStrokeWidth(curr.getStrokeWidth());

                    zoomGroup.getChildren().add(mapCircle);
                    zoomGroup.getChildren().add(annotLabel);
                    break;
                }
            }
        }
    }
    
    private Color getColorForSpeed(double speed, double minSpeed, double maxSpeed) {
        Color accentColor = Color.web("#0685BC");
        if (maxSpeed == minSpeed) return Color.GREEN;

        double ratio = (speed - minSpeed) / (maxSpeed - minSpeed);
        double brig = ratio * accentColor.getBrightness(); 

        return Color.hsb(accentColor.getHue(), 1.0, brig);
    }
    
    private void createChartSpeed(List<TrackPoint> trackPoints) {
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
        
        chartContainer.getChildren().add(lineChart);
        
        setHighlightListeners(lineChart, data);
    }
    
    private void createChartElevation(List<TrackPoint> trackPoints) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Kilometros");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Metros");

        LineChart lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Elevación");

        XYChart.Series data = new XYChart.Series<Number, Number>();

        double totalDistanceKm = 0.0;

        if (!trackPoints.isEmpty()) {
            data.getData().add(new XYChart.Data<>(0.0, 0.0));
        }

        for (int i = 0; i < trackPoints.size() - 1; i++) {
            TrackPoint current = trackPoints.get(i);
            TrackPoint next = trackPoints.get(i + 1);

            double distanceStepKm = current.distanceTo(next) / 1000.0;
            totalDistanceKm += distanceStepKm;

            double speed = current.getElevation();

            data.getData().add(new XYChart.Data<>(totalDistanceKm, speed));
        }

        lineChart.getData().add(data);
        lineChart.setLegendVisible(false);
        lineChart.setMinHeight(300);
        
        chartContainer.getChildren().add(lineChart);
        
        setHighlightListeners(lineChart, data);
    }
    
    private void setHighlightListeners(LineChart<Number, Number> chart, XYChart.Series<Number, Number> series) {
        Node plotBackground = chart.lookup(".chart-plot-background");

        if (plotBackground == null) {
            plotBackground = chart;
        }

        ValueAxis<Number> xAxis = (ValueAxis<Number>) chart.getXAxis();

        plotBackground.setOnMouseMoved(e -> {
            Point2D mouseInAxis = xAxis.sceneToLocal(e.getSceneX(), e.getSceneY());

            double dataX = xAxis.getValueForDisplay(mouseInAxis.getX()).doubleValue();
            int closestIndex = (int)Math.round(dataX / series.getData().getLast().getXValue().doubleValue() * (series.getData().size() - 1));

            for (int j = 0; j < series.getData().size() - 1; j++) {
                unhighlightSegment(j);
            }

            for (int j = 0; j < 5; j++) {
                highlightSegment(closestIndex + j);
            }
        });
    }
    
    private void highlightSegment(int index) {
        if (index < 0 || index >= routeSegments.size()) return;

        Line segment = routeSegments.get(index);
        //segment.setStroke(Color.web("#dddddd"));
        segment.setStrokeWidth(8.0);
        segment.setOpacity(1.0);
        segment.toFront();
    }

    private void unhighlightSegment(int index) {
        if (index < 0 || index >= routeSegments.size()) return;

        Line segment = routeSegments.get(index);
        segment.setStrokeWidth(4.0);
        segment.setStroke(routeSegmentColors.get(index));
    }
    
    private GeoPoint firstAnnotationPoint;
    private GeoPoint secondAnnotationPoint;
    private AnnotationType currentType;


    private void openAnnotationPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/AñadirAnotacion.fxml"));
            Parent root = loader.load();
            AñadirAnotacionController controlador = loader.getController();
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Añadir Anotación");
            stage.setScene(new Scene(root, 350, 260));
            stage.setResizable(false);
            stage.showAndWait();

            if (controlador.isConfirmed()) {
                Annotation ann;
                if (currentType == AnnotationType.POINT || currentType == AnnotationType.TEXT) {
                     ann = new Annotation(
                        currentType,
                        controlador.getText(),
                        controlador.getHexColor(),
                        2.0,
                        List.of(firstAnnotationPoint)
                    );
                } else {
                     ann = new Annotation(
                        currentType,
                        controlador.getText(),
                        controlador.getHexColor(),
                        2.0,
                        List.of(firstAnnotationPoint, secondAnnotationPoint)
                    );
                }
                
                app.addAnnotation(activity, ann);
                drawAnnotations();
            }
            firstAnnotationPoint = null;
            anotationLabel.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ContextMenu createContextMenu() {
        ContextMenu menu = new ContextMenu();
        
        MenuItem text = new MenuItem("Añadir texto");
        text.setOnAction(e -> {
            currentType = AnnotationType.TEXT;
            openAnnotationPopup();
        });
        
        MenuItem point = new MenuItem("Añadir punto");
        point.setOnAction(e -> {
            currentType = AnnotationType.POINT;
            openAnnotationPopup();
        });
        
        MenuItem line = new MenuItem("Añadir linea");
        line.setOnAction(e -> {
            currentType = AnnotationType.LINE;
            anotationLabel.setVisible(true);
        });
        MenuItem circle = new MenuItem("Añadir circulo");
        circle.setOnAction(e -> {
            currentType = AnnotationType.CIRCLE;
            anotationLabel.setVisible(true);
        });
        
        MenuItem reset = new MenuItem("Reset Zoom/Pan");
        reset.setOnAction(e -> {
            zoomGroup.setScaleX(1.0);
            zoomGroup.setScaleY(1.0);
            
            mapScrollPane.setHvalue(0.5);
            mapScrollPane.setVvalue(0.5);
        });

        menu.getItems().addAll(text, point, line, circle, reset);
        return menu;
    }
}
