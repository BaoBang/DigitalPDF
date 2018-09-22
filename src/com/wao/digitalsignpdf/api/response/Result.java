/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsignpdf.api.response;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author BaoBang
 * @param <T>
 */
public class Result<T> {

    @SerializedName("Result")
    private Data<T> result;

    public Result() {
    }

    /**
     *
     * @param result
     */
    public Result(Data<T> result) {
        this.result = result;
    }

    /**
     *
     * @return
     */
    public Data<T> getResult() {
        return result;
    }

    /**
     *
     * @param result
     */
    public void setResult(Data<T> result) {
        this.result = result;
    }
}
