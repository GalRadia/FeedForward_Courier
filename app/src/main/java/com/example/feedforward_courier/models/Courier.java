package com.example.feedforward_courier.models;

import com.example.feedforward_courier.models.server.command.UserId;
import com.example.feedforward_courier.models.server.object.CreatedBy;
import com.example.feedforward_courier.models.server.object.ObjectBoundary;
import com.example.feedforward_courier.models.server.object.ObjectId;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class Courier {
    private ObjectId courierID;
    private String courierName;
    private String courierPhone;
    private String courierEmail;
    private StatusOfCourier status;
    private ArrayList<String> allOrders = new ArrayList<>();

    public Courier() {
    }

    public Courier(ObjectBoundary objectBoundary) {
        Gson gson = new Gson();
        Courier temp = gson.fromJson((String) objectBoundary.getObjectDetails().get("Courier"), Courier.class);
        this.courierName = temp.getCourierName();
        this.courierPhone = temp.getCourierPhone();
        this.courierEmail = temp.getCourierEmail();
        this.status = temp.getStatus();
        this.allOrders = temp.getAllOrders();
        this.courierID = objectBoundary.getObjectId();
    }

    public ObjectId getCourierID() {
        return courierID;
    }

    public Courier setCourierID(ObjectId courierID) {
        this.courierID = courierID;
        return this;
    }

    public ArrayList<String> getAllOrders() {
        return allOrders;
    }

    public Courier setAllOrders(ArrayList<String> allOrders) {
        this.allOrders = allOrders;
        return this;
    }


    public String getCourierName() {
        return courierName;
    }

    public Courier setCourierName(String courierName) {
        this.courierName = courierName;
        return this;
    }

    public String getCourierPhone() {
        return courierPhone;
    }

    public Courier setCourierPhone(String courierPhone) {
        this.courierPhone = courierPhone;
        return this;
    }

    public String getCourierEmail() {
        return courierEmail;
    }

    public Courier setCourierEmail(String courierEmail) {
        this.courierEmail = courierEmail;
        return this;
    }

    public StatusOfCourier getStatus() {
        return status;
    }

    public Courier setStatus(StatusOfCourier status) {
        this.status = status;
        return this;
    }


    public ObjectBoundary toObjectBoundary() {
        ObjectBoundary objectBoundary = new ObjectBoundary();
        objectBoundary.setType("Courier");
        objectBoundary.setAlias(this.courierName);
        objectBoundary.setObjectId(getCourierID());
        objectBoundary.setActive(true);

        CreatedBy createdBy = new CreatedBy();
        UserId userId = new UserId();
        userId.setSuperapp("2024b.gal.said");
        userId.setEmail(this.courierEmail);
        createdBy.setUserId(userId);
        objectBoundary.setCreatedBy(createdBy);


        Gson gson = new Gson();
        Map<String, Object> objectDetails = Map.of("Courier", gson.toJson(this, Courier.class));
        objectBoundary.setObjectDetails(objectDetails);

        return objectBoundary;
    }
}



