package controller;

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
    /** Observable List for all appointments By Current Month*/
    private ObservableList<Appointments> filterByMonthList = FXCollections.observableArrayList();
    /** Observable List for all appointments By Current Week*/
    private ObservableList<Appointments> filterByWeekList = FXCollections.observableArrayList();

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

                Alerts.informationAlert("Appointment Cancelled", "Appointment ID was " +
                                        highlightedAppointment.getAppointmentId(), "It was a " +
                                        highlightedAppointment.getType() + " meeting");
//                Alert deleteConfirmation = new Alert(Alert.AlertType.INFORMATION);
//                deleteConfirmation.setTitle("Appointment Cancelled");
//                deleteConfirmation.setHeaderText("Appointment ID was " + highlightedAppointment.getAppointmentId());
//                deleteConfirmation.setContentText("It was a " + highlightedAppointment.getType() + " meeting");
//                deleteConfirmation.showAndWait();

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
        System.out.println(monthId);
        return monthId;
    }

    public void onActionFilerByMonth(ActionEvent actionEvent) throws SQLException {
        appointmentTblView.setItems(filterByMonth());
    }

    public void onActionFilterByWeek(ActionEvent actionEvent) throws SQLException {
        appointmentTblView.setItems(filterByWeek());
    }

    /** Filters the appointments table by month
     @return Returns the loaded list of monthly appointments*/
    public ObservableList<Appointments> filterByMonth() throws SQLException {
        Month currentMonth = LocalDateTime.now().getMonth();
        String month = currentMonth.toString();
        int monthId = monthSelectionToID(month);
        System.out.println(monthSelectionToID(month));

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
        Month currentMonth = LocalDateTime.now().getMonth();
        String month = currentMonth.toString();
        int monthId = monthSelectionToID(month);
        System.out.println(monthSelectionToID(month));

        Statement weeklyAppointments = JDBC.getConnection().createStatement();
        String filterByWeekSql = "SELECT * " +
                "FROM appointments " +
                "INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                "WHERE DAY(Start) = DAY(CURRENT_DATE()) AND YEAR(Start) = YEAR(CURRENT_DATE()) " +
                "OR DAY(Start) = DAY(CURRENT_DATE() + 1) AND YEAR(Start) = YEAR(CURRENT_DATE()) " +
                "OR DAY(Start) = DAY(CURRENT_DATE() + 2) AND YEAR(Start) = YEAR(CURRENT_DATE()) " +
                "OR DAY(Start) = DAY(CURRENT_DATE() + 3) AND YEAR(Start) = YEAR(CURRENT_DATE()) " +
                "OR DAY(Start) = DAY(CURRENT_DATE() + 4) AND YEAR(Start) = YEAR(CURRENT_DATE()) " +
                "OR DAY(Start) = DAY(CURRENT_DATE() + 5) AND YEAR(Start) = YEAR(CURRENT_DATE()) " +
                "OR DAY(Start) = DAY(CURRENT_DATE() + 6) AND YEAR(Start) = YEAR(CURRENT_DATE()) " +
                "OR DAY(Start) = DAY(CURRENT_DATE() + 7) AND YEAR(Start) = YEAR(CURRENT_DATE())";

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

}






//    public ObservableList<Appointments> getContactsSchedule() throws SQLException {
//        String contactName = contactCombo.getSelectionModel().getSelectedItem();
//
//        Statement statement = JDBC.getConnection().createStatement();
//        String appointmentInfoSQL = "SELECT appointments.*, contacts.* " +
//                "FROM appointments " +
//                "INNER JOIN contacts " +
//                "ON appointments.Contact_ID = contacts.Contact_ID " +
//                "WHERE Contact_Name='" + contactName + "'";
//
//        ResultSet appointmentResults = statement.executeQuery(appointmentInfoSQL);
//
//        while(appointmentResults.next()) {
//            Appointments appointments = new Appointments(appointmentResults.getInt("Appointment_ID"),
//                    appointmentResults.getString("Title"),
//                    appointmentResults.getString("Description"),
//                    appointmentResults.getString("Location"),
//                    appointmentResults.getString("Contact_Name"),
//                    appointmentResults.getString("Type"),
//                    appointmentResults.getTimestamp("Start").toLocalDateTime(),
//                    appointmentResults.getTimestamp("End").toLocalDateTime(),
//                    appointmentResults.getInt("Customer_ID"),
//                    appointmentResults.getInt("User_ID"));
//            contactAppointmentSchedule.add(appointments);
//        }
//        return contactAppointmentSchedule;
//    }



//
//
//    public void searchAppointmentsByMonthAndType(int monthId, String type) throws SQLException {
//
//        Statement appointmentStatement = JDBC.getConnection().createStatement();
//        String searchByMonthAndType = "SELECT COUNT(Appointment_ID) AS Count FROM appointments WHERE month(Start)=" + monthId + " AND Type='" + type + "'";
//        ResultSet appointmentCount = appointmentStatement.executeQuery(searchByMonthAndType);
//
//        while(appointmentCount.next()) {
//            totalNumberByMonthAndType.setText(String.valueOf(appointmentCount.getInt("Count")));
//        }
//    }
//
//
//




























