package com.androiddev.shopitask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androiddev.shopitask.models.MyUser;
import com.androiddev.shopitask.models.ToDoItem;
import com.androiddev.shopitask.models.ToDoList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import androidx.recyclerview.widget.RecyclerView;

class ToDoItemDialog {

    private Activity activity;
    private AlertDialog dialog;
    private MyUser myUser;
    private ToDoList toDoList;
    private RecyclerView.Adapter listAdapter;
    private int position;

    ToDoItem theItem;

    public ToDoItemDialog(Activity activity, MyUser myUser, ToDoList toDoList, ToDoItem toDoItem, int position, RecyclerView.Adapter listAdapter) {
        this.activity = activity;
        this.theItem = toDoItem;
        this.myUser = myUser;
        this.toDoList = toDoList;
        this.listAdapter = listAdapter;
        this.position = position;
    }

    public void startToDoItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_to_do_item, null);
        builder.setView(view);


        TextView taskName = view.findViewById(R.id.taskName);
        TextView taskLocation = view.findViewById(R.id.taskLocation);
        FloatingActionButton navigationButton = view.findViewById(R.id.navigateToTaskButton);
        TextView taskDate = view.findViewById(R.id.taskDate);
        ImageView taskPicture = view.findViewById(R.id.taskPicture);
        Button closeButton = view.findViewById(R.id.closeButton);
        Button itsDoneButton = view.findViewById(R.id.itsDoneButton);

        // Task Name
        taskName.setText(theItem.getActivityName());

        // Task Location
        final String theLocation = theItem.getLocation();
        if (theLocation != null && !theLocation.isEmpty()) {
            taskLocation.setText(theLocation);
            navigationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + theLocation);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    activity.startActivity(mapIntent);
                }
            });
        } else {
            taskLocation.setVisibility(View.GONE);
            navigationButton.setVisibility(View.GONE);
        }

        // Task Date
        long theTaskDate = theItem.getDate();
        if (theTaskDate != 0) {
            Date currentDate = new Date(theTaskDate);
            @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            taskDate.setText(df.format(currentDate));
        } else
            taskDate.setVisibility(View.GONE);

        // TODO Pic

        // Buttons
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        itsDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myUser.deleteItem(toDoList ,theItem);
                toDoList.removeItem(theItem);
                listAdapter.notifyItemRemoved(position);
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
