package com.jclarity.region.fx;

import com.jclarity.region.FrameCounter;
import javafx.scene.control.Control;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Skin;
import javafx.scene.control.Slider;

public class TimeSlider extends Control {

    final private Slider slider;
    final private ProgressBar progressBar;

    public TimeSlider() {
        getStyleClass().add("track");
        setFocusTraversable(true);
        setMinWidth(200);
        setMaxWidth(200);

        slider = new Slider();
        slider.setMin(0);

        progressBar = new ProgressBar(0);
    }

    public void setFrameCounter(FrameCounter frameCounter) {
        slider.valueProperty().bindBidirectional(frameCounter);
        slider.setMax( frameCounter.getFrameCount());
    }

    public int getMax() {
        return (int)slider.getMax();
    }

    protected ProgressBar getProgressBar() {
        return progressBar;
    }

    protected Slider getSlider() {
        return slider;
    }

    @Override public String getUserAgentStylesheet() {
        return this.getClass().getResource("timeslider.css").toExternalForm();
    }

    @Override protected Skin<?> createDefaultSkin() {
        return new TimeSliderSkin(this);
    }
}