/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ahmadouBambaDiagne.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;

/**
 *
 * @author hp
 */
public class Utils {

    private static final String DIGITS = "0123456789abcdef";

    /**
     * Return length many bytes of the passed in byte array as a hex string.
     *
     * @param data the bytes to be converted.
     * @param length the number of bytes in the data block to be converted.
     * @return a hex representation of length bytes of data.
     */
    public static String toHex(byte[] data, int length) {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i != length; i++) {
            int v = data[i] & 0xff;

            buf.append(DIGITS.charAt(v >> 4));
            buf.append(DIGITS.charAt(v & 0xf));
        }

        return buf.toString();
    }

    /**
     * Return the passed in byte array as a hex string.
     *
     * @param data the bytes to be converted.
     * @return a hex representation of data.
     */
    public static String toHex(byte[] data) {
        return toHex(data, data.length);
    }

    public static HashMap<String, String> convertFileToBase64(String filePath, String key) throws ClassNotFoundException {

        HashMap<String, String> result = new HashMap<>();
        InputStream finput = null;
        ObjectInputStream objectOutputStream = null;

        try {
            FileInputStream f = new FileInputStream(filePath);
            if ("Symetrique".equals(key)) {
                objectOutputStream = new ObjectInputStream(f);
            }

            finput = new FileInputStream(filePath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Fichier introuvable", "");
            return result;
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreur de manipulation de fichiers", "");
        }
        String encodedBase64 = null;
        try {
            if (key == "Symetrique") {
                SecretKey s = (SecretKey) objectOutputStream.readObject();
                encodedBase64 = Base64.getEncoder().encodeToString(s.getEncoded());
            } else {
                encodedBase64 = Base64.getEncoder().encodeToString(finput.readAllBytes());
            }
            result.put("SUCCESSFUL", encodedBase64);
            finput.close();
            return result;

        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreur de manipulation de fichiers", "");
            return result;

        }

    }

    public static String hexStringToBase64(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return Base64.getEncoder().encodeToString(data);
    }
}
