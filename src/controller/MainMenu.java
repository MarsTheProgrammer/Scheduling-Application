package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Alerts;
import util.JDBC;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class MainMenu implements Initializable {

    //FXML Variables
    /** Customers Button */
    public Button customersBttn;

    /** Appointment Button */
    public Button appointmentsBttn;

    /** Reports Button */
    public Button reportsBttn;

    /** Logout Button */
    public Button logoutBttn;

    /** Exit Button */
    public Button exitBttn;

    //Variables
    Parent scene;
    Stage stage;



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

    /** Goes to the Customers Table screen
     @param actionEvent The action event */
    public void onActionCustomers(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/customersTable.fxml");
    }

    /** Goes to the Appointment screen
     @param actionEvent The action event */
    public void onActionAppointments(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/appointmentScreen.fxml");
    }

    /** Goes to All Reports screen
     @param actionEvent The action event */
    public void onActionReports(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/allReports.fxml");
    }

    /** Goes to the Login screen
     @param actionEvent The action event */
    public void onActionLogout(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/sample.fxml");
    }

    /** Closes the program
     @param actionEvent The action event */
    public void onActionExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //In here is where we will call the method to display an alert for 15 min appointment
        try {
            getsAppointmentsIn15MinutesLocal();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Displays an alert to the user if there is an appointment that starts in the next 15 minutes.*/
    public void getsAppointmentsIn15MinutesLocal() throws SQLException {
        LocalDateTime start = LocalDateTime.now().plusMinutes(15);
        Timestamp starter = Timestamp.valueOf(start);
        displayAppointmentReminder(starter);
    }

    public void displayAppointmentReminder(Timestamp starter) throws SQLException {
        Statement appointmentWithin15Minutes = JDBC.getConnection().createStatement();
        String checkForAppointments = "SELECT * " +
                "FROM appointments " +
                "INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                "WHERE Start >= DATE_SUB('" + starter + "',INTERVAL 15 MINUTE)";
        ResultSet appointmentResults = appointmentWithin15Minutes.executeQuery(checkForAppointments);

        if(appointmentResults.next()) {

            Alerts.informationAlert("Appointment Reminder",
                    ("Appointment ID = "+ appointmentResults.getInt(("Appointment_ID")) + " is within 15 minutes") ,
                    "You have an upcoming appointment with " +
                            appointmentResults.getString("Contact_Name") +
                            " and is a " + appointmentResults.getString("Type") + " meeting. It starts at " +
                            appointmentResults.getTimestamp("Start").toLocalDateTime() + ".");
        } else {
            Alerts.informationAlert("No Appointments", "No appointments in the next 15 minutes","");
        }
    }

}
