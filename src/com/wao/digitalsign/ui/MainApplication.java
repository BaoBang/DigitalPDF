/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsign.ui;

import com.wao.digitalsign.utils.FXUtils;
import com.wao.digitalsign.callback.ApiCallBack;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wao.digitalsign.errorexception.CanNotGetKeyStoreException;
import com.wao.digitalsign.utils.AppConstants;
import com.wao.digitalsign.utils.MessageConstant;
import java.util.Map;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author BaoBang
 */
public class MainApplication extends Application {

    private MainController mMainController;
    private WorkIndicatorDialog mWorkIndicatorDialog = null;
    private static String ID;
    Stage mStage;

    boolean isSuccessed = false;

    @Override
    public void start(Stage stage) throws Exception {
        stage.getIcons().add(new Image(AppConstants.IMAE_LOGO));
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource(AppConstants.LAYOUT_MAIN));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(AppConstants.CSS_MAIN);
        stage.setScene(scene);
        stage.setTitle(AppConstants.APP_NAME);

        stage.setOnCloseRequest((WindowEvent event) -> {
            event.consume();
            mMainController.stop();
        });

        mMainController = loader.<MainController>getController();

        mWorkIndicatorDialog = new WorkIndicatorDialog(
                scene.getWindow(),
                MessageConstant.MESSAGE_LOADING);

        mWorkIndicatorDialog.addTaskEndNotification(result -> {
            System.out.println(result);
            mWorkIndicatorDialog = null; // don't keep the object, cleanup
        });
        mWorkIndicatorDialog.exec("", (Object inputParam) -> {

            try {
                mMainController.setId(ID);
                mMainController.loadData(new ApiCallBack() {
                    @Override
                    public void onSucess() {
                        isSuccessed = true;
                        Platform.runLater(() -> {
                            mWorkIndicatorDialog.stop();
                            stage.show();

                        });
                    }

                    @Override
                    public void onFailed(String message) {
                        Platform.runLater(() -> {
                            mWorkIndicatorDialog.stop();

                            FXUtils.showMessageAndEndProgram(
                                    MessageConstant.ERROR_TITLE,
                                    message,
                                    "",
                                    Alert.AlertType.ERROR);
                        });

                    }
                });
            } catch (CanNotGetKeyStoreException ex) {
                Platform.runLater(() -> {
                    mWorkIndicatorDialog.stop();
                    FXUtils.showMessageAndEndProgram(
                            MessageConstant.ERROR_TITLE,
                            "Tải dữ liệu thất bại",
                            ex.toString(),
                            Alert.AlertType.ERROR);
                });

            }

            return 1;
        });

    }

    @Override
    public void stop() {
        mMainController.stop();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ID = getIdFromJWT(args.length > 0 ? args[0] : "");
        launch(ID);
    }

    private static String getIdFromJWT(String token) {
        Algorithm algorithm = Algorithm.HMAC256("!@#123");
        JWTVerifier verifier = JWT.require(algorithm)
                .build(); //Reusable verifier instance

        //        DecodedJWT jwt = verifier.verify(args[0]);
        DecodedJWT jwt = verifier.verify("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ3YW8iLCJhdWQiOiJiYW9iYW5nIiwiaWQiOiIyOTUwMSwgMjk1MDIsIDI5NTAzIiwidXJsIjoiaHR0cDovL2FwaS5lcHJvY29uLnVzL0RpZ1Npbi8ifQ.Gm3zxBnBOBAqz1oDbtJNZXqP3RDeiSj2LilFJc3egm8");
        Map<String, Claim> headerClaims = jwt.getClaims();
        return headerClaims.get("id").as(String.class);
    }

}
