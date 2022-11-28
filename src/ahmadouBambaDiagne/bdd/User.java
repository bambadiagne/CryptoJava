/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ahmadouBambaDiagne.bdd;

import java.util.ArrayList;

public class User {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static boolean validatePassword(String password) {
        return password.length() >= 8;
    }

    public static boolean verifyField(String field) {
        return !field.isEmpty();
    }

    public static ArrayList<String> validFormInscription(String username, String password, String confirmPassword, boolean isLogin) {
        ArrayList<String> errors = new ArrayList<>();
        if (!User.verifyField(username)) {
            errors.add("Veuillez renseigner un nom d'utilisateur");
        }

        if (!User.verifyField(password)) {
            errors.add("Veuillez renseigner un mot de passe");
        } else if (!User.validatePassword(password) && !isLogin) {
            errors.add("La longueur mininum du mot de passe est de 8 caracteres");
        } else if (!User.verifyField(confirmPassword) && !isLogin) {
            errors.add("Veuillez confirmer votre password");
        } else if (!password.equals(confirmPassword) && !isLogin) {
            errors.add("Les mots de passe ne sont pas les mêmes");
        }
        return errors;
    }

}
