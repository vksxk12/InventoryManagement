package com.example.dungkunit.inventorymanagement;

import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.dungkunit.inventorymanagement.model.AdapterUtils;
import com.example.dungkunit.inventorymanagement.model.InventoryAdapter;
import com.example.dungkunit.inventorymanagement.model.InventoryContract;

import static com.example.dungkunit.inventorymanagement.model.InventoryContract.InventoryEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, InventoryAdapter.InventoryCallback, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int MAIN_LOADER = 0;
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private InventoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate");
        View emptyView = findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new InventoryAdapter(this, null);
        adapter.setEmptyView(emptyView);
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int id = (int) viewHolder.itemView.getTag();
                Uri uri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
                getContentResolver().delete(uri, null, null);
            }
        }).attachToRecyclerView(recyclerView);
        //Init Loader
        getSupportLoaderManager().initLoader(MAIN_LOADER, null, this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.button_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewItemActivity.class));
            }
        });

        setUpPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_delete_all) {
            int row = getContentResolver().delete(InventoryEntry.CONTENT_URI, null, null);
            if (row == 0) {
                Toast.makeText(this, getString(R.string.delete_all_text_err), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_all_text_ok, row), Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (id == R.id.menu_item_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, InventoryEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onItemClick(int itemId) {
        Intent intent = new Intent(MainActivity.this, NewItemActivity.class);
        intent.setData(ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, itemId));
        startActivity(intent);
    }

    private void setUpPreferences() {
        SharedPreferences sharedPreferences = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);
        String displayImgKey = getString(R.string.pref_display_image_key);
        boolean displayImgDefault = getResources().getBoolean(R.bool.pref_display_image_default);
        AdapterUtils.setDisplayImage(recyclerView, sharedPreferences.getBoolean(displayImgKey, displayImgDefault));
        String displayLayoutKey = getString(R.string.pref_layout_key);
        String displayLayoutDefault = getString(R.string.pref_array_layout_value_1);
        String valueGetFromPreference = sharedPreferences.getString(displayLayoutKey, displayLayoutDefault);
        AdapterUtils.setDisplayLayout(this, recyclerView, valueGetFromPreference);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String displayImgKey = getString(R.string.pref_display_image_key);
        boolean displayImgDefault = getResources().getBoolean(R.bool.pref_display_image_default);
        String displayLayoutKey = getString(R.string.pref_layout_key);
        String displayLayoutDefault = getString(R.string.pref_array_layout_value_1);
        if (key.equals(displayImgKey)) {
            AdapterUtils.setDisplayImage(recyclerView, sharedPreferences.getBoolean(key, displayImgDefault));
        }
        if (key.equals(displayLayoutKey)) {
            AdapterUtils.setDisplayLayout(this, recyclerView, sharedPreferences.getString(key, displayLayoutDefault));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}
