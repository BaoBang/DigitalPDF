/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsign.api;

/**
 *
 * @author BaoBang
 */
public class ApiUtils {

    private static final String BASE_URL = "http://api.eprocon.us/DigSin/";

    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
