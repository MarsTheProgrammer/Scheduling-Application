package util;

import javafx.scene.control.Alert;
import model.Alerts;

import java.sql.*;

public class DataBaseQueries {

    /** Insert customer in to the database query
     @param customerName customer Name
     @param address address
     @param divisionID divisionID
     @param Phone Phone
     @param postalCode postalCode */
    public static int insertIntoCustomersTable(String customerName, String address, String Phone, String postalCode, int divisionID) throws SQLException {
        String sql = "INSERT INTO customers (Customer_Name, Address, Phone, Postal_Code, Division_ID) VALUES(?,?,?,?,?)";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, Phone);
        ps.setString(4, postalCode);
        ps.setInt(5, divisionID);

        int rowsAffected = ps.executeUpdate();

        ps.close();
        return rowsAffected;
    }

    /** Update customers table from the database
     @param customerName customer Name
     @param address address
     @param divisionID divisionID
     @param Phone Phone
     @param postalCode postalCode
     @param customerID customerID*/
    public static int updateToCustomersTable(int customerID, String customerName, String address, String postalCode, String Phone, int divisionID) throws SQLException {

        String modifySQL = "UPDATE customers SET Customer_Name=?, Address=?, Phone=?, Postal_Code=?, Division_ID=? WHERE Customer_ID=?";

        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(modifySQL);

        preparedStatement.setInt(6, customerID);
        preparedStatement.setString(1, customerName);
        preparedStatement.setString(2, address);
        preparedStatement.setString(3, Phone);
        preparedStatement.setString(4, postalCode);
        preparedStatement.setInt(5, divisionID);

        int rowsUpdates = preparedStatement.executeUpdate();

        if (rowsUpdates == 1) {
            Alerts.alertDisplays(7);
        }
        preparedStatement.close();
        return rowsUpdates;
    }

    /** Delete customers from the database
     @param customerID customerID*/
    public static int deleteFromCustomersTable(int customerID) throws SQLException {

        String modifySQL = "DELETE FROM customers WHERE Customer_ID = ?";

        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(modifySQL);
        preparedStatement.setInt(1, customerID);
        int rowsDeleted = preparedStatement.executeUpdate();

        preparedStatement.close();
        return rowsDeleted;
    }

//    public static void getCustomerApptCount(int customerID) throws SQLException {
//
//        Statement customerApptCount = JDBC.getConnection().createStatement();
//        String modifySQL = "SELECT COUNT(Appointment_ID) AS Count " +
//                "FROM appointments " +
//                "INNER JOIN customers ON customers.Customer_ID = appointments.Customer_ID " +
//                "WHERE customers.Customer_ID=" + customerID;
//
//        ResultSet apptCount = customerApptCount.executeQuery(modifySQL);
//
//        if(apptCount.next() && apptCount.getInt("Appointment_ID") > 0) {
//
//        }
//
//    }

    /** Delete appointment from the database
     @param appointmentId customerID*/
    public static int deleteFromAppointmentsTable(int appointmentId) throws SQLException {

        String deleteAppointmentSQL = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(deleteAppointmentSQL);

        preparedStatement.setInt(1, appointmentId);

        int rowsDeletedForAppointments = preparedStatement.executeUpdate();

        preparedStatement.close();
        return rowsDeletedForAppointments;
    }

    /** Insert into the appointments table
     @param userId userId
     @param type type
     @param location location
     @param description description
     @param end end
     @param start start
     @param title title
     @param contactId contactId
     @param customerId customerId*/
    public static int insertAppointment(String title, String description, String location, String type,
                                        Timestamp start, Timestamp end, int customerId, int userId, int contactId) throws SQLException {
        String insertApptSQL = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) " +
                               "VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(insertApptSQL);

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);

        int rowsAffectedAppointmentInsert = ps.executeUpdate();

        ps.close();
        return rowsAffectedAppointmentInsert;
    }

    /** Update appointments from the database from the selected appointment id
     @param appointmentID appointmentID
     @param customerId customerId
     @param contactId contactId
     @param title title
     @param start start
     @param end end
     @param description descripiton
     @param location location
     @param type type
     @param userId user id*/
    public static int updateAppointment(int appointmentID, String title, String description, String location, String type,
                                        Timestamp start, Timestamp end, int customerId, int userId, int contactId) throws SQLException {

        String updateApptSQL = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, " +
                                "Customer_ID=?, User_ID=?, Contact_ID=? WHERE Appointment_ID=?";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(updateApptSQL);

        ps.setInt(10, appointmentID);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);

        int rowsAffectedAppointmentInsert = ps.executeUpdate();

        ps.close();
        return rowsAffectedAppointmentInsert;
    }
}
