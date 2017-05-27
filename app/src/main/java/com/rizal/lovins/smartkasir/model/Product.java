package com.rizal.lovins.smartkasir.model;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class Product {
    private String nama;
    private String sn;
    protected long harga;
    private int stok;

    public Product(String nama, String sn, long harga, int stok) {
        this.nama = nama;
        this.sn = sn;
        this.harga = harga;
        this.stok = stok;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String getNama() {
        return nama;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSn() {
        return sn;
    }

    public void setHarga(long harga) {
        this.harga = harga;
    }

    public long getHarga() {
        return harga;
    }

    public static Product getBySN(Context context, String SN) {
        Cursor cursor = new DBHelper(context).baca(SN);
        if (!cursor.moveToFirst()) return null;
        if (cursor.getCount() < 1) {
            return null;
        }
        String nama = cursor.getString(cursor.getColumnIndex("nama"));
        String sn = cursor.getString(cursor.getColumnIndex("sn"));
        long harga = cursor.getLong(cursor.getColumnIndex("harga"));
        int stok = cursor.getInt(cursor.getColumnIndex("stok"));
        return new Product(nama, sn, harga, stok);
    }

    public static ArrayList<Product> getInit(Context context) {
        ArrayList<Product> produk = new ArrayList<>();
        Cursor cursor = new DBHelper(context).semuaData();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            String nama = cursor.getString(cursor.getColumnIndex("nama"));
            String sn = cursor.getString(cursor.getColumnIndex("sn"));
            long harga = cursor.getLong(cursor.getColumnIndex("harga"));
            int stok = cursor.getInt(cursor.getColumnIndex("stok"));
            produk.add(new Product(nama, sn, harga, stok));
        }
        return produk;
    }

}
