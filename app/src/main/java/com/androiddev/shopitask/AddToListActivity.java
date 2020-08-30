package com.androiddev.shopitask;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import static com.androiddev.shopitask.MainActivity.LIST_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_NAME_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_OWNER_ID_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_SIZE_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_TYPE_KEY;
import static com.androiddev.shopitask.MainActivity.getDateFromDatePicker;

public class AddToListActivity extends AppCompatActivity {

    private static final String TAG = "AddToListActivity";
    private static final int REQUEST_CODE = 100;
    private List theList;
    Fragment addToListFragment;
    String listName;
    String listType;
    boolean isPrivate;
    String listId;
    String listOwnerId;
    int listSize;

    String itemName;
    double qty;
    UOM uom;
    Bitmap pic = null;
    String location;
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
        listOwnerId = b.getString(LIST_OWNER_ID_KEY);
        listSize = b.getInt(LIST_SIZE_KEY);

        TextView addToListPrompt = findViewById(R.id.addToListPrompt);

        if (listId != null) {
            theList = (List) getIntent().getSerializableExtra(LIST_KEY);
        }
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

    private void initCameraButton() {
        // Initialize Camera Button - Permission
        ImageView cameraButton = (ImageView)findViewById(R.id.cameraButton);

        if(ContextCompat.checkSelfPermission(AddToListActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddToListActivity.this, new String[] {
                    Manifest.permission.CAMERA
            },REQUEST_CODE);

        }

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open Camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            // Get Captured Image
            pic = (Bitmap) data.getExtras().get("data");
        }
    }

    private void createToolbar() {
        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    private void setFragment(Fragment fragmentToSet) {
        getSupportFragmentManager().beginTransaction().replace(R.id.addToListFrame, fragmentToSet).commit();
        // TODO Change
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initCameraButton();
            }
        }, 1000);
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
        switch (ListType.valueOf(listType)) {
            case SHOPPING:
                itemName = ((EditText)findViewById(R.id.newItemName)).getText().toString();
                qty = ((NumberPicker)findViewById(R.id.newItemAmount)).getValue();
                int selectedUOMid = ((RadioGroup)findViewById(R.id.unitTypeGroup)).getCheckedRadioButtonId();
                uom = UOM.valueOf(((RadioButton)findViewById(selectedUOMid)).getText().toString().toUpperCase());
                //pic = null;

                ShoppingItem shoppingItem = new ShoppingItem(itemName, qty, uom, pic);
                newList = new ShoppingList(myUser.getUser_id(), listName,null, isPrivate, shoppingItem);
                myUser.getDbReference().child("MyLists").child(newList.getListId()).setValue(newList);
                break;
            case TODO:
                itemName = ((EditText)findViewById(R.id.newTaskName)).getText().toString();
                location = ((EditText)findViewById(R.id.newTaskLocation)).getText().toString();
                date = getDateFromDatePicker((DatePicker)findViewById(R.id.newTaskDate));
                //pic = null;

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
        switch (ListType.valueOf(listType)) {
            case SHOPPING:
                itemName = ((EditText)findViewById(R.id.newItemName)).getText().toString();
                qty = ((NumberPicker)findViewById(R.id.newItemAmount)).getValue();
                int selectedUOMid = ((RadioGroup)findViewById(R.id.unitTypeGroup)).getCheckedRadioButtonId();
                uom = UOM.valueOf(((RadioButton)findViewById(selectedUOMid)).getText().toString().toUpperCase());
                //pic = null;
                ShoppingItem shoppingItem = new ShoppingItem(itemName, qty, uom, pic);
                if(myUser.getUser_id().equals(listOwnerId)) {
                    myUser.getDbReference().child("MyLists").child(listId).child("shoppingItems").child(Integer.toString(listSize)).setValue(shoppingItem);
                } else {
                    myUser.getGeneralDbReference().child(listOwnerId).child("MyLists").child(listId).child("shoppingItems").child(Integer.toString(listSize)).setValue(shoppingItem);
                }
                ((ShoppingList)theList).addShoppingItem(shoppingItem);
                break;
            case TODO:
                itemName = ((EditText)findViewById(R.id.newTaskName)).getText().toString();
                location = ((EditText)findViewById(R.id.newTaskLocation)).getText().toString();
                date = getDateFromDatePicker((DatePicker)findViewById(R.id.newTaskDate));
                //pic = null;
                ToDoItem toDoItem = new ToDoItem(itemName, date, location, pic);
                if(myUser.getUser_id().equals(listOwnerId)) {
                    myUser.getDbReference().child("MyLists").child(listId).child("toDoItems").child(Integer.toString(listSize)).setValue(toDoItem);
                } else {
                    myUser.getGeneralDbReference().child(listOwnerId).child("MyLists").child(listId).child("toDoItems").child(Integer.toString(listSize)).setValue(toDoItem);
                }
                ((ToDoList)theList).addToDoItem(toDoItem);
                break;
            default:
                return;
        }

        Intent intent = new Intent(this, ListDetailsActivity.class);
        intent.putExtra(LIST_KEY, theList);
        startActivity(intent);
    }

}