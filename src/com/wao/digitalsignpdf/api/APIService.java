/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsignpdf.api;

import com.wao.digitalsignpdf.api.response.Bill;
import java.util.List;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 *
 * @author BaoBang
 */
public interface APIService {

    @GET("/bill")
    Call<EndPoint<List<Bill>>> getBills();

    @POST("/upload/")
    Call<EndPoint<String>> upload(@Body RequestBody body);
}
