package com.androiddev.shopitask.models;

import java.util.ArrayList;
import java.util.UUID;

public class List {
    private String listId;
    private String ownerId;
    private String listName;
    private ArrayList<String> contributors;
    private boolean isPrivate;
    private ListType listType;

    public List(){

    }

    public List(String ownerId, String listName, ArrayList<String> contributors, boolean isPrivate, ListType type) {
        this.listId = UUID.randomUUID().toString();
        this.ownerId = ownerId;
        this.listName = listName;
        this.contributors = contributors;
        this.isPrivate = isPrivate;
        this.listType = type;
    }

    public List(String listId, String ownerId, String listName, ArrayList<String> contributors, boolean isPrivate, ListType type) {
        this.listId = listId;
        this.ownerId = ownerId;
        this.listName = listName;
        this.contributors = contributors;
        this.isPrivate = isPrivate;
        this.listName = listName;
        this.listType = type;
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

    public ListType getListType() {
        return listType;
    }

    public void setListType(ListType listType) {
        this.listType = listType;
    }

    public boolean isIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public int getListSize() {
        return 0;
    }
}
