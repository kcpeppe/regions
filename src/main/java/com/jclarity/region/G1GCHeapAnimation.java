package com.jclarity.region;


import com.jclarity.region.fx.VideoControls;
import com.jclarity.region.model.G1GCHeapView;
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
        VideoControls controls = new VideoControls();
        G1GCHeapView view = new G1GCHeapView(jvm);
        FrameCounter frameCounter = new FrameCounter(jvm.getNumberOfViews());
        controls.setFrameCounter(frameCounter);
        frameCounter.addListener((observable, oldValue, newValue) -> view.update(null,newValue));
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

//    private ToolBar getToolBar() {
//        ToolBar toolBar = new ToolBar();
//        toolBar.getItems().addAll(
//                new Separator(),
////                btnNew,
////                btnPause,
////                btnQuit,
//                new Separator(),
////                chkSound,
////                chkMusic,
//                new Separator()
//        );
//        return toolBar;
//    }

//    private HBox addToolBar() {
//        HBox toolBar = new HBox();
//        toolBar.setPadding(new Insets(20));
//        toolBar.setAlignment(Pos.CENTER);
//        toolBar.alignmentProperty().isBound();
//        toolBar.setSpacing(5);
//        toolBar.setStyle("-fx-background-color: Black");
//
//        return toolBar;
//    }

    public static void main(String[] args) {

        launch(args);
    }
}
