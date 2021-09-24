package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Alerts;
import model.Customer;
import util.DataBaseQueries;
import util.JDBC;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomersTable implements Initializable {

    //FXML Variables
    /** Customer table view*/
    public TableView<Customer> customersTblView;
    /** Customer id table column*/
    public TableColumn<Customer, Integer> customersTblID;
    /** Customer name table column*/
    public TableColumn<Customer, String> customersTblName;
    /** Customer address table column*/
    public TableColumn<Customer, String> customersTblAddress;
    /** Customer city table column*/
    public TableColumn<Customer, String> customersTblCity;
    /** Customer county table column*/
    public TableColumn<Customer, String> customersTblCountry;
    /** Customer postal code table column*/
    public TableColumn<Customer, String> customersTblPostalCode;
    /** Customer phone table column*/
    public TableColumn<Customer, String> customersTblPhone;
    /** Add button*/
    public Button addBttn;
    /** Modify button*/
    public Button modifyBttn;
    /** Delete button*/
    public Button deleteBtnn;
    /** Main menu button*/
    public Button mainMenuBttn;
    /** Selected customer*/
    private static Customer highlightedCustomer;

    /** Scene variable*/
    Parent scene;
    /** Stage variable*/
    Stage stage;

    /** Getter for the highlighted customer.*/
    public static Customer getHighlightedCustomer() {
        return highlightedCustomer;
    }

    /** Changed the screen to desired screen.
     @param actionEvent The action event.
     @param resourcesString The link to the desired screen. */
    public void buttonChanging(ActionEvent actionEvent, String resourcesString) throws IOException {
        //Resource Example: "/view/mainMenu.fxml"
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourcesString));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Changes to the add customer screen.
     @param actionEvent Handles button press.*/
    public void onActionAdd(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/addCustomer.fxml");
    }

    /** Changes to the modify customer screen.
     @param actionEvent Handles button press. */
    public void onActionModify(ActionEvent actionEvent) throws IOException {
        highlightedCustomer = customersTblView.getSelectionModel().getSelectedItem();

        if(highlightedCustomer == null) {
            Alerts.errorAlert( "ERROR", "No customer highlighted", "Please select a customer to modify");
        } else {
            buttonChanging(actionEvent, "/view/modifyCustomer.fxml");
        }
    }

    /** Queries the database for a count of appointments per customer.
     If that count is 1 or greater, doesn't allow deletion.
     @param customerID Customer id. */
    public static int getCustomerApptCount(int customerID) throws SQLException {

        Statement customerApptCount = JDBC.getConnection().createStatement();
        String modifySQL =
                "SELECT COUNT(Appointment_ID) AS Count " +
                "FROM appointments " +
                "INNER JOIN customers ON customers.Customer_ID = appointments.Customer_ID " +
                "WHERE customers.Customer_ID=" + customerID;

        ResultSet apptCount = customerApptCount.executeQuery(modifySQL);

        if(apptCount.next() && apptCount.getInt("Count") > 0) {
            Alerts.errorAlert("Cannot Delete Customer",
                    "Customer has existing appointments",
                    "Please delete all appointments associated with this customer before trying to delete.");
            return -1;
        }
        return 0;
    }

    /** Deletes the highlighted customer if all premises are met.
     @param actionEvent Handles button press. */
    public void onActionDelete(ActionEvent actionEvent) throws SQLException, IOException {
        Customer highlightedCustomer = customersTblView.getSelectionModel().getSelectedItem();

        if(highlightedCustomer == null) {
            Alerts.alertDisplays(9);
        } else if(getCustomerApptCount(highlightedCustomer.getCustomerID()) == 0){
            Alert alertForDelete = new Alert(Alert.AlertType.CONFIRMATION);
            alertForDelete.setHeaderText("Are you sure you want to delete this customer?");
            alertForDelete.setContentText("Deleting the customer will remove them and their appointments");
            Optional<ButtonType> deleteResult = alertForDelete.showAndWait();

            if(deleteResult.isPresent() && deleteResult.get() == ButtonType.OK) {
                DataBaseQueries.deleteFromCustomersTable(highlightedCustomer.getCustomerID());
                Alerts.alertDisplays(11);
                buttonChanging(actionEvent, "/view/CustomersTable.fxml");
            }
        }
    }

    /** Changes to the main menu customer screen.
     @param actionEvent Handles button press. */
    public void onActionMainMenu(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/mainMenu.fxml");
    }

    /** Populates the customer table view. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Customer.getGetAllCustomers().clear();
            customersTblView.setItems(Customer.getGetAllCustomers());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        customersTblID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customersTblName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customersTblAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customersTblCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        customersTblCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        customersTblPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customersTblPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    }
}
