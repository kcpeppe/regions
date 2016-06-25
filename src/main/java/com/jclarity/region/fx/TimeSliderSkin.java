package com.jclarity.region.fx;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Slider;


public class TimeSliderSkin extends SkinBase<TimeSlider> {

    final private Slider slider;
    final private ProgressBar progressBar;

    protected TimeSliderSkin(TimeSlider control) {
        super(control);

        slider = control.getSlider();
        slider.setMinWidth(control.getMinWidth());
        slider.setMaxWidth(control.getMaxWidth());

        progressBar = control.getProgressBar();
        progressBar.setMinWidth(control.getMinWidth());
        progressBar.setMaxWidth(control.getMaxWidth());

        slider.valueProperty().addListener((observable, oldValue, newValue) -> progressBar.setProgress(newValue.doubleValue() / control.getMax()));

        getChildren().addAll(progressBar,slider);
    }

    @Override protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        slider.minWidth(height);
        return progressBar.minWidth(height);
    }

    @Override protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        slider.minHeight(width);
        return progressBar.minHeight(width);
    }

    @Override protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        slider.prefWidth(height);
        return progressBar.prefWidth(height) + leftInset + rightInset;
    }

    @Override protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        slider.prefHeight(width);
        return progressBar.prefHeight(width) + topInset + bottomInset;
    }

    @Override protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
    }

    @Override protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    @Override protected void layoutChildren( double x, double y, double w, double h) {
        slider.resizeRelocate(x,y,w,h);
        progressBar.resizeRelocate(x,y,w,h);
    }
}
