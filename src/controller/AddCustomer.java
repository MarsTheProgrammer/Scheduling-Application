package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
    public ComboBox<String> addCustomerCountryComboBox;

    //Variables
    Parent scene;
    Stage stage;
    Customer highlightedCustomer;

    //Created this to remove code redundancy
    public void buttonChanging(ActionEvent actionEvent, String resourcesString) throws IOException {
        //Resources example: "/view/mainMenu.fxml"
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourcesString));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionCancel(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/customersTable.fxml");
    }

    public void onActionSaveCustomer(ActionEvent actionEvent) throws IOException, SQLException {
        //WE will also need to check for if the fields are filled out or not. and if not, give an actual message.
        //We will need much more functionality here but this works for now
        //INSERT INTO
        //This will assign a unique customer id after the customer has added their information
        //CustomersTable.getUniqueCustomerID();
        //maybe we need to check the DB before assigning

        int customerID = 1;

        for(Customer customer : Customer.getGetAllCustomers()) {
            if (customer.getCustomerID() > customerID) {
                customerID = customer.getCustomerID();
            }
        }

        ++customerID;
        String customerName = nameTxtFld.getText();
        String customerAddress = addressTxtFld.getText();
        String customerPostalCode = postalCodeTxtFld.getText();
        String customerPhoneNumber = phoneTxtFld.getText();
        //String customerCity = cityComboBoxChoice
        //String customerCountry = countryComboBoxChoice

        if (allBoxesFull()) {
            buttonChanging(actionEvent, "/view/customersTable.fxml");
        } else {
            alertDisplays(7);
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {




    }

    public void onActionCountryComboBox(ActionEvent actionEvent) {
    }

    public boolean allBoxesFull() {
        //what we need is to check that all the text boxes are full with information
        if(nameTxtFld.getText() == null) {
            alertDisplays(1);
            return false;
        }
        if (addressTxtFld.getText() == null) {
            alertDisplays(2);
            return false;
        }
        if(postalCodeTxtFld.getText() == null) {
            alertDisplays(5);
            return false;
        }
        if (phoneTxtFld.getText() == null) {
            alertDisplays(6);
            return false;
        }

        return true;
    }

    //Copied for my project for SW1
    private void alertDisplays(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

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
                alert.setContentText("Customer city cannot be none.");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Customer country cannot be none.");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Customer postal code field is empty.");
                alert.showAndWait();
                break;
            case 6:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Customer phone number field is empty.");
                alert.showAndWait();
                break;
            case 7:
                alert.setTitle("Error");
                alert.setHeaderText("Fields");
                alert.setContentText("Customer phone number field is empty.");
                alert.showAndWait();
                break;
        }
    }

}

