package com.example.feedforward_courier.models.server.object;



public class Location {

    private double lat;
    private double lng;

    public Location() {
    }

    public Location(Double lat, Double lng) {
        super();
        this.lat = lat;
        this.lng = lng;
    }
    public Double getLat() {
        return lat;
    }
    public void setLat(Double lat) {
        this.lat = lat;
    }
    public Double getLng() {
        return lng;
    }
    public void setLng(Double lng) {
        this.lng = lng;
    }
    @Override
    public String toString() {
        return "Location [latitude=" + lat + ", longitude=" + lng + "]";
    }

}
