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
import model.Appointments;
import util.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AllReports implements Initializable {

    //FXML Variables
    public Label filterReportsBttn;
    public Button mainMenuBttn;
    public ComboBox<String> monthCombo;
    public ComboBox<String> typeCombo;
    public TextField numberOfApptsTextFld;
    public TableView<Appointments> scheduleOfEachCustomerTblView;
    public ComboBox<String> customerComboBox;
    public TextField apptsPerCustomerTextFld;

    private ObservableList<String> typeList = FXCollections.observableArrayList("Meet and Greet", "Conference", "Planning Session");
    private ObservableList<String> monthList = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July",
                                                                            "August", "September", "October", "November", "December");
    private ObservableList<String> customerList = FXCollections.observableArrayList();

    //Variables
    Parent scene;
    Stage stage;

    //Created this to remove code redundancy
    public void buttonChanging(ActionEvent actionEvent, String resourcesString) throws IOException {
        //Resource Example: "/view/mainMenu.fxml"
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourcesString));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    public void onActionMainMenu(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/mainMenu.fxml");
    }

    public void onActionMonthCombo(ActionEvent actionEvent) {
        String month = monthCombo.getSelectionModel().getSelectedItem();
        //We need to get the middle digit?
        //We need to parse the middle digit out of the date
    }

    public void onActionTypeCombo(ActionEvent actionEvent) {
        String type = typeCombo.getSelectionModel().getSelectedItem();
        //uhh matching?
    }

    public void onActionCustomerComboBox(ActionEvent actionEvent) throws SQLException {

        String customerName = customerComboBox.getSelectionModel().getSelectedItem();
        Statement getCustomerCount = DBConnection.getConnection().createStatement();
        String customerCountSQL = "SELECT COUNT(Customer_Name) AS 'Total' FROM appointments " +
                                "INNER JOIN customers " +
                                "ON appointments.Customer_ID = customers.Customer_ID " +
                                "WHERE Customer_Name='" + customerName + "'";
        ResultSet rs = getCustomerCount.executeQuery(customerCountSQL);
        while(rs.next()) {
            apptsPerCustomerTextFld.setText(rs.getString("Total"));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadCustomersList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        monthCombo.setItems(monthList);
        typeCombo.setItems(typeList);
        customerComboBox.setItems(customerList);

        //WE need to populate the apppointments table
    }

    public void loadCustomersList() throws SQLException {

        Statement loadCustomersStatement = DBConnection.getConnection().createStatement();
        String loadCustomerNameSQL = "SELECT * FROM customers";
        ResultSet loadCustomerResults = loadCustomersStatement.executeQuery(loadCustomerNameSQL);

        while(loadCustomerResults.next()) {
            customerList.add(loadCustomerResults.getString("Customer_Name"));
        }
    }
}
