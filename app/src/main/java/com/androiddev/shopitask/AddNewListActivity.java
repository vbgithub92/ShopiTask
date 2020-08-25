package com.androiddev.shopitask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewListActivity extends AppCompatActivity {

    private Vibrator vibe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_list);

        vibe = (Vibrator) AddNewListActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void radioButtonClicked(View view) {
        vibe.vibrate(80);
    }

    public void doneButtonClicked(View view) {
        vibe.vibrate(80);

        // Check input
        EditText editTextListName = findViewById(R.id.newListName);
        String listName = editTextListName.getText().toString();
        if(checkValidListName(listName)) {
            // Return to previous activity
            Intent intent = new Intent(this, TaskListsActivity.class);
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
}