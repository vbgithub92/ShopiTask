package com.androiddev.shopitask.fragments;

import android.content.Context;
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

public class LoginFragment extends Fragment {

    private EditText editTextUserEmail;
    private EditText editTextUserPassword;

    private MainActivity mainActivity;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = ((MainActivity) getActivity());
        editTextUserEmail = mainActivity.findViewById(R.id.existingUserEmail);
        editTextUserPassword = mainActivity.findViewById(R.id.existingUserPassword);

        Button initiateLoginButton = mainActivity.findViewById(R.id.initiateLoginButton);
        initiateLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = editTextUserEmail.getText().toString();
                String userPassword = editTextUserPassword.getText().toString();
                if (checkValidInput(userEmail, userPassword)) {
                    // TODO Valid input - try to connect next
                    test(userEmail, userPassword);
                }
                else {
                    // TODO Invalid input - error
                    test("FAILED", "LOGIN");
                }

            }
        });
    }

    private boolean checkValidInput(String email, String password) {
        // TODO if(checkEmail(email) && checkPassword(password))

        // Just to check if there is an input
        if (email != null && !email.isEmpty() && password != null && !password.isEmpty())
            return true;
        return false;
    }

    private boolean checkEmail(String userEmail) {
        // TODO Add
        return true;
    }

    private boolean checkPassword(String userPassword) {
        // TODO Add
        return true;
    }

    private void test(String email, String password) {
        Context context = mainActivity.getApplicationContext();
        CharSequence text = "Login details: " + email + " | " + password;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}