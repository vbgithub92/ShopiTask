package com.androiddev.shopitask;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddev.shopitask.models.List;
import com.androiddev.shopitask.models.MyUser;
import com.androiddev.shopitask.models.ShoppingList;
import com.androiddev.shopitask.models.ToDoList;

import java.util.Objects;

import static com.androiddev.shopitask.MainActivity.BUNDLE_KEY;
import static com.androiddev.shopitask.MainActivity.IS_PRIVATE_LIST_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_ID_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_NAME_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_OWNER_ID_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_SIZE_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_TYPE_KEY;

public class ListDetailsActivity extends AppCompatActivity implements ShoppingListItemAdapter.OnListItemListener, ToDoListItemAdapter.OnListItemListener {

    private static final String TAG = "ListDetailsActivity";

    private List theList;
    private MyUser myUser = new MyUser();

    private TextView textViewListName;
    private TextView textViewListTotalPrompt;
    private TextView textViewListTotalCount;
    private TextView textViewListMembersCount;

    private ImageView imageViewListTotalIcon;
    private ImageView imageViewListMembersIcon;

    private Button buttonShareList;


    // Dialog
    private EditText editTextTargetEmail;
    private Button shareButton;
    private Button closeButton;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter listAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details);

        theList = (List) getIntent().getSerializableExtra(LIST_KEY);

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

        initShareButton();

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

        if (theList.isIsPrivate())
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

        if (theList instanceof ShoppingList) {
            listAdapter = new ShoppingListItemAdapter((ShoppingList) theList, this, this);
        } else if (theList instanceof ToDoList) {
            listAdapter = new ToDoListItemAdapter((ToDoList) theList, this, this);
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
        b.putString(LIST_ID_KEY, theList.getListId());
        b.putString(LIST_OWNER_ID_KEY, theList.getOwnerId());
        b.putInt(LIST_SIZE_KEY, theList.getListSize());
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
        if (theList instanceof ShoppingList) {
            ShoppingItemDialog dialog = new ShoppingItemDialog(this, ((ShoppingList)theList).get(position));
            dialog.startShoppingItemDialog();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, TaskListsActivity.class);
        startActivity(intent);
    }

    private void initShareButton() {
        buttonShareList = findViewById(R.id.shareListButton);

        if (theList.getOwnerId().equals(myUser.getUser_id())) {
            buttonShareList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareList(view);
                }
            });
        } else {
            buttonShareList.setVisibility(View.INVISIBLE);
        }

    }

    public void shareList(View view) {
        final Dialog shareDialog = new Dialog(this);
        shareDialog.setContentView(R.layout.dialog_share_list);

        editTextTargetEmail = shareDialog.findViewById(R.id.shareTargetEmail);
        shareButton = shareDialog.findViewById(R.id.shareButton);
        closeButton = shareDialog.findViewById(R.id.closeButton);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String targetEmail = editTextTargetEmail.getText().toString();
                if (!targetEmail.isEmpty()) {
                    // TODO Magic
                    shareDialog.dismiss();
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = getString(R.string.share_error);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDialog.dismiss();
            }
        });

        Objects.requireNonNull(shareDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        shareDialog.show();


    }
}