package com.example.dungkunit.inventorymanagement;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
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

import com.example.dungkunit.inventorymanagement.Model.Inventory;
import com.example.dungkunit.inventorymanagement.Model.InventoryContract;
import com.example.dungkunit.inventorymanagement.Model.InventoryHelper;

import java.io.File;
import java.util.UUID;

/**
 * Created by dungkunit on 15/01/2017.
 */

public class NewItemActivity extends AppCompatActivity implements View.OnTouchListener {
    private static final String TAG = NewItemActivity.class.getSimpleName();
    private final int CAPTURE_REQUEST_CODE = 100;
    private TextView txtTitle, txtPrice, txtQuantity;
    private ImageView img;
    private ImageButton imageButton;
    private UUID uuid;
    private boolean isTouched = false;
    private boolean isCaptured = false;
    private Uri uri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_new_item);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_ok:
                Log.i(TAG, "option ok");
                checkInput();
                return true;
            case R.id.menu_item_delete:
                Log.i(TAG, "option delete");
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
                SQLiteDatabase sqLiteDatabase = new InventoryHelper(this).getWritableDatabase();
                sqLiteDatabase.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, contentValues);
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
}
