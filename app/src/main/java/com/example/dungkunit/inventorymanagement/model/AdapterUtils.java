package com.example.dungkunit.inventorymanagement.model;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.dungkunit.inventorymanagement.R;

/**
 * Created by dungkunit on 21/01/2017.
 */

public class AdapterUtils {
    public static void setDisplayImage(RecyclerView recyclerView, boolean value) {
        InventoryAdapter inventoryAdapter = (InventoryAdapter) recyclerView.getAdapter();
        inventoryAdapter.setDisplayImg(value);
    }

    public static void setDisplayLayout(Context context, RecyclerView recyclerView, String value) {
        if (value.equals(context.getString(R.string.pref_array_layout_value_1))) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        if (value.equals(context.getString(R.string.pref_array_layout_value_2))) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
