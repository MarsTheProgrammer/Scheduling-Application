package model;

import javafx.scene.control.Alert;

public class Alerts {

    public static void alertDisplays(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Alert alertForSave = new Alert(Alert.AlertType.INFORMATION);
        Alert alertForCancel = new Alert(Alert.AlertType.CONFIRMATION);

        switch (alertType) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Customer name field is empty.");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Customer address field is empty.");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Customer city must be selected.");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Customer postal code field is empty.");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error");
                alert.setHeaderText("All Fields Required");
                alert.setContentText("Customer phone number field is empty.");
                alert.showAndWait();
                break;
            case 6:
                alertForSave.setTitle("Saved Customer");
                alertForSave.setHeaderText("Customer Saved to the Database");
                alertForSave.setContentText("New customer has been saved");
                alertForSave.showAndWait();
                break;
            case 7:
                alertForSave.setTitle("Modified Customer");
                alertForSave.setHeaderText("Customer Modified ");
                alertForSave.setContentText("Customer has been modified and saved");
                alertForSave.showAndWait();
                break;
            case 8:
                alertForSave.setTitle("Deleted Customer");
                alertForSave.setHeaderText("Are you sure you want to delete this customer?");
                alertForSave.setContentText("Deleting the customer will remove them and their appointments");
                alertForSave.showAndWait();
                break;
            case 9:
                alert.setTitle("No Highlighted Customer");
                alert.setHeaderText("Customer was not highlighted");
                alert.setContentText("Please highlight a customer");
                alert.showAndWait();
                break;
            case 10:
                alert.setTitle("No Country Selected");
                alert.setHeaderText("Country field cannot me empty");
                alert.setContentText("Please select a country");
                alert.showAndWait();
                break;
            case 11:
                alert.setTitle("Customer Deleted");
                alert.setHeaderText("Customer was deleted from the the database successfully.");
                alert.setContentText("All customer information and appointments were deleted.");
                alert.showAndWait();
                break;

        }
    }
}
