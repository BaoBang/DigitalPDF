/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsignpdf.api.requestbody;

/**
 *
 * @author BaoBang
 */
public class GetFileBody {
    private String Id;

    public GetFileBody(String Id) {
        this.Id = Id;
    }

    public GetFileBody() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }
    
    
}
