package com.androiddev.shopitask.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androiddev.shopitask.MainActivity;
import com.androiddev.shopitask.R;

public class SignUpFragment extends Fragment {

    private EditText editTextUserName;
    private EditText editTextUserEmail;
    private EditText editTextUserPassword;

    private MainActivity mainActivity;

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
                test(userName, userEmail,userPassword);
            }
        });
    }

    private void test(String name, String email, String password){
        Context context = mainActivity.getApplicationContext();
        CharSequence text = name+email+password;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}