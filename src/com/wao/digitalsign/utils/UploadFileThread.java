/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsign.utils;

import com.wao.digitalsign.callback.UploadFileCallBack;
import com.wao.digitalsign.api.APIService;
import com.wao.digitalsign.api.response.Result;
import com.wao.digitalsign.api.response.FileResponse;
import java.io.File;
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
public class UploadFileThread extends Thread {

    private final APIService aPIService;
    private final File file;
    private final UploadFileCallBack callBack;

    public UploadFileThread(APIService apiService, File outFile, UploadFileCallBack callBack) {
        this.aPIService = apiService;
        this.file = outFile;
        this.callBack = callBack;
    }

    @Override
    public void run() {
        //To change body of generated methods, choose Tools | Templates.

        RequestBody requestFile
                = RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        file
                );
        MultipartBody.Part body
                = MultipartBody.Part.createFormData("file_upload", file.getName(), requestFile);
        aPIService.upload(body).enqueue(new Callback<Result<FileResponse>>() {
            @Override
            public void onResponse(Call<Result<FileResponse>> call, Response<Result<FileResponse>> response) {

                try {
                    if (response.body().getResult().getErrorCode() == 0) {
                        System.out.println(response.body().getResult().getData().toString());
                        callBack.onSuccess(response.body().getResult());
                    } else {
                        callBack.onFailed(response.body().getResult().getErrorDescription());
                    }
                } catch (Exception e) {
                    callBack.onFailed(e.getMessage());
                }
                if (file.exists()) {
                    file.delete();
                }
            }

            @Override
            public void onFailure(Call<Result<FileResponse>> call, Throwable t) {
                callBack.onFailed(t.getMessage());
                System.out.println(t.getMessage());
                if (file.exists()) {
                    file.delete();
                }
            }
        });
    }

}
