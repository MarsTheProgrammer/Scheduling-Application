package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Alerts;
import model.Appointments;
import util.DataBaseQueries;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentScreen implements Initializable {


    /** Main menu Button*/
    public Button mainMenuBttn;
    /** Appointments table viel*/
    public TableView<Appointments> appointmentTblView;
    /** Month radio button*/
    public RadioButton byMonthRadBttn;
    /** Week radio button*/
    public RadioButton byWeekRadBttn;
    /** Radio button toggle group*/
    public ToggleGroup appointmentRadBtnTgglGrp;
    /** Add appointment button*/
    public Button addAppointmentBttn;
    /** Delete appointment button*/
    public Button deleteAppointmentBttn;
    /** Modify appointment button*/
    public Button modifyAppointmentBttn;
    /** Appointment id table column*/
    public TableColumn<Appointments, Integer> appointmentIdTblCol;
    /** Title table column*/
    public TableColumn<Appointments, String> titleTblCol;
    /** Description table column*/
    public TableColumn<Appointments, String> descriptionTblCol;
    /** Location table column*/
    public TableColumn<Appointments, String> locationTblCol;
    /** Contact table column*/
    public TableColumn<Appointments, String> contactTblCol;
    /** Type table column*/
    public TableColumn<Appointments, String> typeTblCol;
    /** Start time table column*/
    public TableColumn<Appointments, LocalDateTime> startTblCol;
    /** End time table column*/
    public TableColumn<Appointments, LocalDateTime> endTblCol;
    /** Customer id table column*/
    public TableColumn<Appointments, Integer> customerIdTblCol;
    /** User id table column*/
    public TableColumn<Appointments, Integer> userIdTblCol;

    //Variables
    Parent scene;
    Stage stage;
     public static Appointments highlightedAppointment;

    /** Getter for highlighted appointment*/
    public static Appointments getHighlightedAppointment() {
        return highlightedAppointment;
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

    /** Populates the appointments table*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Appointments.getGetAllAppointments().clear();
            appointmentTblView.setItems( Appointments.getGetAllAppointments());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        appointmentIdTblCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleTblCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionTblCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationTblCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactTblCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeTblCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTblCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTblCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIdTblCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdTblCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

    }

    /** This will go to main menu
     @param actionEvent Handles the event of the button being pressed*/
    public void onActionMainMenu(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/mainMenu.fxml");
    }

    /** Will go to the add appointment screen
     @param actionEvent Handles the event of the button being pressed*/
    public void onActionAddAppointment(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/addAppointment.fxml");
    }

    /** Will delete the appointment from the database with error and warning screens
     @param actionEvent Handles the event of the button being pressed*/
    public void onActionDeleteAppointment(ActionEvent actionEvent) throws SQLException, IOException {

        Appointments highlightedAppointment = appointmentTblView.getSelectionModel().getSelectedItem();

        if(highlightedAppointment == null) {
            Alerts.alertDisplays(9);//need to make this the correct one
        } else {
            Alert alertForDelete = new Alert(Alert.AlertType.CONFIRMATION);
            alertForDelete.setHeaderText("Are you sure you want to delete this appointment?");
            alertForDelete.setContentText("Press OK to delete the appointment");
            Optional<ButtonType> deleteResult = alertForDelete.showAndWait();

            if(deleteResult.isPresent() && deleteResult.get() == ButtonType.OK) {

                DataBaseQueries.deleteFromAppointmentsTable(highlightedAppointment.getAppointmentId());

                Alert deleteConfirmation = new Alert(Alert.AlertType.INFORMATION);
                deleteConfirmation.setTitle("Appointment Cancelled");
                deleteConfirmation.setHeaderText("Appointment ID was " + highlightedAppointment.getAppointmentId());
                deleteConfirmation.setContentText("It was a " + highlightedAppointment.getType() + " meeting");
                deleteConfirmation.showAndWait();


                buttonChanging(actionEvent, "/view/appointmentScreen.fxml");
            }
        }

    }

    /** Goes to the modify screen
     @param actionEvent Handles the event of the button being pressed*/
    public void onActionModifyAppointment(ActionEvent actionEvent) throws IOException {

        highlightedAppointment = appointmentTblView.getSelectionModel().getSelectedItem();

        if(highlightedAppointment == null) {
            Alert alertForModify = new Alert(Alert.AlertType.ERROR);
            alertForModify.setHeaderText("No appointment highlighted");
            alertForModify.setContentText("Please select an appointment to modify");
            alertForModify.showAndWait();
        } else {
            buttonChanging(actionEvent, "/view/modifyAppointment.fxml");
        }

    }

    public void onActionFilerByMonth(ActionEvent actionEvent) {
        System.out.println("Filter by month");
    }

    public void onActionFilterByWeek(ActionEvent actionEvent) {
        System.out.println("Filter by week");
    }
}
