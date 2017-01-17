package com.example.dungkunit.inventorymanagement.model;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dungkunit on 09/01/2017.
 */

public class InventoryContract {
    public static final String CONTENT_PREFIX = "content://";
    public static final String CONTENT_AUTHORITY = "com.example.dungkunit.inventorymanagement";
    public static final String PATH_INVENTORY = "inventory";
    public static final Uri BASE_URI = Uri.parse(CONTENT_PREFIX + CONTENT_AUTHORITY);

    private InventoryContract() {
    }

    public static final class InventoryEntry implements BaseColumns {
        /**
         * Content Provider Uri
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH_INVENTORY);
        /**
         * The MIME type of {@link #CONTENT_URI} for a list of inventories
         */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;
        /**
         * The MIME type of {@link #CONTENT_URI} for a inventory
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;


        public static final String TABLE_NAME = "inventory";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_PICTURE = "picture";

    }
}
