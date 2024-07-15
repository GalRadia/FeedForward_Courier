package com.example.feedforward_courier.models;

public class Food {

    private String name;
    private int amount;
    private String type;
    private String expiryDate;

    public Food() {
    }

    public Food(String name, int amount, String expiryDate, String type) {
        this.name = name;
        this.amount = amount;
        this.expiryDate = expiryDate;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Food setName(String name) {
        this.name = name;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public Food setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public String getType() {
        return type;
    }

    public Food setType(String type) {
        this.type = type;
        return this;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public Food setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }
}