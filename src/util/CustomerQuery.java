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

//        if (rowsAffected == 1) {
//            Alerts.alertDisplays(6);
//        }
        return rowsAffected;
    }
//String modifyCustomerSQL = "UPDATE customers SET Customer_Name='" + nameTxtFld.getText() + "', Address='" + addressTxtFld.getText() + "Postal_Code='" +
// postalCodeTxtFld.getText() + "', Phone='" + phoneTxtFld.getText() + "'";

    public static int updateToCustomersTable(int customerID, String customerName, String address, String postalCode, String Phone, int divisionID) throws SQLException {

        String modifySQL = "UPDATE customers SET Customer_Name=?, Address=?, Phone=?, Postal_Code=?, Division_ID=? WHERE Customer_ID=?";

        PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(modifySQL);


        //WE MESSED UP THE DATABASE AND NEED TO RESET IT

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
        return rowsUpdates;
    }

    public static int deleteFromCustomersTable(int customerID) throws SQLException {

        String modifySQL = "DELETE FROM customers WHERE Customer_ID = ?";

        PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(modifySQL);

        preparedStatement.setInt(1, customerID);

        int rowsDeleted = preparedStatement.executeUpdate();
//
//        if (rowsDeleted == 1) {
//            Alerts.alertDisplays(8);
//        }

        return rowsDeleted;
    }

}
