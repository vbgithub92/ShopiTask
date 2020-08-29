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

import com.androiddev.shopitask.models.ToDoItem;
import com.androiddev.shopitask.models.ToDoList;

class ToDoListItemAdapter extends RecyclerView.Adapter<ToDoListItemAdapter.MyViewHolder> {
    private static final String TAG = "ToDoListItemAdapter";

    private ToDoList toDoList;
    private Context context;
    private OnListItemListener onListItemListener;

    public ToDoListItemAdapter(ToDoList toDoList, Context context, OnListItemListener onListItemListener) {
        this.toDoList = toDoList;
        this.context = context;
        this.onListItemListener = onListItemListener;
    }

    public interface OnListItemListener {
        void onListItemClick(int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView taskName;
        ImageView navigationIcon;
        ImageView taskPhoto;

        LinearLayout parentLayout;
        OnListItemListener onListListener;

        public MyViewHolder(View itemView, OnListItemListener onListItemListener) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskName);
            navigationIcon = itemView.findViewById(R.id.navigationIcon);
            taskPhoto = itemView.findViewById(R.id.taskPhoto);

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
    public ToDoListItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_todo_item,
                parent, false);

        MyViewHolder vh = new MyViewHolder(view,onListItemListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoListItemAdapter.MyViewHolder holder, int position) {
        ToDoItem toDoItem = toDoList.getToDoItems().get(position);

        // Task Name
        holder.taskName.setText(toDoItem.getActivityName());

        // Navigation
        int navIconSrc = 0;
        if(toDoItem.getLocation() != null)
            navIconSrc = R.drawable.navigation;
        holder.navigationIcon.setImageResource(navIconSrc);

        // TODO: 29-Aug-20 Pic
    }

    @Override
    public int getItemCount() {
        return toDoList.getToDoItems().size();
    }


}
