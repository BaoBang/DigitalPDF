/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsignpdf.api.response;

/**
 *
 * @author BaoBang
 */
public class Bill {

    private int id;
    private String link;

    public Bill() {
    }

    public Bill(int id, String link) {
        this.id = id;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        String[] items = link.split("\\\\");
        if (items.length > 0) {
            String name = items[items.length - 1];
            return name.substring(0, name.lastIndexOf('.'));
        }
        return link; //To change body of generated methods, choose Tools | Templates.
    }

}