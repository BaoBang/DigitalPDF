/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsignpdf.utils;

import com.wao.digitalpdf.callback.UploadFileCallBack;
import com.wao.digitalsignpdf.CreateSignature;
import com.wao.digitalsignpdf.api.APIService;
import com.wao.digitalsignpdf.api.EndPoint;
import com.wao.digitalsignpdf.ui.PanelKeyStoreList;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author BaoBang
 */
public class UploadFile extends Thread {

    private CreateSignature signing;
    private APIService aPIService;
    private File file;
    private UploadFileCallBack callBack;

    public UploadFile(CreateSignature signing, APIService apiService, File outFile, UploadFileCallBack callBack) {
        this.aPIService = apiService;
        this.file = outFile;
        this.callBack = callBack;
        this.signing = signing;
    }

    @Override
    public void run() {
        try {
            super.run();
            //To change body of generated methods, choose Tools | Templates.
            String name = file.getName();
            String substring = name.substring(0, name.lastIndexOf('.'));
            File outFile = new File(file.getParent(), substring + "_signed.pdf");
            signing.signDetached(file, outFile, null);
//            file.delete();
            RequestBody requestFile
                    = RequestBody.create(
                            MediaType.parse("multipart/form-data"),
                            outFile
                    );
            RequestBody data = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", outFile.getName(), requestFile)
                    .build();

            aPIService.upload(data).enqueue(new Callback<EndPoint<String>>() {
                @Override
                public void onResponse(Call<EndPoint<String>> call, Response<EndPoint<String>> response) {

                    System.out.println(response.body().getMessage());
                    System.out.println(response.body().getData());
                    outFile.delete();
                    callBack.onSuccess();
                }

                @Override
                public void onFailure(Call<EndPoint<String>> call, Throwable t) {
                    callBack.onFailed(t.getMessage());
                    System.out.println(t.getMessage());
                    outFile.delete();

                }
            });
        } catch (IOException ex) {
            callBack.onFailed(ex.getMessage());
        }
    }

}
