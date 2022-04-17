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

    /** Save Button*/
    public Button saveCustomerBttn;
    /** Cancel Button*/
    public Button cancelBttn;
    /** Name text field*/
    public TextField nameTxtFld;
    /** Address text field*/
    public TextField addressTxtFld;
    /** Postal code text field*/
    public TextField postalCodeTxtFld;
    /** Phone number text field*/
    public TextField phoneTxtFld;
    /** City combo box*/
    public ComboBox<String> cityComboBox;
    /** Customer id text field*/
    public TextField customerIdTextFld;
    /** Country combo box*/
    public ComboBox<String> countryComboBox;
    /** Division ID From selected city variable*/
    public int divisionIDFromCity;

    /** Observable List for all cities*/
    ObservableList<String> citiesList = FXCollections.observableArrayList();
    /** Observable List for all countries*/
    ObservableList<String> countriesList = FXCollections.observableArrayList("U.S", "Canada", "UK");

    /** Scene variable*/
    Parent scene;
    /** Stage variable*/
    Stage stage;


    /** Changed the screen to desired screen
     @param actionEvent The action event
     @param resourcesString The link to the desired screen */
    public void buttonChanging(ActionEvent actionEvent, String resourcesString) throws IOException {
        //Resources example: "/view/mainMenu.fxml"
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourcesString));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This will cancel the adding and go back to the main menu
     @param actionEvent Handles the event of the button being pressed*/
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

    /** Sets the cities combo box to the cities available in that country
     @param actionEvent Handles the event of the button being pressed*/
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

    /** Saves the customer to the database. Does various checks to make sure the information is valid for the DB
     @param actionEvent Handles the event of the button being pressed*/
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

        if (nameNotNull(customerName) && addressNotNull(customerAddress) && postalCodeNotNull(customerPostalCode) && phoneNotNull(customerPhoneNumber) && countryNotNull(customerCountry) && cityNotNull(customerCity)) {

            DataBaseQueries.insertIntoCustomersTable(customerName, customerAddress,customerPhoneNumber, customerPostalCode, DataProvider.divisionID);
            Alerts.alertDisplays(6);
            buttonChanging(actionEvent, "/view/customersTable.fxml");
        }
    }

    /** Populates the country combo box*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryComboBox.setItems(countriesList);
    }

    public void onActionCityComboBox(ActionEvent actionEvent) throws SQLException {
        String citySelected = cityComboBox.getSelectionModel().getSelectedItem();

        getAllCitiesDivisionID(citySelected);
        DataProvider.divisionID = divisionIDFromCity;
        System.out.println(divisionIDFromCity);
    }

    /** This gets all the cities of a given division ID
     @param comboBoxSelection Combo box selection.*/
    public void getAllCitiesDivisionID(String comboBoxSelection) throws SQLException {
        Statement state = JDBC.getConnection().createStatement();
        String getAllCitiesDivisionIDSQL = "SELECT Division_ID FROM first_level_divisions WHERE Division='" + comboBoxSelection + "'";
        ResultSet result = state.executeQuery(getAllCitiesDivisionIDSQL);

        while(result.next()) {
            divisionIDFromCity = result.getInt("Division_ID");
        }
    }

    /**Throws error if the name field is empty
     @param name The text in the name field*/
    public boolean nameNotNull(String name) {
        if (nameTxtFld.getText().isEmpty()) {
            Alerts.alertDisplays(1);
            return false;
        }
        return true;
    }

    /**Throws error if the address field is empty
     @param address The text in the address*/
    public boolean addressNotNull(String address) {
        if (addressTxtFld.getText().isEmpty()) {
            Alerts.alertDisplays(2);
            return false;
        }
        return true;
    }

    /**Throws error if the postalCode field is empty
     @param postalCode The text in the postalCode*/
    public boolean postalCodeNotNull(String postalCode) {
        if (postalCodeTxtFld.getText().isEmpty()) {
            Alerts.alertDisplays(4);
            return false;
        }
        return true;
    }

    /**Throws error if the phone field is empty
     @param phone The text in the phone*/
    public boolean phoneNotNull(String phone) {
        if (phoneTxtFld.getText().isEmpty()) {
            Alerts.alertDisplays(5);
            return false;
        }
        return true;
    }

    /**Throws error if the country combo box is empty
     @param country The text in the country combo box*/
    public boolean countryNotNull(String country) {
        if (countryComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(10);
            return false;
        }
        return true;
    }

    /**Throws error if the city combo box is empty
     @param city The text in the city combo box*/
    public boolean cityNotNull(String city) {
        if (cityComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(3);
            return false;
        }
        return true;
    }
}


