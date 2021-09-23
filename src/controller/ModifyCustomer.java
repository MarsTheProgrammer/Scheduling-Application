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
import util.DataBaseQueries;

import util.DataProvider;
import util.JDBC;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;


public class ModifyCustomer implements Initializable {

    //FXML Variables
    /** Save button*/
    public Button saveCustomerBttn;
    /** Cancel button*/
    public Button cancelBttn;
    /** Name text field*/
    public TextField nameTxtFld;
    /** Address text field*/
    public TextField addressTxtFld;
    /** Postal Code text field*/
    public TextField postalCodeTxtFld;
    /** Phone text field*/
    public TextField phoneTxtFld;
    /** City combo box*/
    public ComboBox<String> cityComboBox;
    /** Country combo box*/
    public ComboBox<String> countryComboBox;
    /** Customer id text field*/
    public TextField customerIdTextFld;
    /** Observable List of cities*/
    ObservableList<String> citiesList = FXCollections.observableArrayList();
    /** Observable List of countries*/
    ObservableList<String> countriesList = FXCollections.observableArrayList("U.S", "Canada", "UK");


    //Variables
    Parent scene;
    Stage stage;
    Customer highlightedCustomer;
    public int divisionIDFromCity;

    /** Populates the countries combo box and fills in the fields from the highlighted customer.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Populate the country combo box
        countryComboBox.setItems(countriesList);
        highlightedCustomer = CustomersTable.getHighlightedCustomer();//grabs the highlighted customer

        //Sets the text field values based on highlighted customer
        customerIdTextFld.setText(String.valueOf(highlightedCustomer.getCustomerID()));
        nameTxtFld.setText(highlightedCustomer.getCustomerName());
        addressTxtFld.setText(highlightedCustomer.getAddress());
        postalCodeTxtFld.setText(highlightedCustomer.getPostalCode());
        phoneTxtFld.setText(highlightedCustomer.getPhoneNumber());
        countryComboBox.setValue(highlightedCustomer.getCountry());
        cityComboBox.setValue(highlightedCustomer.getCity());
    }

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

    /** Saves the customer to the database
     @param actionEvent The action event */
    public void onActionSaveCustomer(ActionEvent actionEvent) throws IOException, SQLException {

        String customerName = nameTxtFld.getText();
        String customerAddress = addressTxtFld.getText();
        String customerPostalCode = postalCodeTxtFld.getText();
        String customerPhone = phoneTxtFld.getText();

        DataBaseQueries.updateToCustomersTable(highlightedCustomer.getCustomerID(), customerName, customerAddress, customerPostalCode, customerPhone, DataProvider.divisionID);

        buttonChanging(actionEvent, "/view/customersTable.fxml");

    }

    /** Cancels the modify of the customer and takes them back to the appointments screen.
     @param actionEvent The action event */
    public void onActionCancel(ActionEvent actionEvent)  throws IOException {

        Alert alertForCancel = new Alert(Alert.AlertType.CONFIRMATION);
        alertForCancel.setTitle("Cancel");
        alertForCancel.setHeaderText("Are you sure you want to cancel?");
        alertForCancel.setContentText("This will not change any information");
        Optional<ButtonType> cancelSelection = alertForCancel.showAndWait();

        if(cancelSelection.isPresent() && cancelSelection.get() == ButtonType.OK) {
            buttonChanging(actionEvent, "/view/customersTable.fxml");
        }
    }
    /** Gets the division id from the city combo box.
     @param actionEvent The action event */
    public void onActionCityComboBox(ActionEvent actionEvent) throws SQLException {
        String citySelected = cityComboBox.getSelectionModel().getSelectedItem();
        getAllCitiesDivisionID(citySelected);
        DataProvider.divisionID = divisionIDFromCity;
    }

    /** Gets the division id from the database and selected division
     @param comboBoxSelection combo box selection */
    public void getAllCitiesDivisionID(String comboBoxSelection) throws SQLException {

        Statement state = JDBC.getConnection().createStatement();
        String getAllCitiesDivisionIDSQL = "SELECT Division_ID FROM first_level_divisions WHERE Division='" + comboBoxSelection + "'";
        ResultSet result = state.executeQuery(getAllCitiesDivisionIDSQL);

        while(result.next()) {
            divisionIDFromCity = result.getInt("Division_ID");
        }
    }

    /** Filters the city combo box based on the country combo box selection,
     @param actionEvent Handles the action event */
    public void onActionComboBox(ActionEvent actionEvent) throws SQLException {

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
}
