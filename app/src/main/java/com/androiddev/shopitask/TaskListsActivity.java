package com.androiddev.shopitask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddev.shopitask.models.List;
import com.androiddev.shopitask.models.ShoppingItem;
import com.androiddev.shopitask.models.ShoppingList;
import com.androiddev.shopitask.models.UOM;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class TaskListsActivity extends AppCompatActivity {

    private static final String TAG = "TaskListsActivity";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter listAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Vibrator vibe;
    private String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private TextView textUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_lists);
        createToolbar();
        findViewsById();

        textUserName.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());
        vibe = (Vibrator) TaskListsActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initRecyclerView();
    }

    private void findViewsById() {
        textUserName = findViewById(R.id.username);
        recyclerView = findViewById(R.id.my_recycler_view);
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView:");
        recyclerView.setHasFixedSize(true); // Not sure

        ArrayList<List> myList = new ArrayList<>();

        for(int i = 0 ; i < 4 ; i++) {
            myList.add(new List("1","List " + i, null,true));
        }

        myList.add(new ShoppingList("1", "Shopping1", null, true,
                new ShoppingItem("Vasya",10,UOM.L, null)));

        ShoppingList s1 = new ShoppingList("1", "Shopping1", null, true,
                new ShoppingItem("Vasya",10,UOM.L, null));
        s1.addShoppingItem(new ShoppingItem("Vasdasa",11,UOM.KG, null));

        myList.add(s1);

        listAdapter = new ListAdapter(myList,this);
        recyclerView.setAdapter(listAdapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void addListButtonClicked(View view) {
        vibe.vibrate(80);

        Intent intent = new Intent(this, AddNewListActivity.class);
        startActivity(intent);
    }

    private void createToolbar() {
        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }
}