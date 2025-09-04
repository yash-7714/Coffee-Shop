package com.example.coffeeshop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CoffeeShop.db";
    private static final int DATABASE_VERSION = 2;

    // Users Table
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_USER_NAME = "name";
    private static final String COL_USER_EMAIL = "email";
    private static final String COL_USER_PASSWORD = "password";

    // Admin Table
    private static final String TABLE_ADMIN = "admin";
    private static final String COL_ADMIN_ID = "id";
    private static final String COL_ADMIN_EMAIL = "email";
    private static final String COL_ADMIN_PASSWORD = "password";

    // Categories Table
    private static final String TABLE_CATEGORIES = "categories";
    private static final String COL_CAT_ID = "id";
    private static final String COL_CAT_NAME = "name";

    // Products Table
    private static final String TABLE_PRODUCTS = "products";
    private static final String COL_PROD_ID = "id";
    private static final String COL_PROD_NAME = "name";
    private static final String COL_PROD_PRICE = "price";
    private static final String COL_PROD_CATEGORY = "category";

    // Orders Table
    private static final String TABLE_ORDERS = "orders";
    private static final String COL_ORDER_ID = "id";
    private static final String COL_ORDER_NAME = "customer_name";
    private static final String COL_ORDER_ADDRESS = "customer_address";
    private static final String COL_ORDER_PHONE = "customer_phone";
    private static final String COL_ORDER_TOTAL = "total_price";
    private static final String COL_ORDER_STATUS = "status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users
        db.execSQL("CREATE TABLE " + TABLE_USERS + "(" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_NAME + " TEXT, " +
                COL_USER_EMAIL + " TEXT UNIQUE, " +
                COL_USER_PASSWORD + " TEXT)");

        // Admin
        db.execSQL("CREATE TABLE " + TABLE_ADMIN + "(" +
                COL_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ADMIN_EMAIL + " TEXT UNIQUE, " +
                COL_ADMIN_PASSWORD + " TEXT)");

        // Insert default admin
        db.execSQL("INSERT INTO " + TABLE_ADMIN + " (" + COL_ADMIN_EMAIL + ", " + COL_ADMIN_PASSWORD + ") " +
                "VALUES ('admin@gmail.com', 'admin123')");

        // Categories
        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + "(" +
                COL_CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CAT_NAME + " TEXT)");

        // Products
        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + "(" +
                COL_PROD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PROD_NAME + " TEXT, " +
                COL_PROD_PRICE + " TEXT, " +
                COL_PROD_CATEGORY + " TEXT, " +
                "image_url TEXT)");


        // Orders
        db.execSQL("CREATE TABLE " + TABLE_ORDERS + "(" +
                COL_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ORDER_NAME + " TEXT, " +
                COL_ORDER_ADDRESS + " TEXT, " +
                COL_ORDER_PHONE + " TEXT, " +
                COL_ORDER_TOTAL + " REAL, " +
                COL_ORDER_STATUS + " TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    // ================== USER METHODS ==================
    public boolean registerUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, name);
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                        " WHERE " + COL_USER_EMAIL + "=? AND " + COL_USER_PASSWORD + "=?",
                new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // ================== ADMIN METHODS ==================
    public boolean loginAdmin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ADMIN +
                        " WHERE " + COL_ADMIN_EMAIL + "=? AND " + COL_ADMIN_PASSWORD + "=?",
                new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // ================== CATEGORY METHODS ==================
    public void addCategory(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CAT_NAME, name);
        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    public ArrayList<String> getCategories() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CATEGORIES, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CAT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAT_NAME));
                list.add(id + ". " + name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void deleteCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORIES, COL_CAT_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateCategory(int id, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CAT_NAME, newName);
        db.update(TABLE_CATEGORIES, values, COL_CAT_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ================== PRODUCT METHODS ==================
    public void addProduct(String name, String price, String category, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PROD_NAME, name);
        values.put(COL_PROD_PRICE, price);
        values.put(COL_PROD_CATEGORY, category);
        values.put("image_url", imageUrl);
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    public void updateProduct(int id, String name, String price, String category, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PROD_NAME, name);
        values.put(COL_PROD_PRICE, price);
        values.put(COL_PROD_CATEGORY, category);
        values.put("image_url", imageUrl);
        db.update(TABLE_PRODUCTS, values, COL_PROD_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }


    public ArrayList<String> getProducts() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PROD_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_PROD_NAME));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(COL_PROD_PRICE));
                list.add(id + ". " + name + " - ₹" + price);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COL_PROD_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PROD_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_PROD_NAME));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(COL_PROD_PRICE));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COL_PROD_CATEGORY));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));

                list.add(new Product(id, name, price, category, imageUrl));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // ================== ORDER METHODS ==================
    public long insertOrder(String name, String address, String phone, double total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ORDER_NAME, name);
        values.put(COL_ORDER_ADDRESS, address);
        values.put(COL_ORDER_PHONE, phone);
        values.put(COL_ORDER_TOTAL, total);
        values.put(COL_ORDER_STATUS, "Pending");

        long id = db.insert(TABLE_ORDERS, null, values);
        db.close();
        return id;
    }

    public void updateOrderStatus(long orderId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ORDER_STATUS, status);
        db.update(TABLE_ORDERS, values, COL_ORDER_ID + "=?", new String[]{String.valueOf(orderId)});
        db.close();
    }

    public ArrayList<String> getOrders() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ORDERS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ORDER_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_ORDER_NAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_ORDER_PHONE));
                double total = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_ORDER_TOTAL));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COL_ORDER_STATUS));

                list.add(id + ". " + name + " | " + phone + " | ₹" + total + " | " + status);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
}
