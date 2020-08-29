package com.androiddev.shopitask;

import android.content.Intent;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.androiddev.shopitask.fragments.AddToShoppingListFragment;
import com.androiddev.shopitask.fragments.AddToTaskListFragment;
import com.androiddev.shopitask.models.List;
import com.androiddev.shopitask.models.ListType;
import com.androiddev.shopitask.models.MyUser;
import com.androiddev.shopitask.models.ShoppingItem;
import com.androiddev.shopitask.models.ShoppingList;
import com.androiddev.shopitask.models.ToDoItem;
import com.androiddev.shopitask.models.ToDoList;
import com.androiddev.shopitask.models.UOM;

import java.util.Objects;

import static com.androiddev.shopitask.MainActivity.IS_PRIVATE_LIST_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_ID_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_NAME_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_TYPE_KEY;
import static com.androiddev.shopitask.MainActivity.SHOPPING_LIST_TYPE;
import static com.androiddev.shopitask.MainActivity.TODO_LIST_TYPE;
import static com.androiddev.shopitask.MainActivity.getDateFromDatePicker;

public class AddToListActivity extends AppCompatActivity {

    //AddToShoppingListFragment addToShoppingListFragment;
    //AddToTaskListFragment addToTaskListFragment;

    private static final String TAG = "AddToListActivity";

    Fragment addToListFragment;
    String listName;
    String listType;
    boolean isPrivate;
    String listId;

    String itemName;
    double qty;
    UOM uom;
    Image pic;
    Location location;
    long date;

    List newList;
    MyUser myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_list);
        createToolbar();

        initAddButton();
        myUser = new MyUser();

        Intent intent = getIntent();
        Bundle b = ((Intent) intent).getBundleExtra(MainActivity.BUNDLE_KEY);
        listName = b.getString(LIST_NAME_KEY);
        listType = b.getString(LIST_TYPE_KEY);
        isPrivate = b.getBoolean(IS_PRIVATE_LIST_KEY);
        listId = b.getString(LIST_ID_KEY);

        TextView addToListPrompt = findViewById(R.id.addToListPrompt);

        // Check type of fragment
        switch (ListType.valueOf(listType)) {
            case SHOPPING:
                addToListPrompt.setText(R.string.add_something);
                addToListFragment = new AddToShoppingListFragment();
                break;
            case TODO:
                addToListPrompt.setText(R.string.do_something);
                addToListFragment = new AddToTaskListFragment();
                break;
            default:
                return;
        }
        setFragment(addToListFragment);

    }

    private void createToolbar() {
        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    private void setFragment(Fragment fragmentToSet) {
        getSupportFragmentManager().beginTransaction().replace(R.id.addToListFrame, fragmentToSet).commit();
    }

    private void initAddButton() {
        Button addButton = findViewById(R.id.listAddNewItem);

       addButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (listId == null) {
                   createNewList();
               } else {
                   addItemToList();
               }
           }
       });
    }

    private void createNewList() {
        switch (listType) {
            case SHOPPING_LIST_TYPE:
                itemName = ((EditText)findViewById(R.id.newItemName)).getText().toString();
                qty = ((NumberPicker)findViewById(R.id.newItemAmount)).getValue();
                int selectedUOMid = ((RadioGroup)findViewById(R.id.unitTypeGroup)).getCheckedRadioButtonId();
                uom = UOM.valueOf(((RadioButton)findViewById(selectedUOMid)).getText().toString().toUpperCase());
                pic = null;

                ShoppingItem shoppingItem = new ShoppingItem(itemName, qty, uom, pic);
                newList = new ShoppingList(myUser.getUser_id(), listName,null, isPrivate, shoppingItem);
                myUser.getDbReference().child("MyLists").child(newList.getListId()).setValue(newList);
                break;
            case TODO_LIST_TYPE:
                itemName = ((EditText)findViewById(R.id.newTaskName)).getText().toString();
                location = null;
                date = getDateFromDatePicker((DatePicker)findViewById(R.id.newTaskDate));
                pic = null;

                ToDoItem toDoItem = new ToDoItem(itemName, date, location, pic);
                newList = new ToDoList(myUser.getUser_id(), listName,null, isPrivate, toDoItem);
                myUser.getDbReference().child("MyLists").child(newList.getListId()).setValue(newList);
                break;
            default:
                return;
        }

        Intent intent = new Intent(this, TaskListsActivity.class);
        startActivity(intent);
    }

    private void addItemToList() {

        Log.d(TAG, "addItemToList: wow");

    }

}