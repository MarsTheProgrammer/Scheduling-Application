package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import util.JDBC;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.Month;
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
    public Button defaultButton;
    /** Observable List for all appointments By Current Month*/
    private ObservableList<Appointments> filterByMonthList = FXCollections.observableArrayList();
    /** Observable List for all appointments By Current Week*/
    private ObservableList<Appointments> filterByWeekList = FXCollections.observableArrayList();
    /** Selected appointment*/
    public static Appointments highlightedAppointment;
    Parent scene;
    Stage stage;

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

    /** Populates the appointments table.
     Lambda expression is used to populate the contact name in the appointments table.*/
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
        contactTblCol.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getContactName()));
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
            Alerts.alertDisplays(9);
        } else {
            Alert alertForDelete = new Alert(Alert.AlertType.CONFIRMATION);
            alertForDelete.setHeaderText("Are you sure you want to delete this appointment?");
            alertForDelete.setContentText("Press OK to delete the appointment");
            Optional<ButtonType> deleteResult = alertForDelete.showAndWait();

            if(deleteResult.isPresent() && deleteResult.get() == ButtonType.OK) {

                DataBaseQueries.deleteFromAppointmentsTable(highlightedAppointment.getAppointmentId());

                Alerts.informationAlert("Appointment Cancelled",
                        "Appointment ID was " +  highlightedAppointment.getAppointmentId(),
                        "It was a " + highlightedAppointment.getType() + " meeting with " + highlightedAppointment.getContactName());

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

    /** Gets the month Id from the current month*/
    public int monthSelectionToID(String selectedMonth) {
        int monthId;
        switch(selectedMonth){
            case "FEBRUARY":
                monthId = 2;
                break;
            case "MARCH":
                monthId = 3;
                break;
            case "APRIL":
                monthId = 4;
                break;
            case "MAY":
                monthId = 5;
                break;
            case "JUNE":
                monthId = 6;
                break;
            case "JULY":
                monthId = 7;
                break;
            case "AUGUST":
                monthId = 8;
                break;
            case "SEPTEMBER":
                monthId = 9;
                break;
            case "OCTOBER":
                monthId = 10;
                break;
            case "NOVEMBER":
                monthId = 11;
                break;
            case "DECEMBER":
                monthId = 12;
                break;
            default:
                monthId = 1;
        }
        return monthId;
    }

    /** Filters the appointment table by current month
     @param actionEvent Radio button selection*/
    public void onActionFilerByMonth(ActionEvent actionEvent) throws SQLException {
        filterByMonthList.clear();
        filterByWeek().clear();
        appointmentTblView.setItems(filterByMonth());
    }

    /** Filters the appointment table by current week
     @param actionEvent Radio button selection*/
    public void onActionFilterByWeek(ActionEvent actionEvent) throws SQLException {
        filterByMonthList.clear();
        filterByWeek().clear();
        appointmentTblView.setItems(filterByWeek());
    }

    /** Filters the appointments table by month
     @return Returns the loaded list of monthly appointments*/
    public ObservableList<Appointments> filterByMonth() throws SQLException {
        Month currentMonth = LocalDateTime.now().getMonth();
        String month = currentMonth.toString();
        int monthId = monthSelectionToID(month);

        Statement monthlyAppointments = JDBC.getConnection().createStatement();
        String filterByMonthSql =
                "SELECT * " +
                "FROM appointments " +
                "INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                "WHERE month(Start)=" + monthId;

        ResultSet filterResults = monthlyAppointments.executeQuery(filterByMonthSql);

        while(filterResults.next()) {
            Appointments appointments = new Appointments(
                    filterResults.getInt("Appointment_ID"),
                    filterResults.getString("Title"),
                    filterResults.getString("Description"),
                    filterResults.getString("Location"),
                    filterResults.getString("Contact_Name"),
                    filterResults.getString("Type"),
                    filterResults.getTimestamp("Start").toLocalDateTime(),
                    filterResults.getTimestamp("End").toLocalDateTime(),
                    filterResults.getInt("Customer_ID"),
                    filterResults.getInt("User_ID"));
            filterByMonthList.add(appointments);
        }
        return filterByMonthList;
    }

    /** Filters the appointments table by month
     @return Returns the loaded list of monthly appointments*/
    public ObservableList<Appointments> filterByWeek() throws SQLException {

        //Corrected the appointments to show 7 days including the current day

        Statement weeklyAppointments = JDBC.getConnection().createStatement();
        String filterByWeekSql =
                "SELECT * " +
                "FROM appointments " +
                "INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                "WHERE DATE(Start) = DATE(NOW()) " +
                "OR Start >= NOW() " +
                "AND  Start < DATE_ADD(CURRENT_DATE(), interval 7 day)";

        ResultSet filterResults = weeklyAppointments.executeQuery(filterByWeekSql);

        while(filterResults.next()) {
            Appointments appointments = new Appointments(
                    filterResults.getInt("Appointment_ID"),
                    filterResults.getString("Title"),
                    filterResults.getString("Description"),
                    filterResults.getString("Location"),
                    filterResults.getString("Contact_Name"),
                    filterResults.getString("Type"),
                    filterResults.getTimestamp("Start").toLocalDateTime(),
                    filterResults.getTimestamp("End").toLocalDateTime(),
                    filterResults.getInt("Customer_ID"),
                    filterResults.getInt("User_ID"));
            filterByWeekList.add(appointments);
        }
        return filterByWeekList;
    }

    /** Goes to the Appointment screen
     @param actionEvent The action event */
    public void onActionAppointments(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/appointmentScreen.fxml");
    }

    public void onActionDefaultTable(ActionEvent actionEvent) throws IOException {
        onActionAppointments(actionEvent);
    }
}






























