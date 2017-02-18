package com.example.dungkunit.inventorymanagement.model;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dungkunit.inventorymanagement.R;

/**
 * Created by dungkunit on 12/01/2017.
 */

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryHolder> {
    private Cursor cursor;
    private InventoryCallback inventoryCallback;
    private View emptyView;
    public static final boolean SHOW_IMAGE = true;
    public static final boolean HIDE_IMAGE = false;
    private boolean isDisplayImg;

    public interface InventoryCallback {
        void onItemClick(int id);
    }

    public InventoryAdapter(InventoryCallback inventoryCallback, Cursor cursor) {
        this.inventoryCallback = inventoryCallback;
        this.cursor = cursor;
    }

    public void setEmptyView(View view) {
        emptyView = view;
    }

    public void setDisplayImg(boolean value) {
        this.isDisplayImg = value;
        notifyDataSetChanged();
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) notifyDataSetChanged();
    }

    @Override
    public InventoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new InventoryHolder(view);
    }

    @Override
    public void onBindViewHolder(InventoryHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.bind(cursor, isDisplayImg);
    }

    @Override
    public int getItemCount() {
        if (cursor == null || cursor.getCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            return 0;
        } else {
            emptyView.setVisibility(View.INVISIBLE);
            return cursor.getCount();
        }
    }

    public class InventoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtTitle;
        private TextView txtPrice;
        private TextView txtQuantity;
        private ImageView imageView;
        private int itemID;

        public InventoryHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.list_item_title);
            txtPrice = (TextView) itemView.findViewById(R.id.list_item_price);
            txtQuantity = (TextView) itemView.findViewById(R.id.list_item_quantity);
            imageView = (ImageView) itemView.findViewById(R.id.list_item_img);
            itemView.setOnClickListener(this);
        }

        public void bind(Cursor cursor, boolean displayValue) {
            InventoryWrapper cursorWrapper = new InventoryWrapper(cursor);
            Inventory inventory = cursorWrapper.getInventory();
            txtTitle.setText(inventory.getTitle());
            txtPrice.setText(inventory.getPrice());
            txtQuantity.setText(inventory.getQuantity());
            imageView.setImageURI(Uri.parse(inventory.getImgSrc()));
            this.itemView.setTag(inventory.getId());
            itemID = inventory.getId();
            if (displayValue == HIDE_IMAGE) {
                imageView.setVisibility(View.GONE);
            }
            if (displayValue == SHOW_IMAGE) {
                imageView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
            if (inventoryCallback != null) inventoryCallback.onItemClick(itemID);
        }
    }
}
