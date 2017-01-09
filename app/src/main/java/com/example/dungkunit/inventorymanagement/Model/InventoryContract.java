package com.example.dungkunit.inventorymanagement.Model;

import android.provider.BaseColumns;

/**
 * Created by dungkunit on 09/01/2017.
 */

public class InventoryContract {
    public static final class InventoryEntry implements BaseColumns {

        public static final String TABLE_NAME = "Inventory";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_SUPPLIER = "supplier";
        public static final String COLUMN_PICTURE = "picture";

    }
}
