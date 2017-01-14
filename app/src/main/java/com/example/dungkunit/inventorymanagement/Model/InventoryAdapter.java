package com.example.dungkunit.inventorymanagement.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dungkunit.inventorymanagement.R;

import java.util.List;

/**
 * Created by dungkunit on 12/01/2017.
 */

public class InventoryAdapter extends ArrayAdapter<Inventory> {
    private List<Inventory> list;
    private Context context;

    public InventoryAdapter(Context context, List<Inventory> objects) {
        super(context, 0, objects);
        this.context = context;
        this.list = objects;
    }

    public void setList(List<Inventory> newList) {
        this.list = newList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }
        TextView txtTitle = (TextView) convertView.findViewById(R.id.list_item_title);
        TextView txtPrice = (TextView) convertView.findViewById(R.id.list_item_price);
        TextView txtQuantity = (TextView) convertView.findViewById(R.id.list_item_quantity);

        Inventory inventory = list.get(position);
        txtTitle.setText(inventory.getTitle());
        txtPrice.setText(inventory.getPrice());
        txtQuantity.setText(inventory.getQuantity());
        return convertView;
    }
}
