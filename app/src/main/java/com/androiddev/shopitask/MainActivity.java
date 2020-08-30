package com.androiddev.shopitask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.androiddev.shopitask.fragments.LoginFragment;
import com.androiddev.shopitask.fragments.MainFragment;
import com.androiddev.shopitask.fragments.SignUpFragment;
import com.androiddev.shopitask.models.LoadingDialog;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    final static String BUNDLE_KEY = "BUNDLE_KEY";
    final static String LIST_NAME_KEY = "LIST_NAME_KEY";
    final static String LIST_TYPE_KEY = "LIST_TYPE_KEY";
    final static String IS_PRIVATE_LIST_KEY = "IS_PRIVATE_LIST_KEY";
    final static String LIST_ID_KEY = "LIST_ID_KEY";
    final static String LIST_OWNER_ID_KEY = "LIST_OWNER_ID_KEY";
    final static String LIST_SIZE_KEY = "LIST_SIZE_KEY";
    final static String LIST_KEY = "Selected List";

    private ImageView logo;

    private Fragment currentFragment;
    private MainFragment mainFragment;
    private SignUpFragment signUpFragment;
    private LoginFragment loginFragment;
    private LoadingDialog loadingDialog;

    private boolean isAnimating;

    private Vibrator vibe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createToolbar();
        loadingDialog = new LoadingDialog(this);

        vibe = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);

        isAnimating = false;

        // Fragments
        mainFragment = new MainFragment();
        currentFragment = mainFragment;
        changeFragment(currentFragment);

    }


    public void signUpButtonClicked(View view) {
        vibe.vibrate(80);
        signUpFragment = new SignUpFragment();
        currentFragment = signUpFragment;
        changeFragment(currentFragment);
    }

    public void loginButtonClicked(View view) {
        vibe.vibrate(80);
        loginFragment = new LoginFragment();
        currentFragment = loginFragment;
        changeFragment(currentFragment);
    }


    public void nextScreen(View view) {

        Intent intent = new Intent(this, TaskListsActivity.class);
        Bundle b = new Bundle();
        intent.putExtra(BUNDLE_KEY, b);

        startActivity(intent);
    }


    public void changeFragment(Fragment currentFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityBottomFrame, currentFragment).commit();
    }

    private void testIfWorks() {
        Context context = getApplicationContext();
        CharSequence text = "THIS WORKS!!!!!!!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void animateLogo(View view) {

    /*    if (!isAnimating) {
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
        }*/

        //Intent intent = new Intent(this, ListDetailsActivity.class);
        Intent intent = new Intent(this, TaskListsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (currentFragment != mainFragment) {
            currentFragment = mainFragment;
            changeFragment(currentFragment);

        } else {
            MainActivity.super.onBackPressed();
        }
    }

    private void createToolbar() {
        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public static long getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

    public LoadingDialog getLoadingDialog() {
        return loadingDialog;
    }
}