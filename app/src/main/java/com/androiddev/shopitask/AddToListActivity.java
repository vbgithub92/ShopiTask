package com.androiddev.shopitask;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.androiddev.shopitask.fragments.AddToShoppingListFragment;

public class AddToListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_list);


        // Check type of fragment
        AddToShoppingListFragment fragment = new AddToShoppingListFragment();
        setFragment(fragment);

    }

    private void setFragment(Fragment fragmentToSet) {
        getSupportFragmentManager().beginTransaction().replace(R.id.addToListFrame, fragmentToSet).commit();
    }
}