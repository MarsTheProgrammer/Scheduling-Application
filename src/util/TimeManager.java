package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalTime;

public class TimeManager {

    private ObservableList<LocalTime> appointmentTimeList = FXCollections.observableArrayList();

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
