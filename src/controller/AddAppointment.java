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

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddAppointment implements Initializable {
    public Button backBttn;
    public Button mainMenuBttn;
    public TextField assignedContractTxtFld;
    public TextField titleTxtFld;
    public TextField typeTxtFld;
    public ComboBox<String> endTimeComboBox;
    public ComboBox<String> startTimeComboBox;
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
    public static ObservableList<String> appointmentTimesList = FXCollections.observableArrayList("08:00:00", "08:15:00", "08:30:00", "08:45:00",
                                                                                                    "09:00:00", "09:15:00", "09:30:00", "09:45:00",
                                                                                                    "10:00:00", "10:15:00", "10:30:00", "10:45:00",
                                                                                                    "11:00:00", "11:15:00", "11:30:00", "11:45:00",
                                                                                                    "12:00:00", "12:15:00", "12:30:00", "12:45:00",
                                                                                                    "13:00:00", "13:15:00", "13:30:00", "13:45:00",
                                                                                                    "14:00:00", "14:15:00", "14:30:00", "14:45:00",
                                                                                                    "15:00:00", "15:15:00", "15:30:00", "15:45:00",
                                                                                                    "16:00:00", "16:15:00", "16:30:00", "16:45:00",
                                                                                                    "17:00:00", "17:15:00", "17:30:00", "17:45:00",
                                                                                                    "18:00:00", "18:15:00", "18:30:00", "18:45:00",
                                                                                                    "19:00:00", "19:15:00", "19:30:00", "19:45:00",
                                                                                                    "20:00:00", "20:15:00", "20:30:00", "20:45:00",
                                                                                                    "21:00:00", "21:15:00", "21:30:00", "21:45:00",
                                                                                                    "22:00:00");


    Parent scene;
    Stage stage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //WE NEED TO DISPLAY THE TIMES IN LOCALE TIME
//        for (String list : appointmentTimesList) {
//            DateTimeFormatter dt = DateTimeFormatter.ofPattern("HH:mm:ss");
//            LocalTime lT = LocalTime.parse(list, dt);
//            appointmentTimesList.add(list);
//            System.out.println(lT);
//            System.out.println(appointmentTimesList.toString());
//        }
        startTimeComboBox.setItems(appointmentTimesList);
        endTimeComboBox.setItems(appointmentTimesList);
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

        //in here we need to grab the getUserId form the controller class. We also need to figure out how to get the username
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


    public void onActionSaveAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        //Here's the long one. WE need to make sure we check the time for start and end. End cannot be the same time or less than the start time
        //We need to check the collides and end not being before start

        //INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID)
        //VALUES ('title', 'description', 'location', 'type', '2021-09-20 08:00', '2021-09-20 09:00', 2, 1, 3)

//        if(existingCustomerComboBox.getSelectionModel().getSelectedItem() == null) {
//            Alerts.alertDisplays(20);
//        }


        //Big mess saying if everything is filled out,  add the appt in

        //UserId, customer Id, date, both times are not working if null
        if(existingCustomerComboBox.getSelectionModel().getSelectedItem() != null && !titleTxtFld.getText().equals("") && !descriptionTxtFld.getText().equals("") && !locationTextFld.getText().equals("") && !typeTxtFld.getText().equals("") &&
                contactComboBox.getSelectionModel().getSelectedItem() != null && startTimeComboBox.getSelectionModel().getSelectedItem() != null &&
                endTimeComboBox.getSelectionModel().getSelectedItem() != null && dateDatePicker.getValue() != null &&
                userIdCombo.getSelectionModel().getSelectedItem() != null) {

            String titleInfo = titleTxtFld.getText();
            String descInfo = descriptionTxtFld.getText();
            String locationInfo = locationTextFld.getText();
            int contactInfo = contactId;
            String typeInfo = typeTxtFld.getText();
            LocalDate date = dateDatePicker.getValue();
            String start = startTimeComboBox.getSelectionModel().getSelectedItem();
            String end = endTimeComboBox.getSelectionModel().getSelectedItem();
            int custID = Integer.parseInt(customerIdTextFld.getText());
            int userID = userIdCombo.getSelectionModel().getSelectedItem();
            Timestamp startTimestamp = Timestamp.valueOf(date + " " +  start);
            Timestamp endTimestamp = Timestamp.valueOf(date + " " +  end);

            Alerts.alertDisplays(23);

            DataBaseQueries.insertAppointment(titleInfo, descInfo, locationInfo, typeInfo, startTimestamp, endTimestamp, custID, userID, contactInfo);

            buttonChanging(actionEvent, "/view/appointmentScreen.fxml");
        } else {
            Alerts.alertDisplays(20);
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


//    //The below handles empty text fields
//    public boolean titleNotNull(String title) {
//        if (titleTxtFld.getText().isEmpty()) {
//            Alerts.alertDisplays(13);
//            return false;
//        }
//        return true;
//    }
//    public boolean descriptionNotNull(String des) {
//        if (descriptionTxtFld.getText().isEmpty()) {
//            Alerts.alertDisplays(14);
//            return false;
//        }
//        return true;
//    }
//    public boolean typeNotNull(String type) {
//        if (typeTxtFld.getText().isEmpty()) {
//            Alerts.alertDisplays(16);
//            return false;
//        }
//        return true;
//    }
//    public boolean locationNotNull(String location) {
//        if (locationTextFld.getText().isEmpty()) {
//            Alerts.alertDisplays(15);
//            return false;
//        }
//        return true;
//    }
//    public boolean startNotNull(Timestamp start) {
//        if (startTimeComboBox.getSelectionModel().getSelectedItem() == null) {
//            Alerts.alertDisplays(18);
//            return false;
//        }
//        return true;
//    }
//    public boolean endNotNull(Timestamp end) {
//        if (endTimeComboBox.getSelectionModel().getSelectedItem() == null) {
//            Alerts.alertDisplays(19);
//            return false;
//        }
//        return true;
//    }
//    public boolean dateNotNull(LocalDate date) {
//        if (dateDatePicker.getValue() == null) {
//            Alerts.alertDisplays(17);
//            return false;
//        }
//        return true;
//    }
//    public boolean customerNotNull(int customerId) {
//        if (existingCustomerComboBox.getSelectionModel().getSelectedItem() == null) {
//            Alerts.alertDisplays(20);
//            return false;
//        }
//        return true;
//    }
//    public boolean userIdNotNull(int userId) {
//        if (userIdCombo.getSelectionModel().getSelectedItem() == null) {
//            Alerts.alertDisplays(21);
//            return false;
//        }
//        return true;
//    }
//    public boolean contactNotNull(int contactId) {
//        if (contactComboBox.getSelectionModel().getSelectedItem() == null) {
//            Alerts.alertDisplays(22);
//            return false;
//        }
//        return true;
//    }



}
