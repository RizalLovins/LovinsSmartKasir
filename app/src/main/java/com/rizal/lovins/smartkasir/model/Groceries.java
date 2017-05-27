package com.rizal.lovins.smartkasir.model;

public class Groceries {
    private Product produk;
    private int jumlah;

    public Groceries(Product produk, int jumlah) {
        this.produk = produk;
        this.jumlah = jumlah;
    }

    public void setProduk(Product produk) {
        this.produk = produk;
    }

    public Product getProduk() {
        return produk;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getJumlah() {
        return jumlah;
    }

}
