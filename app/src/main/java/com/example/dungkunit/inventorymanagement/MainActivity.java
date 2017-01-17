package com.example.dungkunit.inventorymanagement;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dungkunit.inventorymanagement.model.InventoryAdapter;
import com.example.dungkunit.inventorymanagement.model.InventoryContract;

import static com.example.dungkunit.inventorymanagement.model.InventoryContract.InventoryEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int MAIN_LOADER = 0;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ListView listView;
    private InventoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_view);

        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        adapter = new InventoryAdapter(this, null);
        listView.setAdapter(adapter);

        //Init Loader
        getSupportLoaderManager().initLoader(MAIN_LOADER, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, NewItemActivity.class);
                intent.setData(ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, id));
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.button_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewItemActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_delete_all) {
            int row = getContentResolver().delete(InventoryEntry.CONTENT_URI, null, null);
            if (row == 0) {
                Toast.makeText(this, getString(R.string.delete_all_text_err), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_all_text_ok, row), Toast.LENGTH_SHORT).show();
            }
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
}
