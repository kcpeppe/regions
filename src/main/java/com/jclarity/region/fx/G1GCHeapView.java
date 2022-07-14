package com.jclarity.region.fx;

import com.jclarity.region.model.G1GCHeap;
import com.jclarity.region.model.G1GCRegion;
import com.jclarity.region.model.JavaVirtualMachine;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.util.Iterator;
import java.util.Random;

public class G1GCHeapView extends Region implements ChangeListener<Number> {

    private int row = 0, col = 0;
    private int colNum;
    private JavaVirtualMachine jvm;

    public G1GCHeapView(JavaVirtualMachine jvm) {
        this.jvm = jvm;
        GridPane pane = initGridPane();
        getChildren().add(pane);
    }

    private GridPane initGridPane() {
        int regionCount = jvm.regionCount();
        colNum = calculateGridSize(regionCount);
        GridPane grid = new GridPane();
        for(int n=0, nMax=colNum*colNum; n < nMax; n++) {
            G1Region rec = new G1Region();
            GridPane.setRowIndex(rec,row);
            GridPane.setColumnIndex(rec,col);
            grid.getChildren().add(rec);
            col = (col + 1) % colNum;
            if ( col == 0)
                row++;
        };
        return grid;
    }

    private int calculateGridSize( int numberOfRectangles) {
        int square = (int)Math.sqrt((double)numberOfRectangles);
        while (((square*square) - numberOfRectangles) < 0) square += 1;
        return square;
    }

    Random random = new Random(9);

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        double index = (Double)newValue;
        final G1GCHeap heap = jvm.getG1GCHeapAt((int)index);
        Iterator<G1GCRegion> regionIterator = heap.iterator();
        GridPane grid = (GridPane) getChildren().get(0);

        Platform.runLater(() -> {
            while (regionIterator.hasNext()) {
                G1GCRegion activeRegion = regionIterator.next();
                int gridIndex = jvm.getRegionIndex(activeRegion);
                ((G1Region) grid.getChildren().get(gridIndex)).update(activeRegion.assignment(), (double) activeRegion.bytesUsed() / (double) activeRegion.regionSize());
                }
        });
    }
}
