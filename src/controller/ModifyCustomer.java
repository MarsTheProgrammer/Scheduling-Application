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
import util.CustomerQuery;

import util.DBConnection;
import util.DataProvider;


import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;


public class ModifyCustomer implements Initializable {

    //FXML Variables

    public Button saveCustomerBttn;
    public Button cancelBttn;
    public Label countryLabel;
    public TextField nameTxtFld;
    public TextField addressTxtFld;
    public TextField postalCodeTxtFld;
    public TextField phoneTxtFld;
    public ComboBox<String> cityComboBox;
    ObservableList<String> citiesList = FXCollections.observableArrayList();
    ObservableList<String> city = FXCollections.observableArrayList();

    //Variables
    Parent scene;
    Stage stage;
    Customer highlightedCustomer;
    private int customerID;
    public int divisionIDFromCity;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Populates the cities combo box
        try {
            getAllCities();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        highlightedCustomer = CustomersTable.getHighlightedCustomer();

        int customerID = highlightedCustomer.getCustomerID();
        nameTxtFld.setText(highlightedCustomer.getCustomerName());
        addressTxtFld.setText(highlightedCustomer.getAddress());
        postalCodeTxtFld.setText(highlightedCustomer.getPostalCode());
        phoneTxtFld.setText(highlightedCustomer.getPhoneNumber());
        cityComboBox.setValue(highlightedCustomer.getCity());


    }

    //Created this to remove code redundancy
    public void buttonChanging(ActionEvent actionEvent, String resourcesString) throws IOException {
        //Resources example: "/view/mainMenu.fxml"
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourcesString));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    public void onActionSaveCustomer(ActionEvent actionEvent) throws IOException, SQLException {

        String customerName = nameTxtFld.getText();
        String customerA = addressTxtFld.getText();
        String customerPC = postalCodeTxtFld.getText();
        String customerP = phoneTxtFld.getText();


        CustomerQuery.updateToCustomersTable(highlightedCustomer.getCustomerID(), customerName, customerA, customerPC, customerP, DataProvider.divisionID);

        buttonChanging(actionEvent, "/view/customersTable.fxml");

    }

    public void onActionCancel(ActionEvent actionEvent)  throws IOException {

        //ERROR HERE. SOMETHING TO DO WITH THE CITY COMBO BOX NOT UPDATING WHEN PULLING CUSTOMERS INFORMATION
        Alert alertForCancel = new Alert(Alert.AlertType.CONFIRMATION);
        alertForCancel.setTitle("Cancel");
        alertForCancel.setHeaderText("Are you sure you want to cancel?");
        alertForCancel.setContentText("This will not change any information");
        Optional<ButtonType> cancelSelection = alertForCancel.showAndWait();

        if(cancelSelection.isPresent() && cancelSelection.get() == ButtonType.OK) {
            buttonChanging(actionEvent, "/view/customersTable.fxml");
        }
        //buttonChanging(actionEvent, "/view/customersTable.fxml");
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
        DataProvider.divisionID = divisionIDFromCity;
    }

    public void getAllCitiesDivisionID(String comboBoxSelection) throws SQLException {

        Statement state = DBConnection.getConnection().createStatement();

        String getAllCitiesDivisionIDSQL = "SELECT Division_ID FROM first_level_divisions WHERE Division='" + comboBoxSelection + "'";

        ResultSet result = state.executeQuery(getAllCitiesDivisionIDSQL);

        while(result.next()) {
            divisionIDFromCity = result.getInt("Division_ID");
        }


    }


}
