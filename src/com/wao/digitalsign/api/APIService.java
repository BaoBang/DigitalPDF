/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsign.api;

import com.wao.digitalsign.api.response.Result;
import com.wao.digitalsign.api.requestbody.GetFileBody;
import com.wao.digitalsign.api.response.FileResponse;
import java.util.List;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 *
 * @author BaoBang
 */
public interface APIService {

    @POST("GetFiles")
    Call<Result<List<String>>> getBills(@Body GetFileBody body);

    @Multipart
    @POST("Upload")
    Call<Result<FileResponse>> upload(@Part MultipartBody.Part file);
}
