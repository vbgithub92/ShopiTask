package com.androiddev.shopitask.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androiddev.shopitask.MainActivity;
import com.androiddev.shopitask.R;

import java.util.Objects;

public class MainFragment extends Fragment {

    private Button goToLoginFragmentButton;
    private Button goToSignUpFragmentButton;

    private Vibrator vibe;


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vibe = (Vibrator)((MainActivity) Objects.requireNonNull(getActivity())).getSystemService(Context.VIBRATOR_SERVICE);
        findViewsById(view);
        attachListeners();
    }

    public void findViewsById(View view) {
        goToLoginFragmentButton = (Button)view.findViewById(R.id.loginButton);
        goToSignUpFragmentButton = (Button)view.findViewById(R.id.nextButton);
    }

    public void attachListeners() {
        goToLoginFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(80);
                ((MainActivity) Objects.requireNonNull(getActivity())).loginButtonClicked(view);
            }
        });

        goToSignUpFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(80);
                ((MainActivity) Objects.requireNonNull(getActivity())).signUpButtonClicked(view);
            }
        });
    }
}