package util;

import javafx.scene.control.Alert;
import model.Alerts;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerQuery {

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

//
//    INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID)
//    VALUES (5,'title', 'description', 'location', 'type', '2020-05-28 12:00:00', '2020-05-28 12:00:00', 2, 2, 2)

    public static int insertIntoAppointmentsTable(int appointmentId, String title, String description, String location, String type, String start, String end, int CustomerId,
                                                  int userId, int ContactId) throws SQLException {
        String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID)" +
                    " VALUES(?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

        ps.setInt(1, appointmentId );
        ps.setString(2, title);
        ps.setString(3, description);
        ps.setString(4,location );
        ps.setString(5, type);
        ps.setString(6,start);
        ps.setString(7, end);
        ps.setInt(8,CustomerId );
        ps.setInt(9, userId);
        ps.setInt(10, ContactId);

        int rowsAffectedAppointmentInsert = ps.executeUpdate();

        ps.close();
        return rowsAffectedAppointmentInsert;
    }
//    public static int populateExistingCustomersComboBox(String customerName) throws SQLException {
//        String sql = "SELECT * FROM customers";
//
//        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
//
//        ps.setString(1, customerName);
//
//
//        int rowsAffectedAppointmentInsert = ps.executeUpdate();
//
//        ps.close();
//        return rowsAffectedAppointmentInsert;
//    }
}
