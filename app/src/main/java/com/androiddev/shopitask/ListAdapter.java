package com.androiddev.shopitask;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.androiddev.shopitask.models.List;

import java.util.ArrayList;

class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private static final String TAG = "ListAdapter";
    
    private ArrayList<List> lists = new ArrayList<>();
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView listName;
        TextView listItemsAmount;
        ImageView listMembersIcon;
        TextView listMembersAmount;

        LinearLayout parentLayout;
        
        public MyViewHolder(View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.listName);
            listItemsAmount = itemView.findViewById(R.id.listItemsAmount);
            listMembersIcon = itemView.findViewById(R.id.listMembersIcon);
            listMembersAmount = itemView.findViewById(R.id.listMembersAmount);
            parentLayout = itemView.findViewById(R.id.recyclerListItem);
        }
    }

    public ListAdapter(ArrayList<List> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list,
                parent, false);

        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        List list = lists.get(position);

        holder.listName.setText(list.getListName());
        holder.listItemsAmount.setText(String.valueOf(list.getListSize()));

        // TODO HELP
        //  holder.listItemsAmount.setText(String.valueOf(lists.get(position)));
        //  ...


    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
