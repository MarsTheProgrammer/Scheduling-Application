package util;

import javafx.scene.control.Alert;
import model.Alerts;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DataBaseQueries {

    public static int insertIntoCustomersTable(String customerName, String address, String Phone, String postalCode, int divisionID) throws SQLException {
        String sql = "INSERT INTO customers (Customer_Name, Address, Phone, Postal_Code, Division_ID) VALUES(?,?,?,?,?)";

        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, Phone);
        ps.setString(4, postalCode);
        ps.setInt(5, divisionID);

        int rowsAffected = ps.executeUpdate();

        ps.close();
        return rowsAffected;
    }

    public static int updateToCustomersTable(int customerID, String customerName, String address, String postalCode, String Phone, int divisionID) throws SQLException {

        String modifySQL = "UPDATE customers SET Customer_Name=?, Address=?, Phone=?, Postal_Code=?, Division_ID=? WHERE Customer_ID=?";

        PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(modifySQL);

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

    public static int deleteFromCustomersTable(int customerID) throws SQLException {

        String modifySQL = "DELETE FROM customers WHERE Customer_ID = ?";

        PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(modifySQL);
        preparedStatement.setInt(1, customerID);
        int rowsDeleted = preparedStatement.executeUpdate();

        preparedStatement.close();
        return rowsDeleted;
    }

    public static int deleteFromAppointmentsTable(int appointmentId) throws SQLException {

        String deleteAppointmentSQL = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(deleteAppointmentSQL);
        preparedStatement.setInt(1, appointmentId);

        int rowsDeletedForAppointments = preparedStatement.executeUpdate();

        preparedStatement.close();
        return rowsDeletedForAppointments;
    }

    public static int insertAppointment(String title, String description, String location, String type,
                                        Timestamp start, Timestamp end, int customerId, int userId, int contactId) throws SQLException {
        String insertApptSQL = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) " +
                               "VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(insertApptSQL);

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

    public static int updateAppointment(int appointmentID, String title, String description, String location, String type,
                                        Timestamp start, Timestamp end, int customerId, int userId, int contactId) throws SQLException {

        String insertApptSQL = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, " +
                                "Customer_ID=?, User_ID=?, Contact_ID=? WHERE Appointment_ID=?";

        PreparedStatement ps = DBConnection.getConnection().prepareStatement(insertApptSQL);

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
