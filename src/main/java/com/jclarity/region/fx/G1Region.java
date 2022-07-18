package com.jclarity.region.fx;

import com.jclarity.region.model.RegionType;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.Stack;


public class G1Region extends Pane {

    private final double width = 25;
    private final double height = 25;

    private final RegionType[] type;
    private final double[] occupancyPercentage;

    private final int numberOfViews;

    private int currentView;

    private Tooltip tooltip;

    public G1Region(int nViews) {
        numberOfViews = nViews;
        type = new RegionType[nViews];
        for (int n=0; n<numberOfViews; n++) type[n] = RegionType.FREE;
        occupancyPercentage = new double[nViews];
        for (int n=0; n<numberOfViews; n++) occupancyPercentage[n] = 1.0;
        getStylesheets().add(G1Region.class.getResource("regions.css").toExternalForm());
        getStyleClass().setAll("g1-region", "free");
//        setPrefSize(width, height);
        setMinSize(width, height);

        setOnMouseEntered(e -> {
            tooltip = new Tooltip(type[currentView].getLabel() +" - " + String.format("%2.1f%%", occupancyPercentage[currentView] * 100.0d));
            Tooltip.install(this, tooltip);
        });
        setOnMouseExited(e -> {
            Tooltip.uninstall(this, tooltip);
            tooltip = null;
        });
    }

    Stack<Pair<RegionType, Double>> history = new Stack<>();


    public void setView(int view, RegionType type, double occupancyPercentage)  {
        for (int n=view; n<numberOfViews; n++){
            this.type[n] = type;
            this.occupancyPercentage[n] = occupancyPercentage;
        }
    }

    public void update(int view)  {
        currentView = view;
        RegionType _type = type[currentView];
        double _occupancyPercentage = occupancyPercentage[currentView];
        int occupancy = (int)(_occupancyPercentage * 10.0);
        // TODO: Use pseudoclass state here instead of style class, e.g. g1-region:eden
        //       This is much faster than changing styleclass.
        //       Styleclass should be set when region is created.
        getStyleClass().setAll("g1-region", _type.toStyleClass());
        // TODO: There should be some way to do this programmatically rather than setStyle.
        setStyle("-fx-background-insets: 0.1, 0 0 0 " + occupancy + ";");
    }


}