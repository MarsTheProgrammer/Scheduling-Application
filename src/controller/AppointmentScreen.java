package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Alerts;
import model.Appointments;
import util.DataBaseQueries;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentScreen implements Initializable {

    //FXML Variables
    public Button mainMenuBttn;
    public TableView<Appointments> appointmentTblView;
    public RadioButton byMonthRadBttn;
    public RadioButton byWeekRadBttn;
    public ComboBox<String> viewBySelectionComboBox;
    public Button searchBttn;
    public ToggleGroup appointmentRadBtnTgglGrp;
    public Button addAppointmentBttn;
    public Button deleteAppointmentBttn;
    public Button modifyAppointmentBttn;
    public TableColumn<Appointments, Integer> appointmentIdTblCol;
    public TableColumn<Appointments, String> titleTblCol;
    public TableColumn<Appointments, String> descriptionTblCol;
    public TableColumn<Appointments, String> locationTblCol;
    public TableColumn<Appointments, String> contactTblCol;
    public TableColumn<Appointments, String> typeTblCol;
    public TableColumn<Appointments, LocalDateTime> startTblCol;
    public TableColumn<Appointments, LocalDateTime> endTblCol;
    public TableColumn<Appointments, Integer> customerIdTblCol;
    public TableColumn<Appointments, Integer> userIdTblCol;


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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Appointments.getGetAllAppointments().clear();
            appointmentTblView.setItems( Appointments.getGetAllAppointments());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        appointmentIdTblCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleTblCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionTblCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationTblCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactTblCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeTblCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTblCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTblCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIdTblCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdTblCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

    }

    public void onActionMainMenu(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/mainMenu.fxml");
    }

    public void onActionSearch(ActionEvent actionEvent) {
    }

    public void onSort(SortEvent<TableView> tableViewSortEvent) {
        //need to mess with this
    }

    public void onActionAddAppointment(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/addAppointment.fxml");
    }

    public void onActionDeleteAppointment(ActionEvent actionEvent) throws SQLException, IOException {

        Appointments highlightedAppointment = appointmentTblView.getSelectionModel().getSelectedItem();

        if(highlightedAppointment == null) {
            Alerts.alertDisplays(9);//need to make this the correct one
        } else {
            Alert alertForDelete = new Alert(Alert.AlertType.CONFIRMATION);
            alertForDelete.setHeaderText("Are you sure you want to delete this appointment?");
            alertForDelete.setContentText("Press OK to delete the appointment");
            Optional<ButtonType> deleteResult = alertForDelete.showAndWait();

            if(deleteResult.isPresent() && deleteResult.get() == ButtonType.OK) {

                DataBaseQueries.deleteFromAppointmentsTable(highlightedAppointment.getAppointmentId());

                Alerts.alertDisplays(11);

                buttonChanging(actionEvent, "/view/appointmentScreen.fxml");
            }
        }

    }

    public void onActionModifyAppointment(ActionEvent actionEvent) throws IOException {
        buttonChanging(actionEvent, "/view/modifyAppointment.fxml");
    }

}
