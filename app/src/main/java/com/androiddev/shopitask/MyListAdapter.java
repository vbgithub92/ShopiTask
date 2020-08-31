package com.androiddev.shopitask;

import android.content.Context;

import com.androiddev.shopitask.models.List;

import java.util.ArrayList;

public class MyListAdapter  extends ListAdapter{
    public MyListAdapter(ArrayList<List> lists, Context context, OnListListener onlistListener) {
        super(lists, context, onlistListener);
    }

}
