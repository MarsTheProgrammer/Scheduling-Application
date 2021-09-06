package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AppointmentScreen {

    //FXML Variables
    public Button mainMenuBttn;
    public TableView calendarSortComboBox;
    public RadioButton byMonthRadBttn;
    public RadioButton byWeekRadBttn;
    public ComboBox calendarSort;
    public Button searchBttn;
    public ToggleGroup appointmentRadBtnTgglGrp;
    public Button addAppointmentBttn;
    public Button deleteAppointmentBttn;
    public Button modifyAppointmentBttn;


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

    public void onActionMainMenu(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/mainMenu.fxml");
    }

    public void onActionSearch(ActionEvent actionEvent) {
    }

    public void onSort(SortEvent<TableView> tableViewSortEvent) {
        //need to mess with this
    }

    public void onActionAddAppointment(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/addAppointment.fxml");
    }

    public void onActionDeleteAppointment(ActionEvent actionEvent) {
        System.out.println("Deleted Appointement");
    }

    public void onActionModifyAppointment(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/modifyAppointment.fxml");
    }
}
