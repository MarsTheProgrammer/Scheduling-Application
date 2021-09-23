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
    public TableView<Customer> customersTblView;
    public TableColumn<Customer, Integer> customersTblID;
    public TableColumn<Customer, String> customersTblName;
    public TableColumn<Customer, String> customersTblAddress;
    public TableColumn<Customer, String> customersTblCity;
    public TableColumn<Customer, String> customersTblCountry;
    public TableColumn<Customer, String> customersTblPostalCode;
    public TableColumn<Customer, String> customersTblPhone;
    public Button addBttn;
    public Button modifyBttn;
    public Button deleteBtnn;
    public Button mainMenuBttn;

    //Variables
    Parent scene;
    Stage stage;
    private static Customer highlightedCustomer;

    //Getter for highlightedCustomer
    public static Customer getHighlightedCustomer() {
        return highlightedCustomer;
    }

    //Created this to remove code redundancy
    public void buttonChanging(ActionEvent actionEvent, String resourcesString) throws IOException {
        //Resource Example: "/view/mainMenu.fxml"
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourcesString));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionAdd(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/addCustomer.fxml");
    }

    public void onActionModify(ActionEvent actionEvent) throws IOException {

        //Grabs highlighted customer and pushes to modify table
        highlightedCustomer = customersTblView.getSelectionModel().getSelectedItem();

        if(highlightedCustomer == null) {
            Alert alertForModify = new Alert(Alert.AlertType.ERROR);
            alertForModify.setHeaderText("No customer highlighted");
            alertForModify.setContentText("Please select a customer to modify");
            alertForModify.showAndWait();
        } else {
            buttonChanging(actionEvent, "/view/modifyCustomer.fxml");
        }
    }

    public static int getCustomerApptCount(int customerID) throws SQLException {

        Statement customerApptCount = JDBC.getConnection().createStatement();
        String modifySQL = "SELECT COUNT(Appointment_ID) AS Count " +
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


    public void onActionMainMenu(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/mainMenu.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //added this clear in order to make sure no duplicates are present in CustomerTableView
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
