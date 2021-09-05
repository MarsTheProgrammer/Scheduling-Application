package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class CustomersTable {

    //FXML Variables
    public TableView customersTblView;
    public TableColumn customersTblID;
    public TableColumn customersTblName;
    public TableColumn customersTblAddress;
    public TableColumn customersTblCity;
    public TableColumn customersTblCountry;
    public TableColumn customersTblPostalCode;
    public TableColumn customersTblPhone;
    public Button addBttn;
    public Button modifyBttn;
    public Button deleteBtnn;
    public Button addAppointmentBttn;
    public Button mainMenuBttn;

    //Variables
    Parent scene;
    Stage stage;

    //Created this to remove code redundancy
    public void buttonChanging(ActionEvent actionEvent, String resourcesString) throws IOException {
        //Resource Example: "/view/mainMenu.fxml"
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourcesString));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionAdd(ActionEvent actionEvent) {
    }

    public void onActionModify(ActionEvent actionEvent) {
    }

    public void onActionDelete(ActionEvent actionEvent) {
    }

    public void onActionAddAppointment(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/addAppointment.fxml");
    }

    public void onActionMainMenu(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/mainMenu.fxml");
    }
}
