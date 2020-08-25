package com.androiddev.shopitask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TaskListsActivity extends AppCompatActivity {

    private Vibrator vibe;
    private String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private TextView textUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_lists);
        textUserName = this.findViewById(R.id.username);

        DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("userName");
        currentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textUserName.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        vibe = (Vibrator) TaskListsActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void addListButtonClicked(View view) {
        vibe.vibrate(80);

        Intent intent = new Intent(this, AddNewListActivity.class);
        startActivity(intent);
    }
}