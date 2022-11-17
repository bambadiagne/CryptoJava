package ahmadouBambaDiagne.bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
public class ConnexionDB {
        public static Connection connecterDB() {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver ok");
            String url = "jdbc:mysql://localhost:3306/cryptojava";
            String user = "root";
            String password = "Ir00t@dmin12";
            Connection cnx = DriverManager.getConnection(url, user, password);
            return cnx;
        } catch (ClassNotFoundException | SQLException e) {
            return null;
        }
    }

}
