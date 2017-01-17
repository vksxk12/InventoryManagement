package com.example.dungkunit.inventorymanagement;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dungkunit.inventorymanagement.model.Inventory;
import com.example.dungkunit.inventorymanagement.model.InventoryWrapper;

import java.io.File;
import java.util.UUID;

import static com.example.dungkunit.inventorymanagement.model.InventoryContract.InventoryEntry;

/**
 * Created by dungkunit on 15/01/2017.
 */

public class NewItemActivity extends AppCompatActivity implements View.OnTouchListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = NewItemActivity.class.getSimpleName();
    private final int CAPTURE_REQUEST_CODE = 100;
    private final int NEW_ITEM_LOADER = 1;
    private TextView txtTitle, txtPrice, txtQuantity;
    private ImageView img;
    private ImageButton imageButton;
    private UUID uuid;
    private boolean isTouched = false;
    private boolean isCaptured = false;
    private Uri uri;
    private Uri updateUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_new_item);
        checkUpdate();
        initComponents();
    }

    private void initComponents() {
        txtTitle = (TextView) findViewById(R.id.new_item_title);
        txtPrice = (TextView) findViewById(R.id.new_item_price);
        txtQuantity = (TextView) findViewById(R.id.new_item_quantity);
        img = (ImageView) findViewById(R.id.new_item_img);
        imageButton = (ImageButton) findViewById(R.id.new_item_camera);
        File file = getPhotoFile();
        PackageManager packageManager = getPackageManager();
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canCapture = file != null && intent.resolveActivity(packageManager) != null;
        if (!canCapture) {
            imageButton.setEnabled(false);
        } else {
            uri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(intent, CAPTURE_REQUEST_CODE);
                }
            });
        }
        txtTitle.setOnTouchListener(this);
        txtPrice.setOnTouchListener(this);
        txtQuantity.setOnTouchListener(this);
    }

    private void checkUpdate() {
        if (getIntent() != null) updateUri = getIntent().getData();
        if (updateUri == null) {
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.update_name));
            getSupportLoaderManager().initLoader(NEW_ITEM_LOADER, null, this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_REQUEST_CODE) {
                switch (resultCode) {
                    case RESULT_OK:
                        Log.i(TAG, "captured");
                        isTouched = true;
                        isCaptured = true;
                        img.setImageURI(uri);
                        break;
                    case RESULT_CANCELED:
                        Log.i(TAG, "cancelled");
                        break;
                    default:
                        Log.i(TAG, "default");
                }
            }
        }
    }

    private File getPhotoFile() {
        File externalFileDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFileDir == null) return null;
        uuid = UUID.randomUUID();
        return new File(externalFileDir, String.valueOf(uuid));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (updateUri == null) {
            MenuItem menuItem = menu.findItem(R.id.menu_item_delete);
            menuItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_ok:
                checkInput();
                return true;
            case R.id.menu_item_delete:
                if (updateUri != null) {
                    int row = getContentResolver().delete(updateUri, null, null);
                    if (row == 0) {
                        Toast.makeText(this, getString(R.string.delete_text_err), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getString(R.string.delete_text_ok), Toast.LENGTH_SHORT).show();
                    }
                }
                finish();
                return true;
            case android.R.id.home:
                if (!isTouched) {
                    NavUtils.navigateUpFromSameTask(this);
                } else {
                    checkUnsaved();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkInput() {
        String title = txtTitle.getText().toString().trim();
        String price = txtPrice.getText().toString().trim();
        String quantity = txtQuantity.getText().toString().trim();
        StringBuilder sb = new StringBuilder();
        if (!isCaptured) sb.append("- Image is empty\n");
        if (TextUtils.isEmpty(title)) sb.append("- Title is empty\n");
        if (TextUtils.isEmpty(price)) sb.append("- Price is empty\n");
        if (TextUtils.isEmpty(quantity)) sb.append("- Quantity is empty");
        if (!TextUtils.isEmpty(sb.toString())) {
            new AlertDialog.Builder(this).setCancelable(false).setPositiveButton("Check it",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).setMessage(sb.toString()).create().show();
        } else {
            if (uri == null) {
                new AlertDialog.Builder(this).setCancelable(false).setPositiveButton("Back to Home",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).setMessage("Your phone has no camera and(or) SD card. Cannot create new item").create().show();
            } else {
                String imgUri = uri.toString();
                Inventory inventory = new Inventory(imgUri, title, price, quantity);
                Log.i(TAG, inventory.toString());
                ContentValues contentValues = Inventory.getContentValues(inventory);
                if (updateUri != null) {
                    int row = getContentResolver().update(updateUri, contentValues, null, null);
                    if (row == 0) {
                        Toast.makeText(this, getString(R.string.update_text_err), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getString(R.string.update_text_ok), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, contentValues);
                    if (newUri == null) {
                        Toast.makeText(this, getString(R.string.insert_text_err), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getString(R.string.insert_text_ok, ContentUris.parseId(newUri)), Toast.LENGTH_SHORT).show();
                    }
                }
                finish();
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        isTouched = true;
        return false;
    }

    private void checkUnsaved() {
        new AlertDialog.Builder(this)
                .setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setMessage("Are you sure you want to discard editing?")
                .create().show();
    }

    @Override
    public void onBackPressed() {
        if (!isTouched) {
            super.onBackPressed();
        } else {
            checkUnsaved();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, updateUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        InventoryWrapper wrapper = new InventoryWrapper(data);
        if (wrapper.getCount() != 0) {
            wrapper.moveToFirst();
            Inventory inventory = wrapper.getInventory();
            uri = Uri.parse(inventory.getImgSrc());
            txtTitle.setText(inventory.getTitle());
            txtPrice.setText(inventory.getPrice());
            txtQuantity.setText(inventory.getQuantity());
            img.setImageURI(uri);
            isCaptured = true;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        txtTitle.setText(null);
        txtPrice.setText(null);
        txtQuantity.setText(null);
        img.setImageResource(R.drawable.ic_empty_shelter);
        isCaptured = false;
    }
}
