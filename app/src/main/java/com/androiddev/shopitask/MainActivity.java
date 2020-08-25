package com.androiddev.shopitask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.androiddev.shopitask.fragments.MainFragment;
import com.androiddev.shopitask.fragments.SignUpFragment;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    final static String BUNDLE_KEY = "BUNDLE_KEY";

    private ImageView logo;

    private Button signUpButton;
    private Button loginButton;

    private Fragment currentFragment;
    private MainFragment mainFragment;
    private SignUpFragment signUpFragment;

    private boolean isAnimating;

    private Vibrator vibe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibe = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);

        isAnimating = false;

        // Fragments
        mainFragment = new MainFragment();
        currentFragment = mainFragment;
        changeFragment(currentFragment);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initListenersForButtons();
    }

    public void loginButtonClicked(View view) {
        vibe.vibrate(80);

        /* Code to change activity */

        /*Intent intent = new Intent(this, GameActivity.class);
        Bundle b = new Bundle();
        b.putString(DIFFICULTY_KEY, difficulty);
        intent.putExtra(BUNDLE_KEY, b);

        startActivity(intent);*/
    }

    public void signUpButtonClicked(View view) {
        signUpFragment = new SignUpFragment();
        currentFragment = signUpFragment;
        changeFragment(currentFragment);
    }

    public void animateLogo(View view) {

        if (!isAnimating) {
            isAnimating = true;
            logo = findViewById(R.id.logo);
            final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.animation_logo);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            logo.startAnimation(animation);
                        }
                    });
                }
            }, 0, 2000); // Run animation every 2 sec
        }
    }

    public void nextScreen(View view) {

        Intent intent = new Intent(this, TaskListsActivity.class);
        Bundle b = new Bundle();
        intent.putExtra(BUNDLE_KEY, b);

        startActivity(intent);
    }

    public void initListenersForButtons() {
        // Sign up
        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpButtonClicked(view);
            }
        });

        // Login

    }

    public void changeFragment(Fragment currentFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityBottomFrame, currentFragment).commit();
    }

    private  void testIfWorks() {
        Context context = getApplicationContext();
        CharSequence text = "THIS WORKS!!!!!!!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}