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
import util.DBConnection;
import util.DataBaseQueries;
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
    public Button backBttn;
    public Button mainMenuBttn;
    public TextField assignedContractTxtFld;
    public TextField titleTxtFld;
    public TextField typeTxtFld;
    public ComboBox<LocalTime> endTimeComboBox;
    public ComboBox<LocalTime> startTimeComboBox;
    public DatePicker dateDatePicker;
    public Button saveAppointmentBtn;
    public ComboBox<String> existingCustomerComboBox;
    public TextField descriptionTxtFld;
    public ComboBox<String> contactComboBox;
    public ComboBox<Integer> userIdCombo;
    public TextField locationTextFld;
    public TextField customerIdTextFld;
    private int contactId;



    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
    public static ObservableList<String> existingCustList = FXCollections.observableArrayList();
    public static ObservableList<String> contactNameList = FXCollections.observableArrayList();
    public static ObservableList<Integer> userIdList = FXCollections.observableArrayList();
    public static ObservableList<LocalTime> appointmentTimesList = FXCollections.observableArrayList();
    public static ObservableList<LocalTime> appointmentEndTimesList = FXCollections.observableArrayList();

    Parent scene;
    Stage stage;

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

        //Populates combo boxes from DB
        try {
            //Populates the existing customers combo box
            Statement st = DBConnection.getConnection().createStatement();
            String sqlStatement = "SELECT * FROM customers";
            ResultSet result = st.executeQuery(sqlStatement);

            while(result.next()) {
                existingCustList.add(result.getString("Customer_Name"));
                existingCustomerComboBox.setItems(existingCustList);
            }
            st.close();

            //This populates the contact name combo box
            Statement contactStatement = DBConnection.getConnection().createStatement();
            String sqlContactStatement = "SELECT * FROM contacts";
            ResultSet contactResult = contactStatement.executeQuery(sqlContactStatement);

            while(contactResult.next()) {
                contactNameList.add(contactResult.getString("Contact_Name"));
                contactComboBox.setItems(contactNameList);
            }
            contactStatement.close();

            //This will populate the User ID combo box
            Statement userIdStatement = DBConnection.getConnection().createStatement();
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
            Statement validAppointmentStatement = DBConnection.getConnection().createStatement();
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


    public void onActionSaveAppointment(ActionEvent actionEvent) throws IOException, SQLException {

        try {
            String titleInfo = titleTxtFld.getText();
            String descInfo = descriptionTxtFld.getText();
            String locationInfo = locationTextFld.getText();
            int contactInfo = contactId;
            String typeInfo = typeTxtFld.getText();
            int custID = Integer.parseInt(customerIdTextFld.getText());
            int userID = userIdCombo.getSelectionModel().getSelectedItem();
            LocalDate date = dateDatePicker.getValue();
            LocalTime start = startTimeComboBox.getSelectionModel().getSelectedItem();
            LocalTime end = endTimeComboBox.getSelectionModel().getSelectedItem();
            Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(date, start));
            Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(date, end));

            if(titleNotNull(titleInfo) && descriptionNotNull(descInfo) && typeNotNull(typeInfo) && locationNotNull(locationInfo) && startNotNull(startTimestamp) &&
                    endNotNull(endTimestamp) && dateNotNull(date) && customerNotNull(custID) && contactNotNull(contactId) && userIdNotNull(userID) && isValidAppointment(startTimestamp, endTimestamp)) {

                Alerts.alertDisplays(23);
                DataBaseQueries.insertAppointment(titleInfo, descInfo, locationInfo, typeInfo, startTimestamp, endTimestamp, custID, userID, contactInfo);
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
            } else if(existingCustomerComboBox.getValue() == null) {
                Alerts.alertDisplays(20);
            }
        }
    }



    public void onActionExistingCustomer(ActionEvent actionEvent) throws SQLException {
        String customerName = existingCustomerComboBox.getSelectionModel().getSelectedItem();
        //userIdTextFld.setText(String.valueOf(Controller.getUserIdFromUsername(User.username)));

        Statement st = DBConnection.getConnection().createStatement();
        String sql = "SELECT Customer_ID FROM customers WHERE Customer_Name='" + customerName + "'";
        ResultSet resultSet = st.executeQuery(sql);

        while(resultSet.next()){
            customerIdTextFld.setText(String.valueOf(resultSet.getInt("Customer_ID")));
        }
        st.close();
    }


    //Created this to remove code redundancy
    public void buttonChanging(ActionEvent actionEvent, String resourcesString) throws IOException {
        //Resource Example: "/view/mainMenu.fxml"
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourcesString));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    public void onActionContactComboBox(ActionEvent actionEvent) throws SQLException {
        //This will get the contact name after it's selected in the contacts combo box
        String contactName = contactComboBox.getSelectionModel().getSelectedItem();

        //query to get the contact id
        Statement st = DBConnection.getConnection().createStatement();
        String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name='" + contactName + "'";
        ResultSet resultSet = st.executeQuery(sql);

        //set the contact id to the matching name in the DB
        while(resultSet.next()){
            int contactId = resultSet.getInt("Contact_ID");
            setContactId(contactId);
            System.out.println(contactId);
        }
        st.close();

    }


    //The below handles empty text fields
    public boolean titleNotNull(String title) {
        if (titleTxtFld.getText().isEmpty()) {
            Alerts.alertDisplays(13);
            return false;
        }
        return true;
    }
    public boolean descriptionNotNull(String desc) {
        if (descriptionTxtFld.getText().isEmpty()) {
            Alerts.alertDisplays(14);
            return false;
        }
        return true;
    }
    public boolean typeNotNull(String type) {
        if (typeTxtFld.getText().isEmpty()) {
            Alerts.alertDisplays(16);
            return false;
        }
        return true;
    }
    public boolean locationNotNull(String location) {
        if (locationTextFld.getText().isEmpty()) {
            Alerts.alertDisplays(15);
            return false;
        }
        return true;
    }
    public boolean startNotNull(Timestamp start) {
        if (startTimeComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(18);
            return false;
        }
        return true;
    }
    public boolean endNotNull(Timestamp end) {
        if (endTimeComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(19);
            return false;
        }
        return true;
    }
    public boolean dateNotNull(LocalDate date) {
        if (dateDatePicker.getValue() == null) {
            Alerts.alertDisplays(17);
            return false;
        }
        return true;
    }
    public boolean customerNotNull(int customerId) {
        if (existingCustomerComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(20);
            return false;
        }
        return true;
    }
    public boolean userIdNotNull(int userId) {
        if (userIdCombo.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(21);
            return false;
        }
        return true;
    }
    public boolean contactNotNull(int contact) {
        if (contactComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(22);
            return false;
        }
        return true;
    }



}
