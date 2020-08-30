package com.androiddev.shopitask;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androiddev.shopitask.models.MyUser;
import com.androiddev.shopitask.models.ShoppingItem;
import com.androiddev.shopitask.models.ShoppingList;
import com.androiddev.shopitask.models.UOM;

import java.util.Objects;

import androidx.recyclerview.widget.RecyclerView;

class ShoppingItemDialog {

    private Activity activity;
    private AlertDialog dialog;
    private MyUser myUser;
    private ShoppingList shoppingList;
    private RecyclerView.Adapter listAdapter;
    private int position;

    ShoppingItem theItem;

    public ShoppingItemDialog(Activity myActivity, MyUser myUser, ShoppingList shoppingList, ShoppingItem shoppingItem, int position, RecyclerView.Adapter listAdapter) {
        activity = myActivity;
        this.theItem = shoppingItem;
        this.myUser = myUser;
        this.shoppingList = shoppingList;
        this.listAdapter = listAdapter;
        this.position = position;
    }

    public void startShoppingItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_shopping_item, null);
        builder.setView(view);

        TextView itemName = view.findViewById(R.id.itemName);
        TextView amountAndType = view.findViewById(R.id.itemAmountAndType);
        ImageView itemPicture = view.findViewById(R.id.itemPicture);
        Button closeButton = view.findViewById(R.id.closeButton);
        Button gotItButton = view.findViewById(R.id.gotItButton);

        // Item Name
        itemName.setText(theItem.getName());

        // Item amount and type
        String amountAndTypeFormatted = String.valueOf((int) theItem.getQuantity()) + " ";
        String type = theItem.getUom().toString();
        if (!type.equals(UOM.KG.toString()) && !type.equals(UOM.L.toString())) {
            type = type.toLowerCase();
            type = type.substring(0, 1).toUpperCase() + type.substring(1);
        }
        amountAndTypeFormatted += type;
        amountAndType.setText(amountAndTypeFormatted);

        // TODO Pic

        // Buttons
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        gotItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myUser.deleteItem(shoppingList ,theItem);
                shoppingList.removeItem(theItem);
                listAdapter.notifyItemRemoved(position);
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
