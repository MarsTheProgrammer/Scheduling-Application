package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.JDBC;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Customer {

    /** Customer ID*/
    private int customerID;
    /** Customer Name*/
    private String customerName;
    /** Address*/
    private String address;
    /** City*/
    private String city;
    /** Postal Code*/
    private String postalCode;
    /** Phone number*/
    private String phoneNumber;
    /** Country*/
    private String country;
    /** Observable List of Customer called allCustomersList*/
    public static ObservableList<Customer> allCustomersList = FXCollections.observableArrayList();

    /** Customer constructor
     @param customerID
     @param customerName
     @param address
     @param city
     @param country
     @param postal_Code
     @param phoneNumber */
    public Customer(int customerID, String customerName, String address, String city, String postal_Code, String phoneNumber, String country) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.city = city;
        this.country = country;
        this.postalCode = postal_Code;
        this.phoneNumber = phoneNumber;
    }
    /** Getter for customer ID
     @return customerID*/
    public int getCustomerID() {
        return customerID;
    }

    /** Setter for customer id*/
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /** Getter for customer Name
     @return customer Name*/
    public String getCustomerName() {
        return customerName;
    }

    /** Setter for customerN ame*/
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /** Getter for address
     @return address*/
    public String getAddress() {
        return address;
    }

    /** Setter for address*/
    public void setAddress(String address) {
        this.address = address;
    }

    /** Getter for city
     @return city*/
    public String getCity() {
        return city;
    }

    /** Setter for city*/
    public void setCity(String city) {
        this.city = city;
    }

    /** Getter for Postal Code
     @return Postal Code*/
    public String getPostalCode() {
        return postalCode;
    }

    /** Setter for Postal Code*/
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /** Getter for Phone number
     @return Phone number*/
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /** Setter for Phone number*/
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /** Getter for country
     @return country*/
    public String getCountry() {
        return country;
    }

    /** Setter for country*/
    public void setCountry(String country) {
        this.country = country;
    }

    /** Getter for all customers from the database
     @return allCustomersList*/
    public static ObservableList<Customer> getGetAllCustomers() throws SQLException {

        Statement statement = JDBC.getConnection().createStatement();
        String customerInfoSQL = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, customers.Phone, countries.Country, first_level_divisions.*" +
                                "FROM customers " +
                                "INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                                "INNER JOIN countries ON first_level_divisions.COUNTRY_ID = countries.Country_ID";

        ResultSet customerInfoResults = statement.executeQuery(customerInfoSQL);

        while(customerInfoResults.next()) {
            Customer customer = new Customer(customerInfoResults.getInt("Customer_ID"),
                                            customerInfoResults.getString("Customer_Name"),
                                            customerInfoResults.getString("Address"),
                                            customerInfoResults.getString("Division"),
                                            customerInfoResults.getString("Postal_Code"),
                                            customerInfoResults.getString("Phone"),
                                            customerInfoResults.getString("Country"));
            allCustomersList.add(customer);
        }
        return allCustomersList;
    }
}
