package com.androiddev.shopitask;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Base64;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
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
    public static final String BUNDLE_KEY = "BUNDLE";
    public static final String ADD_TO_GOOGLE = "ADD TO GOOGLE";
    public static final String ITEM_KEY = "ITEM";

    private static final int REQUEST_CODE = 100;
    private List theList;

    Fragment addToListFragment;
    Button addButton;

    String listName;
    String listType;
    boolean isPrivate;
    String listId;
    String listOwnerId;
    int listSize;

    String itemName;
    double qty;
    UOM uom;
    String pic = "";
    String location;
    long date;

    List newList;
    MyUser myUser;

    private boolean nextScreen = false;
    private boolean addToGoogleCalendar = false;

    private Vibrator vibe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_list);
        createToolbar();

        vibe = (Vibrator) AddToListActivity.this.getSystemService(Context.VIBRATOR_SERVICE);

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
        ImageView cameraButton = (ImageView) findViewById(R.id.cameraButton);

        if (ContextCompat.checkSelfPermission(AddToListActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddToListActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CODE);

        }

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open Camera
                vibe.vibrate(80);
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
            if(data != null) {
                pic = BitMapToString((Bitmap) data.getExtras().get("data"));
            }
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
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
        addButton = findViewById(R.id.listAddNewItem);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(80);
                if (listId == null) {
                    createNewList();
                } else {
                    addItemToList();
                }
            }
        });
    }

    private void createNewList() {

        ToDoItem toDoItem = null;

        switch (ListType.valueOf(listType)) {
            case SHOPPING:
                itemName = ((EditText) findViewById(R.id.newItemName)).getText().toString();
                if (checkInput(itemName)) {
                    nextScreen = true;
                    qty = ((NumberPicker) findViewById(R.id.newItemAmount)).getValue();
                    int selectedUOMid = ((RadioGroup) findViewById(R.id.unitTypeGroup)).getCheckedRadioButtonId();
                    uom = UOM.valueOf(((RadioButton) findViewById(selectedUOMid)).getText().toString().toUpperCase());

                    ShoppingItem shoppingItem = new ShoppingItem(itemName, qty, uom, pic);
                    newList = new ShoppingList(myUser.getUser_id(), listName, null, isPrivate, shoppingItem);
                    myUser.getDbReference().child("MyLists").child(newList.getListId()).setValue(newList);
                } else
                    showSnackbar(getString(R.string.item_name_error));

                break;
            case TODO:
                itemName = ((EditText) findViewById(R.id.newTaskName)).getText().toString();
                if (checkInput(itemName)) {
                    nextScreen = true;
                    location = ((EditText) findViewById(R.id.newTaskLocation)).getText().toString();
                    date = getDateFromDatePicker((DatePicker) findViewById(R.id.newTaskDate));

                    // Calendar
                    if (((AddToTaskListFragment) addToListFragment).getAddToGoogleCalendar()) {
                        addToGoogleCalendar = true;
                    }

                    toDoItem = new ToDoItem(itemName, date, location, pic);
                    newList = new ToDoList(myUser.getUser_id(), listName, null, isPrivate, toDoItem);
                    myUser.getDbReference().child("MyLists").child(newList.getListId()).setValue(newList);
                } else
                    showSnackbar(getString(R.string.task_name_error));

                break;
            default:
                return;
        }

        if (nextScreen) {
            Intent intent = new Intent(this, ListDetailsActivity.class);
            Bundle b = new Bundle();
            b.putBoolean(ADD_TO_GOOGLE,addToGoogleCalendar);
            intent.putExtra(BUNDLE_KEY,b);
            if(addToGoogleCalendar)
                intent.putExtra(ITEM_KEY,toDoItem);
            intent.putExtra(LIST_KEY, newList);
            startActivity(intent);
        }
    }

    private void addItemToList() {

        ToDoItem toDoItem = null; // For Calendar

        switch (ListType.valueOf(listType)) {
            case SHOPPING:
                itemName = ((EditText) findViewById(R.id.newItemName)).getText().toString();
                if (checkInput(itemName)) {
                    nextScreen = true;
                    qty = ((NumberPicker) findViewById(R.id.newItemAmount)).getValue();
                    int selectedUOMid = ((RadioGroup) findViewById(R.id.unitTypeGroup)).getCheckedRadioButtonId();
                    uom = UOM.valueOf(((RadioButton) findViewById(selectedUOMid)).getText().toString().toUpperCase());

                    final ShoppingItem shoppingItem = new ShoppingItem(itemName, qty, uom, pic);
                    if (myUser.getUser_id().equals(listOwnerId)) {
                        myUser.getDbReference().child("MyLists").child(listId).child("shoppingItems").child(Integer.toString(listSize)).setValue(shoppingItem);
                    } else {
                        final DatabaseReference ref = myUser.getGeneralDbReference().child(listOwnerId).child("MyLists").child(listId);
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot ds) {
                                if (ds.hasChild("shoppingItems")) {
                                    ref.child("shoppingItems").child(Integer.toString(listSize)).setValue(shoppingItem);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }

                        });
                    }
                    ((ShoppingList) theList).addShoppingItem(shoppingItem);
                } else
                    showSnackbar(getString(R.string.item_name_error));


                break;
            case TODO:
                itemName = ((EditText) findViewById(R.id.newTaskName)).getText().toString();
                if (checkInput(itemName)) {
                    nextScreen = true;
                    location = ((EditText) findViewById(R.id.newTaskLocation)).getText().toString();
                    date = getDateFromDatePicker((DatePicker) findViewById(R.id.newTaskDate));

                    // Calendar
                    if (((AddToTaskListFragment) addToListFragment).getAddToGoogleCalendar()) {
                        addToGoogleCalendar = true;
                    }

                    toDoItem = new ToDoItem(itemName, date, location, pic);
                    if (myUser.getUser_id().equals(listOwnerId)) {
                        myUser.getDbReference().child("MyLists").child(listId).child("toDoItems").child(Integer.toString(listSize)).setValue(toDoItem);
                    } else {
                        myUser.getGeneralDbReference().child(listOwnerId).child("MyLists").child(listId).child("toDoItems").child(Integer.toString(listSize)).setValue(toDoItem);
                    }
                    ((ToDoList) theList).addToDoItem(toDoItem);

                } else
                    showSnackbar(getString(R.string.task_name_error));
                break;
            default:
                return;
        }

        if (nextScreen) {
            Intent intent = new Intent(this, ListDetailsActivity.class);
            Bundle b = new Bundle();
            b.putBoolean(ADD_TO_GOOGLE,addToGoogleCalendar);
            intent.putExtra(BUNDLE_KEY,b);
            if(addToGoogleCalendar)
                intent.putExtra(ITEM_KEY,toDoItem);
            intent.putExtra(LIST_KEY, theList);
            startActivity(intent);
        }
    }

    private boolean checkInput(String input) {
        return input != null && !input.isEmpty();
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(addButton, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(this.getResources().getColor(R.color.colorAccent));
        View v = snackbar.getView();
        TextView tv = (TextView) v.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.show();
    }

}