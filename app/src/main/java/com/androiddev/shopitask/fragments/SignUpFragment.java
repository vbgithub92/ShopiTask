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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpFragment extends Fragment {

    private EditText editTextUserName;
    private EditText editTextUserEmail;
    private EditText editTextUserPassword;
    private Button initiateSignUpButton;

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

        initiateSignUpButton = mainActivity.findViewById(R.id.initiateSignUpButton);
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
                    showUndoSnackbar(getString(R.string.sign_up_no_input));
                }

            }
        });
    }

    private boolean checkValidInput(String name, String email, String password) {
        return checkName(name) && checkEmail(email) && checkPassword(password);
    }

    private boolean checkName(String userName) {
        return !userName.isEmpty();
    }

    private boolean checkEmail(String userEmail) {
        return !userEmail.isEmpty() && userEmail.contains("@") && userEmail.contains(".");
    }

    private boolean checkPassword(String userPassword) {
        return !userPassword.isEmpty();
    }

    private void SignUp(final String name, final String email, final String password){
        mAut = FirebaseAuth.getInstance();
        mainActivity.getLoadingDialog().startLoadingDialog(getString(R.string.signing_up));
        mAut.createUserWithEmailAndPassword(email, password).addOnCompleteListener(mainActivity, new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if (!task.isSuccessful()) {
                   showUndoSnackbar(getString(R.string.sign_up_fail));
                   mainActivity.getLoadingDialog().dismissLoadingDialog();
               } else {
                   FirebaseUser user = mAut.getCurrentUser();
                   UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                   user.updateProfile(profileUpdates);
                   FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("email").setValue(email);

                   mAut.signInWithEmailAndPassword(email, password).addOnCompleteListener(mainActivity, new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (!task.isSuccessful()) {
                               showUndoSnackbar(getString(R.string.login_fail));
                           }
                           else {
                               mainActivity.getLoadingDialog().dismissLoadingDialog();
                               Intent intent = new Intent(getActivity(), TaskListsActivity.class);
                               startActivity(intent);
                           }
                       }
                   });
               }
           }
       });
    }

    private void showUndoSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(initiateSignUpButton, message , Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(mainActivity.getResources().getColor(R.color.colorAccent));
        View v = snackbar.getView();
        TextView tv = (TextView) v.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.show();
    }
}


