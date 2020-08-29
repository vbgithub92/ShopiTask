package com.androiddev.shopitask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddev.shopitask.models.List;
import com.androiddev.shopitask.models.ShoppingItem;
import com.androiddev.shopitask.models.ShoppingList;
import com.androiddev.shopitask.models.ToDoItem;
import com.androiddev.shopitask.models.ToDoList;
import com.androiddev.shopitask.models.UOM;
import com.androiddev.shopitask.models.MyUser;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class TaskListsActivity extends AppCompatActivity implements ListAdapter.OnListListener {

    public static final String LIST_KEY = "Selected List";
    private Vibrator vibe;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter listAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private TextView textUserName;

    ArrayList<List> tasksListTest = new ArrayList<>();

    private TextView textTotalShoppingItems;
    private TextView textTotalTasks;
  
    private MyUser myUser = new MyUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_lists);
        createToolbar();
        findViewsById();
        textUserName.setText(myUser.getUserName());
        vibe = (Vibrator) TaskListsActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        myUser.initLists();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initRecyclerView();
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
        tasksListTest = new ArrayList<>();
        ArrayList<String> listContributors1 = new ArrayList<>(Arrays.asList(
                "Vlad", "Moshe", "Alex"));
        ArrayList<String> listContributors2 = new ArrayList<>(Arrays.asList(
                "Liza"));
        ArrayList<String> listContributors3 = new ArrayList<>(Arrays.asList(
                "Jimmy"));
        ArrayList<String> listContributors4 = new ArrayList<>(Arrays.asList(
                "Chris", "Alon", "Doggo", "Fanta"));


        ArrayList<ToDoItem> itemsList1 = new ArrayList<>(Arrays.asList(
                new ToDoItem("Pick up package", 0, null, null),
                new ToDoItem("Do something", 0, null, null),
                new ToDoItem("Sleep", 0, null, null)
        ));

        ArrayList<ToDoItem> itemsList2 = new ArrayList<>(Arrays.asList(
                new ToDoItem("Pick up package", 0, null, null),
                new ToDoItem("Do something", 0, null, null),
                new ToDoItem("Sleep", 0, null, null),
                new ToDoItem("AAA", 0, null, null),
                new ToDoItem("BBB", 0, null, null),
                new ToDoItem("Sleep", 0, null, null),
                new ToDoItem("Pick up package", 0, null, null),
                new ToDoItem("Do something", 0, null, null),
                new ToDoItem("Sleep", 0, null, null)
        ));

        ArrayList<ShoppingItem> shoppingItems1 = new ArrayList<>(Arrays.asList(
                new ShoppingItem("Milk", 3, UOM.L, null),
                new ShoppingItem("Chips", 4, UOM.PACKS, null),
                new ShoppingItem("Coffee Beans", 2, UOM.KG, null),
                new ShoppingItem("Chocolate", 10, UOM.PACKS, null)
        ));

        ArrayList<ShoppingItem> shoppingItems2 = new ArrayList<>(Arrays.asList(
                new ShoppingItem("Boxes", 3, UOM.ITEMS, null),
                new ShoppingItem("Pizza", 15, UOM.ITEMS, null),
                new ShoppingItem("Coke", 2, UOM.L, null),
                new ShoppingItem("Popcorn", 4, UOM.PACKS, null)
        ));

        ToDoList todoList1 = new ToDoList("1", "FirstTDList", listContributors1, false, itemsList1);
        ToDoList todoList2 = new ToDoList("2", "SecondTDList", listContributors2, true, itemsList2);

        ShoppingList shoppingList1 = new ShoppingList("1", "ShoppingList1", listContributors3, false, shoppingItems1);
        ShoppingList shoppingList2 = new ShoppingList("1", "ShoppingList2", listContributors4, false, shoppingItems2);

        tasksListTest.add(todoList1);
        tasksListTest.add(todoList2);
        tasksListTest.add(shoppingList1);
        tasksListTest.add(shoppingList2);

        // Test
        //listAdapter = new ListAdapter(tasksListTest, this, this);
        ArrayList<List> myList = new ArrayList<>();

        myList.addAll(myUser.getToDoLists());
        myList.addAll(myUser.getShoppingLists());
      
        // From server
        listAdapter = new ListAdapter(myList,this,this);
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    private void updateTotals() {
        int totalShoppingItems = 0;
        int totalTasks = 0;

        for (List list : tasksList) {
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
        List selectedList = tasksList.get(position);
        Intent intent = new Intent(this, ListDetailsActivity.class);
        intent.putExtra(LIST_KEY, selectedList);
        startActivity(intent);
    }
}