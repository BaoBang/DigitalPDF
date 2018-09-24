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
public class CanNotGetKeyStoreException extends Exception{

    public CanNotGetKeyStoreException(String message) {
        super(message);
    }

    public CanNotGetKeyStoreException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
