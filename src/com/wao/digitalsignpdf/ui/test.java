/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsignpdf.ui;

/**
 *
 * @author BaoBang
 */
public class test {
    public static void main(String[] args) {
        if(args.length > 0){
            for (int i = 0; i < args.length; i++) {
                System.out.println(args[i]);
            }
        }else{
            System.out.println("null");
        }
    }
}
