package com.jclarity.region.fx;

import com.jclarity.region.model.G1GCHeap;
import com.jclarity.region.model.G1GCRegion;
import com.jclarity.region.model.JavaVirtualMachine;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.Iterator;
import java.util.Random;

public class G1GCHeapView extends Region implements ChangeListener<Number> {

    private int row = 0, col = 0;
    private int colNum;
    private JavaVirtualMachine jvm;

    public G1GCHeapView(JavaVirtualMachine jvm) {
        this.jvm = jvm;
        Pane pane = initGridPane();
        getChildren().add(pane);
    }

    private Pane initGridPane() {
        int regionCount = jvm.regionCount();
        colNum = calculateGridSize(regionCount);
        GridPane grid = new GridPane();
        for(int n=0, nMax=colNum*colNum; n < nMax; n++) {
            G1Region rec = new G1Region(jvm.getNumberOfViews());
            grid.add(rec, col, row);
            col = (col + 1) % colNum;
            if ( col == 0)
                row++;
        };

        for (int n=0; n<jvm.getNumberOfViews(); n++) {
            G1GCHeap heap = jvm.getG1GCHeapAt(n);
            int view = n;
            heap.forEach(g1gcRegion -> {
                int gridIndex = jvm.getRegionIndex(g1gcRegion);
                ((G1Region)grid.getChildren().get(gridIndex)).setView(view, g1gcRegion.assignment(), (double) g1gcRegion.bytesUsed() / (double) g1gcRegion.regionSize());
            });
        }
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
        final int frame = newValue.intValue();

        Platform.runLater(() -> {
            final G1GCHeap heap = jvm.getG1GCHeapAt(frame);
            Iterator<G1GCRegion> regionIterator = heap.iterator();
            GridPane grid = (GridPane) getChildren().get(0);
            for (int index = 0, nMax = grid.getColumnCount()*grid.getRowCount(); index <nMax; index++) {
                ((G1Region) grid.getChildren().get(index)).update(frame);
            }
        });
    }
}
