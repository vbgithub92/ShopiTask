package com.androiddev.shopitask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import static com.androiddev.shopitask.MainActivity.BUNDLE_KEY;
import static com.androiddev.shopitask.MainActivity.IS_PRIVATE_LIST_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_NAME_KEY;
import static com.androiddev.shopitask.MainActivity.LIST_TYPE_KEY;
import static com.androiddev.shopitask.MainActivity.SHOPPING_LIST_TYPE;
import static com.androiddev.shopitask.MainActivity.TODO_LIST_TYPE;

public class AddNewListActivity extends AppCompatActivity {

    private Vibrator vibe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_list);
        createToolbar();
        vibe = (Vibrator) AddNewListActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void radioButtonClicked(View view) {
        vibe.vibrate(80);
    }

    public void nextButtonClicked(View view) {
        vibe.vibrate(80);

        // Check input
        EditText editTextListName = findViewById(R.id.newListName);
        RadioButton rbShoppingListType = findViewById(R.id.radioButtonShopping);
        RadioButton rbToDoListType = findViewById(R.id.radioButtonToDoList);
        RadioButton rbIsPrivate = findViewById(R.id.radioButtonPersonal);
        String listName = editTextListName.getText().toString();
        String listType = "";
        boolean isPrivate = false;
        if(checkValidListName(listName)) {
            // Add items to list
            if (rbShoppingListType.isChecked()) {
                listType = SHOPPING_LIST_TYPE;
            } else if (rbToDoListType.isChecked()) {
                listType = TODO_LIST_TYPE;
            }

            if (rbIsPrivate.isChecked()) {
                isPrivate = true;
            } else {
                isPrivate = false;
            }

            Intent intent = new Intent(this, AddToListActivity.class);
            Bundle b = new Bundle();
            b.putString(LIST_NAME_KEY, listName);
            b.putString(LIST_TYPE_KEY, listType);
            b.putBoolean(IS_PRIVATE_LIST_KEY, isPrivate);
            intent.putExtra(BUNDLE_KEY, b);
            startActivity(intent);
        }
        else
            informUserOfBadInput();
    }

    private boolean checkValidListName(String listName){
        if(listName != null && !listName.isEmpty())
            return true;
        return false;
    }

    private void informUserOfBadInput() {
        Context context = getApplicationContext();
        CharSequence text = getString(R.string.bad_list_name);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void createToolbar() {
        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }
}