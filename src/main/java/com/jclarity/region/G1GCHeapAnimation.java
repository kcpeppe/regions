package com.jclarity.region;


import com.jclarity.region.fx.FrameCounter;
import com.jclarity.region.fx.VideoControls;
import com.jclarity.region.fx.G1GCHeapView;
import com.jclarity.region.model.JavaVirtualMachine;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class G1GCHeapAnimation extends Application {


    private JavaVirtualMachine jvm;

    private void initDataSource(List<String> parameters) throws IOException {
        jvm = new JavaVirtualMachine( parameters.get(0));
        jvm.load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> parameters = getParameters().getRaw();
        initDataSource(parameters);

        final VBox box = new VBox();
        VideoControls controls = new VideoControls(primaryStage);
        G1GCHeapView view = new G1GCHeapView(jvm);
        FrameCounter frameCounter = new FrameCounter(jvm.getNumberOfViews());
        controls.setFrameCounter(frameCounter);
        frameCounter.addListener(frameIndex -> view.update(null,frameIndex));
        box.getChildren().addAll(view,controls);

        Scene scene = new Scene(box);
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
