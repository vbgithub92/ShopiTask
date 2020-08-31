package com.androiddev.shopitask.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androiddev.shopitask.AddToListActivity;
import com.androiddev.shopitask.R;

import java.util.Date;
import java.util.Objects;

public class AddToTaskListFragment extends Fragment {

    private EditText editTextNewTaskName;
    private EditText editTextNewTaskLocation;
    private DatePicker datePickerNewTaskDate;
    private ImageView imageViewCameraButton;
    private CheckBox checkBoxAddToCalendar;


    private String newTaskName;
    private String newTaskLocation;
    private Date newTaskDate;
    private boolean addToGoogleCalendar = false;

    private Vibrator vibe;

    public AddToTaskListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_to_task_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vibe = (Vibrator) ((AddToListActivity) Objects.requireNonNull(getActivity())).getSystemService(Context.VIBRATOR_SERVICE);
        findViewsById(view);
        attachListeners();
    }

    public void findViewsById(View view) {
        editTextNewTaskName = (EditText) view.findViewById(R.id.newTaskName);
        editTextNewTaskLocation = (EditText) view.findViewById(R.id.newTaskLocation);
        datePickerNewTaskDate = (DatePicker) view.findViewById(R.id.newTaskDate);
        imageViewCameraButton = (ImageView) view.findViewById(R.id.cameraButton);
        checkBoxAddToCalendar = (CheckBox) view.findViewById(R.id.saveToGoogleCalendarCheckBox);
    }

    public void attachListeners() {

        imageViewCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(80);
            }
        });

        checkBoxAddToCalendar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                addToGoogleCalendar = isChecked;
                vibe.vibrate(80);
            }
        });
    }

    public String getNewTaskName() {
        newTaskName = editTextNewTaskName.getText().toString();
        if (checkValidInput(newTaskName))
            return newTaskName;
        return null;
    }

    public String getNewTaskLocation() {
        newTaskLocation = editTextNewTaskLocation.getText().toString();
        if (checkValidInput(newTaskLocation))
            return newTaskLocation;
        return null;
    }

    private boolean checkValidInput(String textToCheck) {
        return textToCheck != null && !textToCheck.isEmpty();
    }

    public boolean getAddToGoogleCalendar() {
        return addToGoogleCalendar;
    }


}