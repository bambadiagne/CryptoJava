/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ahmadouBambaDiagne.crypto.genkey;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * @author hp
 */
public class SymetricGenKey {

    public static HashMap<String, String> genKey(String algorithm, int keySize, String filePath) {
        KeyGenerator keyGenerator;
        HashMap<String, String> result = new HashMap<>();
        String encodedKey="";
        try {
            keyGenerator = KeyGenerator.getInstance(algorithm);
            keyGenerator.init(keySize);
            SecretKey secretKey = keyGenerator.generateKey();
            encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            String saveResult=saveKey(secretKey, filePath);
            result.put(encodedKey,saveResult);
            return  result;

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            result.put(encodedKey,"Cet algorithme n'existe pas");
            return result;
        }
    }

    public static String saveKey(SecretKey secretKey, String filePath) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(filePath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            return "Fichier introuvable";
        }
        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(secretKey);

            objectOutputStream.close();

        } catch (IOException ex) {
            Logger.getLogger(SymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            return "Erreur lors de la création du fichier";
        }
        return "SUCCESSFUL";
    }

     public static HashMap<String,SecretKey> getKey(String filePath){
	HashMap<String,SecretKey> result = new HashMap<>();
         FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Fichier introuvable",null);
            return result;
        }
		ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(fileInputStream);
        } catch (IOException ex) {
            Logger.getLogger(SymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreur d'operations avec le fichier",null);
            return result;
        }
		SecretKey secretKey = null;
        try {
            secretKey = (SecretKey) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(SymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Classe introuvable || Erreur d'operations avec le fichier",null);
            return  result;
        }
        try {
            objectInputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(SymetricGenKey.class.getName()).log(Level.SEVERE, null, ex);
            result.put("Erreur d'operations avec le fichier",null);
            return result;
        }
        result.put("SUCCESSFUL",secretKey);
		return result;
	}  
     
}
