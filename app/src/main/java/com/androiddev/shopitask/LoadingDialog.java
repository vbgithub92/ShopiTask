package com.androiddev.shopitask;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity myActivity) {
        activity = myActivity;
    }

    public void startLoadingDialog(String prompt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_loading,null);
        builder.setView(view);

        TextView loadingPrompt = view.findViewById(R.id.loadingPrompt);
        loadingPrompt.setText(prompt);

        dialog = builder.create();
        dialog.show();
    }

    public void dismissLoadingDialog() {
        dialog.dismiss();
    }
}
