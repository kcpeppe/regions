package com.jclarity.region.model;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

public class G1GCHeapView extends Region implements Observer {

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
            Rectangle rec = new Rectangle();
            rec.setWidth(10);
            rec.setHeight(10);
            rec.setFill(colors.get(region.getAssignment()));
            GridPane.setRowIndex(rec,row);
            GridPane.setColumnIndex(rec,col);
            grid.getChildren().add(rec);
            col = (col + 1) % colNum;
            if ( col == 0)
                row++;
        });
        return grid;
    }

    private void colorGridPane() {
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

    private HashMap<RegionType,Color> colors;

    {
        colors = new HashMap<>();
        colors.put( RegionType.FREE, Color.BLACK);
        colors.put( RegionType.EDEN, Color.GREEN);
        colors.put( RegionType.SURV, Color.YELLOW);
        colors.put( RegionType.OLD, Color.BLUE);
        colors.put( RegionType.HUMS, Color.CORAL);
        colors.put( RegionType.HUMC, Color.RED);
    }

    @Override
    public void update(Observable o, Object arg) {
        double index = (Double)arg;
        final G1GCHeap heap = jvm.getG1GCHeapAt((int)index);
        Iterator<G1GCRegion> regionIterator = heap.iterator();
        GridPane grid = (GridPane) getChildren().get(0);
        grid.getChildren().forEach(node -> ((Rectangle) node).setFill(colors.get(regionIterator.next().getAssignment())));
    }
}
