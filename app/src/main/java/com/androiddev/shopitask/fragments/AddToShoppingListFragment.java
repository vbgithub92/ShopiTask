package com.androiddev.shopitask.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androiddev.shopitask.R;

import java.util.Objects;


public class AddToShoppingListFragment extends Fragment {

    private final int AMOUNT_MIN = 1;
    private final int AMOUNT_MAX = 20;


    private int newItemAmount = 1;

    public AddToShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_to_shopping_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initiateNumberPicker(view);
    }

    private void initiateNumberPicker(View view) {
        NumberPicker numberPicker = view.findViewById(R.id.newItemAmount);
        if (numberPicker != null) {
            numberPicker.setMinValue(AMOUNT_MIN);
            numberPicker.setMaxValue(AMOUNT_MAX);
            numberPicker.setWrapSelectorWheel(true);
            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                    newItemAmount = newVal;
                }
            });
        }
    }

    /* ********************* DEV ********************* */
    public void testIfWorks(String newTaskName, String newTaskLocation) {
        Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
        CharSequence text = newTaskName + " | " + newTaskLocation;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void testIfWorks(int val) {
        Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
        CharSequence text = String.valueOf(val);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}