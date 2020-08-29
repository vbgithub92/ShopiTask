package com.androiddev.shopitask.models;

import java.util.ArrayList;
import java.util.UUID;

public class ToDoList extends List {
    private ArrayList<ToDoItem> toDoItems;

    public ToDoList(String ownerId, String listName, ArrayList<String> contributors, boolean isPrivate, ArrayList<ToDoItem> toDoItems) {
        super(ownerId, listName, contributors, isPrivate);
        this.toDoItems = toDoItems;
    }

    public ToDoList(String ownerId, String listName, ArrayList<String> contributors, boolean isPrivate, ToDoItem toDoItem) {
        super(ownerId, listName, contributors, isPrivate);
        this.toDoItems = new ArrayList<>();
        addToDoItem(toDoItem);
    }

    public ToDoList(UUID listId, String ownerId, String listName, ArrayList<String> contributors, boolean isPrivate, ArrayList<ToDoItem> toDoItems) {
        super(listId, ownerId, listName, contributors, isPrivate);
        this.toDoItems = toDoItems;
    }

    public ArrayList<ToDoItem> getToDoItems() {
        return toDoItems;
    }

    public void setToDoItems(ArrayList<ToDoItem> toDoItems) {
        this.toDoItems = toDoItems;
    }

    public void addToDoItem(ToDoItem toDoItem) {
        this.toDoItems.add(toDoItem);
    }

    public void addToDoItems(ArrayList<ToDoItem> toDoItems) {
        this.toDoItems.addAll(toDoItems);
    }

    public int getListSize() {
        return this.toDoItems.size();
    }

    public void removeItemAt(int index) {
        this.toDoItems.remove(index);
    }

    public void removeItem(ToDoItem item) {
        this.toDoItems.remove(item);
    }
}
