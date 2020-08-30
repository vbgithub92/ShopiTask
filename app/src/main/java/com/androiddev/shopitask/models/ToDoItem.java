package com.androiddev.shopitask.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ToDoItem implements Serializable {

    private String id;
    private String activityName;
    private long date;
    private String location;
    private String pic;


    public ToDoItem() {}

    public ToDoItem(String activityName, long date, String location, String pic) {
        this.activityName = activityName;
        this.date = date;
        this.location = location;
        this.pic = pic;
        this.id = UUID.randomUUID().toString();
    }

    public ToDoItem(String id, String activityName, long date, String location, String pic) {
        this.id = id;
        this.activityName = activityName;
        this.date = date;
        this.location = location;
        this.pic = pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
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
