/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ba Thuc
 */
public class DbConText {

    static String user = "sa";
    static String Pass = "123";
    static String url = "jdbc:sqlserver://ADMIN\\SQLEXPRESS:1433;databaseName=Giay_123";

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (Exception e) {
        }
    }

    public static Connection getConnection()  {
        
        Connection con=null;
        try {
            con = DriverManager.getConnection(url,user,Pass);
        } catch (SQLException ex) {
            Logger.getLogger(DbConText.class.getName()).log(Level.SEVERE, null, ex);
        }

        return con;
    }
    public static void main(String[] args) {
        Connection cn = getConnection();
        if(cn!=null){
            System.out.println("Thanh Cong");
        }
    }

   

}
