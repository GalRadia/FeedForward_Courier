package com.example.feedforward_courier.models.server.object;



public class Location {

    private double latitude;
    private double longitude;

    public Location() {
    }

    public Location(Double latitude, Double longitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    @Override
    public String toString() {
        return "Location [latitude=" + latitude + ", longitude=" + longitude + "]";
    }

}
