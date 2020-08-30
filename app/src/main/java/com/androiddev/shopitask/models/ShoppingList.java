package com.androiddev.shopitask.models;

import java.util.ArrayList;

public class ShoppingList extends List {
    private ArrayList<ShoppingItem> shoppingItems;

    public ShoppingList() {
        super();
    }

    public ShoppingList(String ownerId, String listName, ArrayList<String> contributors, boolean isPrivate, ArrayList<ShoppingItem> shoppingItems) {
        super(ownerId, listName, contributors, isPrivate, ListType.SHOPPING);
        this.shoppingItems = shoppingItems;
    }

    public ShoppingList(String ownerId, String listName, ArrayList<String> contributors, boolean isPrivate, ShoppingItem shoppingItem) {
        super(ownerId, listName, contributors, isPrivate, ListType.SHOPPING);
        this.shoppingItems = new ArrayList<>();
        addShoppingItem(shoppingItem);
    }

    public ShoppingList(String listId, String ownerId, String listName, ArrayList<String> contributors, boolean isPrivate, ArrayList<ShoppingItem> shoppingItems) {
        super(listId, ownerId, listName, contributors, isPrivate, ListType.SHOPPING);
        this.shoppingItems = shoppingItems;
    }

    public int getItemIndex(ShoppingItem shoppingItem) {
        return this.shoppingItems.indexOf(shoppingItem);
    }

    public ArrayList<ShoppingItem> getShoppingItems() {
        return shoppingItems;
    }

    public void setShoppingItems(ArrayList<ShoppingItem> shoppingItems) {
        this.shoppingItems = shoppingItems;
    }

    public void addShoppingItem(ShoppingItem shoppingItem) {
        this.shoppingItems.add(shoppingItem);
    }

    public void addShoppingItems(ArrayList<ShoppingItem> shoppingItems) {
        this.shoppingItems.addAll(shoppingItems);
    }

    public int getListSize() {
        return this.shoppingItems.size();
    }

    public void removeItemAt(int index) {
        this.shoppingItems.remove(index);
    }

    public void removeItem(ShoppingItem item) {
        this.shoppingItems.remove(item);
    }

    public ShoppingItem get(int position) {
        return shoppingItems.get(position);
    }
}
