package com.example.dungkunit.inventorymanagement.model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import static com.example.dungkunit.inventorymanagement.model.InventoryContract.CONTENT_AUTHORITY;
import static com.example.dungkunit.inventorymanagement.model.InventoryContract.InventoryEntry;
import static com.example.dungkunit.inventorymanagement.model.InventoryContract.PATH_INVENTORY;

/**
 * Created by dungkunit on 17/01/2017.
 */

public class InventoryProvider extends ContentProvider {
    public static final String TAG = InventoryProvider.class.getSimpleName();
    public static final int INVENTORIES = 100;
    public static final int INVENTORY_ID = 101;
    public static final UriMatcher sURI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURI_MATCHER.addURI(CONTENT_AUTHORITY, PATH_INVENTORY, INVENTORIES);
        sURI_MATCHER.addURI(CONTENT_AUTHORITY, PATH_INVENTORY + "/#", INVENTORY_ID);
    }

    private InventoryHelper inventoryHelper;

    @Override
    public boolean onCreate() {
        inventoryHelper = new InventoryHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase sqLiteDatabase = inventoryHelper.getReadableDatabase();
        int match = sURI_MATCHER.match(uri);
        Cursor cursor;
        switch (match) {
            case INVENTORIES:
                cursor = sqLiteDatabase.query(InventoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case INVENTORY_ID:
                selection = InventoryEntry._ID + " = ?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(InventoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI: " + uri);
        }
        //notify
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase sqLiteDatabase = inventoryHelper.getWritableDatabase();
        int match = sURI_MATCHER.match(uri);
        int rs;
        switch (match) {
            case INVENTORIES:
                rs = sqLiteDatabase.delete(InventoryEntry.TABLE_NAME, s, strings);
                break;
            case INVENTORY_ID:
                s = InventoryEntry._ID + " = ?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rs = sqLiteDatabase.delete(InventoryEntry.TABLE_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        //notify change
        if (rs != 0) getContext().getContentResolver().notifyChange(uri, null);
        return rs;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = sURI_MATCHER.match(uri);
        switch (match) {
            case INVENTORIES:
                return InventoryEntry.CONTENT_LIST_TYPE;
            case INVENTORY_ID:
                return InventoryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int match = sURI_MATCHER.match(uri);
        switch (match) {
            case INVENTORIES:
                return updateInventory(uri, contentValues, s, strings);
            case INVENTORY_ID:
                s = InventoryEntry._ID + " = ?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateInventory(uri, contentValues, s, strings);
            default:
                throw new IllegalArgumentException("Updating is not supported for " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match = sURI_MATCHER.match(uri);
        switch (match) {
            case INVENTORIES:
                return insertInventory(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertInventory(Uri uri, ContentValues contentValues) {
        validateData(contentValues);
        SQLiteDatabase sqLiteDatabase = inventoryHelper.getWritableDatabase();
        long id = sqLiteDatabase.insert(InventoryEntry.TABLE_NAME, null, contentValues);
        //notify change
        if (id != -1) getContext().getContentResolver().notifyChange(uri, null);
        //return a new URI with the id of new inserted row
        return ContentUris.withAppendedId(uri, id);
    }

    private int updateInventory(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        validateData(contentValues);
        //check if content values size is 0
        if (contentValues.size() == 0) return 0;
        SQLiteDatabase sqLiteDatabase = inventoryHelper.getWritableDatabase();
        int row = sqLiteDatabase.update(InventoryEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        //notify change
        if (row != 0) getContext().getContentResolver().notifyChange(uri, null);
        return row;
    }

    private void validateData(ContentValues contentValues) {
        /**
         * Check if the {@link InventoryEntry#COLUMN_NAME} is present
         * Check if name is not null
         */
        if (contentValues.containsKey(InventoryEntry.COLUMN_NAME)) {
            if (contentValues.getAsString(InventoryEntry.COLUMN_NAME) == null) {
                throw new IllegalArgumentException("Inventory requires a name");
            }
        }
        /**
         * Check if the {@link InventoryEntry#COLUMN_PRICE} is present
         * Check if price is not null
         */
        if (contentValues.containsKey(InventoryEntry.COLUMN_PRICE)) {
            if (contentValues.getAsString(InventoryEntry.COLUMN_PRICE) == null) {
                throw new IllegalArgumentException("Inventory requires a price");
            }
        }
        /**
         * Check if the {@link InventoryEntry#COLUMN_QUANTITY} is present
         * Check if quantity is not null
         */
        if (contentValues.containsKey(InventoryEntry.COLUMN_QUANTITY)) {
            if (contentValues.getAsString(InventoryEntry.COLUMN_QUANTITY) == null) {
                throw new IllegalArgumentException("Inventory requires a quantity");
            }
        }
        /**
         * Check if the {@link InventoryEntry#COLUMN_PICTURE} is present
         * Check if picture source is not null
         */
        if (contentValues.containsKey(InventoryEntry.COLUMN_PICTURE)) {
            if (contentValues.getAsString(InventoryEntry.COLUMN_PICTURE) == null) {
                throw new IllegalArgumentException("Inventory requires a picture source");
            }
        }
    }
}
