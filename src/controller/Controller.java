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

import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    //FXML Variables

    public TextField usernameTxtField;

    public TextField passwordTxtField;

    public Button loginButton;

    public Label zoneID;

    //Variables
    Parent scene;
    Stage stage;


    public void onActionLogin(ActionEvent actionEvent) throws IOException {
        //Switches screen when login button is pressed
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //We need to add the zone label to auto to whatever language the OS has selected and change the language
    }
}
