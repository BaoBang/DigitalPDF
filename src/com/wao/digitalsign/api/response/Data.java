/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsign.api.response;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author BaoBang
 * @param <T>
 */
public class Data<T> {

    @SerializedName("ErrorCode")
    private int errorCode;

    @SerializedName("ErrorDescription")
    private String errorDescription;

    @SerializedName("Data")
    private T data;

    public Data(int errorCode, String errorDescription, T data) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.data = data;
    }

    public Data() {
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
