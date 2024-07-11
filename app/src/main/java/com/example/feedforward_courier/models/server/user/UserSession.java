package com.example.feedforward_courier.models.server.user;


import com.example.feedforward_courier.models.Courier;

public class UserSession {
    private static UserSession instance;
    private Courier courier;
    private UserBoundary user;
    private final String SUPERAPP = "2024b.gal.said";

    public String getSUPERAPP() {
        return SUPERAPP;
    }

    private UserSession() {
    }

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public static void setInstance(UserSession instance) {
        UserSession.instance = instance;
    }

    public Courier getCourier() {
        return courier;
    }

    public UserSession setCourier(Courier courier) {
        this.courier = courier;
        return this;
    }

    public UserBoundary getUser() {
        return user;
    }

    public UserSession setUser(UserBoundary user) {
        this.user = user;
        return this;
    }
}
