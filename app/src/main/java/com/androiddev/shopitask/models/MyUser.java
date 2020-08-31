package com.androiddev.shopitask.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.androiddev.shopitask.ListDetailsActivity;
import com.androiddev.shopitask.MyListAdapter;
import com.androiddev.shopitask.TaskListsActivity;
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
                initMyLists(dataSnapshot, null, null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DB ERROR", error.getMessage());
            }
        });
    }


    public void initListsAndUpdateAdapter(final MyListAdapter myListAdapter, final TaskListsActivity activity) {
        // START
        this.dbReference.child("MyLists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                initMyLists(dataSnapshot, myListAdapter, activity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DB ERROR", error.getMessage());
            }
        });

        this.dbReference.child("Contributions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                initContributionsLists(dataSnapshot, myListAdapter, activity);
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
        for (List l : tasksList) {
            if (l.getListType() == ListType.SHOPPING)
                shoppingLists.add((ShoppingList) l);
        }
        return shoppingLists;
    }

    public ArrayList<ToDoList> getToDoLists() {
        ArrayList<ToDoList> toDoLists = new ArrayList<>();
        for (List l : tasksList) {
            if (l.getListType() == ListType.TODO)
                toDoLists.add((ToDoList) l);
        }
        return toDoLists;
    }

    private void initMyLists(@NonNull DataSnapshot dataSnapshot, MyListAdapter myListAdapter, TaskListsActivity activity) {
        Log.d(TAG, "initMyLists: Im here");
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            switch (ListType.valueOf(ds.child("listType").getValue(String.class))) {
                case SHOPPING:
                    ShoppingList l = ds.getValue(ShoppingList.class);
                    if (!tasksList.contains(l))
                        tasksList.add(l);
                    break;
                case TODO:
                    ToDoList l2 = ds.getValue(ToDoList.class);
                    if (!tasksList.contains(l2))
                        tasksList.add(l2);
                    break;
            }
        }
        if (myListAdapter != null) {
            myListAdapter.setItems(tasksList);
            myListAdapter.notifyDataSetChanged();
        }
        activity.getLoadingDialog().dismissLoadingDialog();
        activity.updateTotals();
    }

    private void initContributionsLists(@NonNull DataSnapshot dataSnapshot, final MyListAdapter myListAdapter, final TaskListsActivity activity) {
        Log.d(TAG, "initContributionsLists: Im here");
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String listId = ds.getKey();
            String userId = ds.getValue(String.class);
            final DatabaseReference dsRef = ds.getRef();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("MyLists").child(listId);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot ds2) {
                    if (ds2.hasChild("listId")) {
                        switch (ListType.valueOf(ds2.child("listType").getValue(String.class))) {
                            case SHOPPING:
                                tasksList.add(ds2.getValue(ShoppingList.class));
                                break;
                            case TODO:
                                tasksList.add(ds2.getValue(ToDoList.class));
                                break;
                        }
                        if (myListAdapter != null) {
                            myListAdapter.setItems(tasksList);
                            myListAdapter.notifyDataSetChanged();
                        }
                        activity.updateTotals();
                    } else {
                        dsRef.removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }
    }


    public void addUserToList(final String email, final List theList, final ListDetailsActivity activity) {
        final String listId = theList.getListId();
        final String ownerId = theList.getOwnerId();

        this.generalDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("email").getValue(String.class).equals(email)) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(ds.getKey()).child("Contributions").child(listId).setValue(ownerId);
                        updateListContributors(listId, ownerId, ds.getKey());
                        theList.getContributors().add(ds.getKey());
                        if (activity != null)
                            activity.updateViews();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DB ERROR", error.getMessage());
            }
        });
    }

    private void updateListContributors(final String listId, final String userId, final String contributorId) {
        this.getDbReference().child("MyLists").child(listId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long contributorsCount = dataSnapshot.child("contributors").getChildrenCount();
                FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("MyLists").child(listId)
                        .child("contributors").child(Long.toString(contributorsCount)).setValue(contributorId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DB ERROR", error.getMessage());
            }
        });
    }

    public void deleteItem(final List theList, Object item) {
        final String listItemTypes;
        final int itemIndex;

        switch (theList.getListType()) {
            case SHOPPING:
                listItemTypes = "shoppingItems";
                itemIndex = ((ShoppingList) theList).getItemIndex((ShoppingItem) item);
                ((ShoppingList) theList).removeItem((ShoppingItem) item);
                break;
            case TODO:
                listItemTypes = "toDoItems";
                itemIndex = ((ToDoList) theList).getItemIndex((ToDoItem) item);
                ((ToDoList) theList).removeItem((ToDoItem) item);
                break;
            default:
                return;
        }

        FirebaseDatabase.getInstance().getReference().child("Users").child(theList.getOwnerId()).child("MyLists").child(theList.getListId()).child(listItemTypes).child(Integer.toString(itemIndex)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
                FirebaseDatabase.getInstance().getReference().child("Users").child(theList.getOwnerId()).child("MyLists").child(theList.getListId()).child(listItemTypes).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            int currIndex = Integer.parseInt(ds.getKey());
                            if (currIndex > itemIndex) {
                                String newIndex = String.valueOf(currIndex - 1);
                                dataSnapshot.getRef().child(newIndex).setValue(ds.getValue());
                                ds.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

    }

    public void deleteList(List theList) {
        final String myId = this.user_id;
        if (theList.getOwnerId().equals(myId)) {
            this.getDbReference().child("MyLists").child(theList.getListId()).removeValue();
        } else {
            this.getDbReference().child("Contributions").child(theList.getListId()).removeValue();
            this.getGeneralDbReference().child(theList.getOwnerId()).child("MyLists").child("contributors").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String id = "";
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        id = ds.getValue(String.class);
                        if (id != null && id.equals(myId)) {
                            ds.getRef().removeValue();
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });
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
