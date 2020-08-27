package com.androiddev.shopitask.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androiddev.shopitask.MainActivity;
import com.androiddev.shopitask.R;
import com.androiddev.shopitask.TaskListsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpFragment extends Fragment {

    private EditText editTextUserName;
    private EditText editTextUserEmail;
    private EditText editTextUserPassword;

    private MainActivity mainActivity;

    private FirebaseAuth mAut;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = ((MainActivity) getActivity());
        editTextUserName = mainActivity.findViewById(R.id.newUserName);
        editTextUserEmail = mainActivity.findViewById(R.id.newUserEmail);
        editTextUserPassword = mainActivity.findViewById(R.id.newUserPassword);

        Button initiateSignUpButton = mainActivity.findViewById(R.id.initiateSignUpButton);
        initiateSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = editTextUserName.getText().toString();
                String userEmail = editTextUserEmail.getText().toString();
                String userPassword = editTextUserPassword.getText().toString();

                if(checkValidInput(userName,userEmail,userPassword)) {
                  SignUp(userName, userEmail,userPassword);
                }
                else {
                    // TODO FAIL
                }

            }
        });
    }

    // TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private boolean checkValidInput(String name, String email, String password) {
        if (checkName(name) && checkEmail(email) && checkPassword(password))
            return true;
        return false;
    }

    private boolean checkName(String userName) {
        // TODO Add
        return true;
    }

    private boolean checkEmail(String userEmail) {
        // TODO Add
        return true;
    }

    private boolean checkPassword(String userPassword) {
        // TODO Add
        return true;
    }

    private void SignUp(final String name, final String email, final String password){
        Context context = mainActivity.getApplicationContext();
        CharSequence text = "Input: " + name + " | " + email + " | " + password;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        mAut = FirebaseAuth.getInstance();
        mAut.createUserWithEmailAndPassword(email, password).addOnCompleteListener(mainActivity, new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if (!task.isSuccessful()) {
                   Toast.makeText(mainActivity.getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
               } else {
                   FirebaseUser user = mAut.getCurrentUser();
                   UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                   user.updateProfile(profileUpdates);

                   mAut.signInWithEmailAndPassword(email, password).addOnCompleteListener(mainActivity, new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (!task.isSuccessful()) {
                               Toast.makeText(mainActivity.getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                           }
                           else {
                               Intent intent = new Intent(getActivity(), TaskListsActivity.class);
                               startActivity(intent);
                           }
                       }
                   });
               }
           }
       });
    }
}


