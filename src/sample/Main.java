package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.JDBC;

import java.sql.SQLException;

/** @author Marshall Christian*/

public class Main extends Application {

    /** Sets the main stage*/
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/sample.fxml"));
        primaryStage.setTitle("Scheduling Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    /** Starts the program with JDBC connection and entry point for progrom*/
    public static void main(String[] args) throws SQLException {
        JDBC.startConnection();
        launch(args);
        JDBC.closeConnection();
    }
}
