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
import model.Customer;
import util.DBConnection;

import javax.xml.transform.Result;
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
    //public ComboBox<String> addCustomerCountryComboBox;
    public Label countryLabel;
    public int divisionIDFromCity;

    //Variables
    Parent scene;
    Stage stage;
    Customer highlightedCustomer;

    //ASK CI IF WE NEED ALL THE CITIES AND COUNTRIES FROM DB FOR THESE LISTS. SHOULD I QUERY FOR THE CITY?
    //ObservableList<String> citiesList = FXCollections.observableArrayList("Montreal, Canada", "Phoenix, Arizona", "White Plains, New York", "London, England");
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

    public void onActionSaveCustomer(ActionEvent actionEvent) throws IOException, SQLException {

        //This block is to get the customerID to the highest number from all customers. This is for unique customerID
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
        String customerCountry = countryLabel.getText();

        Statement statement = DBConnection.getConnection().createStatement();

        //Should return the highest Customer ID and increase it to make it the next highest before adding new customer
        ResultSet customerIDMax = statement.executeQuery("SELECT MAX(Customer_ID) FROM customers");
        if(customerIDMax.next()) {
            customerID = customerIDMax.getInt(1);
            customerID++;
        }

        //WHAT'S GOING IN HERE IS WE CANNOT ADD THIS IN WITHOUT GETTING THE DIVISION_ID TO WORK
        //CAN'T FIGURE OUT A WAY TO GET THE DIVISION_ID TO UPDATE
        //CAN'T UPDATE A FOREIGN KEY CONSTRAINT

        String addCustomerSQL = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID)" +
                "VALUES (Customer_ID=" + customerID + ", Customer_Name='" + customerName + "', Address='" +
                customerAddress + "', Postal_Code='" + customerPostalCode + "', Phone='" + customerPhoneNumber + "', Division_ID=" + divisionIDFromCity + ")";

        int result = statement.executeUpdate(addCustomerSQL);


        Statement cityStatement = DBConnection.getConnection().createStatement();
        String addCustomerCity = "INSERT INTO first_level_divisions VALUES Division='" + customerCity + "'";
        int innerResult = cityStatement.executeUpdate(addCustomerCity);

        Statement countryStatement = DBConnection.getConnection().createStatement();
        String addCustomerCountry = "INSERT INTO countries VALUES Country='" + customerCountry + "'";
        int innerInnerResult = countryStatement.executeUpdate(addCustomerCountry);


        //If all fields are filled and selected, add the customer to the customers list.
        if (nameNotNull(customerName) && addressNotNull(customerAddress) && postalCodeNotNull(customerPostalCode) && phoneNotNull(customerPhoneNumber) && cityNotNull(customerCity)) {

            Customer customer = new Customer(customerID, customerName, customerAddress, customerCity, customerPostalCode, customerPhoneNumber, customerCountry);
            Customer.allCustomersList.add(customer);

            //Displays an informative box telling user the customer was saved
            alertDisplays(6);

            //Goes back to the customer table screen
            buttonChanging(actionEvent, "/view/customersTable.fxml");
        }

        statement.close();
        cityStatement.close();
        countryStatement.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Initializes the cities list for the city combo box
        try {
            getAllCities();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    //When a city is select, it will update the country label to the appropriate country
    public void onActionCityComboBox(ActionEvent actionEvent) throws SQLException {
       String citySelected = cityComboBox.getSelectionModel().getSelectedItem();

        if (citySelected.equals("Alberta") || citySelected.equals("Northwest Territories") || citySelected.equals("British Columbia") || citySelected.equals("Manitoba") ||
                citySelected.equals("New Brunswick") || citySelected.equals("Nova Scotia") || citySelected.equals("Prince Edward Island") ||
                citySelected.equals("Ontario") || citySelected.equals("Québec") || citySelected.equals("Saskatchewan") ||
                citySelected.equals("Nunavut") || citySelected.equals("Yukon") || citySelected.equals("Newfoundland and Labrador")) {
            countryLabel.setText("Canada");
        } else if (citySelected.equals("England") || citySelected.equals("Wales") || citySelected.equals("Scotland") || citySelected.equals("Northern Ireland")) {
            countryLabel.setText("UK");
        } else {
            countryLabel.setText("U.S");
        }
        getAllCitiesDivisionID(citySelected);
        System.out.println(divisionIDFromCity);
    }


    //override with division as parameter and get the divison id from DB

    public void getAllCitiesDivisionID(String comboBoxSelection) throws SQLException {

        Statement state = DBConnection.getConnection().createStatement();

        String getAllCitiesDivisionIDSQL = "SELECT Division_ID FROM first_level_divisions WHERE Division='" + comboBoxSelection + "'";

        ResultSet result = state.executeQuery(getAllCitiesDivisionIDSQL);

        while(result.next()) {
            divisionIDFromCity = result.getInt("Division_ID");
        }

    }

    //This will populate the combo box with all the cities in the table
    public void getAllCities() throws SQLException {

        Statement statement = DBConnection.getConnection().createStatement();

        String getAllCitiesSQL = "SELECT * FROM first_level_divisions";

        ResultSet cityResults = statement.executeQuery(getAllCitiesSQL);

        while (cityResults.next()) {
            String city = cityResults.getString("Division");
            citiesList.add(city);
            cityComboBox.setItems(citiesList);
        }
        statement.close();

    }


    //The 5 below handles empty text fields
    public boolean nameNotNull(String name) {
        if (nameTxtFld.getText().isEmpty()) {
            alertDisplays(1);
            return false;
        }
        return true;
    }

    public boolean addressNotNull(String address) {
        if (addressTxtFld.getText().isEmpty()) {
            alertDisplays(2);
            return false;
        }
        return true;
    }

    public boolean postalCodeNotNull(String postalCode) {
        if (postalCodeTxtFld.getText().isEmpty()) {
            alertDisplays(4);
            return false;
        }
        return true;
    }

    public boolean phoneNotNull(String phone) {
        if (phoneTxtFld.getText().isEmpty()) {
            alertDisplays(5);
            return false;
        }
        return true;
    }

    public boolean cityNotNull(String city) {
        if (cityComboBox.getSelectionModel().getSelectedItem() == null) {
            alertDisplays(3);
            return false;
        }
        return true;
    }


    //Copied for my project for SW1
    private void alertDisplays(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Alert alertForSave = new Alert(Alert.AlertType.INFORMATION);
        Alert alertForCancel = new Alert(Alert.AlertType.CONFIRMATION);

        switch (alertType) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Customer name field is empty.");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Customer address field is empty.");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Customer city must be selected.");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Customer postal code field is empty.");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Customer phone number field is empty.");
                alert.showAndWait();
                break;
            case 6:
                alertForSave.setTitle("Save");
                alertForSave.setHeaderText("Customer Saved");
                alertForSave.setContentText("New customer hasCustomer Saved been saved");
                alertForSave.showAndWait();
                break;

        }
    }
}


