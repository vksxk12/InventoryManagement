package com.example.dungkunit.inventorymanagement.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.dungkunit.inventorymanagement.Model.InventoryContract.InventoryEntry;

/**
 * Created by dungkunit on 09/01/2017.
 */

public class InventoryHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "InventoryMng.db";
    private static final int DB_VERSION = 1;
    private final String TAG = InventoryHelper.class.getSimpleName();
    private final String CREATE_TABLE = "CREATE TABLE " + InventoryEntry.TABLE_NAME + " ( "
            + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + InventoryEntry.COLUMN_NAME + " TEXT NOT NULL, "
            + InventoryEntry.COLUMN_PRICE + " REAL NOT NULL DEFAULT 0, "
            + InventoryEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
            + InventoryEntry.COLUMN_PICTURE + " TEXT NOT NULl );";
    private final String DROP_TABLE = "DROP TABLE IF EXISTS " + InventoryEntry.TABLE_NAME;

    public InventoryHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e(TAG, "Creating table");
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.e(TAG, "Updating table");
        sqLiteDatabase.execSQL(DROP_TABLE);
    }
}
