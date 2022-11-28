/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ahmadouBambaDiagne.crypto.signature;

import ahmadouBambaDiagne.crypto.ciphers.AsymetricCipher;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;

/**
 *
 * @author hp
 */
public class CryptoSignature {

    public static HashMap<String, String> signRSA(String fileInput, String fileOutPut, RSAPrivateKey privateKey) {
        HashMap<String, String> result = new HashMap<>();
        FileInputStream fileInputStream = null;
        Signature signature = null;
        try {
            fileInputStream = new FileInputStream(fileInput);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Fichier d'entrée introuvable", "");
            return result;
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileOutPut);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Fichier de sortie introuvable", "");
            return result;
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA256");
            signature = Signature.getInstance("SHA256WITHRSA");

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Algorithme inexistant ou non supporté", "");
            return result;
        }
        DigestInputStream digestInputStream = new DigestInputStream(fileInputStream, messageDigest);

        try {
            signature.initSign(privateKey);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Clé invalide", "");
            return result;
        }
        byte[] hash = null;
        try {
            hash = new byte[fileInputStream.available()];
            digestInputStream.read(hash);

        } catch (IOException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreurs d'operation de fichiers.", "");
            return result;

        }
        byte[] sign = null;
        try {
            signature.update(hash);
            sign = signature.sign();

        } catch (SignatureException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreurs dans la signature", "");
            return result;
        }

