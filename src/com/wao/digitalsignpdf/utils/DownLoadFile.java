/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsignpdf.utils;

import java.io.File;
import java.util.concurrent.Callable;

/**
 *
 * @author BaoBang
 */
public class DownLoadFile implements Callable<File> {
    private final String linkFile;
    public DownLoadFile(String linkFile) {
        this.linkFile = linkFile;
    }

    @Override
    public File call() throws Exception {
        File file = Utils.getFileFromURL(linkFile);
        return file;
    }

}
