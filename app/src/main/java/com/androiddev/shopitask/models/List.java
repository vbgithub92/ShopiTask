package com.androiddev.shopitask.models;

import java.util.ArrayList;

public class List {
    private String listId;
    private String ownerId;
    private ArrayList<String> contributors;
    private boolean isPrivate;
    private String listName;

    public List(String listId, String ownerId, ArrayList<String> contributors, boolean isPrivate, String listName) {
        this.listId = listId;
        this.ownerId = ownerId;
        this.contributors = contributors;
        this.isPrivate = isPrivate;
        this.listName = listName;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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

    public String getListName() { return listName;}

    public int getListSize() {
        return 0;
    }
}
