package com.androiddev.shopitask.models;

import android.location.Location;
import android.media.Image;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ToDoItem implements Serializable {
    private UUID id;
    private String activityName;
    private long date;
    private Location location;
    private Image pic;

    public ToDoItem(String activityName, long date, Location location, Image pic) {
        this.activityName = activityName;
        this.date = date;
        this.location = location;
        this.pic = pic;
        this.id = UUID.randomUUID();
    }

    public ToDoItem(UUID id, String activityName, long date, Location location, Image pic) {
        this.id = id;
        this.activityName = activityName;
        this.date = date;
        this.location = location;
        this.pic = pic;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Image getPic() {
        return pic;
    }

    public void setPic(Image pic) {
        this.pic = pic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToDoItem toDoItem = (ToDoItem) o;
        return id.equals(toDoItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
