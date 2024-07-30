package com.craftsilicon.shumul.agency.data.security;

import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;

public class Util {
    public static String newEncrypt(String text) {
        String data = "";
        try {
            CryptLib _crypt = new CryptLib();
            String key = CryptLib.SHA256(new AppStaticKeys().longKeyValue(), 32);
            data = _crypt.encrypt(text, key, new AppStaticKeys().iv());
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = data.replace("\n", "");
        return data;
    }

    public static String encryptString(String decryptedString,
                                       String _key,
                                       String serverIV) {
        String data = "";

        try {

            CryptLib crypt = new CryptLib();
            String key = CryptLib.SHA256(_key, 32);
            data = crypt.encrypt(decryptedString, key, serverIV);
            data = data.replaceAll("\\r\\n|\\r|\\n", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String decryptLatest(String encryptedStr, String keyVal, boolean doBase64Decrypt, String v) {

        String data = "";
        String mv =  v;
        Log.e("KV-OLD", keyVal);
        Log.e("IV-OLD", v);
        try {
            CryptLib crypt = new CryptLib();
            String key = CryptLib.SHA256(keyVal, 32);
            data = crypt.decrypt(encryptedStr, key, mv);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //added decryption by base 64
        if (doBase64Decrypt) {
            data = base64Decrypt(data);
        }
        return data;
    }

    public static String base64Decrypt(String str) {
        // Receiving side
        String text = "";
        try {
            byte[] data = Base64.decode(str, Base64.DEFAULT);
            text = new String(data, StandardCharsets.UTF_8);
        } catch (Exception e) {
            text = "";
        }
        return text;
    }




}
