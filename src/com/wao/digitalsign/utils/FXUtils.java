/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsign.utils;

import com.wao.digitalsign.callback.ConfirmCallBack;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author BaoBang
 */
public class FXUtils {

    public static void showMessageComfirmation(String title, String message, ConfirmCallBack callBack) {

        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(AppConstants.IMAE_LOGO));
            alert.setTitle(title);
            alert.setHeaderText(message);

            // option != null.
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == ButtonType.OK) {
                callBack.onOkButton();
            } else if (option.get() == ButtonType.CANCEL) {
                callBack.onCancelButton();
            }
        });

    }

    public static void showMessage(String title, String message, String detailm, Alert.AlertType alertType) {

        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(AppConstants.IMAE_LOGO));
            alert.setTitle(title);
            alert.setHeaderText(message);
            if (!detailm.isEmpty()) {
                alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(detailm)));
            }
            alert.showAndWait();
        });

    }

    public static void showMessageAndEndProgram(String title, String message, String detailm, Alert.AlertType alertType) {

        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(AppConstants.IMAE_LOGO));
            alert.setTitle(title);
            alert.setHeaderText(message);
            if (!detailm.isEmpty()) {
                alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(detailm)));
            }
            alert.showAndWait();
            System.exit(0);
        });

    }

}
