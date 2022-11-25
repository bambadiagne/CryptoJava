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
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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

/**
 *
 * @author hp
 */
public class AsymetricCipher {

    public static HashMap<String, String> crypt(String fileInput, String fileOutPut, RSAPublicKey publicKey, String algorithm) {
        Cipher cipher = null;
        HashMap<String, String> result = new HashMap<>();
        try {
            cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Algorithme inexistant", "");
            return result;
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Clé invalide", "");
            return result;
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(fileInput);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Fichier d'entrée introuvable", "");
            return result;
        }
        CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileOutPut);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Fichier de sortie introuvable", "");
            return result;
        }

        byte[] b = new byte[8];
        int i = 0;
        try {
            i = cipherInputStream.read(b);
        } catch (IOException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Fichier de sortie introuvable", "");
            return result;
        }
        while (i != -1) {
            try {
                fileOutputStream.write(b, 0, i);
                i = cipherInputStream.read(b);
            } catch (IOException ex) {
                Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
                result.put("Erreur dans des operations de manipulation de fichier", "");
                return result;
            }
        }

        try {
            fileInputStream.close();
            InputStream finput = new FileInputStream(fileOutPut);
            String cipherStr = Base64.getEncoder().encodeToString(finput.readAllBytes());
            result.put("SUCCESSFUL", cipherStr);
            finput.close();
            fileOutputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreur fermeture fichier", "");
            return result;
        }
        return result;
    }

    public static HashMap<String, String> cryptString(String plainText, String publicKey, String algorithm) {
        Cipher cipher = null;
        HashMap<String, String> result = new HashMap<>();
        RSAPublicKey pubKey = null;

        try {
            byte[] byteKey = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance(algorithm);
            pubKey = (RSAPublicKey) kf.generatePublic(X509publicKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);

            result.put("Algorithme inexistant ou clé invalide", "");
            return result;
        }

        try {
            cipher = Cipher.getInstance(algorithm);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Algorithme inexistant ou padding inexistant", "");
            return result;
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        } catch (InvalidKeyException e) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, e);
            result.put("Clé invalide", "");
            return result;
        }
        try {

            result.put("SUCCESSFUL", Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes("UTF-8"))));

        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Taille du block incorrect", "");
            return result;
        } catch (BadPaddingException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Mauvais padding", "");
            return result;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Encode non supporté", "");
            return result;
        }

        return result;
    }

    public static HashMap<String, String> decryptString(String plainText, String privateKey, String algorithm) {
        Cipher cipher = null;
        HashMap<String, String> result = new HashMap<>();
        RSAPrivateKey privKey = null;

        try {
            byte[] byteKey = Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec keySpecPv = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance(algorithm);
            privKey = (RSAPrivateKey) kf.generatePrivate(keySpecPv);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);

            result.put("Algorithme inexistant ou clé invalide", "");
            return result;
        }

        try {
            cipher = Cipher.getInstance(algorithm);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Algorithme inexistant ou padding inexistant", "");
            return result;
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, privKey);
        } catch (InvalidKeyException e) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, e);
            result.put("Clé invalide", "");
            return result;
        }
        try {

            result.put("SUCCESSFUL", new String(cipher.doFinal(Base64.getDecoder()
                    .decode(plainText))));
            return result;

        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Taille du block incorrect", "");
            return result;
        } catch (BadPaddingException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Mauvais padding", "");
            return result;
        }

    }

    public static HashMap<String, String> decrypt(String fileInput, String fileOutPut, RSAPrivateKey privateKey, String algorithm) {
        Cipher cipher = null;
        HashMap<String, String> result = new HashMap<>();

        try {
            cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Algorithme inexistant", "");
            return result;
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Clé invalide", "");
            return result;

        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(fileInput);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Fichier d'entrée introuvable", "");
            return result;
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileOutPut);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Fichier de sortie introuvable", "");
            return result;
        }
        CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);

        byte[] b = new byte[8];
        int i = 0;
        try {
            i = fileInputStream.read(b);
        } catch (IOException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreur dans des operations de manipulation de fichier", "");
            return result;
        }
        while (i != -1) {
            try {
                cipherOutputStream.write(b, 0, i);
                i = fileInputStream.read(b);
            } catch (IOException ex) {
                Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
                result.put("Erreur dans des operations de manipulation de fichier", "");
                return result;
            }
        }

        try {
            fileInputStream.close();
            cipherOutputStream.close();
            InputStream finput = new FileInputStream(fileOutPut);
            String plainText = new String(finput.readAllBytes());
            result.put("SUCCESSFUL", plainText);
            finput.close();

            return result;
        } catch (IOException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreur fermeture fichier", "");
            return result;
        }

    }
}
