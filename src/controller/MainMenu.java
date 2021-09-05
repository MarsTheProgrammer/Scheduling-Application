package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu {

    //FXML Variables
    public Button customersBttn;
    public Button appointmentsBttn;
    public Button reportsBttn;
    public Button logoutBttn;
    public Button exitBttn;

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

    //Goes to the customers page
    public void onActionCustomers(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/customersTable.fxml");
    }

    //Goes to the appointments page
    public void onActionAppointments(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/appointmentScreen.fxml");
    }

    //Goes to the report page
    public void onActionReports(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/allReports.fxml");
    }

    //Logs the user out
    public void onActionLogout(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/sample.fxml");
    }

    //Handles the exiting of the program
    public void onActionExit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
