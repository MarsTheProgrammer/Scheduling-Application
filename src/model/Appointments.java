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

    /**Appointment ID*/
    private int appointmentId;
    /** Title*/
    private String title;
    /** Description*/
    private String description;
    /** Location*/
    private String location;
    /** Type*/
    private String type;
    /** Start*/
    private LocalDateTime start;
    /** End*/
    private LocalDateTime end;
    /** Customer ID*/
    private int customerId;
    /** User ID*/
    private int userId;
    /** Contact Name*/
    private String contactName;

    /** Observable List of all appointments*/
    public static ObservableList<Appointments> allAppointmentsList = FXCollections.observableArrayList();
    /** Observable List of selected contact names*/
    public static ObservableList<Appointments> selectedContactNameList = FXCollections.observableArrayList();

    /**Basic constructor*/
    public Appointments() {}


    /** Appointment constructor
     @param title
     @param start
     @param appointmentId
     @param contactId
     @param customerId
     @param description
     @param location
     @param type
     @param userId
     @param end */
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

    /** Getter for appointment Id
     @return appointment ID*/
    public int getAppointmentId() {
        return appointmentId;
    }

    /** Setter for appointment Id*/
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /** Getter for title
     @return title*/
    public String getTitle() {
        return title;
    }

    /** Setter for title*/
    public void setTitle(String title) {
        this.title = title;
    }

    /** Getter for description
     @return description*/
    public String getDescription() {
        return description;
    }

    /** Setter for description*/
    public void setDescription(String description) {
        this.description = description;
    }

    /** Getter for location
     @return location*/
    public String getLocation() {
        return location;
    }

    /** Setter for location*/
    public void setLocation(String location) {
        this.location = location;
    }
    /** Getter for type
     @return type*/
    public String getType() {
        return type;
    }

    /** Getter for start time
     @return start*/
    public LocalDateTime getStart() {return start;}

    /** Setter for start time*/
    public void setStart(LocalDateTime start) {this.start = start;}

    /** Getter for end time
     @return end*/
    public LocalDateTime getEnd() {
        return end;
    }

    /** Setter for end time*/
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /** Getter for customer Id
     @return Customer ID*/
    public int getCustomerId() {
        return customerId;
    }

    /** Setter for customer Id*/
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /** Getter for user Id
     @return User ID*/
    public int getUserId() {
        return userId;
    }

    /** Setter for user Id*/
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /** Getter for contact name
     @return Contact name*/
    public String getContactName() {
        return contactName;
    }

    /** Setter for contact name*/
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /** Getter to get all appointment from the database
     @return allAppointmentsList*/
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
}
