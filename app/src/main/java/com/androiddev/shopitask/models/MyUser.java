package com.androiddev.shopitask.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.androiddev.shopitask.MyListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MyUser {

    private static final String TAG = "MyUser";
    private String user_id;
    private String userName;
    private String email;
    private DatabaseReference dbReference;
    private DatabaseReference generalDbReference;
    ArrayList<List> tasksList;

    public MyUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        this.user_id = user.getUid();
        this.userName = user.getDisplayName();
        this.email = user.getEmail();
        this.dbReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        this.generalDbReference = FirebaseDatabase.getInstance().getReference().child("Users");
        tasksList = new ArrayList<>();
    }

     public void initLists() {
        this.dbReference.child("MyLists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                initMyLists(dataSnapshot, null);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DB ERROR", error.getMessage());
            }
        });
    }

    public void initListsAndUpdateAdapter(final MyListAdapter myListAdapter) {
        this.dbReference.child("MyLists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                initMyLists(dataSnapshot, myListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DB ERROR", error.getMessage());
            }
        });
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

    public DatabaseReference getGeneralDbReference() {
        return generalDbReference;
    }

    public void setGeneralDbReference(DatabaseReference generalDbReference) {
        this.generalDbReference = generalDbReference;
    }

    public ArrayList<List> getTasksList() {
        return tasksList;
    }

    public void setTasksList(ArrayList<List> tasksList) {
        this.tasksList = tasksList;
    }

    public ArrayList<ShoppingList> getShoppingLists() {
        ArrayList<ShoppingList> shoppingLists = new ArrayList<>();
        for (List l: tasksList) {
            if (l.getListType() == ListType.SHOPPING)
                shoppingLists.add((ShoppingList)l);
        }
        return shoppingLists;
    }

    public ArrayList<ToDoList> getToDoLists() {
        ArrayList<ToDoList> toDoLists = new ArrayList<>();
        for (List l: tasksList) {
            if (l.getListType() == ListType.TODO)
                toDoLists.add((ToDoList)l);
        }
        return toDoLists;
    }

    private void initMyLists(@NonNull DataSnapshot dataSnapshot, MyListAdapter myListAdapter) {
        Log.d(TAG, "initMyLists: Im here");
        for (DataSnapshot ds: dataSnapshot.getChildren()) {
            switch (ListType.valueOf(ds.child("listType").getValue(String.class))) {
                case SHOPPING:
                    tasksList.add(ds.getValue(ShoppingList.class));
                    break;
                case TODO:
                    tasksList.add(ds.getValue(ToDoList.class));
                    break;
            }
        }
        if (myListAdapter != null) {
            myListAdapter.setItems(tasksList);
            myListAdapter.notifyDataSetChanged();
        }
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
