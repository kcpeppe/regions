package com.jclarity.region;


import com.jclarity.region.fx.FrameCounter;
import com.jclarity.region.fx.VideoControls;
import com.jclarity.region.fx.G1GCHeapView;
import com.jclarity.region.model.JavaVirtualMachine;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class G1GCHeapAnimation extends Application {


    private JavaVirtualMachine jvm;
    final StackPane box = new StackPane();
    VideoControls controls;
    Button openFileDialog;
    private void initDataSource(List<String> parameters) throws IOException {
        jvm = new JavaVirtualMachine(parameters.get(0));
        jvm.load();
    }

    private void initDataSource(File dataSource) throws IOException {
        box.getChildren().clear();
        box.getChildren().add(openFileDialog);
        jvm = new JavaVirtualMachine(dataSource.toPath());
        jvm.load();
        G1GCHeapView view = new G1GCHeapView(jvm);
        FrameCounter frameCounter = new FrameCounter(jvm.getNumberOfViews() - 1);
        controls.setFrameCounter(frameCounter);
        frameCounter.addListener(frameIndex -> view.update(null, frameIndex));
        //box.getChildren().addAll(view,controls);

        box.getChildren().add(view);
        view.visibleProperty().bind(controls.visibleProperty());
        box.getChildren().add(controls);
        controls.setVisible(true);
        box.getScene().getWindow().sizeToScene();
        ((Stage) box.getScene().getWindow()).setResizable(false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> parameters = getParameters().getRaw();
        initDataSource(parameters);

         openFileDialog = new Button("open");

        openFileDialog.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Log File");
            File file = fileChooser.showOpenDialog(primaryStage);
            try {
                initDataSource(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        controls = new VideoControls(primaryStage);
        controls.setPickOnBounds(false);
        controls.getStylesheets().add(getClass().getResource("fx/videocontrols.css").toExternalForm());
        controls.getStyleClass().add("controls");
        controls.setAlignment(Pos.BOTTOM_CENTER);
        box.getChildren().addAll(openFileDialog);//,controls);

        Scene scene = new Scene(box);

        primaryStage.setTitle("G1GC Heap");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        FadeTransition fade = new FadeTransition(Duration.millis(500), controls);
        fade.setFromValue(0);
        fade.setToValue(0.8);
        scene.setOnMouseEntered(e -> {fade.setRate(1); fade.play();});
        scene.setOnMouseExited(e -> {fade.setRate(-1); fade.play();});
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
