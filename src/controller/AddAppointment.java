package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AddAppointment {
    public Button backBttn;
    public Button mainMenuBttn;
    public TextField assignedContractTxtFld;
    public TextField titleTxtFld;
    public TextField typeTxtFld;
    public TextField urlTxtFld;
    public ComboBox locationComboBox;
    public ComboBox endTimeComboBox;
    public ComboBox startTimeComboBox;
    public ComboBox descriptionComboBox;
    public DatePicker dateDatePicker;
    public Button saveAppointmentBtn;
    public TableView currentAppointmentTblView;

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

    public void onActionBack(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/appointmentScreen.fxml");
    }

    public void onActionMainMenu(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/mainMenu.fxml");
    }

    public void onActionLocationCmbBox(ActionEvent actionEvent) {
    }

    public void onActionEndTimeCmbBox(ActionEvent actionEvent) {
    }

    public void onActionStartTimeCmbBox(ActionEvent actionEvent) {
    }

    public void onActionDescriptionCmbBox(ActionEvent actionEvent) {
    }

    public void onActionSaveAppointment(ActionEvent actionEvent) throws IOException {
        System.out.println("Saved appointment");
        buttonChanging(actionEvent, "/view/appointmentScreen.fxml");
    }
}
