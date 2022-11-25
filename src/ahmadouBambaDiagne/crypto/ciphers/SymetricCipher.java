/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ahmadouBambaDiagne.crypto.ciphers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author hp
 */
public class SymetricCipher {

    public static HashMap<String, String> crypt(String fileInput, String fileOutPut, SecretKey sk, String algorithm) {
        HashMap<String, String> result = new HashMap<>();
        //System.out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Algorithme inexistant", "");
            return result;

        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, sk);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Clé invalide", "");
            return result;
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(fileInput);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Fichier inexistant", "");
            return result;
        }
        CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileOutPut);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Fichier inexistant", "");
            return result;
        }

        byte[] b = new byte[8];
        int i = 0;
        try {
            i = cipherInputStream.read(b);
        } catch (IOException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreurs d'operation sur les fichiers", "");
            return result;
        }
        while (i != -1) {
            try {
                fileOutputStream.write(b, 0, i);
            } catch (IOException ex) {
                Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
                result.put("Erreurs d'operation sur les fichiers", "");
                return result;
            }
            try {
                i = cipherInputStream.read(b);
            } catch (IOException ex) {
                Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
                result.put("Erreurs d'operation sur les fichiers", "");
                return result;
            }
        }

        try {
            fileInputStream.close();
            InputStream finput = new FileInputStream(fileOutPut);
            String imageStr = Base64.getEncoder().encodeToString(finput.readAllBytes());
            finput.close();
            result.put("SUCCESSFUL", imageStr);
        } catch (IOException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreurs d'operation sur les fichiers", "");
            return result;
        }
        try {
            fileOutputStream.close();
            cipherInputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreurs d'operation sur les fichiers", "");
            return result;
        }

        return result;
    }

    public static HashMap<String, String> decrypt(String fileIn, String fileOut, SecretKey sk, String algorithm) {
        HashMap<String, String> result = new HashMap<>();
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Algorithme inexistant", "");
            return result;

        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, sk);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Clé invalide", "");
            return result;
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(fileIn);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Fichier inexistant", "");
            return result;
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileOut);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Fichier inexistant", "");
            return result;
        }
        CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);

        byte[] b = new byte[8];
        int i = 0;
        try {
            i = fileInputStream.read(b);
        } catch (IOException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreurs d'operation sur les fichiers", "");
            return result;

        }
        while (i != -1) {
            try {
                cipherOutputStream.write(b, 0, i);
            } catch (IOException ex) {
                Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
                result.put("Erreurs d'operation sur les fichiers", "");
                return result;

            }
            try {
                i = fileInputStream.read(b);
            } catch (IOException ex) {
                Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
                result.put("Erreurs d'operation sur les fichiers", "");
                return result;

            }
        }

        try {
            fileInputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreurs d'operation sur les fichiers", "");
            return result;

        }
        try {
            cipherOutputStream.close();
            InputStream finput = new FileInputStream(fileOut);
            String plainText = Base64.getEncoder().encodeToString(finput.readAllBytes());
            finput.close();

            result.put("SUCCESSFUL", plainText);
        } catch (IOException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreurs d'operation sur les fichiers", "");
            return result;

        }

        return result;
    }

    public static HashMap<String, String> cryptorDecryptString(String plainText, String encodedKey, String algorithm, int mode) {
        HashMap<String, String> result = new HashMap<>();
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, algorithm);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Algorithme inexistant", "");
            return result;
        }
        try {
            cipher.init(mode, secretKey);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Clé invalide", "");
            return result;
        }
        try {
            switch (mode) {
                case Cipher.ENCRYPT_MODE:
                    result.put("SUCCESSFUL", Base64.getEncoder()
                            .encodeToString(cipher.doFinal(plainText.getBytes("UTF-8"))));
                    return result;

                case Cipher.DECRYPT_MODE:
                    result.put("SUCCESSFUL", new String(cipher.doFinal(Base64.getDecoder()
                            .decode(plainText))));
                    return result;
                default:
                    throw new AssertionError();
            }

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Encodage non supporté", "");
            return result;
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Taille du block incorrect", "");
            return result;
        } catch (BadPaddingException ex) {
            Logger.getLogger(SymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Mauvais padding", "");
            return result;
        }
    }

}
