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
import model.Customer;
import model.User;
import util.DataBaseQueries;
import util.JDBC;
import util.TimeManager;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class AddAppointment implements Initializable {

    /** Back Button*/
    public Button backBttn;
    /** Main Menu Button*/
    public Button mainMenuBttn;
    /** Contract Text Field*/
    public TextField contractTxtFld;
    /** Title Text Field*/
    public TextField titleTxtFld;
    /** End Time combo box*/
    public ComboBox<LocalTime> endTimeComboBox;
    /** Start time combo box*/
    public ComboBox<LocalTime> startTimeComboBox;
    /** Date picker*/
    public DatePicker dateDatePicker;
    /** Save appointment*/
    public Button saveAppointmentBtn;
    /** Existing customer combo box*/
    public ComboBox<String> existingCustomerComboBox;
    /** Description Text Field*/
    public TextField descriptionTxtFld;
    /** Contact Name combo box*/
    public ComboBox<String> contactComboBox;
    /** User id*/
    public ComboBox<Integer> userIdCombo;
    /** Location text field*/
    public TextField locationTextFld;
    /** Customer id text fieldn*/
    public TextField customerIdTextFld;
    /** Type combo box*/
    public ComboBox<String> typeComboBox;
    /** Contact Id variable*/
    private int contactId;

    /** Getter for contact id*/
    public int getContactId() {
        return contactId;
    }
    /** Setter for contact id*/
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /** Observable List for existing customers*/
    public static ObservableList<String> existingCustList = FXCollections.observableArrayList();
    /** Observable List for contact customers*/
    public static ObservableList<String> contactNameList = FXCollections.observableArrayList();
    /** Observable List for users customers*/
    public static ObservableList<Integer> userIdList = FXCollections.observableArrayList();
    /** Observable List for types customers*/
    private ObservableList<String> typeList = FXCollections.observableArrayList("Meet and Greet", "Conference", "Planning Session");

    //Variables
    Parent scene;
    Stage stage;

    /** Loads the combo boxes with time, customers, users, and contacts.
     @param url URL
     @param resourceBundle Resource Bundle */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Loads the time combo boxes
        TimeManager startTime = new TimeManager();
        startTimeComboBox.setItems(startTime.generateTimeList());
        startTimeComboBox.getSelectionModel().selectFirst();

        TimeManager endTime = new TimeManager();
        ObservableList<LocalTime> endTimeList = endTime.generateTimeList();
        endTimeList.add(LocalTime.of(0, 0));

        endTimeComboBox.setItems(endTimeList);
        endTimeComboBox.getSelectionModel().selectFirst();

        typeComboBox.setItems(typeList);

        //Populates combo boxes from DB
        try {
            //Populates the existing customers combo box
            Statement st = JDBC.getConnection().createStatement();
            String sqlStatement = "SELECT * FROM customers";
            ResultSet result = st.executeQuery(sqlStatement);

            while(result.next()) {
                existingCustList.add(result.getString("Customer_Name"));
                existingCustomerComboBox.setItems(existingCustList);
            }
            st.close();

            //This populates the contact name combo box
            Statement contactStatement = JDBC.getConnection().createStatement();
            String sqlContactStatement = "SELECT * FROM contacts";
            ResultSet contactResult = contactStatement.executeQuery(sqlContactStatement);

            while(contactResult.next()) {
                contactNameList.add(contactResult.getString("Contact_Name"));
                contactComboBox.setItems(contactNameList);
            }
            contactStatement.close();

            //This will populate the User ID combo box
            Statement userIdStatement = JDBC.getConnection().createStatement();
            String sqlUserIdStatement = "SELECT * FROM users";
            ResultSet userIdResult = userIdStatement.executeQuery(sqlUserIdStatement);

            while(userIdResult.next()) {
                userIdList.add(userIdResult.getInt("User_ID"));
                userIdCombo.setItems(userIdList);
            }
            userIdStatement.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /** Goes back to the appointments screen
     @param actionEvent Handles the event of the button being pressed*/
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

    /** Goes back to the main menu screen
     @param actionEvent Handles the event of the button being pressed*/
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

    /** Checks to see if the appointment is within business hours, end isn't before start, and start and end are different times
     @param end Timestamp of the selected end time
     @param start Timestamp of the selected start time*/
    public boolean isValidAppointment(Timestamp start, Timestamp end) {

        boolean endBeforeStart = end.before(start);
        boolean endEqualsStart = end.equals(start);

        //These are for checking to make sure the time is within business hours
        Timestamp startingHours = Timestamp.valueOf(dateDatePicker.getValue() + " 08:00:00");
        Timestamp endingHours = Timestamp.valueOf(dateDatePicker.getValue() + " 22:00:00");

        LocalTime localTimeOfStart = startTimeComboBox.getSelectionModel().getSelectedItem();
        LocalTime localTimeEnd = endTimeComboBox.getSelectionModel().getSelectedItem();

        ZonedDateTime.of(dateDatePicker.getValue(), localTimeOfStart, ZoneId.of("America/New_York"));
        Timestamp startLocal = Timestamp.valueOf(LocalDateTime.of(dateDatePicker.getValue(), localTimeOfStart));
        Timestamp endLocal = Timestamp.valueOf(LocalDateTime.of(dateDatePicker.getValue(), localTimeEnd));

        if(startLocal.before(startingHours) || endLocal.after(endingHours)) {
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
            String validApptSQL = "SELECT * FROM appointments WHERE ('" + start + "' BETWEEN Start AND End OR '" + end + "' BETWEEN Start and End OR '" + start + "' > Start AND '" + end + "' < end)";
            ResultSet checkApptValidation = validAppointmentStatement.executeQuery(validApptSQL);

            if(checkApptValidation.next()) {
                Alerts.alertDisplays(26);
                return false;
            }
        } catch (SQLException se) {
            se.getMessage();
        }
        return true;
    }

    /** Saves the customer to the database. Does various checks to make sure each item follows the requirements of the database.
     @param actionEvent Handles the event of the button being pressed*/
    public void onActionSaveAppointment(ActionEvent actionEvent) throws IOException, SQLException {

        try {
            String titleInfo = titleTxtFld.getText();
            String descInfo = descriptionTxtFld.getText();
            String locationInfo = locationTextFld.getText();
            int contactInfo = contactId;
            String typeInfo = typeComboBox.getSelectionModel().getSelectedItem();
            int custID = Integer.parseInt(customerIdTextFld.getText());
            int userID = userIdCombo.getSelectionModel().getSelectedItem();
            LocalDate date = dateDatePicker.getValue();
            LocalTime start = startTimeComboBox.getSelectionModel().getSelectedItem();
            LocalTime end = endTimeComboBox.getSelectionModel().getSelectedItem();
            Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(date, start));
            Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(date, end));

            if (titleNotNull(titleInfo) && descriptionNotNull(descInfo) && typeNotNull(typeInfo) && locationNotNull(locationInfo) && startNotNull(startTimestamp) &&
                    endNotNull(endTimestamp) && dateNotNull(date) && customerNotNull(custID) &&
                    contactNotNull(contactId) && userIdNotNull(userID) && isValidAppointment(startTimestamp, endTimestamp)) {

                Alerts.alertDisplays(23);
                DataBaseQueries.insertAppointment(titleInfo, descInfo, locationInfo, typeInfo, startTimestamp, endTimestamp, custID, userID, contactInfo);
                buttonChanging(actionEvent, "/view/appointmentScreen.fxml");
            }

        } catch (Exception e) {
            if (customerIdTextFld.getText() == null) {
                Alerts.alertDisplays(20);
            } else if (userIdCombo.getSelectionModel().getSelectedItem() == null) {
                Alerts.alertDisplays(21);
            } else if (dateDatePicker.getValue() == null) {
                Alerts.alertDisplays(17);
            } else if (startTimeComboBox.getSelectionModel().getSelectedItem() == null) {
                Alerts.alertDisplays(18);
            } else if (endTimeComboBox.getValue() == null) {
                Alerts.alertDisplays(19);
            } else if (existingCustomerComboBox.getValue() == null) {
                Alerts.alertDisplays(20);
            }
        }
    }

    /** Sets the customer id text field based on the customer selected from the customer combo box
     @param actionEvent Handles the event of the button being pressed*/
    public void onActionExistingCustomer(ActionEvent actionEvent) throws SQLException {
        String customerName = existingCustomerComboBox.getSelectionModel().getSelectedItem();
        Statement st = JDBC.getConnection().createStatement();
        String sql = "SELECT Customer_ID FROM customers WHERE Customer_Name='" + customerName + "'";
        ResultSet resultSet = st.executeQuery(sql);

        while(resultSet.next()){
            customerIdTextFld.setText(String.valueOf(resultSet.getInt("Customer_ID")));
        }
        st.close();
    }

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

    /** Sets the contact id text field based on the contact name selected from the contact combo box
     @param actionEvent Handles the event of the button being pressed*/
    public void onActionContactComboBox(ActionEvent actionEvent) throws SQLException {
        String contactName = contactComboBox.getSelectionModel().getSelectedItem();

        Statement st = JDBC.getConnection().createStatement();
        String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name='" + contactName + "'";
        ResultSet resultSet = st.executeQuery(sql);

        while(resultSet.next()){
            int contactId = resultSet.getInt("Contact_ID");
            setContactId(contactId);
        }
        st.close();
    }

    /**Throws error if the title field is empty
     @param title The text in the title*/
    public boolean titleNotNull(String title) {
        if (titleTxtFld.getText().isEmpty()) {
            Alerts.alertDisplays(13);
            return false;
        }
        return true;
    }
    /**Throws error if the description field is empty
     @param desc The text in the description*/
    public boolean descriptionNotNull(String desc) {
        if (descriptionTxtFld.getText().isEmpty()) {
            Alerts.alertDisplays(14);
            return false;
        }
        return true;
    }
    /**Throws error if the type combo box is empty
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
     @param start The timestamp of the start combo box*/
    public boolean startNotNull(Timestamp start) {
        if (startTimeComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(18);
            return false;
        }
        return true;
    }
    /**Throws error if the end combo box is empty
     @param end The timestamp of the end combo box*/
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
        if (existingCustomerComboBox.getSelectionModel().getSelectedItem() == null) {
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
    /**Throws error if the contact combo box is empty
     @param contact The text in the contact combo box*/
    public boolean contactNotNull(int contact) {
        if (contactComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(22);
            return false;
        }
        return true;
    }
}
