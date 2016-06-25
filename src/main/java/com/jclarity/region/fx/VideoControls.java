package com.jclarity.region.fx;


import com.jclarity.region.FrameCounter;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import java.util.Timer;
import java.util.TimerTask;

public class VideoControls extends HBox {

    private ObjectProperty<ObservableList<ToggleButton>> buttons;
    private boolean playing = false;
    private Timer timer = new Timer();
    private Button playPause;
    private FrameCounter frameCounter;
    final TimeSlider slider;

    public VideoControls() {
        getStyleClass().add("video-controls");
        slider = new TimeSlider();
        setButtons();
//        buttons.addListener(new InvalidationListener() {
//            @Override
//            public void invalidated(Observable observable) {
//                updateButtons();
//            }
//        });
    }

    public void setFrameCounter(FrameCounter frameCounter) {
        this.frameCounter = frameCounter;
        slider.setFrameCounter( frameCounter);
    }

    private void setButtons() {

        ToggleButton openFileDialog = new ToggleButton("open");

        openFileDialog.setOnAction(event -> {
            pause();
            System.out.println("open file" + event.toString());
        });


        Image image;
        try {
        image = new Image(getClass().getResourceAsStream("step_to_beginning.png"));
        } catch (Throwable t) {
            System.out.println(t);
        }
        //ToggleButton skipToBeginning = new ToggleButton("|<", new ImageView(image));  //rewind
        ToggleButton skipToBeginning = new ToggleButton("|<");  //rewind
        skipToBeginning.setOnAction(event -> {
            pause();
            frameCounter.set(0);
            System.out.println(event.toString() + ", counter: " + frameCounter.getFrameIndex());
        });

        image = new Image(getClass().getResourceAsStream("step_backwards.png"));
        //ToggleButton rewind = new ToggleButton("", new ImageView(image));
        ToggleButton rewind = new ToggleButton("<<");
        rewind.setOnAction(event -> {
            pause();
            timer.cancel();
            frameCounter.stepBackwards();
            System.out.println(event.toString() + ", counter: " + frameCounter.getFrameIndex());
        });


        image = new Image(getClass().getResourceAsStream("play.png"));
        //playPause = new Button("",new ImageView(image));
        playPause = new Button(">");
        playPause.setOnAction(event -> {
            new Timer().schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            frameCounter.stepForward();
                        }
                    }, 0, 500);
            /*
            jvm.getView
                                        if (cursor < jvm.getNumberOfViews()) {
                                Platform.runLater(() -> colorGridPane(grid) );
                            } else {
                                this.cancel();
                            }
             */
        });


        image = new Image(getClass().getResourceAsStream("step_forward.png"));
        //ToggleButton fastForward = new ToggleButton("",new ImageView(image)); //
        ToggleButton fastForward = new ToggleButton(">>"); //
        fastForward.setOnAction(event -> {
            pause();
            frameCounter.stepForward();
            System.out.println(event.toString() + ", counter: " + frameCounter.getFrameIndex());
        });


        image = new Image(getClass().getResourceAsStream("step_to_end.png"));
        //ToggleButton skipToEnd = new ToggleButton("",new ImageView(image)); //end
        ToggleButton skipToEnd = new ToggleButton(">|"); //end
        skipToEnd.setOnAction(event -> {
            pause();
            frameCounter.stepToEnd();
            System.out.println(event.toString() + ", counter: " + frameCounter.getFrameIndex());
        });
        getChildren().addAll(openFileDialog, skipToBeginning, rewind, playPause, slider, fastForward, skipToEnd);
    }

    private void pause() {
        if ( playing) {
            playPause.setText(">");
            timer.cancel();
            playing = false;
        }
    }

    private void playPause() {
        if ( ! playing) {
            playPause.setText("||");
            timer = new Timer();
            timer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            if (! frameCounter.atEnd()) {
                                frameCounter.stepForward();
                                System.out.println("counter: " + frameCounter.getFrameIndex());
                            } else {
                                this.cancel();
                            }
                        }
                    }, 0, 500);
            playing = true;
        } else
            pause();
    }

    private void updateButtons() {
//        for ( int i = 0; i < getButtons().size(); i++) {
//            ToggleButton t = getButtons().get(i);
//            t.setToggleGroup(group);
//            getChildren().add(t);
//            if (i == buttons.get().size() - 1) {
//                t.getStyleClass().add((i == 0) ? "only-button" : "last-button");
//            } else if (i == 0)
//                t.getStyleClass().add("first-button");
//            else
//                t.getStyleClass().add("middle-button")
//        }
    }
}
