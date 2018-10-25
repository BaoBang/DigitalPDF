/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsign.callback;

import com.wao.digitalsign.api.response.Data;
import com.wao.digitalsign.api.response.FileResponse;

/**
 *
 * @author BaoBang
 */
public interface UploadFileCallBack {
    void onSuccess(Data<FileResponse> fileResponse);
    void onFailed(String message);
}
