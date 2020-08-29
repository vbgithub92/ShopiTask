package com.androiddev.shopitask.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class List implements Serializable {
    private UUID listId;
    private String ownerId;
    private String listName;
    private ArrayList<String> contributors;
    private boolean isPrivate;

    public List(String ownerId, String listName, ArrayList<String> contributors, boolean isPrivate) {
        this.listId = UUID.randomUUID();
        this.ownerId = ownerId;
        this.listName = listName;
        this.contributors = contributors;
        this.isPrivate = isPrivate;
    }

    public List(UUID listId, String ownerId, String listName, ArrayList<String> contributors, boolean isPrivate) {
        this.listId = listId;
        this.ownerId = ownerId;
        this.listName = listName;
        this.contributors = contributors;
        this.isPrivate = isPrivate;
        this.listName = listName;
    }

    public UUID getListId() {
        return listId;
    }

    public void setListId(UUID listId) {
        this.listId = listId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public ArrayList<String> getContributors() {
        return contributors;
    }

    public void setContributors(ArrayList<String> contributors) {
        this.contributors = contributors;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public int getListSize() {
        return 0;
    }

}
