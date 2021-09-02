/*
* Given by Malcolm Wabar
* */

package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    //JDBC URL Parts
    private static final String protocol = "jdbc"; //Database protocol
    private static final String vendorName = ":mysql:"; //Database vendor
    private static final String ipAddress = "//wgudb.ucertify.com:3306/"; //Database IPAddress
    private static final String dbName = "WJ08Qjx"; //Database name

    //Makes up the JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName;

    //Driver interface reference
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";

    //Username: Not the same as the DB name
    private static final String username = "U08Qjx";
    //Password
    private static final String password = "53689362297";

    //Connection interface reference
    private static Connection conn = null;

    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection Successful");
        } catch (SQLException e) { //Use Printstacktrack for outputting exceptions
            //System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) { //Use Printstacktrack for outputting exceptions
            //System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection getConnection() {
        return conn;
    }

    //We need to get the connection
    public static void closeConnection() {
        try {
            conn.close();
        } catch (Exception e) {
            //do nothing
        }
    }









}
