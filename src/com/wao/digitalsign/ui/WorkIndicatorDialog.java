/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsign.ui;

import com.wao.digitalsign.utils.AppConstants;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.function.ToIntFunction;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class WorkIndicatorDialog<P> {

    private Task animationWorker;
    private Task<Integer> taskWorker;

    private final ProgressIndicator progressIndicator = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);
    private final Stage dialog = new Stage(StageStyle.UNDECORATED);
    private final Label label = new Label();
    private final Group root = new Group();
    private final Scene scene = new Scene(root, 330, 120, Color.WHITE);
    private final BorderPane mainPane = new BorderPane();
    private final VBox vbox = new VBox();

    /**
     * Placing a listener on this list allows to get notified BY the result when
     * the task has finished.
     */
    public ObservableList<Integer> resultNotificationList = FXCollections.observableArrayList();

    public Integer resultValue;

    /**
     *
     * @param owner
     * @param label
     */
    public WorkIndicatorDialog(Window owner, String label) {
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(owner);
        dialog.setResizable(false);
        dialog.getIcons().add(new Image(AppConstants.IMAE_LOGO));

        this.label.setText(label);
        this.label.setId("label");
        
        mainPane.setId("mainPane");

        scene.getStylesheets().add(AppConstants.CSS_DIALOG);
    }

    /**
     *
     * @param c
     */
    public void addTaskEndNotification(Consumer<Integer> c) {
        resultNotificationList.addListener((ListChangeListener<? super Integer>) n -> {
            resultNotificationList.clear();
            c.accept(resultValue);
        });
    }

    /**
     *
     * @param parameter
     * @param func
     */
    public void exec(P parameter, ToIntFunction func) {
        setupDialog();
        setupAnimationThread();
        setupWorkerThread(parameter, func);
    }

    /**
     *
     */
    private void setupDialog() {
        root.getChildren().add(mainPane);
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMinSize(330, 120);
        vbox.getChildren().addAll(label, progressIndicator);
        mainPane.setTop(vbox);
        dialog.setScene(scene);

        dialog.setOnHiding(event -> {
            /* Gets notified when task ended, but BEFORE 
                     result value is attributed. Using the observable list above is 
                     recommended. */ });

        dialog.show();
    }

    /**
     *
     */
    private void setupAnimationThread() {

        animationWorker = new Task() {
            @Override
            protected Object call() throws Exception {
                /*
                This is activated when we have a "progressing" indicator.
                This thread is used to advance progress every XXX milliseconds.
                In case of an INDETERMINATE_PROGRESS indicator, it's not of use.
                for (int i=1;i<=100;i++) {
                    Thread.sleep(500);
                    updateMessage();
                    updateProgress(i,100);
                }
                 */
                return true;
            }
        };

        progressIndicator.setProgress(0);
        progressIndicator.progressProperty().unbind();
        progressIndicator.progressProperty().bind(animationWorker.progressProperty());

        animationWorker.messageProperty().addListener((observable, oldValue, newValue) -> {
            // Do something when the animation value ticker has changed
        });

        new Thread(animationWorker).start();
    }

    /**
     *
     */
    private void setupWorkerThread(P parameter, ToIntFunction<P> func) {

        taskWorker = new Task<Integer>() {
            @Override
            public Integer call() {
                return func.applyAsInt(parameter);
            }
        };

        new Thread(taskWorker).start();
    }

    public void stop() {
        animationWorker.cancel(true);
        progressIndicator.progressProperty().unbind();
        dialog.close();
        try {
            resultValue = taskWorker.get();
            resultNotificationList.add(resultValue);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * For those that like beans :)
     *
     * @return
     */
    public Integer getResultValue() {
        return resultValue;
    }

}
