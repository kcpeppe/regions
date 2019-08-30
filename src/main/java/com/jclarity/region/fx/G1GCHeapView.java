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
        final G1GCHeap heap = jvm.getG1GCHeapAt(0);
        calculateGridSize(heap.size());
        GridPane grid = new GridPane();
        heap.forEach(region -> {
            G1Region rec = new G1Region();
            GridPane.setRowIndex(rec,row);
            GridPane.setColumnIndex(rec,col);
            grid.getChildren().add(rec);
            col = (col + 1) % colNum;
            if ( col == 0)
                row++;
        });
        return grid;
    }

    private void calculateGridSize( int numberOfRectangles) {
        int limit = (int)Math.sqrt( (double)numberOfRectangles);
        int value = numberOfRectangles;
        int prime = 2;
        colNum = 1;
        while ( value != 1 && prime < limit) {
            if ( value % prime == 0) {
                colNum *= prime;
                value = value / prime;
                if ( colNum > limit)
                    break;;
            } else
                prime++;
        }
    }

    Random random = new Random(9);

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        double index = (Double)newValue;
        final G1GCHeap heap = jvm.getG1GCHeapAt((int)index);
        Iterator<G1GCRegion> regionIterator = heap.iterator();
        GridPane grid = (GridPane) getChildren().get(0);

        Platform.runLater(() ->
            grid.getChildren().forEach(node -> {
                G1GCRegion activeRegion = regionIterator.next();
                ((G1Region) node).update(activeRegion.getAssignment(), (double) activeRegion.getNextLive() / (double)activeRegion.getRegionSize());
        }));
    }
}
