
package com.progressoft.induction.atm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MyConnection {
        public static Connection getConnection() throws SQLException {

        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system","root", "12345678sS-");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return con;
    }
}
