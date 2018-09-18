/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsignpdf.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BaoBang
 */
public class Utils {

    public static List<String> getKeystores() {
        List<String> keys = new ArrayList<>();
        try {
            KeyStore personalKS = KeyStore.getInstance("Window-MY");
            personalKS.load(null, null);
            Enumeration<String> enums = personalKS.aliases();
            keys = Collections.list(enums);
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return keys;
    }

    public static File getFileFromURL(String order) {
        try {
            URL url = new URL(order);
            URLConnection connection = url.openConnection();
            InputStream in = connection.getInputStream();
            String fileName = order.substring(order.lastIndexOf('/') + 1, order.length());
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buf = new byte[512];
            while (true) {
                int len = in.read(buf);
                if (len == -1) {
                    break;
                }
                fos.write(buf, 0, len);
            }
            in.close();
            fos.flush();
            fos.close();
            return file;
        } catch (IOException ex) {
            File myFile = new File(order);
            return myFile;
        }

    }
}
