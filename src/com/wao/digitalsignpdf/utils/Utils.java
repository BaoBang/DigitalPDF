/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsignpdf.utils;

import com.wao.digitalsignpdf.errorexception.CanNotGetKeyStoreException;
import com.wao.digitalsignpdf.errorexception.URLInvalidException;
import java.io.File;
import java.io.FileNotFoundException;
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

/**
 *
 * @author BaoBang
 */
public class Utils {

    public static KeyStore getKeyStore() throws CanNotGetKeyStoreException {
        KeyStore keyStore = null;
        try {
            String osName = System.getProperty("os.name");
            if (osName.contains("Window")) {
                keyStore = KeyStore.getInstance("Windows-MY");
                keyStore.load(null, null);
            } else if (osName.contains("Linux")) {

            }
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            throw new CanNotGetKeyStoreException(MessageConstant.KEYSTORE_NOT_FOUND_EXCEPTION, e);
        }
        return keyStore;
    }

    public static List<String> getKeystores() throws CanNotGetKeyStoreException {
        List<String> keys = new ArrayList<>();
        try {
            KeyStore personalKS = getKeyStore();
            if (personalKS == null) {
                return keys;
            }
            Enumeration<String> enums = personalKS.aliases();
            keys = Collections.list(enums);
        } catch (KeyStoreException ex) {
            throw new CanNotGetKeyStoreException(MessageConstant.KEYSTORE_NOT_FOUND_EXCEPTION, ex);
        }

        return keys;
    }

    public static File getFileFromURL(String order) throws URLInvalidException {
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
        } catch (MalformedURLException ex) {
            throw new URLInvalidException(order + " không đúng định dạng", ex);
        } catch (FileNotFoundException ex) {
            throw new URLInvalidException("Không tìm thấy file tại " + order, ex);
        } catch (IOException ex) {
            throw new URLInvalidException(MessageConstant.NOT_CONNECT_TO_SERVER_EXCEPTION, ex);
        }
    }
}
