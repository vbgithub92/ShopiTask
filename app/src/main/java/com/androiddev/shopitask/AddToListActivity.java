package com.androiddev.shopitask;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.androiddev.shopitask.fragments.AddToShoppingListFragment;
import com.androiddev.shopitask.fragments.AddToTaskListFragment;

import java.util.Objects;

public class AddToListActivity extends AppCompatActivity {

    AddToShoppingListFragment addToShoppingListFragment;
    AddToTaskListFragment addToTaskListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_list);
        createToolbar();

        initAddButton();

        // TODO CHANGE!!!!!
        // Check type of fragment
        addToShoppingListFragment = new AddToShoppingListFragment();
        addToTaskListFragment= new AddToTaskListFragment();
        //setFragment(addToShoppingListFragment);
        setFragment(addToTaskListFragment);

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
                // TODO
                addToTaskListFragment.testIfWorks(addToTaskListFragment.getNewTaskName(), addToTaskListFragment.getNewTaskLocation());
            }
        });
    }
}