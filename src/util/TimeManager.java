package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalTime;

public class TimeManager {

    /** ObservableList for appointment times.*/
    private ObservableList<LocalTime> appointmentTimeList = FXCollections.observableArrayList();
    /** Generates all the times for the time combo boxes on the add and modify appointments screen
     @return appointmentTimeList*/
    public ObservableList<LocalTime> generateTimeList() {
        LocalTime appointmentTime = LocalTime.of(0,0);
        appointmentTimeList.add(appointmentTime);
        for(int i =0; i < 23; i++) {
            appointmentTime = appointmentTime.plusHours(1);
            appointmentTimeList.add(appointmentTime);
        }
        return appointmentTimeList;
    }
}
