package com.example.feedforward_courier.models.server.object;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public  class ObjectBoundary {
    private ObjectId objectId;
    private String type;
    private String alias;
    private Location location;
    private Boolean active;
    private Date creationTimestamp;
    private CreatedBy createdBy;
    private Map<String, Object> objectDetails;

    public ObjectBoundary() {
    }

    public ObjectBoundary(String type, String alias, CreatedBy createdBy, Location location,
                          boolean active) {
        this.objectId = new ObjectId("2024b.gal.said", "1234");
        this.setType(type);
        this.setAlias(alias);
        this.setCreatedBy(createdBy);
        this.setLocation(location);
        this.setActive(active);
        this.setObjectDetails(new HashMap<>());
    }


    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Map<String, Object> getObjectDetails() {
        return objectDetails;
    }

    public void setObjectDetails(Map<String, Object> objectDetails) {
        this.objectDetails = objectDetails;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @Override
    public String toString() {
        return "ObjectBoundary [objectId=" + objectId + ", type=" + type + ", alias=" + alias + ", location=" + location
                + ", active=" + active + ", creationTimestamp=" + creationTimestamp + ", createdBy=" + createdBy
                + ", objectDetails=" + objectDetails + "]";
    }


}











