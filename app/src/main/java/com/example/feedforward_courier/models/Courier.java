package com.example.feedforward_courier.models;

import com.example.feedforward_courier.models.server.command.UserId;
import com.example.feedforward_courier.models.server.object.CreatedBy;
import com.example.feedforward_courier.models.server.object.ObjectBoundary;
import com.google.gson.Gson;

import java.util.Map;

public class Courier {
    private String courierId;
    private String courierName;
    private String courierPhone;
    private String courierEmail;
    private StatusOfCourier status;

    public String getCourierId() {
        return courierId;
    }

    public Courier setCourierId(String courierId) {
        this.courierId = courierId;
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



    public ObjectBoundary toObjectBoundary(String email) {
        ObjectBoundary objectBoundary = new ObjectBoundary();
        objectBoundary.setType("Courier");
        objectBoundary.setAlias(this.courierName);

        objectBoundary.setActive(true);

        CreatedBy createdBy = new CreatedBy();
        UserId userId = new UserId();
        userId.setSuperapp("2024b.gal.said");
        userId.setEmail(email);
        createdBy.setUserId(userId);
        objectBoundary.setCreatedBy(createdBy);

        Gson gson = new Gson();
        Map<String, Object> objectDetails = Map.of("Courier", gson.toJson(this, Courier.class));
        objectBoundary.setObjectDetails(objectDetails);

        return objectBoundary;
    }
}



