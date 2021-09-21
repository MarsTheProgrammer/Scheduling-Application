package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointments;
import model.Customer;
import util.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ModifyAppointment implements Initializable {

    //FXML Variables
    public Button backBttn;
    public Button mainMenuBttn;
    public TextField typeTxtFld;
    public ComboBox<String> endTimeComboBox;
    public ComboBox<String> startTimeComboBox;
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
    public TextField typeTextFld;
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
        buttonChanging(actionEvent, "/view/appointmentScreen.fxml");
    }

    public void onActionMainMenu(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/mainMenu.fxml");
    }

    public void onActionLocationCmbBox(ActionEvent actionEvent) {
    }

    public void onActionEndTimeCmbBox(ActionEvent actionEvent) {
    }

    public void onActionStartTimeCmbBox(ActionEvent actionEvent) {
    }

    public void onActionDescriptionCmbBox(ActionEvent actionEvent) {
    }

    public void onActionSaveAppointment(ActionEvent actionEvent) throws IOException {


        System.out.println("Appointment saved");
        buttonChanging(actionEvent, "/view/appointmentScreen.fxml");
    }

    public void onActionCustomerCombo(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        highlightedAppointment = AppointmentScreen.getHighlightedAppointment();


        appointmentTxtFld.setText(String.valueOf(highlightedAppointment.getAppointmentId()));
        titleTextFld.setText(highlightedAppointment.getTitle());
        locationTextFld.setText(highlightedAppointment.getLocation());
        descriptionTextFld.setText(highlightedAppointment.getDescription());
        typeTextFld.setText(highlightedAppointment.getType());
        contactNameCombo.setValue(highlightedAppointment.getContactName());
        dateDatePicker.setValue(highlightedAppointment.getStart().toLocalDate());
        startTimeComboBox.setValue(String.valueOf(highlightedAppointment.getStart()));
        endTimeComboBox.setValue(String.valueOf(highlightedAppointment.getEnd()));
        customerIdTextFld.setText(String.valueOf(highlightedAppointment.getCustomerId()));
        userIdCombo.setValue(highlightedAppointment.getUserId());

        try {
            Statement st = DBConnection.getConnection().createStatement();
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
            Statement st = DBConnection.getConnection().createStatement();
            String sqlStatement = "SELECT * FROM customers";
            ResultSet result = st.executeQuery(sqlStatement);

            while(result.next()) {
                existingCustList.add(result.getString("Customer_Name"));
                customerComboBox.setItems(existingCustList);
            }
            st.close();

            //This populates the contact name combo box
            Statement contactStatement = DBConnection.getConnection().createStatement();
            String sqlContactStatement = "SELECT * FROM contacts";
            ResultSet contactResult = contactStatement.executeQuery(sqlContactStatement);

            while(contactResult.next()) {
                contactNameList.add(contactResult.getString("Contact_Name"));
                contactNameCombo.setItems(contactNameList);
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
}
