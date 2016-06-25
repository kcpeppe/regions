package com.jclarity.region;


import com.jclarity.region.fx.VideoControls;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VideoControlTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        VideoControls controls = new VideoControls();
        controls.setFrameCounter( new FrameCounter((50)));
        Scene scene = new Scene(controls);
        primaryStage.setTitle("Buttons");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
