package com.example.dungkunit.inventorymanagement.Model;

import android.content.ContentValues;

/**
 * Created by dungkunit on 12/01/2017.
 */

public class Inventory {
    private int id;
    private String imgSrc, title, price, quantity;

    public Inventory(String imgSrc, String title, String price, String quantity) {
        this.imgSrc = imgSrc;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public Inventory(int id, String imgSrc, String title, String price, String quantity) {
        this.id = id;
        this.imgSrc = imgSrc;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public static ContentValues getContentValues(Inventory inventory) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_NAME, inventory.getTitle());
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_PRICE, inventory.getPrice());
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, inventory.getQuantity());
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_PICTURE, inventory.getImgSrc());
        return contentValues;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", imgSrc='" + imgSrc + '\'' +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
