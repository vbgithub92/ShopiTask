package com.androiddev.shopitask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddev.shopitask.models.ShoppingItem;
import com.androiddev.shopitask.models.ShoppingList;
import com.androiddev.shopitask.models.UOM;

class ShoppingListItemAdapter extends RecyclerView.Adapter<ShoppingListItemAdapter.MyViewHolder> {

    private static final String TAG = "ShoppingListItemAdapter";

    private ShoppingList shoppingItemsList;
    private Context context;
    private OnListItemListener onListItemListener;

    public ShoppingListItemAdapter(ShoppingList shoppingItemsList, Context context, OnListItemListener onListItemListener) {
        this.shoppingItemsList = shoppingItemsList;
        this.context = context;
        this.onListItemListener = onListItemListener;
    }

    public interface OnListItemListener {
        void onListItemClick(int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemName;
        TextView itemAmountAndType;
        ImageView itemPhoto;

        LinearLayout parentLayout;
        OnListItemListener onListListener;

        public MyViewHolder(View itemView, OnListItemListener onListItemListener) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemAmountAndType = itemView.findViewById(R.id.itemAmountAndType);
            itemPhoto = itemView.findViewById(R.id.itemPhoto);
            parentLayout = itemView.findViewById(R.id.recyclerListItem);

            // Listener
            this.onListListener = onListItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onListListener.onListItemClick(getAdapterPosition());
        }
    }
    
    
    @NonNull
    @Override
    public ShoppingListItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                   int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_shopping_item,
                parent,false);

        MyViewHolder vh = new MyViewHolder(view , onListItemListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListItemAdapter.MyViewHolder holder, int position) {
        ShoppingItem shoppingItem = shoppingItemsList.getShoppingItems().get(position);

        // Item Name
        holder.itemName.setText(shoppingItem.getName());

        // Item amount and type
        String amountAndType = String.valueOf((int)shoppingItem.getQuantity()) + " ";
        String type = shoppingItem.getUom().toString();
        if(!type.equals(UOM.KG.toString()) && !type.equals(UOM.L.toString())){
            type = type.toLowerCase();
            type = type.substring(0, 1).toUpperCase() + type.substring(1);
        }
        amountAndType += type;
        holder.itemAmountAndType.setText(amountAndType);

        // TODO Pic


    }

    @Override
    public int getItemCount() {
        if (shoppingItemsList.getShoppingItems() != null)
            return shoppingItemsList.getShoppingItems().size();
        else
            return 0;
    }
}
