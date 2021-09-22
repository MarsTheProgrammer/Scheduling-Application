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
import model.Customer;
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

    //FXML Variables
    public Button backBttn;
    public Button mainMenuBttn;
    public ComboBox<LocalTime> endTimeComboBox;
    public ComboBox<LocalTime> startTimeComboBox;
    public DatePicker dateDatePicker;
    public Button saveAppointmentBtn;
    public TextField appointmentTxtFld;
    public TextField locationTextFld;
    public TextField descriptionTextFld;
    public ComboBox<String> customerComboBox;
    public ComboBox<Integer> userIdCombo;
    public TextField titleTextFld;
    public ComboBox<String> contactNameCombo;
    public TextField customerIdTextFld;
    public ComboBox<String> typeComboBox;
    private int contactId;
    private static Appointments highlightedAppointment;

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public static ObservableList<String> existingCustList = FXCollections.observableArrayList();
    public static ObservableList<String> contactNameList = FXCollections.observableArrayList();
    public static ObservableList<Integer> userIdList = FXCollections.observableArrayList();
    private ObservableList<String> typeList = FXCollections.observableArrayList("Meet and Greet", "Conference", "Planning Session");


    //Variables
    Parent scene;
    Stage stage;

    //Created this to remove code redundancy
    public void buttonChanging(ActionEvent actionEvent, String resourcesString) throws IOException {
        //Resources example: "/view/mainMenu.fxml"
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourcesString));
        stage.setScene(new Scene(scene));
        stage.show();
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

    public void onActionSaveAppointment(ActionEvent actionEvent) throws IOException {

        try {
            int contactInfo = contactId;
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

            System.out.println(appointmentId);
            System.out.println(titleInfo);
            System.out.println(descInfo);
            System.out.println(userID);
            System.out.println(locationInfo);
            System.out.println(typeInfo);
            System.out.println(custID);
            System.out.println(startTimestamp);
            System.out.println(endTimestamp);
            System.out.println(contactId);


            if(titleNotNull(titleInfo) && descriptionNotNull(descInfo) && typeNotNull(typeInfo) && locationNotNull(locationInfo) && startNotNull(startTimestamp) &&
                    endNotNull(endTimestamp) && dateNotNull(date) && customerNotNull(custID) &&
                    contactNotNull(contactId) && userIdNotNull(userID) && isValidAppointment(startTimestamp, endTimestamp)) {

                Alerts.alertDisplays(23);
                DataBaseQueries.updateAppointment(appointmentId, titleInfo, descInfo, locationInfo, typeInfo,
                                                    startTimestamp, endTimestamp, custID, userID, contactId);

                //Why is this being skipped?
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

//        buttonChanging(actionEvent, "/view/appointmentScreen.fxml");
    }

    public void onActionCustomerCombo(ActionEvent actionEvent) throws SQLException {
        String customerName = customerComboBox.getSelectionModel().getSelectedItem();
        //userIdTextFld.setText(String.valueOf(Controller.getUserIdFromUsername(User.username)));

        Statement st = JDBC.getConnection().createStatement();
        String sql = "SELECT Customer_ID FROM customers WHERE Customer_Name='" + customerName + "'";
        ResultSet resultSet = st.executeQuery(sql);

        while(resultSet.next()){
            customerIdTextFld.setText(String.valueOf(resultSet.getInt("Customer_ID")));
        }
        st.close();
    }

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

        //Loads the time combo boxes
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
            //Populates the existing customers combo box
            Statement st = JDBC.getConnection().createStatement();
            String sqlStatement = "SELECT * FROM customers";
            ResultSet result = st.executeQuery(sqlStatement);

            while(result.next()) {
                existingCustList.add(result.getString("Customer_Name"));
                customerComboBox.setItems(existingCustList);
            }
            st.close();

            //This populates the contact name combo box
            Statement contactStatement = JDBC.getConnection().createStatement();
            String sqlContactStatement = "SELECT * FROM contacts";
            ResultSet contactResult = contactStatement.executeQuery(sqlContactStatement);

            while(contactResult.next()) {
                contactNameList.add(contactResult.getString("Contact_Name"));
                contactNameCombo.setItems(contactNameList);
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


    //The below handles empty text fields
    public boolean titleNotNull(String title) {
        if (titleTextFld.getText().isEmpty()) {
            Alerts.alertDisplays(13);
            return false;
        }
        return true;
    }
    public boolean descriptionNotNull(String desc) {
        if (descriptionTextFld.getText().isEmpty()) {
            Alerts.alertDisplays(14);
            return false;
        }
        return true;
    }
    public boolean typeNotNull(String type) {
        if (typeComboBox.getSelectionModel().getSelectedItem() == null) {
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
        if (customerComboBox.getSelectionModel().getSelectedItem() == null) {
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
        if (contactNameCombo.getSelectionModel().getSelectedItem() == null) {
            Alerts.alertDisplays(22);
            return false;
        }
        return true;
    }

    public void getContactIDFromContactName() throws SQLException {
        //This will get the contact name after it's selected in the contacts combo box
        String contactName = contactNameCombo.getSelectionModel().getSelectedItem();

        //query to get the contact id
        Statement st = JDBC.getConnection().createStatement();
        String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name='" + contactName + "'";
        ResultSet resultSet = st.executeQuery(sql);

        //set the contact id to the matching name in the DB
        while(resultSet.next()){
            int contactId = resultSet.getInt("Contact_ID");
            setContactId(contactId);
//            System.out.println(contactId);
        }
        st.close();
    }

    public void onActionContactComboBoc(ActionEvent actionEvent) throws SQLException {

        getContactIDFromContactName();
        //This will get the contact name after it's selected in the contacts combo box
//        String contactName = contactNameCombo.getSelectionModel().getSelectedItem();
//
//        //query to get the contact id
//        Statement st = JDBC.getConnection().createStatement();
//        String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name='" + contactName + "'";
//        ResultSet resultSet = st.executeQuery(sql);
//
//        //set the contact id to the matching name in the DB
//        while(resultSet.next()){
//            int contactId = resultSet.getInt("Contact_ID");
//            setContactId(contactId);
//            System.out.println(contactId);
//        }
//        st.close();
    }
}
