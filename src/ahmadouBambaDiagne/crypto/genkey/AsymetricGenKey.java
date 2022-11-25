/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ahmadouBambaDiagne.crypto.genkey;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
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

/**
 *
 * @author hp
 */
public class AsymetricGenKey {

    public static String genKey(String algorithm, String filePath, int keySize) {
        KeyPairGenerator keyPairGenerator = null;
        String privKeyoperation = null;
        String pubKeyoperation = null;
        String privFilePath = null;
        Key publicKey = null;
        Key privateKey = null;

        try {
            keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AsymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            return "Algorithme inexistant";
        }
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        if ("RSA".equals(algorithm)) {

            publicKey = (RSAPublicKey) keyPair.getPublic();
            privateKey = (RSAPrivateKey) keyPair.getPrivate();

        } else if ("DSA".equals(algorithm)) {
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        }
        Path pathToAFile = Paths.get(filePath).getFileName();
        String parentDirectory = new File(filePath).getParentFile().getAbsolutePath();
        pubKeyoperation = saveKey(publicKey, filePath);
        privFilePath = String.join(System.getProperty("file.separator"), parentDirectory, "(key_priv)") + pathToAFile;
        privKeyoperation = saveKey(privateKey, privFilePath);

        if (pubKeyoperation.equals("SUCCESSFUL") && privKeyoperation.equals("SUCCESSFUL")) {
            System.out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            System.out.println(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
            return "SUCCESSFUL";
        } else if (pubKeyoperation.equals("SUCCESSFUL") && !privKeyoperation.equals("SUCCESSFUL")) {
            File f = new File(privFilePath);
            f.delete();
            return privKeyoperation;
        } else if (!pubKeyoperation.equals("SUCCESSFUL") && privKeyoperation.equals("SUCCESSFUL")) {
            File f = new File(filePath);
            f.delete();
            return pubKeyoperation;
        } else if (!pubKeyoperation.equals("SUCCESSFUL") && !privKeyoperation.equals("SUCCESSFUL")) {
            return String.join("\n", pubKeyoperation, privKeyoperation);
        }

        return "";
    }

    private static String saveKey(Key key, String filePath) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AsymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            return "Fichier introuvable";
        }
        System.out.println("key format : " + key.getFormat());
        if (key.getFormat().equalsIgnoreCase("x.509")) {
            try {
                fileOutputStream.write(key.getEncoded());
            } catch (IOException ex) {
                Logger.getLogger(AsymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
                return "Erreur lors de l'ecriture du fichier.Veuillez réessayer";
            }
        } else if (key.getFormat().equalsIgnoreCase("PKCS#8")) {
            try {
                fileOutputStream.write(key.getEncoded());
            } catch (IOException ex) {
                Logger.getLogger(AsymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
                return "Erreur lors de l'ecriture du fichier.Veuillez réessayer";
            }
        }
        try {
            fileOutputStream.close();
            return "SUCCESSFUL";
        } catch (IOException ex) {
            Logger.getLogger(AsymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            return "Erreur lors de la fermeture du fichier.Regardez s'il n'est pas utilisé par un autre programme";

        }

    }

    public static HashMap<String, RSAPublicKey> getPublicKey(String filePath) {
        HashMap<String, RSAPublicKey> result = new HashMap<>();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AsymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Fichier introuvable", null);
            return result;
        }
        byte[] b = null;
        try {
            b = new byte[fileInputStream.available()];
        } catch (IOException ex) {
            Logger.getLogger(AsymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreur d'operations avec le fichier", null);
            return result;
        }
        try {
            fileInputStream.read(b);
        } catch (IOException ex) {
            Logger.getLogger(AsymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreur d'operations avec le fichier", null);
            return result;
        }
        X509EncodedKeySpec publiKeySpec = new X509EncodedKeySpec(b);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AsymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Algorithme inexistant", null);
            return result;
        }
        RSAPublicKey publicKey = null;
        try {
            publicKey = (RSAPublicKey) keyFactory.generatePublic(publiKeySpec);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(AsymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Clé invalide", null);
            return result;
        }
        result.put("SUCCESSFUL", publicKey);
        return result;
    }

    public static HashMap<String, RSAPrivateKey> getPrivateKey(String filePath) {
        HashMap<String, RSAPrivateKey> result = new HashMap<>();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AsymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Fichier introuvable", null);
            return result;
        }
        byte[] b = null;
        try {
            b = new byte[fileInputStream.available()];
        } catch (IOException ex) {
            Logger.getLogger(AsymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreur d'operations avec le fichier", null);
            return result;
        }
        try {
            fileInputStream.read(b);
        } catch (IOException ex) {
            Logger.getLogger(AsymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreur d'operations avec le fichier", null);
            return result;
        }
        PKCS8EncodedKeySpec privaKeySpec = new PKCS8EncodedKeySpec(b);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AsymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Algorithme inexistant", null);
            return result;
        }
        RSAPrivateKey privateKey = null;
        try {
            privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privaKeySpec);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(AsymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Clé invalide", null);
            return result;
        }
        result.put("SUCCESSFUL", privateKey);
        return result;
    }
}
