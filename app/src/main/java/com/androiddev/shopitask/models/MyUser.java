package com.androiddev.shopitask.models;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;

public class MyUser {
    private String user_id;
    private String userName;
    private String email;
    private DatabaseReference dbReference;

    private ArrayList<ShoppingList> shoppingLists;
    private ArrayList<ToDoList> toDoLists;

    public MyUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        this.user_id = user.getUid();
        this.userName = user.getDisplayName();
        this.email = user.getEmail();
        this.dbReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
    }

    public ArrayList<ShoppingList> initShoppingLists() {
        this.dbReference.child("ShoppingLists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    shoppingLists.add(ds.getValue(ShoppingList.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DB ERROR", error.getMessage());
            }
        });
        return getShoppingLists();
    }

    public ArrayList<ToDoList> initToDoLists() {
        this.dbReference.child("ToDoLists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    toDoLists.add(ds.getValue(ToDoList.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DB ERROR", error.getMessage());
            }
        });
        return getToDoLists();
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DatabaseReference getDbReference() {
        return dbReference;
    }

    public void setDbReference(DatabaseReference dbReference) {
        this.dbReference = dbReference;
    }

    public ArrayList<ShoppingList> getShoppingLists() {
        return shoppingLists;
    }

    public void setShoppingLists(ArrayList<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }

    public ArrayList<ToDoList> getToDoLists() {
        return toDoLists;
    }

    public void setToDoLists(ArrayList<ToDoList> toDoLists) {
        this.toDoLists = toDoLists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyUser myUser = (MyUser) o;
        return user_id.equals(myUser.user_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id);
    }
}
