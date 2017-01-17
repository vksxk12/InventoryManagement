package com.example.dungkunit.inventorymanagement.Model;

import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * Created by dungkunit on 15/01/2017.
 */

public class InventoryWrapper extends CursorWrapper {
    public InventoryWrapper(Cursor cursor) {
        super(cursor);
    }

    public Inventory getInventory() {
        int idColumn = getColumnIndex(InventoryContract.InventoryEntry._ID);
        int titleColumn = getColumnIndex(InventoryContract.InventoryEntry.COLUMN_NAME);
        int priceColumn = getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE);
        int quantityColumn = getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
        int pictureColumn = getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PICTURE);
        int id = getInt(idColumn);
        String title = getString(titleColumn);
        String price = getString(priceColumn);
        String quantity = getString(quantityColumn);
        String imgSrc = getString(pictureColumn);
        return new Inventory(id, imgSrc, title, price, quantity);
    }
}
