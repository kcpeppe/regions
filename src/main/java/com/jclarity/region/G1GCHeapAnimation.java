package com.jclarity.region;


import com.jclarity.region.fx.FrameCounter;
import com.jclarity.region.fx.G1GCHeapView;
import com.jclarity.region.fx.VideoControls;
import com.jclarity.region.model.JavaVirtualMachine;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class G1GCHeapAnimation extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        List<String> parameters = getParameters().getRaw();
        final File dataSource;
        if (!parameters.isEmpty()) {
            dataSource = new File(parameters.get(0));
        } else {
            throw new IllegalArgumentException("Expected the path to a GC log file as an argument. We'll fix the open file dialog later.");
        }

        JavaVirtualMachine jvm = new JavaVirtualMachine(dataSource.getPath());
        jvm.load();

        G1GCHeapView g1GCHeapView = new G1GCHeapView(jvm);
        g1GCHeapView.setStyle("-fx-border-color: red;");

        HBox centerPane = new HBox(g1GCHeapView);
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setPadding(new Insets(5,10,5,10));
        centerPane.setStyle("-fx-border-color: yellow;");

        FrameCounter frameCounter = new FrameCounter(jvm.getNumberOfViews() - 1);
        frameCounter.framePointerProperty().addListener(g1GCHeapView);
        VideoControls videoControls = new VideoControls();
        videoControls.setFrameCounter(frameCounter);

        videoControls.setPickOnBounds(false);
        videoControls.getStylesheets().add(getClass().getResource("fx/videocontrols.css").toExternalForm());

        HBox bottomPane = new HBox(videoControls);
        bottomPane.setStyle("-fx-border-color: red;");
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setPadding(new Insets(5,10,5,10));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(centerPane);
        borderPane.setBottom(bottomPane);

        Scene scene = new Scene(borderPane);

        primaryStage.setTitle("G1GC Heap");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
