package com.example.feedforward_courier.models;


import android.location.Location;

public class Restaurant extends User {
    private Location location;
    private TypeOfFood typeOfFood;
    private Cuisine cuisine;
    private String startTime;
    private String endTime;

    public Restaurant(Location location, TypeOfFood typeOfFood, Cuisine cuisine, String startTime, String endTime) {

    }

    public Restaurant(String email, String name, String userName, String password, String phoneNumber, Location location, TypeOfFood typeOfFood, Cuisine cuisine, String startTime, String endTime) {
        super(email, name, userName, password, phoneNumber);
        this.location = location;
        this.typeOfFood = typeOfFood;
        this.cuisine = cuisine;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Location getLocation() {
        return location;
    }

    public Restaurant setLocation(Location location) {
        this.location = location;
        return this;
    }

    public TypeOfFood getTypeOfFood() {
        return typeOfFood;
    }

    public Restaurant setTypeOfFood(TypeOfFood typeOfFood) {
        this.typeOfFood = typeOfFood;
        return this;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public Restaurant setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
        return this;
    }

    public String getStartTime() {
        return startTime;
    }

    public Restaurant setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public String getEndTime() {
        return endTime;
    }

    public Restaurant setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }
}
