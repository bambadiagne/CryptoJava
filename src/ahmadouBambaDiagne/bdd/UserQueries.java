/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ahmadouBambaDiagne.bdd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
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
        boolean isUserExists=false;
        try {
            if(this.findUserByName(user.getUsername())){
                isUserExists=true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        user.setPassword(Sha256.sha256(user.getPassword()));
        if(!isUserExists){
        try {
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO user (USERNAME,PASSWORD) VALUES(?,?);");
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(3,user.getPassword());
   

            preparedStatement.executeUpdate();
            return "SUCCESSFUL";
        } catch (SQLException e) {
            
            e.printStackTrace();
        }
        
        }
        return "Ce nom d'utilisateur est dèjà pris";

    }

    public  boolean findUser(String email,String password) throws SQLException {
        ResultSet rs = null;
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT *FROM acheteur where  USERNAME=? and PASSWORD=? ;");
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            preparedStatement.executeQuery();

            rs = preparedStatement.executeQuery(); 
      
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
         return rs.next();
        
    }
    public  boolean findUserByName(String username) throws SQLException {
        ResultSet rs = null;
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT *FROM user where  USERNAME=?;");
            preparedStatement.setString(1,username);
            preparedStatement.executeQuery();

            rs = preparedStatement.executeQuery(); 
      
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
         return rs.next();
        
    }
}