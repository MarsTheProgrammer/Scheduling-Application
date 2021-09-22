package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Appointments {

    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerId;
    private int userId;
    private String contactName;

    public static ObservableList<Appointments> allAppointmentsList = FXCollections.observableArrayList();
    public static ObservableList<Appointments> selectedContactNameList = FXCollections.observableArrayList();

    //private String customerName;
    //private LocalTime time;

    //Basic constructor in the event we need it
    public Appointments() {}

    //Constructor
    public Appointments(int appointmentId, String title, String description, String location, String contactId, String type,
                        LocalDateTime start, LocalDateTime end, int customerId, int userId) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactName = contactId;
    }

    //Getters and Setters
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    //public void setType(String type) {
//        this.type = type;
//    }

    public LocalDateTime getStart() {return start;}

    public void setStart(LocalDateTime start) {this.start = start;}

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public static ObservableList<Appointments> getGetAllAppointments() throws SQLException {

        Statement statement = JDBC.getConnection().createStatement();

        String appointmentInfoSQL = "SELECT appointments.*, contacts.* FROM appointments INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID";

        ResultSet appointmentResults = statement.executeQuery(appointmentInfoSQL);

        while(appointmentResults.next()) {
            Appointments appointments = new Appointments(appointmentResults.getInt("Appointment_ID"),
                                                        appointmentResults.getString("Title"),
                                                        appointmentResults.getString("Description"),
                                                        appointmentResults.getString("Location"),
                                                        appointmentResults.getString("Contact_Name"),
                                                        appointmentResults.getString("Type"),
                                                        appointmentResults.getTimestamp("Start").toLocalDateTime(),
                                                        appointmentResults.getTimestamp("End").toLocalDateTime(),
                                                        appointmentResults.getInt("Customer_ID"),
                                                        appointmentResults.getInt("User_ID"));
            allAppointmentsList.add(appointments);
        }
        return allAppointmentsList;
    }

    public static ObservableList<Appointments> getSelectedContactNameList(int contactID) throws SQLException {

        Statement statement = JDBC.getConnection().createStatement();

        String appointmentInfoSQL = "Select * from appointments WHERE Contact_ID=" + contactID;

        ResultSet appointmentResults = statement.executeQuery(appointmentInfoSQL);

        while(appointmentResults.next()) {
            Appointments appointments = new Appointments(appointmentResults.getInt("Appointment_ID"),
                    appointmentResults.getString("Title"),
                    appointmentResults.getString("Description"),
                    appointmentResults.getString("Location"),
                    appointmentResults.getString("Contact_Name"),
                    appointmentResults.getString("Type"),
                    appointmentResults.getTimestamp("Start").toLocalDateTime(),
                    appointmentResults.getTimestamp("End").toLocalDateTime(),
                    appointmentResults.getInt("Customer_ID"),
                    appointmentResults.getInt("User_ID"));
            selectedContactNameList.add(appointments);
        }
        return selectedContactNameList;
    }


}
