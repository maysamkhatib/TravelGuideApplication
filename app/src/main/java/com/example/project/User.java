package com.example.project;

public class User {

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private TravelDestination [] travelDestinations;

    public User() {

    }

    public User(String email, String firstName, String lastName, String password, TravelDestination[] travelDestinations) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.travelDestinations = travelDestinations;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TravelDestination[] getTravelDestinations() {
        return travelDestinations;
    }

    public void setTravelDestinations(TravelDestination[] travelDestinations) {
        this.travelDestinations = travelDestinations;
    }
}
