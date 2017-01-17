package com.example.dungkunit.inventorymanagement;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dungkunit.inventorymanagement.Model.InventoryAdapter;
import com.example.dungkunit.inventorymanagement.Model.InventoryHelper;

import static com.example.dungkunit.inventorymanagement.Model.InventoryContract.InventoryEntry;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_UPDATE = "update";
    private static final String TAG = MainActivity.class.getSimpleName();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_view);
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        SQLiteDatabase sqLiteDatabase = new InventoryHelper(this).getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(InventoryEntry.TABLE_NAME, null, null, null, null, null, null);

        listView.setAdapter(new InventoryAdapter(this, cursor));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, NewItemActivity.class);
                intent.putExtra(KEY_UPDATE, id);
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
            Log.i(TAG, "option delete all");
        }
        return super.onOptionsItemSelected(item);
    }
}
