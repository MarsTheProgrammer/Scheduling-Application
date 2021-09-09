package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
    private Customer highlightedCustomer;

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
        buttonChanging(actionEvent, "/view/modifyCustomer.fxml");
    }

    public void onActionDelete(ActionEvent actionEvent) {
    }

    public void onActionMainMenu(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/mainMenu.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
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
