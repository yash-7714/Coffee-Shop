package com.example.coffeeshop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class OrderDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CoffeeShop.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_ORDERS = "orders";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "customerName";
    private static final String COLUMN_ADDRESS = "customerAddress";
    private static final String COLUMN_PHONE = "customerPhone";
    private static final String COLUMN_TOTAL = "total";
    private static final String COLUMN_STATUS = "status";

    public OrderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_ORDERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_ADDRESS + " TEXT, "
                + COLUMN_PHONE + " TEXT, "
                + COLUMN_TOTAL + " TEXT, "
                + COLUMN_STATUS + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    public void insertOrder(String name, String address, String phone, String total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_TOTAL, total);
        values.put(COLUMN_STATUS, "Pending");
        db.insert(TABLE_ORDERS, null, values);
        db.close();
    }

    public ArrayList<String> getAllOrders() {
        ArrayList<String> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ORDERS, null);

        if (cursor.moveToFirst()) {
            do {
                String order = "Order #" + cursor.getInt(0) +
                        "\nName: " + cursor.getString(1) +
                        "\nAddress: " + cursor.getString(2) +
                        "\nPhone: " + cursor.getString(3) +
                        "\nTotal: ₹" + cursor.getString(4) +
                        "\nStatus: " + cursor.getString(5);
                orders.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orders;
    }

    public void deleteOrder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDERS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void markOrderCompleted(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, "Completed");
        db.update(TABLE_ORDERS, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
