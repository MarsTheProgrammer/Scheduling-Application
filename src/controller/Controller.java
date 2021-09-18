package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import model.Alerts;
import util.DBConnection;
import util.LoginAttemptTracker;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    //FXML Variables

    public TextField usernameTxtField;
    public TextField passwordTxtField;
    public Button loginButton;
    public Label zoneID;
    public Button exitBttn;

    //Variables
    Parent scene;
    Stage stage;

    //This will login the user if the credentials are correct, else throw an error
    public void onActionLogin(ActionEvent actionEvent) throws IOException, SQLException {

        //Commenting this out now to save time

        String username = usernameTxtField.getText();
        String password = passwordTxtField.getText();

        getUsername(username);
        getPassword(password);

        if (getUsername(username) && getPassword(password)) {
            //This should log all log in attempts
            LoginAttemptTracker.logAttempt(username, true, "You are now logged in.");

            getUserIdFromUsername(username);

            //Go to the main menu
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            //This should log all log in attempts
            LoginAttemptTracker.logAttempt(username, false, "Login failed, please try again.");
            Alert alert = new Alert((Alert.AlertType.ERROR));
            alert.setTitle("Invalid Credentials");
            alert.setHeaderText("Incorrect username and/or password");
            alert.setContentText("Please enter a valid username and password");
            alert.showAndWait();
        }
        //Go to the main menu
//            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
//            scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
//            stage.setScene(new Scene(scene));
//            stage.show();
    }

    //Gets the password from the database
    private boolean getPassword(String password) throws SQLException {

        Statement statement = DBConnection.getConnection().createStatement();
        String sqlPassword = "SELECT Password FROM users WHERE Password ='" + password + "'";
        ResultSet results = statement.executeQuery(sqlPassword);

        //Gets the password from the results
        while(results.next()) {
            if(results.getString("Password").equals(password)) {
                return true;
            }
        }
        statement.close();
        return false;
    }

    //Gets the username from the database
    private boolean getUsername(String username) throws SQLException {

        Statement statement = DBConnection.getConnection().createStatement();
        String sqlUsername = "SELECT User_Name FROM users WHERE User_Name ='" + username + "'";
        ResultSet results = statement.executeQuery(sqlUsername);

        //Gets the username from the results
        while(results.next()) {
            if(results.getString("User_Name").equals(username)) {
                return true;
            }
        }
        statement.close();
        return false;
    }

    public static int getUserIdFromUsername(String username) throws SQLException {

        Statement statement = DBConnection.getConnection().createStatement();
        String sqlUsername = "SELECT User_ID, User_Name FROM users WHERE User_Name ='" + username + "'";
        ResultSet results = statement.executeQuery(sqlUsername);

        //Gets the username from the results
        while(results.next()) {
            if(results.getString("User_Name").equals(username)) {
                System.out.println(results.getInt("User_ID"));
                return results.getInt("User_ID");
            }
        }
        statement.close();
        Alerts.alertDisplays(12);
        return -1;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //We need to add the zone label to auto to whatever language the OS has selected and change the language
        //this will include getting a resource bundle and local?
    }

    public void onActionExit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
