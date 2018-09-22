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
 */
public class FileResponse {

    @SerializedName("file_name")
    private String fileName;
    @SerializedName("link")
    private String link;
    @SerializedName("size")
    private double size;

    public FileResponse(String fileName, String link, double size) {
        this.fileName = fileName;
        this.link = link;
        this.size = size;
    }

    public FileResponse() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    @Override
    public String toString() {
        String s = "File name: " + fileName + "\r\n"
                + "Link: " + link + "\r\n"
                + "Size: " + size + "\r\n";
        return s; //To change body of generated methods, choose Tools | Templates.
    }

}
