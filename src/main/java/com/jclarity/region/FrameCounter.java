package com.jclarity.region;


import javafx.beans.property.SimpleDoubleProperty;

public class FrameCounter extends SimpleDoubleProperty {

    final private int maxFrameCount;
    public FrameCounter(int numberOfFrames) {
        if ( numberOfFrames < 0)
            throw new IllegalStateException("Frame count must be a positive integer");
        maxFrameCount = numberOfFrames;
    }

    public int getFrameCount() {
        return maxFrameCount;
    }

    public int getFrameIndex() {
        return (int)get();
    }

    public void rewind() {
        set(0);
    }

    public void stepBackwards() {
        if ( get() > 0)
            set(get() - 1);
    }

    public void stepForward() {
        if ( get() < maxFrameCount)
            set(get() + 1);
    }

    public void stepToEnd() {
        set(maxFrameCount);
    }

    public boolean atBeginning() {
        return get() == 0;
    }
    public boolean atEnd() {
        return get() >= maxFrameCount;
    }
}
