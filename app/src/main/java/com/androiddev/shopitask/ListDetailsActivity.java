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

import java.util.Objects;

public class ListDetailsActivity extends AppCompatActivity implements ShoppingListItemAdapter.OnListItemListener {

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

        theList = (List)getIntent().getSerializableExtra(TaskListsActivity.LIST_KEY);

        createToolbar();
        initializeViews();
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

        recyclerView.setAdapter(listAdapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void startAddToListActivity(View view) {
        Intent intent = new Intent(this, AddToListActivity.class);
        startActivity(intent);
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onListItemClick(int position) {

    }
}