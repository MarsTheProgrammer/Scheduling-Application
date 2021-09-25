package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Alerts;

import util.JDBC;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import model.User;
import util.LoginAttemptTracker;


public class Controller implements Initializable {

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
    public Label usernameLabel;
    public Label passwordLabel;

    public static String titleForUserID;
    public static String headerForUserID;
    public static String contextForUserID;

    public static String titleForLogin;
    public static String headerForLogin;
    public static String contextForLogin;


    /** Scene variable*/
    Parent scene;
    /** Stage variable*/
    Stage stage;

    /** Log the user in if user and pass match information in the database
     All login attempts are tracked to the C195LoginAttempts text file
     @param actionEvent Handles the event of the button being pressed*/
    public void onActionLogin(ActionEvent actionEvent) throws IOException, SQLException {
        String username = usernameTxtField.getText();
        String password = passwordTxtField.getText();
        User.username = username;

        getUsername(username);
        getPassword(password);

        if (getUsername(username) && getPassword(password)) {
            LoginAttemptTracker.logAttempt(username, true, "You are now logged in.");
            getUserIdFromUsername(username);

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            LoginAttemptTracker.logAttempt(username, false, "Login failed, please try again.");
            Alerts.errorAlert(titleForLogin, headerForLogin, contextForLogin);

        }
//            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
//            scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
//            stage.setScene(new Scene(scene));
//            stage.show();
    }

    /** Gets the passwords from the database and checks it with inserted password
     @param password Password entered at login*/
    private boolean getPassword(String password) throws SQLException {
        Statement statement = JDBC.getConnection().createStatement();
        String sqlPassword = "SELECT Password FROM users WHERE Password ='" + password + "'";
        ResultSet results = statement.executeQuery(sqlPassword);

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

        while(results.next()) {
            if(results.getString("User_Name").equals(username)) {
                System.out.println(results.getInt("User_ID"));
                return results.getInt("User_ID");
            }
        }
        statement.close();
        Alerts.alertDisplays(12);
        Alerts.errorAlert(titleForUserID, headerForUserID, contextForUserID);
        return -1;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ZoneId zone = ZoneId.systemDefault();
        zoneID.setText(zone.toString());

        Locale locale = Locale.FRANCE;
//        Locale locale = Locale.getDefault();
        ResourceBundle rsBundle = ResourceBundle.getBundle("LanguageBundles/mchristian", locale);

        if(locale.getLanguage().equals("fr")) {

            this.usernameTxtField.setPromptText(rsBundle.getString("usernameFieldPromptText"));
            this.passwordTxtField.setPromptText(rsBundle.getString("passwordFieldPromptText"));
            this.usernameLabel.setText(rsBundle.getString("username"));
            this.passwordLabel.setText(rsBundle.getString("password"));
            this.loginButton.setText(rsBundle.getString("loginButtonText"));
            this.exitBttn.setText(rsBundle.getString("exitBttnText"));
            titleForUserID = rsBundle.getString("titleForUserID");
            headerForUserID = rsBundle.getString("headerForUserID");
            contextForUserID = rsBundle.getString("contextForUserID");
            titleForLogin = rsBundle.getString("titleForLogin");
            headerForLogin = rsBundle.getString("headerForLogin");
            contextForLogin = rsBundle.getString("contextForLogin");
        }

        //We need to add the zone label to auto to whatever language the OS has selected and change the language
        //this will include getting a resource bundle and local?


//
//# lblErrorAlert = Nom d'utilisateur ou mot de passe incorrect
//
//# We should all the title, header, and context error here for the french version


    }

    /** Exits the program when the exit button is pressed
     @param actionEvent Handles the button being pressed. */
    public void onActionExit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
