package util;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbConText {

    public static String Url = "jdbc:sqlserver://localhost:1433;databaseName=MEOMEO11;encrypt=true;trustServerCertificate=true;";
    public static String Username = "sa";
    public static String Pass = "123";

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Chưa cài JDBC!");
            ex.printStackTrace();

        }
    }

    public static Connection getConnection() {
        Connection cn = null;
        try {
            cn = DriverManager.getConnection(Url, Username, Pass);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cn;
    }

    public static void main(String[] args) {
        Connection cn = getConnection();
        if (cn != null) {
            System.out.println("Connect DB is Successful!");
        } else {
            System.out.println("Connect DB is Fail!");
        }
    }
   

}
