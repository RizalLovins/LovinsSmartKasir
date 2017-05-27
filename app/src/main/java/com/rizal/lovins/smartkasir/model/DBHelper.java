package com.rizal.lovins.smartkasir.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static String DATABASE_NAME = "produk";
    private Cursor cursor;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME + ".db", null, 2);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_NAME + " (sn TEXT null,nama TEXT null,harga INTEGER null, stok INTEGER null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int p2, int p3) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
    }

    public void tambah(ContentValues contentValues) {
        db.insert(DATABASE_NAME, null, contentValues);
    }

    public void update(ContentValues val, String sn) {
        db.update(DATABASE_NAME, val, "sn=" + sn, null);
    }

    public void delete(String sn) {
        db.delete(DATABASE_NAME, "sn=" + sn, null);
    }

    Cursor semuaData() {
        cursor = db.rawQuery("SELECT * FROM " + DATABASE_NAME, null);
        return cursor;
    }

    Cursor baca(String sn) {
        cursor = db.rawQuery("SELECT * FROM " + DATABASE_NAME + " WHERE sn=" + sn, null);
        return cursor;
    }
}
