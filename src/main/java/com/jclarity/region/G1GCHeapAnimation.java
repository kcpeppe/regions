package com.jclarity.region;


import com.jclarity.region.fx.FrameCounter;
import com.jclarity.region.fx.G1GCHeapView;
import com.jclarity.region.fx.G1Region;
import com.jclarity.region.fx.VideoControls;
import com.jclarity.region.model.JavaVirtualMachine;
import com.jclarity.region.model.RegionType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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

        VBox centerPane = new VBox(g1GCHeapView, new Separator(), createLegend());
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

    private Region createLegend() {
        final int maxCol = 4;
        int col = 0;
        int row = 0;
        GridPane legend = new GridPane();
        for (int c = 0; c < maxCol; c++) {
            ColumnConstraints columnConstraint = new ColumnConstraints();
            columnConstraint.setPercentWidth(100d / maxCol);
            legend.getColumnConstraints().add(columnConstraint);
        }

        legend.getStylesheets().add(G1Region.class.getResource("regions.css").toExternalForm());
        legend.setGridLinesVisible(false);
        RegionType[] regionTypes = RegionType.values();
        for (int rt=0; rt < regionTypes.length; rt++) {
            legend.add(createLegendLabel(regionTypes[rt]), col++, row);
            if (col % maxCol == 0) {
                row += 1;
                col = 0;
            }
        }
        return legend;
    }

    private Label createLegendLabel(RegionType regionType) {
        Region graphic = new Region();
        graphic.setPrefSize(Font.getDefault().getSize(), Font.getDefault().getSize());
        graphic.getStyleClass().setAll("g1-region");
        graphic.setStyle("-fx-background-insets: 0.1, 0 0 0 .5em;");

        graphic.pseudoClassStateChanged(regionType.getPseudoClass(), true);
        return new Label(regionType.getLabel(), graphic);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
