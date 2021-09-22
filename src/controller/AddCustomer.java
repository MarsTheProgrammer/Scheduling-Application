package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Alerts;
import model.Customer;
import util.DataBaseQueries;
import util.DataProvider;
import util.JDBC;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddCustomer implements Initializable {

    //FXML variables
    public Button saveCustomerBttn;
    public Button cancelBttn;
    public TextField nameTxtFld;
    public TextField addressTxtFld;
    public TextField postalCodeTxtFld;
    public TextField phoneTxtFld;
    public ComboBox<String> cityComboBox;
    public int divisionIDFromCity;
    public TextField customerIdTextFld;
    public ComboBox<String> countryComboBox;

    //Variables
    Parent scene;
    Stage stage;

    ObservableList<String> citiesList = FXCollections.observableArrayList();
    ObservableList<String> countriesList = FXCollections.observableArrayList("U.S", "Canada", "UK");

    //Created this to remove code redundancy
    public void buttonChanging(ActionEvent actionEvent, String resourcesString) throws IOException {
        //Resources example: "/view/mainMenu.fxml"
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourcesString));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    //this is is good. Handles the cancel button
    public void onActionCancel(ActionEvent actionEvent) throws IOException {
        Alert alertForCancel = new Alert(Alert.AlertType.CONFIRMATION);
        alertForCancel.setTitle("Cancel");
        alertForCancel.setHeaderText("Are you sure you want to cancel?");
        alertForCancel.setContentText("This will clear all text fields");
        Optional<ButtonType> cancelSelection = alertForCancel.showAndWait();

        if(cancelSelection.isPresent() && cancelSelection.get() == ButtonType.OK) {
            buttonChanging(actionEvent, "/view/customersTable.fxml");
        }
    }

    public void onActionCountryComboBox(ActionEvent actionEvent) throws SQLException {
        String countrySelected = countryComboBox.getSelectionModel().getSelectedItem();

        if(countrySelected.equals("U.S")) {
            Statement statement = JDBC.getConnection().createStatement();
            String getAllCitiesSQL = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 1";
            ResultSet usCityResults = statement.executeQuery(getAllCitiesSQL);

            while (usCityResults.next()) {
                String city = usCityResults.getString("Division");
                citiesList.add(city);
                cityComboBox.setItems(citiesList);
            }
            statement.close();

        } else if(countrySelected.equals("UK")) {
            Statement statement = JDBC.getConnection().createStatement();
            String getAllCitiesSQL = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 2";
            ResultSet ukCityResults = statement.executeQuery(getAllCitiesSQL);

            while (ukCityResults.next()) {
                String city = ukCityResults.getString("Division");
                citiesList.add(city);
                cityComboBox.setItems(citiesList);
            }
            statement.close();

        } else {
            Statement statement = JDBC.getConnection().createStatement();
            String getAllCitiesSQL = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 3";
            ResultSet canadaCityResults = statement.executeQuery(getAllCitiesSQL);

            while (canadaCityResults.next()) {
                String city = canadaCityResults.getString("Division");
                citiesList.add(city);
                cityComboBox.setItems(citiesList);
            }
            statement.close();
        }
    }

    public void onActionSaveCustomer(ActionEvent actionEvent) throws IOException, SQLException {

        int customerID = 0;
        for (Customer customer : Customer.getGetAllCustomers()) {
            if (customer.getCustomerID() > customerID) {
                customerID = customer.getCustomerID();
            }
        }
        String customerName = nameTxtFld.getText();
        String customerAddress = addressTxtFld.getText();
        String customerPostalCode = postalCodeTxtFld.getText();
        String customerPhoneNumber = phoneTxtFld.getText();
        String customerCity = cityComboBox.getSelectionModel().getSelectedItem();
        String customerCountry = countryComboBox.getSelectionModel().getSelectedItem();

        //If all fields are filled and selected, add the customer to the customers list.
        if (nameNotNull(customerName) && addressNotNull(customerAddress) && postalCodeNotNull(customerPostalCode) && phoneNotNull(customerPhoneNumber) && countryNotNull(customerCountry) && cityNotNull(customerCity)) {

            //Customer_ID is auto incremented
            DataBaseQueries.insertIntoCustomersTable(customerName, customerAddress,customerPhoneNumber, customerPostalCode, DataProvider.divisionID);


            // WHY DO I HAVE THIS HERE?
            //Customer customer = new Customer(customerID, customerName, customerAddress, customerCity, customerPostalCode, customerPhoneNumber, customerCountry);
            //Customer.allCustomersList.add(customer);

            //Displays an informative box telling user the customer was saved
            Alerts.alertDisplays(6);

            //Goes back to the customer table screen
            buttonChanging(actionEvent, "/view/customersTable.fxml");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryComboBox.setItems(countriesList);
    }

    //When a country is select, it will update the city combo box
    public void onActionCityComboBox(ActionEvent actionEvent) throws SQLException {
        String citySelected = cityComboBox.getSelectionModel().getSelectedItem();

        getAllCitiesDivisionID(citySelected);
        DataProvider.divisionID = divisionIDFromCity;
        System.out.println(divisionIDFromCity);
    }

    //Get the cities division ID and assigns it to the DataProvider class variable named: divisionIDFromCity
    public void getAllCitiesDivisionID(String comboBoxSelection) throws SQLException {

        Statement state = JDBC.getConnection().createStatement();
        String getAllCitiesDivisionIDSQL = "SELECT Division_ID FROM first_level_divisions WHERE Division='" + comboBoxSelection + "'";
        ResultSet result = state.executeQuery(getAllCitiesDivisionIDSQL);

        while(result.next()) {
            divisionIDFromCity = result.getInt("Division_ID");
        }
    }

    //The 5 below handles empty text fields
    public boolean nameNotNull(String name) {
        if (nameTxtFld.getText().isEmpty()) {
            Alerts.alertDisplays(1);
            return false;
        }
        return true;
    }
    public boolean addressNotNull(String address) {
        if (addressTxtFld.getText().isEmpty()) {
            Alerts.alertDisplays(2);
            return false;
        }
        return true;
    }
    public boolean postalCodeNotNull(String postalCode) {
        if (postalCodeTxtFld.getText().isEmpty()) {
            Alerts.alertDisplays(4);
            return false;
        }
        return true;
    }
    public boolean phoneNotNull(String phone) {
        if (phoneTxtFld.getText().isEmpty()) {
            Alerts.alertDisplays(5);
            return false;
        }
        return true;
    }
    public boolean countryNotNull(String country) {
        if (countryComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(10);
            return false;
        }
        return true;
    }
    public boolean cityNotNull(String city) {
        if (cityComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(3);
            return false;
        }
        return true;
    }
}


