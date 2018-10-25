/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsign.errorexception;

/**
 *
 * @author BaoBang
 */
public class SignXmlFailException extends Exception{

    public SignXmlFailException(String message) {
        super(message);
    }

    public SignXmlFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
