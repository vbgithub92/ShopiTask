package com.androiddev.shopitask;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddev.shopitask.models.List;
import com.androiddev.shopitask.models.ShoppingList;
import com.androiddev.shopitask.models.ToDoList;

import java.util.Objects;

import static com.androiddev.shopitask.MainActivity.BUNDLE_KEY;
import static com.androiddev.shopitask.MainActivity.IS_PRIVATE_LIST_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_NAME_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_OWNER_ID_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_SIZE_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_TYPE_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_ID_KEY;

public class ListDetailsActivity extends AppCompatActivity implements ShoppingListItemAdapter.OnListItemListener, ToDoListItemAdapter.OnListItemListener {

    private List theList;

    private TextView textViewListName;
    private TextView textViewListTotalPrompt;
    private TextView textViewListTotalCount;
    private TextView textViewListMembersCount;

    private ImageView imageViewListTotalIcon;
    private ImageView imageViewListMembersIcon;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter listAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details);

        theList = (List)getIntent().getSerializableExtra(LIST_KEY);

        createToolbar();
        initializeViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listAdapter.notifyDataSetChanged();
    }

    private void initializeViews() {
        textViewListName = findViewById(R.id.listName);
        textViewListTotalPrompt = findViewById(R.id.listTotalPrompt);
        textViewListTotalCount = findViewById(R.id.listTotalCount);
        textViewListMembersCount = findViewById(R.id.listMembersCount);

        imageViewListTotalIcon = findViewById(R.id.listTotalIcon);
        imageViewListMembersIcon = findViewById(R.id.listMembersIcon);

        recyclerView = findViewById(R.id.my_recycler_view);

        initRecyclerView();
        updateViews();
    }

    private void updateViews() {

        String listName = theList.getListName();

        String listTypePrompt = getString(R.string.total) + " ";

        int listTotalCount;
        int totalIconSrc;
        int memberCount;
        int membersIconSrc;

        if (theList instanceof ShoppingList) {
            listTypePrompt += getString(R.string.items);
            totalIconSrc = R.drawable.shopping_cart;

        } else {
            listTypePrompt += getString(R.string.tasks);
            totalIconSrc = R.drawable.tasklist;
        }

        listTotalCount = theList.getListSize();

        if(theList.isIsPrivate())
            membersIconSrc = R.drawable.single_person;
        else
            membersIconSrc = R.drawable.multiple_people;

        if (theList.getContributors() != null)
            memberCount = theList.getContributors().size() + 1;
        else
            memberCount = 1;

        textViewListName.setText(listName);
        textViewListTotalPrompt.setText(listTypePrompt);
        imageViewListTotalIcon.setImageResource(totalIconSrc);
        textViewListTotalCount.setText(String.valueOf(listTotalCount));
        imageViewListMembersIcon.setImageResource(membersIconSrc);
        textViewListMembersCount.setText(String.valueOf(memberCount));

    }

    private void initRecyclerView() {

        recyclerView.setHasFixedSize(true);

        if(theList instanceof ShoppingList) {
            listAdapter = new ShoppingListItemAdapter((ShoppingList)theList, this, this);
        }
        else if(theList instanceof ToDoList) {
            listAdapter = new ToDoListItemAdapter((ToDoList) theList, this,this);
        }

        recyclerView.setAdapter(listAdapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void startAddToListActivity(View view) {
        Intent intent = new Intent(this, AddToListActivity.class);
        Bundle b = new Bundle();
        b.putString(LIST_NAME_KEY, theList.getListName());
        b.putString(LIST_TYPE_KEY, theList.getListType().toString());
        b.putString(LIST_ID_KEY ,theList.getListId());
        b.putString(LIST_OWNER_ID_KEY ,theList.getOwnerId());
        b.putInt(LIST_SIZE_KEY ,theList.getListSize());
        b.putBoolean(IS_PRIVATE_LIST_KEY, theList.isIsPrivate());
        intent.putExtra(LIST_KEY, theList);
        intent.putExtra(BUNDLE_KEY, b);
        startActivity(intent);
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onListItemClick(int position) {
        // TODO
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, TaskListsActivity.class);
        startActivity(intent);
    }
}