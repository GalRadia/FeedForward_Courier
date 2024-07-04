package com.example.feedforward_courier.models.server.object;

public class ObjectId {
    private String superapp;
    private String id;

    public ObjectId(String superapp, String id) {
        this.superapp = superapp;
        this.id = id;
    }

    public ObjectId() {
    }

    public String getSuperapp() {
        return superapp;
    }

    public void setSuperapp(String uperapp) {
        this.superapp = uperapp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ObjectId [superapp=" + superapp + ", id=" + id + "]";
    }


}
