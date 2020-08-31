package com.androiddev.shopitask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.androiddev.shopitask.models.List;
import com.androiddev.shopitask.models.MyUser;
import com.androiddev.shopitask.models.ShoppingList;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private static final String TAG = "ListAdapter";

    private ArrayList<List> lists = new ArrayList<>();

    private Context context;
    private OnListListener onlistListener;

    private List deletedList;
    private int deletedListPosition;
    private boolean undo = false;

    private MyUser myUser = new MyUser();

    public ListAdapter(ArrayList<List> lists, Context context, OnListListener onlistListener) {
        this.lists = lists;
        this.context = context;
        this.onlistListener = onlistListener;
    }

    public interface OnListListener {
        void onListClick(int position);
    }

    public void setItems(ArrayList<List> lists) {
        this.lists = lists;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView listTypeIcon;
        TextView listName;
        TextView listItemsAmount;
        ImageView listMembersIcon;
        TextView listMembersAmount;

        LinearLayout parentLayout;

        OnListListener onListListener;

        public MyViewHolder(View itemView, OnListListener onListListener) {
            super(itemView);
            listTypeIcon = itemView.findViewById(R.id.listTypeIcon);
            listName = itemView.findViewById(R.id.listName);
            listItemsAmount = itemView.findViewById(R.id.listItemsAmount);
            listMembersIcon = itemView.findViewById(R.id.listMembersIcon);
            listMembersAmount = itemView.findViewById(R.id.listMembersAmount);
            parentLayout = itemView.findViewById(R.id.recyclerListItem);

            // Listener
            this.onListListener = onListListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onListListener.onListClick(getAdapterPosition());
        }
    }

    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list,
                parent, false);

        MyViewHolder vh = new MyViewHolder(view, onlistListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        List list = lists.get(position);

        // List Type Icon
        int listTypeIconSrc = 0;
        if (list instanceof ShoppingList)
            listTypeIconSrc = R.drawable.shopping_cart_transparent;
        else {
            listTypeIconSrc = R.drawable.tasklist_tranparent;
            holder.listTypeIcon.setPadding(16, 16, 16, 16);
        }
        holder.listTypeIcon.setImageResource(listTypeIconSrc);

        // Name
        holder.listName.setText(list.getListName());

        // List Size
        holder.listItemsAmount.setText(String.valueOf(list.getListSize()));

        // List Members Icon
        boolean isPrivate = list.isIsPrivate();
        int imageSrc = 0;
        if (isPrivate) {
            imageSrc = R.drawable.single_person;
            holder.listMembersIcon.setPadding(16, 16, 16, 16);
        } else
            imageSrc = R.drawable.multiple_people;
        holder.listMembersIcon.setImageResource(imageSrc);

        int membersAmount = 0;
        if (list.getContributors() != null)
            membersAmount = list.getContributors().size() + 1;
        else
            membersAmount = 1;
        holder.listMembersAmount.setText(String.valueOf(membersAmount));
    }

    public ArrayList<List> getLists() {
        return lists;
    }

    public List getLists(int position) {
        return lists.get(position);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void deleteItem(int position) {
        deletedList = lists.get(position);
        deletedListPosition = position;
        lists.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        Snackbar snackbar = Snackbar.make(((TaskListsActivity) context).findViewById(R.id.my_recycler_view), R.string.undo_delete_prompt, Snackbar.LENGTH_SHORT);
        snackbar.setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoDelete();
            }
        });

        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (!undo)
                    myUser.deleteList(deletedList);
                undo = false;
            }
        });

        snackbar.setActionTextColor(context.getResources().getColor(R.color.colorAccent));
        snackbar.show();

    }

    private void undoDelete() {
        undo = true;
        lists.add(deletedListPosition,
                deletedList);
        notifyItemInserted(deletedListPosition);
    }

    public Context getContext() {
        return context;
    }
}
