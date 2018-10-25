/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsign.ui;

import com.wao.digitalsign.pdf.CreateSignature;
import com.wao.digitalsign.api.ApiUtils;
import com.wao.digitalsign.api.response.Bill;
import com.wao.digitalsign.api.response.Data;
import com.wao.digitalsign.api.response.FileResponse;
import com.wao.digitalsign.callback.UploadFileCallBack;
import com.wao.digitalsign.errorexception.CanNotGetKeyStoreException;
import com.wao.digitalsign.errorexception.SignFailedException;
import com.wao.digitalsign.errorexception.URLInvalidException;
import com.wao.digitalsign.utils.FileUpload;
import com.wao.digitalsign.utils.UploadFileThread;
import com.wao.digitalsign.utils.Utils;
import java.io.File;
import java.security.KeyStore;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author BaoBang
 */
public class RowItem extends Observable implements Runnable {

    private static final String[] STATUSES
            = {"", "Downloading", "Signing", "Uploading", "Error", "Completed"};

    public static int DEFAULT = 0;
    public static int DOWLOADING = 1;
    public static int SIGNING = 2;
    public static int UPLOADING = 3;
    public static int ERROR = 4;
    public static int COMPLETED = 5;

    private BooleanProperty isChecked;
    private String id;
    private String link;
    private StringProperty status;

    private int mState;

    public RowItem(Bill bill) {
        isChecked = new SimpleBooleanProperty(this, "isChecked", false);
        id = bill.getId();
        link = bill.getLink();
        mState = DEFAULT;
        status = new SimpleStringProperty(STATUSES[mState]);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked.set(isChecked);
    }

    public boolean getIsChecked() {
        return isChecked.get();
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setStatus(String status) {
        this.status.set(status);
        setChanged();
        notifyObservers();
    }

    public String getStatus() {
        return status.get();
    }

    public int getState() {
        return mState;
    }

    public void setState(int mState) {
        this.mState = mState;
        setStatus(STATUSES[this.mState]);
    }

    public boolean isFinshed() {
        return mState == COMPLETED || mState == ERROR;
    }

    @Override
    public void run() {
        try {

            // Tải file
            setState(DOWLOADING);

            File inFile = Utils.getFileFromURL(link);
            String name = inFile.getName();
            String substring = name.substring(0, name.lastIndexOf('.'));
            File outFile = new File(inFile.getParent(), substring + "_signed.pdf");

            // Kí file
            setState(SIGNING);
            KeyStore keystore = Utils.getKeyStore();
            CreateSignature signing = new CreateSignature(keystore, MainController.keyAlias, "".toCharArray());
            signing.setExternalSigning(true);
            signing.signDetached(inFile, outFile, null);

            // Sau khi kí file thì xóa file tải xuống
            if (inFile.exists()) {
                inFile.delete();
            }

            // Uploading file
            setState(UPLOADING);
            FileUpload fileUpload = new FileUpload(outFile);
            UploadFileThread file = new UploadFileThread(
                    ApiUtils.getAPIService(),
                    outFile, new UploadFileCallBack() {
                @Override
                public void onSuccess(Data<FileResponse> data) {
                    setState(COMPLETED);
                    System.out.println(data.getData().getLink());
                }

                @Override
                public void onFailed(String message) {
                    setState(ERROR);
                    fileUpload.setIsSuccess(false);
                }
            });
            file.start();

        } catch (CanNotGetKeyStoreException | URLInvalidException | SignFailedException ex) {
            setState(ERROR);
            Logger.getLogger(RowItem.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void start() {
        setState(DEFAULT);
        Thread thread = new Thread(this);
        thread.start();
    }

    public boolean isSucced() {
        return mState == COMPLETED;
    }

}
