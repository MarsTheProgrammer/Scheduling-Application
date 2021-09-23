package controller;

import javafx.event.ActionEvent;
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

import util.JDBC;
import util.LoginAttemptTracker;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import model.User;




public class Controller implements Initializable {

    //FXML Variables

    /** Username text field*/
    public TextField usernameTxtField;
    /** Password text field*/
    public TextField passwordTxtField;
    /** Login button*/
    public Button loginButton;
    /** Zone Id label*/
    public Label zoneID;
    /** Exit button*/
    public Button exitBttn;

    //Variables
    Parent scene;
    Stage stage;

    /** Log the user in if user and pass match information in the database
     All login attempts are tracked to the C195LoginAttempts text file
     @param actionEvent Handles the event of the button being pressed*/
    public void onActionLogin(ActionEvent actionEvent) throws IOException, SQLException {

        //Commenting this out now to save time

        String username = usernameTxtField.getText();
        String password = passwordTxtField.getText();
        User.username = username;


//        getUsername(username);
//        getPassword(password);
//
//        if (getUsername(username) && getPassword(password)) {
//            //This should log all log in attempts
//            LoginAttemptTracker.logAttempt(username, true, "You are now logged in.");
//
//            getUserIdFromUsername(username);
//
//            //Go to the main menu
//            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
//            scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
//            stage.setScene(new Scene(scene));
//            stage.show();
//        } else {
//            //This should log all log in attempts
//            LoginAttemptTracker.logAttempt(username, false, "Login failed, please try again.");
//            Alert alert = new Alert((Alert.AlertType.ERROR));
//            alert.setTitle("Invalid Credentials");
//            alert.setHeaderText("Incorrect username and/or password");
//            alert.setContentText("Please enter a valid username and password");
//            alert.showAndWait();
//        }
        //Go to the main menu
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
    }

    /** Gets the passwords from the database and checks it with inserted password
     @param password Password entered at login*/
    private boolean getPassword(String password) throws SQLException {

        Statement statement = JDBC.getConnection().createStatement();
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

    /** Gets the username from the database and checks it with inserted username
     @param username username entered at login*/
    private boolean getUsername(String username) throws SQLException {

        Statement statement = JDBC.getConnection().createStatement();
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

    /** Gets the User id from the username in the database
     @param username username entered at login*/
    public static int getUserIdFromUsername(String username) throws SQLException {

        Statement statement = JDBC.getConnection().createStatement();
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

    /** Exits the program when the exit button is pressed
     @param actionEvent Handles the button being pressed. */
    public void onActionExit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
