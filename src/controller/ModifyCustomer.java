package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
    public ComboBox cityComboBox;


    //Variables
    Parent scene;
    Stage stage;

    //Created this to remove code redundancy
    public void buttonChanging(ActionEvent actionEvent, String resourcesString) throws IOException {
        //Resources example: "/view/mainMenu.fxml"
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourcesString));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    public void onActionSaveCustomer(ActionEvent actionEvent) throws IOException {
        System.out.println("Modified customers information");
        buttonChanging(actionEvent, "/view/customersTable.fxml");
    }

    public void onActionCancel(ActionEvent actionEvent)  throws IOException {
        System.out.println("Cancelled saving customers information");
        buttonChanging(actionEvent, "/view/customersTable.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //highlightedCustomer = CustomersTable.getHighlightedCustomer();

        //nameTxtFld = highlightedCustomer.getCustomerName();
    }
}
