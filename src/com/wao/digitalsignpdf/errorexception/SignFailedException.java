/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsignpdf.errorexception;

/**
 *
 * @author BaoBang
 */
public class SignFailedException extends Exception{

    public SignFailedException(String message) {
        super(message);
    }

    public SignFailedException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
