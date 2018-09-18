/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalpdf.callback;

/**
 *
 * @author BaoBang
 */
public interface UploadFileCallBack {
    void onSuccess();
    void onFailed(String message);
}
