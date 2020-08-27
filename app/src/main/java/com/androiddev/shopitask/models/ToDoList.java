package com.androiddev.shopitask.models;

import java.util.ArrayList;

public class ToDoList extends List{
    private ArrayList<ToDoItem> toDoItems;

    public ToDoList(String listId, String ownerId, ArrayList<String> contributors, boolean isPrivate, ArrayList<ToDoItem> toDoItems) {
        super(listId, ownerId, contributors, isPrivate);
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
