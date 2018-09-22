/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsignpdf.utils;

import com.wao.digitalpdf.callback.UploadFileCallBack;
import com.wao.digitalsignpdf.CreateSignature;
import com.wao.digitalsignpdf.api.APIService;
import com.wao.digitalsignpdf.api.response.Result;
import com.wao.digitalsignpdf.api.response.FileResponse;
import java.io.File;
import java.io.IOException;
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
            MultipartBody.Part body
                    = MultipartBody.Part.createFormData("file_upload", file.getName(), requestFile);
            
            aPIService.upload(body).enqueue(new Callback<Result<FileResponse>>() {
                @Override
                public void onResponse(Call<Result<FileResponse>> call, Response<Result<FileResponse>> response) {
                    
                    try {
                        outFile.delete();
                        if (response.body().getResult().getErrorCode() == 0) {
                            System.out.println(response.body().getResult().getData().toString());
                            callBack.onSuccess(response.body().getResult());
                            file.delete();
                        } else {
                            callBack.onFailed(response.body().getResult().getErrorDescription());
                        }
                    } catch (Exception e) {
                        callBack.onFailed(e.getMessage());
                    }
                }
                
                @Override
                public void onFailure(Call<Result<FileResponse>> call, Throwable t) {
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
