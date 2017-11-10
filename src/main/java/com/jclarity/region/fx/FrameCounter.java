package com.jclarity.region.fx;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.function.Consumer;

public class FrameCounter {

    final private SimpleDoubleProperty framePointer = new SimpleDoubleProperty(0.0);
    final private int maxFrameCount;
    public FrameCounter(int numberOfFrames) {
        if ( numberOfFrames < 0)
            throw new IllegalStateException("Frame count must be a positive integer");
        maxFrameCount = numberOfFrames;
    }

    public DoubleProperty framePointerProperty() {
        return framePointer;
    }

    public int getFrameCount() {
        return maxFrameCount;
    }

    public int getFrameIndex() {
        return (int)framePointer.get();
    }

    public void rewind() {
        framePointer.set(0);
    }

    public void stepBackwards() {
        if ( framePointer.get() > 0)
            framePointer.set(framePointer.get() - 1);
    }

    public void stepForward() {
        if ( framePointer.get() < maxFrameCount)
            framePointer.set(framePointer.get() + 1);
    }

    public void stepToEnd() {
        framePointer.set(maxFrameCount - 1);
    }

    public boolean atBeginning() {
        return framePointer.get() == 0;
    }
    public boolean atEnd() {
        return framePointer.get() >= maxFrameCount;
    }

    public void reset() {
        framePointer.set(0);
    }

    public void addListener(Consumer<Double> consumer) {
        framePointer.addListener(
                (observable, oldValue, newValue) -> consumer.accept(newValue.doubleValue())
        );
    }
}
