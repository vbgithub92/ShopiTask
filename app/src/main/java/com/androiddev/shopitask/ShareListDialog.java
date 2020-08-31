package com.androiddev.shopitask;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androiddev.shopitask.models.List;
import com.androiddev.shopitask.models.MyUser;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

class ShareListDialog {

    private Activity activity;
    private AlertDialog dialog;

    private List theList;
    private MyUser myUser = new MyUser();

    private EditText editTextTargetEmail;
    private Button shareButton;
    private Button closeButton;

    public ShareListDialog(Activity activity, List theList) {
        this.activity = activity;
        this.theList = theList;

    }

    public void startShareListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_share_list, null);
        builder.setView(view);

        editTextTargetEmail = view.findViewById(R.id.shareTargetEmail);
        shareButton = view.findViewById(R.id.shareButton);
        closeButton = view.findViewById(R.id.closeButton);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListDetailsActivity) activity).getVibrator().vibrate(80);
                String targetEmail = editTextTargetEmail.getText().toString();
                if (!targetEmail.isEmpty()) {
                    myUser.addUserToList(targetEmail, theList, (ListDetailsActivity) activity);
                    dialog.dismiss();
                    ((ListDetailsActivity) activity).updateViews();
                    showSnackbar(activity.getString(R.string.share_success) + targetEmail);
                } else {
                    showSnackbar(activity.getString(R.string.share_error));
                }
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListDetailsActivity) activity).getVibrator().vibrate(80);
                dialog.dismiss();
            }
        });


        dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(shareButton, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(activity.getResources().getColor(R.color.colorAccent));
        View v = snackbar.getView();
        TextView tv = (TextView) v.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.show();
    }

}
