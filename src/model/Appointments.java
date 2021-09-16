package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Appointments {

    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private String start;
    private String end;
    private int customerId;
    private int userId;
    private String contactName;

    public static ObservableList<Appointments> allAppointmentsList = FXCollections.observableArrayList();

    //private String customerName;
    //private LocalTime time;

    //Basic constructor in the event we need it
    public Appointments() {}

    //Constructor
    public Appointments(int appointmentId, String title, String description, String location, String contactId, String type,
                        String start, String end, int customerId, int userId) {
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

    public void setType(String type) {
        this.type = type;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
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

        Statement statement = DBConnection.getConnection().createStatement();

        String appointmentInfoSQL = "SELECT appointments.*, contacts.* FROM appointments INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID";

        ResultSet appointmentInfoResults = statement.executeQuery(appointmentInfoSQL);

        while(appointmentInfoResults.next()) {
            Appointments appointments = new Appointments(appointmentInfoResults.getInt("Appointment_ID"),appointmentInfoResults.getString("Title"),
                    appointmentInfoResults.getString("Description"),appointmentInfoResults.getString("Location"),
                    appointmentInfoResults.getString("Contact_Name"), appointmentInfoResults.getString("Type"), appointmentInfoResults.getString("Start"),
                    appointmentInfoResults.getString("End"), appointmentInfoResults.getInt("Customer_ID"), appointmentInfoResults.getInt("User_ID"));
            allAppointmentsList.add(appointments);
        }
        return allAppointmentsList;
    }

}
