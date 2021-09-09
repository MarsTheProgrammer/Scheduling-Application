package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Customer {

    //Class variables
    private int customerID;
    private String customerName;
    private String address;
    private String city;
    private String postalCode;
    private String phoneNumber;
    private String country;

    //List
    private static ObservableList<Customer> allCustomersList = FXCollections.observableArrayList();

    //Basic constructor
    public Customer(int customerID, String customerName, String address, String city, String postal_Code, String phoneNumber, String country) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.city = city;
        this.country = country;
        this.postalCode = postal_Code;
        this.phoneNumber = phoneNumber;
    }

    //Getters and Setters
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    //Getter for all customers list
    public static ObservableList<Customer> getGetAllCustomers() throws SQLException {
        Statement statement = DBConnection.getConnection().createStatement();

        //Getting an error here for some reason
        //INNER JOIN first_level_divisions ON customers.Division_ID = first_level_division' at line 1
        String customerInfoSQL = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, customers.Phone, countries.Country, first_level_divisions.Division" +
                                    "FROM customers " +
                                    "INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                                    "INNER JOIN countries ON first_level_divisions.COUNTRY_ID = countries.Country_ID";

        ResultSet customerInfoResults = statement.executeQuery(customerInfoSQL);

        while(customerInfoResults.next()) {
            Customer customer = new Customer(customerInfoResults.getInt("Customer_ID"),customerInfoResults.getString("Customer_Name"),
                    customerInfoResults.getString("Address"),customerInfoResults.getString("Division"),
                    customerInfoResults.getString("Postal_Code"), customerInfoResults.getString("Phone"), customerInfoResults.getString("Country"));
            allCustomersList.add(customer);
        }
        return allCustomersList;
    }

}
