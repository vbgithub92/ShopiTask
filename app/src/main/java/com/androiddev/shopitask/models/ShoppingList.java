package com.androiddev.shopitask.models;

import java.util.ArrayList;

public class ShoppingList extends List {
    private ArrayList<ShoppingItem> shoppingItems;

    public ShoppingList(String listId, String ownerId, ArrayList<String> contributors, boolean isPrivate, ArrayList<ShoppingItem> shoppingItems) {
        super(listId, ownerId, contributors, isPrivate);
        this.shoppingItems = shoppingItems;
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
}
