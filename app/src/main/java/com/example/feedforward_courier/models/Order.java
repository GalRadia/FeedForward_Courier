package com.example.feedforward_courier.models;

import com.example.feedforward_courier.models.server.object.CreatedBy;
import com.example.feedforward_courier.models.server.object.Location;
import com.example.feedforward_courier.models.server.object.ObjectBoundary;
import com.example.feedforward_courier.models.server.object.ObjectId;
import com.example.feedforward_courier.models.server.user.UserSession;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Order {
    ObjectId orderID;
    String donatorEmail;
    String associationName;
    String donatorName;
    Location donatorLocation;
    String orderDate;
    String orderTime;
    List<Food> foods;
    OrderStatus orderStatus;
    WhoCarries whoCarries;
    Location associationLocation;

    public Order() {
    }


    public Order(ObjectId id, String donatorEmail, String donatorName, Location donatorLocation, String orderDate, String orderTime, List<Food> foods, OrderStatus orderStatus, WhoCarries whoCarries, String associationName, Location associationLocation) {
        this.orderID = id;
        this.donatorEmail = donatorEmail;
        this.associationName = associationName;
        this.donatorName = donatorName;
        this.donatorLocation = donatorLocation;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.foods = foods;
        this.orderStatus = orderStatus;
        this.whoCarries = whoCarries;
        this.associationLocation = associationLocation;
    }

    public Order(ObjectBoundary objectBoundary) {
        Gson gson = new Gson();
        Order temp =gson.fromJson((String) objectBoundary.getObjectDetails().get("Order"), Order.class);
        this.orderID = objectBoundary.getObjectId();
        this.donatorEmail = temp.getDonatorEmail();
        this.donatorName = temp.getDonatorName();
        this.donatorLocation = temp.getDonatorLocation();
        this.orderDate = temp.getOrderDate();
        this.orderTime = temp.getOrderTime();
        this.foods = temp.getFoods();
        this.orderStatus = temp.getOrderStatus();
    }

    public ObjectId getOrderID() {
        return orderID;
    }

    public void setOrderID(ObjectId orderID) {
        this.orderID = orderID;
    }

    public void setDonatorEmail(String donatorEmail) {
        this.donatorEmail = donatorEmail;
    }

    public Location getAssociationLocation() {
        return associationLocation;
    }

    public void setAssociationLocation(Location associationLocation) {
        this.associationLocation = associationLocation;
    }

    public WhoCarries getWhoCarries() {
        return whoCarries;
    }

    public void setWhoCarries(WhoCarries whoCarries) {
        this.whoCarries = whoCarries;
    }

    public String getAssociationName() {
        return associationName;
    }

    public void setAssociationName(String associationName) {
        this.associationName = associationName;
    }


    public static List<Order> convertObjectBoundaryList(List<ObjectBoundary> objectBoundaryList) {
        List<Order> orders = new ArrayList<>();
        for (ObjectBoundary objectBoundary : objectBoundaryList) {
            orders.add(new Order(objectBoundary));
        }
        return orders;
    }

    public String getDonatorEmail() {
        return donatorEmail;
    }

    public String getDonatorName() {
        return donatorName;
    }

    public Order setDonatorName(String donatorName) {
        this.donatorName = donatorName;
        return this;
    }

    public Location getDonatorLocation() {
        return donatorLocation;
    }

    public Order setDonatorLocation(Location donatorLocation) {
        this.donatorLocation = donatorLocation;
        return this;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public Order setOrderDate(String orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public Order setOrderTime(String orderTime) {
        this.orderTime = orderTime;
        return this;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public Order setFoods(List<Food> foods) {
        this.foods = foods;
        return this;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Order setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID='" + donatorEmail + '\'' +
                ", donatorName='" + donatorName + '\'' +
                ", donatorLocation='" + donatorLocation + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", foods=" + foods +
                ", orderStatus=" + orderStatus +
                '}';
    }

    public ObjectBoundary convert(Order order)  {
        ObjectBoundary objectBoundary = new ObjectBoundary();
        objectBoundary.setObjectId(orderID);
        objectBoundary.setType("Order");
        objectBoundary.setAlias(order.getDonatorEmail());
        objectBoundary.setCreatedBy(new CreatedBy(UserSession.getInstance().getSUPERAPP(), UserSession.getInstance().getUserEmail()));
        objectBoundary.setLocation(new Location(100.0, 100.0));//TODO: get location from device
        objectBoundary.setActive(true);
        Gson gson = new Gson();
        Map<String, Object> orderMap = Map.of("Order",gson.toJson(order, Order.class));
        objectBoundary.setObjectDetails(orderMap);
        return objectBoundary;
    }
}
