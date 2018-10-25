/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsign.api.response;

/**
 *
 * @author BaoBang
 */
public class Bill {

    private String id;
    private String link;

    public Bill() {
    }

    public Bill(String id, String link) {
        this.id = id;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        String[] items = link.split("/");
        if (items.length > 0) {
            String name = items[items.length - 1];
            return name;
        }
        return link; //To change body of generated methods, choose Tools | Templates.
    }

}
