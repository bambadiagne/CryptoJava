/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ahmadouBambaDiagne.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ahmadouBambaDiagne.utils.Sha256;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hp
 */
public class UserQueries {

    Connection con = ConnexionDB.connecterDB();

    public String addUser(User user) {
        boolean isUserExists = false;
        try {
            if (this.findUserByName(user.getUsername())) {
                isUserExists = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        user.setPassword(Sha256.sha256(user.getPassword()));
        if (!isUserExists) {
            try {
                PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO users (USERNAME,PASSWORD) VALUES(?,?);");
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword());

                preparedStatement.executeUpdate();
                return "SUCCESSFUL";
            } catch (SQLException e) {

                e.printStackTrace();
            }

        }
        return "Ce nom d'utilisateur est dèjà pris";

    }

    public String findUser(String username, String password) {
        ResultSet rs = null;
        password = Sha256.sha256(password);
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT *FROM users where  USERNAME=? and PASSWORD=? ;");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeQuery();

            rs = preparedStatement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
            return e.toString();
        }
        try {
            if (rs.next()) {
                return "SUCCESSFUL";
            } else {
                return "Combinaison username + mot de passe introuvable";
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserQueries.class.getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        }
    }

    public boolean findUserByName(String username) throws SQLException {
        ResultSet rs = null;
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT *FROM users where  USERNAME=?;");
            preparedStatement.setString(1, username);
            preparedStatement.executeQuery();

            rs = preparedStatement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs.next();

    }
}
