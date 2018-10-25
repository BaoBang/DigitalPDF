/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsign.utils;

import java.io.File;

/**
 *
 * @author BaoBang
 */
public class FileUpload {
   private File fileUpload;
   private boolean isSuccess = true;

    public FileUpload() {
    }

    public FileUpload(File fileUpload) {
        this.fileUpload = fileUpload;
    }
    
    public File getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(File fileUpload) {
        this.fileUpload = fileUpload;
    }

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
   
   
}