        try {
            fileOutputStream.write(sign);
            fileInputStream.close();
            InputStream finput = new FileInputStream(fileOutPut);
            String encodedSignature = Base64.getEncoder().encodeToString(finput.readAllBytes());
            result.put("SUCCESSFUL", encodedSignature);
            fileOutputStream.close();

            return result;

        } catch (IOException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreurs d'operation de fichiers.", "");
            return result;
        }
    }

    public static HashMap<String, String> signText(String plainText, String privateKey) {
        HashMap<String, String> result = new HashMap<>();
        Signature privateSignature = null;
        Cipher cipher = null;
        PrivateKey privKey = null;

        try {
            byte[] byteKey = Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec keySpecPv = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            privKey = kf.generatePrivate(keySpecPv);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);

            result.put("Algorithme inexistant ou clé invalide", "");
            return result;
        }
        try {
            privateSignature = Signature.getInstance("SHA256WITHRSA");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Algorithme non supporté", "");
            return result;
        }
        try {
            privateSignature.initSign(privKey);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Clé invalide", "");
            return result;
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA256");
            byte[] hash = messageDigest.digest(plainText.getBytes(StandardCharsets.UTF_8));
            privateSignature.update(hash);
        } catch (SignatureException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreur dans la création de la signature", "");
            return result;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Algorithme non supporté", "");
            return result;
        }

        byte[] signature;
        try {
            signature = privateSignature.sign();
            System.out.println("TEST : " + Base64.getEncoder().encodeToString(signature));
            result.put("SUCCESSFUL", Base64.getEncoder().encodeToString(signature));
            return result;
        } catch (SignatureException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreur dans la création de la signature", "");
            return result;
        }

    }

    public static String verifyRSASignature(String message, String sign, RSAPublicKey publicKey) {
        FileInputStream fileInputStreamMessage = null;
        FileInputStream fileInputStreamSign = null;
        try {
            fileInputStreamMessage = new FileInputStream(message);
            fileInputStreamSign = new FileInputStream(sign);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Fichier d'entrée ou de sortie introuvable";
        }
        MessageDigest messageDigest = null;
        Signature signature = null;

        try {
            messageDigest = MessageDigest.getInstance("SHA256");
            signature = Signature.getInstance("SHA256WITHRSA");

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Algorithme inexistant ou non supporté";

        }
        DigestInputStream digestInputStream = new DigestInputStream(fileInputStreamMessage, messageDigest);

        try {
            signature.initVerify(publicKey);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Clé invalide";
        }

        byte[] hash = null;
        byte[] s = null;

        try {
            hash = new byte[fileInputStreamMessage.available()];
            digestInputStream.read(hash);
            s = new byte[fileInputStreamSign.available()];
            fileInputStreamSign.read(s);

        } catch (IOException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Erreurs d'operations de fichier";
        }

        try {
            signature.update(hash);
        } catch (SignatureException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Erreur(s) dans la verification de la signature";
        }

        try {
            fileInputStreamMessage.close();
            fileInputStreamSign.close();

        } catch (IOException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Erreurs dans la fermeture des fichiers";
        }
        try {
            boolean verify = signature.verify(s);
            if (verify) {
                return "Signature valide";
            } else {
                return "Signature invalide";
            }
        } catch (SignatureException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Erreur(s) dans la verification de la signature";
        }

    }

    public static String verifyStringSignature(String plainText, String signature, String publicKey) {
        RSAPublicKey pubKey = null;
        Signature publicSignature = null;
        try {
            byte[] byteKey = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            pubKey = (RSAPublicKey) kf.generatePublic(X509publicKey);
            publicSignature = Signature.getInstance("SHA256withRSA");

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            return "Algorithme inexistant ou clé invalide";
        }
        try {
            publicSignature.initVerify(pubKey);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Clé invalide";
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA256");
            byte[] hash = messageDigest.digest(plainText.getBytes(StandardCharsets.UTF_8));

            publicSignature.update(hash);
        } catch (SignatureException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Erreur dans la verification de la signature";
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Algorithme inexistant ou clé invalide";
        }

        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        try {
            if (publicSignature.verify(signatureBytes)) {
                return "Signature valide";
            } else {
                return "Signature invalide";
            }
        } catch (SignatureException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Erreur dans la verification de la signature";
        }
    }

    public static HashMap<String, String> signDSA(String fileInput, String fileOutPut, PrivateKey privateKey) {
        HashMap<String, String> result = new HashMap<>();
        FileInputStream fileInputStream = null;
        Signature signature = null;
        try {
            fileInputStream = new FileInputStream(fileInput);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Fichier d'entrée introuvable", "");
            return result;
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileOutPut);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Fichier de sortie introuvable", "");
            return result;
        }
        try {
            signature = Signature.getInstance("DSA");

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Algorithme inexistant ou non supporté", "");
            return result;
        }
        try {
            signature.initSign(privateKey);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Clé invalide", "");
            return result;
        }
        byte[] sign = null;

        try {
            sign = signature.sign();

        } catch (SignatureException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreurs dans la signature", "");
            return result;
        }
        try {

            fileOutputStream.write(sign);
            fileInputStream.close();
            InputStream finput = new FileInputStream(fileOutPut);
            String encodedSignature = Base64.getEncoder().encodeToString(finput.readAllBytes());
            result.put("SUCCESSFUL", encodedSignature);
            fileOutputStream.close();

            return result;

        } catch (IOException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreurs d'operation de fichiers.", "");
            return result;
        }
    }

    public static HashMap<String, String> signTextDSA(String plainText, String privateKey) {
        HashMap<String, String> result = new HashMap<>();
        Signature privateSignature = null;
        Cipher cipher = null;
        PrivateKey privKey = null;

        try {
            byte[] byteKey = Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec keySpecPv = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("DSA");
            privKey = kf.generatePrivate(keySpecPv);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Algorithme inexistant ou clé invalide", "");
            return result;
        }
        try {
            privateSignature = Signature.getInstance("DSA");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Algorithme non supporté", "");
            return result;
        }
        try {
            privateSignature.initSign(privKey);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Clé invalide", "");
            return result;
        }
        try {
            privateSignature.update(plainText.getBytes(StandardCharsets.UTF_8));
        } catch (SignatureException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreur dans la création de la signature", "");
            return result;
        }

        byte[] signature;
        try {
            signature = privateSignature.sign();
            result.put("SUCCESSFUL", Base64.getEncoder().encodeToString(signature));
            return result;
        } catch (SignatureException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreur dans la création de la signature", "");
            return result;
        }

    }

    public static String verifyDSASignature(String message, String sign, PublicKey publicKey) {
        FileInputStream fileInputStreamMessage = null;
        FileInputStream fileInputStreamSign = null;
        try {
            fileInputStreamMessage = new FileInputStream(message);
            fileInputStreamSign = new FileInputStream(sign);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Fichier d'entrée ou de sortie introuvable";
        }
        Signature signature = null;

        try {
            signature = Signature.getInstance("DSA");

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Algorithme inexistant ou non supporté";

        }

        try {
            signature.initVerify(publicKey);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Clé invalide";
        }

        byte[] s = null;
        try {
            s = new byte[fileInputStreamSign.available()];
            fileInputStreamSign.read(s);

        } catch (IOException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Erreurs d'operations de fichier";
        }

        try {
            fileInputStreamMessage.close();
            fileInputStreamSign.close();

        } catch (IOException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Erreurs dans la fermeture des fichiers";
        }
        try {
            boolean verify = signature.verify(s);
            if (verify) {
                return "Signature valide";
            } else {
                return "Signature invalide";
            }
        } catch (SignatureException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Erreur(s) dans la verification de la signature";
        }

    }

    public static String verifyStringSignatureDSA(String plainText, String signature, String publicKey) {
        PublicKey pubKey = null;
        Signature publicSignature = null;
        try {
            byte[] byteKey = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("DSA");
            pubKey = kf.generatePublic(X509publicKey);
            publicSignature = Signature.getInstance("DSA");

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(AsymetricCipher.class.getName()).log(Level.SEVERE, null, ex);
            return "Algorithme inexistant ou clé invalide";
        }
        try {
            publicSignature.initVerify(pubKey);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Clé invalide";
        }
        try {
            publicSignature.update(plainText.getBytes(StandardCharsets.UTF_8));
        } catch (SignatureException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Erreur dans la verification de la signature";
        }

        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        try {
            if (publicSignature.verify(signatureBytes)) {
                return "Signature valide";
            } else {
                return "Signature invalide";
            }
        } catch (SignatureException ex) {
            Logger.getLogger(CryptoSignature.class.getName()).log(Level.SEVERE, null, ex);
            return "Erreur dans la verification de la signature";
        }
    }
}
