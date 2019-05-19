package com.nju.Service;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.*;

/**
 * @Author shisj
 * @Date: 2018/9/11 13:10
 */
public class ImageBase64Service {
    public static String getBase64OfImage(File file){
        byte[] data = null;

        try (InputStream inputStream = new FileInputStream(file)){
            data = new byte[inputStream.available()];
            inputStream.read(data);
        } catch (IOException e){
            e.printStackTrace();
        }
        return new String(Base64.encode(data));
    }
}
