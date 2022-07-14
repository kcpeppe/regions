package com.jclarity.region.fx;

import com.jclarity.region.model.RegionType;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;


public class G1Region extends Pane {

    private double width = 10;
    private double height = 10;

    private RegionType type = RegionType.FREE;
    private double occupancyPercentage = 1.0d;

    private Tooltip tooltip;

    public G1Region() {
        getStylesheets().add(G1Region.class.getResource("regions.css").toExternalForm());
        getStyleClass().setAll("g1-region", "free");
        setPrefSize(width, height);

        setOnMouseEntered(e -> {
            tooltip = new Tooltip(type.getLabel() +" - " + String.format("%2.1f%%", occupancyPercentage * 100.0d));
            Tooltip.install(this, tooltip);
        });
        setOnMouseExited(e -> {
            Tooltip.uninstall(this, tooltip);
            tooltip = null;
        });
    }

    public void update(RegionType type, double occupancyPercentage) {
        this.type = type;
        this.occupancyPercentage = occupancyPercentage;
        int occupancy = (int)(occupancyPercentage * 10.0);
        // TODO: Use pseudoclass state here instead of style class, e.g. g1-region:eden
        //       This is much faster than changing styleclass.
        //       Styleclass should be set when region is created.
        getStyleClass().setAll("g1-region", type.toStyleClass());
        // TODO: There should be some way to do this programmatically rather than setStyle.
        setStyle("-fx-background-insets: 0.1, 0 0 0 " + occupancy + ";");
    }


}