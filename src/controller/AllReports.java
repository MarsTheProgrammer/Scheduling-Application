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
import util.JDBC;


import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AllReports implements Initializable {

    /** Wait on this */
    public Label filterReportsBttn;
    /** Main meny Button*/
    public Button mainMenuBttn;
    /** Month combo boc*/
    public ComboBox<String> monthCombo;
    /** Type combo box*/
    public ComboBox<String> typeCombo;
    /** Number of appointments per customer text field*/
    public TextField numberOfApptsTextFld;
    /** Schedule table fiel*/
    public TableView<Appointments> scheduleOfEachContactTblView;
    /** Save Button*/
    public ComboBox<String> customerComboBox;
    /** Save Button*/
    public Button appointmentCountSearchBttn;
    /** Save Button*/
    public ComboBox<String> contactCombo;
    /** Save Button*/
    public int contactId;
    /** Appointment Id table column*/
    public TableColumn<Appointments, Integer> appointmentIdTblCol;
    /** Title table column*/
    public TableColumn<Appointments, String> titleTblCol;
    /** User Id table column*/
    public TableColumn<Appointments, Integer> userIdTblCol;
    /** Customer Id table column*/
    public TableColumn<Appointments, Integer> customerIdTblCol;
    /** Ending time table column*/
    public TableColumn<Appointments, LocalDateTime> endTblCol;
    /** Starting time table column*/
    public TableColumn<Appointments, LocalDateTime> startTblCol;
    /** Contact name table column*/
    public TableColumn<Appointments, String> contactTblCol;
    /** Type table column*/
    public TableColumn<Appointments, String> typeTblCol;
    /** Location table column*/
    public TableColumn<Appointments, String> locationTblCol;
    /** Description table column*/
    public TableColumn<Appointments, String> descriptionTblCol;
    /** Total number of appointments by month and type label*/
    public Label totalNumberByMonthAndType;
    /** Total number of appointment for each selected customer label*/
    public Label totalAppointmentsByCustomer;

    /** Observable List for types of meetings*/
    private ObservableList<String> typeList = FXCollections.observableArrayList("Meet and Greet", "Conference", "Planning Session");
    /** Observable List for all customers*/
    private ObservableList<String> customerList = FXCollections.observableArrayList();
    /** Observable List for all contacts*/
    private ObservableList<String> contactList = FXCollections.observableArrayList();
    /** Observable List for populating the table view by contact name*/
    private ObservableList<Appointments> contactAppointmentSchedule = FXCollections.observableArrayList();
    /** Observable List for months*/
    private ObservableList<String> monthList = FXCollections.observableArrayList("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY",
            "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER");

    /** Scene variable*/
    Parent scene;
    /** Stage variable*/
    Stage stage;

    /** Changed the screen to desired screen
     @param actionEvent The action event
     @param resourcesString The link to the desired screen */
    public void buttonChanging(ActionEvent actionEvent, String resourcesString) throws IOException {
        //Resource Example: "/view/mainMenu.fxml"
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourcesString));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Takes the user to the main menu
     @param actionEvent Main menu button*/
    public void onActionMainMenu(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/mainMenu.fxml");
    }

    /** Queries database for count of all selected customers appointments
     @param actionEvent Combo box selection*/
    public void onActionCustomerComboBox(ActionEvent actionEvent) throws SQLException {

        String customerName = customerComboBox.getSelectionModel().getSelectedItem();
        Statement getCustomerCount = JDBC.getConnection().createStatement();
        String customerCountSQL = "SELECT COUNT(Customer_Name) AS 'Total' FROM appointments " +
                                "INNER JOIN customers " +
                                "ON appointments.Customer_ID = customers.Customer_ID " +
                                "WHERE Customer_Name='" + customerName + "'";
        ResultSet rs = getCustomerCount.executeQuery(customerCountSQL);
        while(rs.next()) {
            totalAppointmentsByCustomer.setText(rs.getString("Total"));
        }
    }

    /** Initializes the combo boxes of the page*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadCustomersList();
            loadContactList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        monthCombo.setItems(monthList);
        typeCombo.setItems(typeList);

    }

    /** Loads the contacts list and sets that to the combo box*/
    public void loadContactList() throws SQLException {
        Statement loadContactStatement = JDBC.getConnection().createStatement();
        String loadContactNameSQL = "SELECT * FROM contacts";
        ResultSet loadContactResults = loadContactStatement.executeQuery(loadContactNameSQL);

        while(loadContactResults.next()) {
            contactList.add(loadContactResults.getString("Contact_Name"));
            contactCombo.setItems(contactList);
        }
    }

    /** Loads the customers list and sets that to the combo box*/
    public void loadCustomersList() throws SQLException {

        Statement loadCustomersStatement = JDBC.getConnection().createStatement();
        String loadCustomerNameSQL = "SELECT * FROM customers";
        ResultSet loadCustomerResults = loadCustomersStatement.executeQuery(loadCustomerNameSQL);

        while(loadCustomerResults.next()) {
            customerList.add(loadCustomerResults.getString("Customer_Name"));
            customerComboBox.setItems(customerList);
        }
    }

    /** Gets the month Id from the month selected in the combo box*/
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

    /** Queries the database for all appointments by month and type.
     @param type Type of appointment.
     @param monthId ID of the month selected.*/
    public void searchAppointmentsByMonthAndType(int monthId, String type) throws SQLException {

        Statement appointmentStatement = JDBC.getConnection().createStatement();
        String searchByMonthAndType = "SELECT COUNT(Appointment_ID) AS Count FROM appointments WHERE month(Start)=" + monthId + " AND Type='" + type + "'";
        ResultSet appointmentCount = appointmentStatement.executeQuery(searchByMonthAndType);

        while(appointmentCount.next()) {
            totalNumberByMonthAndType.setText(String.valueOf(appointmentCount.getInt("Count")));
        }
    }

    /** Enabled by the search button to search for appointments by month and type.
     @param actionEvent Handles the button being pressed*/
    public void onActionSearchNumberOfAppointments(ActionEvent actionEvent) throws SQLException {

        if(monthCombo.getSelectionModel().getSelectedItem() == null) {
            Alerts.errorAlert("Month Empty", "Please fill in the Month combo box.", "");
        } else if(typeCombo.getSelectionModel().getSelectedItem() == null) {
            Alerts.errorAlert("Type Empty", "Please fill in the Type combo box.", "");
        } else {
            String selectedMonth = monthCombo.getSelectionModel().getSelectedItem();
            int monthId = monthSelectionToID(selectedMonth);
            String type = typeCombo.getSelectionModel().getSelectedItem();
            searchAppointmentsByMonthAndType(monthId, type);
        }
    }

    /** Populates the contactAppointmentsSchedule list with all appointments matching selected contact name.
     @return List of selected contact appointments*/
    public ObservableList<Appointments> getContactsSchedule() throws SQLException {
        String contactName = contactCombo.getSelectionModel().getSelectedItem();

        Statement statement = JDBC.getConnection().createStatement();
        String appointmentInfoSQL = "SELECT appointments.*, contacts.* " +
                                "FROM appointments " +
                                "INNER JOIN contacts " +
                                "ON appointments.Contact_ID = contacts.Contact_ID " +
                                "WHERE Contact_Name='" + contactName + "'";

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
            contactAppointmentSchedule.add(appointments);
        }
        return contactAppointmentSchedule;
    }

//    //NEEDED??????????????
//    public void getContactID() throws SQLException {
//        String contactName = contactCombo.getSelectionModel().getSelectedItem();
//
//        Statement st = JDBC.getConnection().createStatement();
//        String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name='" + contactName + "'";
//        ResultSet resultSet = st.executeQuery(sql);
//
//        while(resultSet.next()){
//            contactId = resultSet.getInt("Contact_ID");
//        }
//        st.close();
//    }

    /** Populates the contact schedule table
     @param actionEvent Handles combo box selection*/
    public void onActionContactAppointmentTable(ActionEvent actionEvent) throws SQLException {
        getContactsSchedule();
        try {
            getContactsSchedule().clear();
            scheduleOfEachContactTblView.setItems(getContactsSchedule());
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
}
