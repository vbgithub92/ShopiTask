package com.androiddev.shopitask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddev.shopitask.models.List;
import com.androiddev.shopitask.models.MyUser;
import com.androiddev.shopitask.models.ShoppingList;
import com.androiddev.shopitask.models.ToDoList;

import java.util.ArrayList;
import java.util.Objects;

import static com.androiddev.shopitask.MainActivity.LIST_KEY;

public class TaskListsActivity extends AppCompatActivity implements ListAdapter.OnListListener {

    private static final String TAG = "TaskListsActivity";

    private Vibrator vibe;

    private RecyclerView recyclerView;
    private MyListAdapter listAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextView textUserName;
    private TextView textTotalShoppingItems;
    private TextView textTotalTasks;

    ArrayList<List> tasksList = new ArrayList<>();

    private LoadingDialog loadingDialog;

    private MyUser myUser = new MyUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_lists);
        createToolbar();
        findViewsById();
        String userName = myUser.getUserName();
        userName = userName.toLowerCase();
        userName = userName.substring(0, 1).toUpperCase() + userName.substring(1) + "!";
        textUserName.setText(userName);
        vibe = (Vibrator) TaskListsActivity.this.getSystemService(Context.VIBRATOR_SERVICE);

        loadingDialog = new LoadingDialog(this);

        initRecyclerView();

        loadingDialog.startLoadingDialog(getString(R.string.do_something));
        myUser.initListsAndUpdateAdapter(listAdapter, this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateTotals();
    }

    private void findViewsById() {
        textUserName = findViewById(R.id.username);
        textTotalTasks = findViewById(R.id.totalTasksToDo);
        textTotalShoppingItems = findViewById(R.id.totalItemsToBuy);
        recyclerView = findViewById(R.id.my_recycler_view);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        tasksList = new ArrayList<>();

        listAdapter = new MyListAdapter(tasksList, this, this);

        recyclerView.setAdapter(listAdapter);
        layoutManager = new LinearLayoutManager(this);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(listAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(layoutManager);
    }

    public void addListButtonClicked(View view) {
        vibe.vibrate(80);

        Intent intent = new Intent(this, AddNewListActivity.class);
        startActivity(intent);
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    public void updateTotals() {
        int totalShoppingItems = 0;
        int totalTasks = 0;

        for (List list : myUser.getTasksList()) {
            if (list instanceof ShoppingList)
                totalShoppingItems += ((ShoppingList) list).getListSize();
            else
                totalTasks += ((ToDoList) list).getListSize();
        }

        textTotalShoppingItems.setText(String.valueOf(totalShoppingItems));
        textTotalTasks.setText(String.valueOf(totalTasks));
    }

    @Override
    public void onListClick(int position) {
        List selectedList = listAdapter.getLists(position);// tasksList.get(position);
        Intent intent = new Intent(this, ListDetailsActivity.class);
        intent.putExtra(LIST_KEY, selectedList);
        startActivity(intent);
    }

    public LoadingDialog getLoadingDialog() {
        return loadingDialog;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}