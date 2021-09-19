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
import model.Customer;
import model.User;
import util.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    public static ObservableList<String> appointmentTimesList = FXCollections.observableArrayList("08:00:00", "09:00:00", "10:00:00", "11:00:00","12:00:00","13:00:00",
             "14:00:00", "15:00:00","16:00:00", "17:00:00", "18:00:00", "19:00:00", "20:00:00","21:00:00","22:00:00");

    public TextField customerIdTextFld;

    Parent scene;
    Stage stage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
        buttonChanging(actionEvent, "/view/appointmentScreen.fxml");
    }

    public void onActionMainMenu(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/mainMenu.fxml");
    }

    //NOT SURE IF I NEED THESE??????????
    public void onActionEndTimeCmbBox(ActionEvent actionEvent) {
    }

    public void onActionStartTimeCmbBox(ActionEvent actionEvent) {
    }

    public void onActionSaveAppointment(ActionEvent actionEvent) throws IOException {
        //Here's the long one. WE need to make sure we check the time for start and end. End cannot be the same time or less than the start time
        //We also need to move the time to the users current time. We can store in DB the UTC time but display based on system default

        buttonChanging(actionEvent, "/view/appointmentScreen.fxml");
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
        }
        st.close();

    }

    public void onActionDatePicker(ActionEvent actionEvent) {
    }
}
