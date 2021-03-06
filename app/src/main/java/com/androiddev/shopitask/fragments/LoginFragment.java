package com.androiddev.shopitask.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androiddev.shopitask.MainActivity;
import com.androiddev.shopitask.R;
import com.androiddev.shopitask.TaskListsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private EditText editTextUserEmail;
    private EditText editTextUserPassword;
    private Button initiateLoginButton;

    private MainActivity mainActivity;

    private FirebaseAuth mAut;

    String errorMessage = "";

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

        initiateLoginButton = mainActivity.findViewById(R.id.initiateLoginButton);
        initiateLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = editTextUserEmail.getText().toString();
                String userPassword = editTextUserPassword.getText().toString();

                errorMessage = getString(R.string.login_no_input);

                if (checkValidInput(userEmail, userPassword)) {
                    login(userEmail, userPassword);
                }
                else {
                    showSnackbar(errorMessage);
                }
            }
        });
    }

    private boolean checkValidInput(String email, String password) {
        return checkEmail(email) && checkPassword(password);
    }

    private boolean checkEmail(String userEmail) {
        return !userEmail.isEmpty() && userEmail.contains("@") && userEmail.contains(".");
    }

    private boolean checkPassword(String userPassword) {
        if(userPassword.isEmpty())
            return false;
        if(userPassword.length() < 6) {
            errorMessage = getString(R.string.password_short);
            return false;
        }
        return true;
    }

    private void login(String email, String password) {
        mAut = FirebaseAuth.getInstance();
        mainActivity.getLoadingDialog().startLoadingDialog(getString(R.string.logging_in));
        mAut.signInWithEmailAndPassword(email, password).addOnCompleteListener(mainActivity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    errorMessage = getString(R.string.login_fail);
                    showSnackbar(errorMessage);
                    mainActivity.getLoadingDialog().dismissLoadingDialog();
                }
                else {
                    mainActivity.getLoadingDialog().dismissLoadingDialog();
                    Intent intent = new Intent(getActivity(), TaskListsActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(initiateLoginButton, message , Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(mainActivity.getResources().getColor(R.color.colorAccent));
        View v = snackbar.getView();
        TextView tv = (TextView) v.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.show();
    }
}