package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Alerts;
import model.Appointments;
import util.DataBaseQueries;
import util.JDBC;
import util.TimeManager;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyAppointment implements Initializable {

    /** Back button*/
    public Button backBttn;
    /** Main menu button*/
    public Button mainMenuBttn;
    /** End time combo box*/
    public ComboBox<LocalTime> endTimeComboBox;
    /** Start time combo box*/
    public ComboBox<LocalTime> startTimeComboBox;
    /** Date picker*/
    public DatePicker dateDatePicker;
    /** Save button*/
    public Button saveAppointmentBtn;
    /** Appointment id text field */
    public TextField appointmentTxtFld;
    /** Location text field*/
    public TextField locationTextFld;
    /** Description text field*/
    public TextField descriptionTextFld;
    /** Customer name combo box*/
    public ComboBox<String> customerComboBox;
    /** User id combo box*/
    public ComboBox<Integer> userIdCombo;
    /** Title text field*/
    public TextField titleTextFld;
    /** Contact name combo box*/
    public ComboBox<String> contactNameCombo;
    /** Customer id text field*/
    public TextField customerIdTextFld;
    /** Type combo box*/
    public ComboBox<String> typeComboBox;
    /** Contact id*/
    private int contactId;
    /** Highlighted appointment*/
    private static Appointments highlightedAppointment;

    /** Setter for contact id*/
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /** Observable List of existing customers*/
    public static ObservableList<String> existingCustList = FXCollections.observableArrayList();
    /** Observable List of contact name*/
    public static ObservableList<String> contactNameList = FXCollections.observableArrayList();
    /** Observable List of user id*/
    public static ObservableList<Integer> userIdList = FXCollections.observableArrayList();
    /** Observable List of types*/
    private ObservableList<String> typeList = FXCollections.observableArrayList("Meet and Greet", "Conference", "Planning Session");

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
    /** Goes back to the appointments screen
     @param actionEvent The action event */
    public void onActionBack(ActionEvent actionEvent) throws IOException {
        Alert alertForBack = new Alert(Alert.AlertType.CONFIRMATION);
        alertForBack.setTitle("Cancel");
        alertForBack.setHeaderText("Are You Sure You Want To Go Back?");
        alertForBack.setContentText("This will clear all text fields and your data will be lost");
        Optional<ButtonType> backSelection = alertForBack.showAndWait();

        if(backSelection.isPresent() && backSelection.get() == ButtonType.OK) {
            buttonChanging(actionEvent, "/view/appointmentScreen.fxml");
        }
    }

    /** Goes back to the main menu screen.
     @param actionEvent The action event. */
    public void onActionMainMenu(ActionEvent actionEvent) throws IOException {
        Alert alertForMainMenu = new Alert(Alert.AlertType.CONFIRMATION);
        alertForMainMenu.setTitle("Cancel");
        alertForMainMenu.setHeaderText("Are You Sure You Want To Go the Main Menu?");
        alertForMainMenu.setContentText("This will clear all text fields and your data will be lost");
        Optional<ButtonType> MainMenuSelection = alertForMainMenu.showAndWait();

        if(MainMenuSelection.isPresent() && MainMenuSelection.get() == ButtonType.OK) {
            buttonChanging(actionEvent, "/view/mainMenu.fxml");
        }
    }

    /** Saves the appointments to the database after check valid input.
     @param actionEvent The action event. */
    public void onActionSaveAppointment(ActionEvent actionEvent) throws IOException {

        try {
            int custID = Integer.parseInt(customerIdTextFld.getText());
            int userID = userIdCombo.getSelectionModel().getSelectedItem();
            int appointmentId = highlightedAppointment.getAppointmentId();
            String titleInfo = titleTextFld.getText();
            String descInfo = descriptionTextFld.getText();
            String locationInfo = locationTextFld.getText();
            String typeInfo = typeComboBox.getSelectionModel().getSelectedItem();
            LocalDate date = dateDatePicker.getValue();
            LocalTime start = startTimeComboBox.getSelectionModel().getSelectedItem();
            LocalTime end = endTimeComboBox.getSelectionModel().getSelectedItem();
            Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(date, start));
            Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(date, end));

            if(titleNotNull(titleInfo) && descriptionNotNull(descInfo) && typeNotNull(typeInfo) && locationNotNull(locationInfo) && startNotNull(startTimestamp) &&
                    endNotNull(endTimestamp) && dateNotNull(date) && customerNotNull(custID) &&
                    contactNotNull(contactId) && userIdNotNull(userID) && isValidAppointment(startTimestamp, endTimestamp)) {

                Alerts.alertDisplays(23);
                DataBaseQueries.updateAppointment(appointmentId, titleInfo, descInfo, locationInfo, typeInfo,
                                                    startTimestamp, endTimestamp, custID, userID, contactId);

                buttonChanging(actionEvent, "/view/appointmentScreen.fxml");
            }
        } catch(Exception e) {
            if(customerIdTextFld.getText() == null) {
                Alerts.alertDisplays(20);
            } else if(userIdCombo.getSelectionModel().getSelectedItem() == null) {
                Alerts.alertDisplays(21);
            } else if(dateDatePicker.getValue() == null) {
                Alerts.alertDisplays(17);
            } else if(startTimeComboBox.getSelectionModel().getSelectedItem() == null) {
                Alerts.alertDisplays(18);
            } else if(endTimeComboBox.getValue() == null) {
                Alerts.alertDisplays(19);
            } else if(customerComboBox.getValue() == null) {
                Alerts.alertDisplays(20);
            }
        }
    }

    /** Populates the appointments id text field with the matching customer information from the DB
     @param actionEvent The action event */
    public void onActionCustomerCombo(ActionEvent actionEvent) throws SQLException {
        String customerName = customerComboBox.getSelectionModel().getSelectedItem();

        Statement st = JDBC.getConnection().createStatement();
        String sql = "SELECT Customer_ID FROM customers WHERE Customer_Name='" + customerName + "'";
        ResultSet resultSet = st.executeQuery(sql);

        while(resultSet.next()){
            customerIdTextFld.setText(String.valueOf(resultSet.getInt("Customer_ID")));
        }
        st.close();
    }

    /** Loads the data from the appointments screen into the matching fields */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        highlightedAppointment = AppointmentScreen.getHighlightedAppointment();

        appointmentTxtFld.setText(String.valueOf(highlightedAppointment.getAppointmentId()));
        titleTextFld.setText(highlightedAppointment.getTitle());
        locationTextFld.setText(highlightedAppointment.getLocation());
        descriptionTextFld.setText(highlightedAppointment.getDescription());
        typeComboBox.setValue(highlightedAppointment.getType());
        contactNameCombo.setValue(highlightedAppointment.getContactName());

        dateDatePicker.setValue(highlightedAppointment.getStart().toLocalDate());
        startTimeComboBox.setValue(highlightedAppointment.getStart().toLocalTime());
        endTimeComboBox.setValue((highlightedAppointment.getEnd().toLocalTime()));
        customerIdTextFld.setText(String.valueOf(highlightedAppointment.getCustomerId()));
        userIdCombo.setValue(highlightedAppointment.getUserId());
        try {
            getContactIDFromContactName();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Malcome Warara helped set this up
        TimeManager startTime = new TimeManager();
        startTimeComboBox.setItems(startTime.generateTimeList());

        TimeManager endTime = new TimeManager();
        ObservableList<LocalTime> endTimeList = endTime.generateTimeList();
        endTimeList.add(LocalTime.of(0, 0));

        endTimeComboBox.setItems(endTimeList);
        typeComboBox.setItems(typeList);

        try {
            Statement st = JDBC.getConnection().createStatement();
            String sql = "SELECT * FROM customers WHERE Customer_ID=" + highlightedAppointment.getCustomerId();
            ResultSet rs = st.executeQuery(sql);

            if(rs.next()) {
                customerComboBox.setValue(rs.getString("Customer_Name"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            Statement populateExistingCustomers = JDBC.getConnection().createStatement();
            String sqlStatement = "SELECT * FROM customers";
            ResultSet result = populateExistingCustomers.executeQuery(sqlStatement);

            while(result.next()) {
                existingCustList.add(result.getString("Customer_Name"));
                customerComboBox.setItems(existingCustList);
            }
            populateExistingCustomers.close();

            Statement populateContactStatement = JDBC.getConnection().createStatement();
            String sqlContactStatement = "SELECT * FROM contacts";
            ResultSet contactResult = populateContactStatement.executeQuery(sqlContactStatement);

            while(contactResult.next()) {
                contactNameList.add(contactResult.getString("Contact_Name"));
                contactNameCombo.setItems(contactNameList);
            }
            populateContactStatement.close();

            Statement populateUserIdStatement = JDBC.getConnection().createStatement();
            String sqlUserIdStatement = "SELECT * FROM users";
            ResultSet userIdResult = populateUserIdStatement.executeQuery(sqlUserIdStatement);

            while(userIdResult.next()) {
                userIdList.add(userIdResult.getInt("User_ID"));
                userIdCombo.setItems(userIdList);
            }
            populateUserIdStatement.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /** Checks to make sure the appointments are after each other, not equal, and fall within business hours
     @param start Start appointment timestamp
     @param end End appointment timestamp*/
    public boolean isValidAppointment(Timestamp start, Timestamp end) {

        boolean endBeforeStart = end.before(start);
        boolean endEqualsStart = end.equals(start);

        Timestamp startingBusinessHours = Timestamp.valueOf(dateDatePicker.getValue() + " 08:00:00");
        Timestamp endingBusinessHours = Timestamp.valueOf(dateDatePicker.getValue() + " 22:00:00");

        LocalTime localTimeOfStart = startTimeComboBox.getSelectionModel().getSelectedItem();
        LocalTime localTimeEnd = endTimeComboBox.getSelectionModel().getSelectedItem();

        ZonedDateTime.of(dateDatePicker.getValue(), localTimeOfStart, ZoneId.of("America/New_York"));
        Timestamp startLocal = Timestamp.valueOf(LocalDateTime.of(dateDatePicker.getValue(), localTimeOfStart));
        Timestamp endLocal = Timestamp.valueOf(LocalDateTime.of(dateDatePicker.getValue(), localTimeEnd));

        if(startLocal.before(startingBusinessHours) || endLocal.after(endingBusinessHours)) {
            Alerts.alertDisplays(29);
            return false;
        }
        if(endBeforeStart) {
            Alerts.alertDisplays(24);
            return false;
        }
        if(endEqualsStart) {
            Alerts.alertDisplays(25);
            return false;
        }
        try {
            Statement validAppointmentStatement = JDBC.getConnection().createStatement();
            String validApptSQL = "SELECT COUNT(appointments.Start) AS Count " +
                    "FROM appointments " +
                    "WHERE ('" + start + "' BETWEEN Start AND End " +
                    "OR '" + end + "' BETWEEN Start AND End)" +
                    "OR ('" + start + "' > Start " +
                    "AND '" + end + "' < End)";

            ResultSet checkApptValidation = validAppointmentStatement.executeQuery(validApptSQL);

            if(checkApptValidation.next() && checkApptValidation.getInt("Count") > 1) {
                Alerts.alertDisplays(26);
                return false;
            }
        } catch (SQLException se) {
            se.getMessage();
        }
        return true;
    }


    /**Throws error if the title field is empty
     @param title The text in the title*/
    public boolean titleNotNull(String title) {
        if (titleTextFld.getText().isEmpty()) {
            Alerts.alertDisplays(13);
            return false;
        }
        return true;
    }

    /**Throws error if the desc field is empty
     @param desc The text in the description*/
    public boolean descriptionNotNull(String desc) {
        if (descriptionTextFld.getText().isEmpty()) {
            Alerts.alertDisplays(14);
            return false;
        }
        return true;
    }

    /**Throws error if the type field is empty
     @param type The text in the type*/
    public boolean typeNotNull(String type) {
        if (typeComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(16);
            return false;
        }
        return true;
    }

    /**Throws error if the location field is empty
     @param location The text in the location*/
    public boolean locationNotNull(String location) {
        if (locationTextFld.getText().isEmpty()) {
            Alerts.alertDisplays(15);
            return false;
        }
        return true;
    }

    /**Throws error if the start combo box is empty
     @param start The time of the start combo box*/
    public boolean startNotNull(Timestamp start) {
        if (startTimeComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(18);
            return false;
        }
        return true;
    }

    /**Throws error if the end combo box is empty
     @param end The time of the end combo box*/
    public boolean endNotNull(Timestamp end) {
        if (endTimeComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(19);
            return false;
        }
        return true;
    }

    /**Throws error if the date picker is empty
     @param date The local date of the date picker*/
    public boolean dateNotNull(LocalDate date) {
        if (dateDatePicker.getValue() == null) {
            Alerts.alertDisplays(17);
            return false;
        }
        return true;
    }

    /**Throws error if the customerId field is empty
     @param customerId The text in the customerId*/
    public boolean customerNotNull(int customerId) {
        if (customerComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(20);
            return false;
        }
        return true;
    }

    /**Throws error if the userId field is empty
     @param userId The text in the userId*/
    public boolean userIdNotNull(int userId) {
        if (userIdCombo.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(21);
            return false;
        }
        return true;
    }

    /**Throws error if the contact field is empty
     @param contact The text in the location*/
    public boolean contactNotNull(int contact) {
        if (contactNameCombo.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(22);
            return false;
        }
        return true;
    }

    /** Gets the contact id from the selected contact name in the combo box */
    public void getContactIDFromContactName() throws SQLException {
        String contactName = contactNameCombo.getSelectionModel().getSelectedItem();

        Statement st = JDBC.getConnection().createStatement();
        String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name='" + contactName + "'";
        ResultSet resultSet = st.executeQuery(sql);

        while(resultSet.next()){
            int contactId = resultSet.getInt("Contact_ID");
            setContactId(contactId);
        }
        st.close();
    }

    /** Calls to get the contact id from the contact name
     @param actionEvent Action even*/
    public void onActionContactComboBoc(ActionEvent actionEvent) throws SQLException {
        getContactIDFromContactName();
    }
}
