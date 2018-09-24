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
public class URLInvalidException extends Exception {

    public URLInvalidException(String message) {
        super(message);
    }

    public URLInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

}
