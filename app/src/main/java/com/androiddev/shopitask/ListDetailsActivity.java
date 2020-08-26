package com.androiddev.shopitask;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ListDetailsActivity extends AppCompatActivity {

    // TODO
    // private List theList;

    private TextView textViewListName;
    private TextView textViewListTotalPrompt;
    private TextView textViewListTotalCount;
    private TextView textViewListMembersCount;

    private ImageView imageViewListTotalIcon;
    private ImageView imageViewListMembersIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details);

        initializeViews();
    }

    private void initializeViews() {
        textViewListName = findViewById(R.id.listName);
        textViewListTotalPrompt = findViewById(R.id.listTotalPrompt);
        textViewListTotalCount = findViewById(R.id.listTotalCount);
        textViewListMembersCount = findViewById(R.id.listMembersCount);

        imageViewListTotalIcon = findViewById(R.id.listTotalIcon);
        imageViewListMembersIcon = findViewById(R.id.listMembersIcon);

        // TODO updateViews(ListType);
        updateViews(2);

    }

    // TODO add list type and update views according to the type
    private void updateViews(int type) {

        // TODO Get listName from the list
        String listName = "Amazing List";

        String listTypePrompt = getString(R.string.total) + " ";

        int listTotalCount; // TODO get from list
        int memberCount;
        int totalIconSrc;
        int membersIconSrc;


        // TODO Change
        if (type == 1) {
            listTypePrompt += getString(R.string.items);
            totalIconSrc = R.drawable.shopping_cart;

            // for testing
            listTotalCount = 21;
            membersIconSrc = R.drawable.multiple_people;
            memberCount = 5;

        } else {
            listTypePrompt += getString(R.string.tasks);
            totalIconSrc = R.drawable.tasklist;

            // for testing
            listTotalCount = -1;
            membersIconSrc = R.drawable.single_person;
            memberCount = 1;
        }

        // TODO get list members amount from list
        // Just for test
        //int membersCount = // get from list, check if 1 -> update icon.


        textViewListName.setText(listName);
        textViewListTotalPrompt.setText(listTypePrompt);
        textViewListTotalCount.setText(String.valueOf(listTotalCount));
        textViewListMembersCount.setText(String.valueOf(memberCount));
        imageViewListTotalIcon.setImageResource(totalIconSrc);
        imageViewListMembersIcon.setImageResource(membersIconSrc);

    }

    public void startAddToListActivity(View view) {
        Intent intent = new Intent(this, AddToListActivity.class);
        startActivity(intent);
    }
}