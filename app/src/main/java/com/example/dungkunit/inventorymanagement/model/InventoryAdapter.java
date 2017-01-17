package com.example.dungkunit.inventorymanagement.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dungkunit.inventorymanagement.R;

/**
 * Created by dungkunit on 12/01/2017.
 */

public class InventoryAdapter extends CursorAdapter {

    public InventoryAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtTitle = (TextView) view.findViewById(R.id.list_item_title);
        TextView txtPrice = (TextView) view.findViewById(R.id.list_item_price);
        TextView txtQuantity = (TextView) view.findViewById(R.id.list_item_quantity);
        ImageView imageView = (ImageView) view.findViewById(R.id.list_item_img);

        InventoryWrapper cursorWrapper = new InventoryWrapper(cursor);
        Inventory inventory = cursorWrapper.getInventory();
        txtTitle.setText(inventory.getTitle());
        txtPrice.setText(inventory.getPrice());
        txtQuantity.setText(inventory.getQuantity());
        imageView.setImageURI(Uri.parse(inventory.getImgSrc()));
    }
}
