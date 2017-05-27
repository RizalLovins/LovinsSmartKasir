package com.rizal.lovins.smartkasir.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rizal.lovins.smartkasir.model.Groceries;
import com.rizal.lovins.smartkasir.model.Product;

import java.text.NumberFormat;
import java.util.ArrayList;

import de.codecrafters.tableview.TableDataAdapter;

@SuppressWarnings({"unchecked", "WeakerAccess"})
public class GroceriesDataAdapter extends TableDataAdapter {
    public static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
    public static long totalBelanja = 0;
    public static int sisaStok = 0;
    private ArrayList<Product> product = (ArrayList<Product>) getData();
    private ArrayList<Groceries> groceries = (ArrayList<Groceries>) getData();

    public GroceriesDataAdapter(Context context) {
        super(context, new ArrayList<Groceries>());
    }

    @Override
    public View getCellView(int row, int column, ViewGroup p3) {
        Groceries groceries = (Groceries) getRowData(row);
        Product product = groceries.getProduk();
        View render = null;
        switch (column) {
            case 0:
                render = renderString(product.getNama());
                break;
            case 1:
                render = renderString("Rp. " + PRICE_FORMATTER.format(product.getHarga()));
                break;
            case 2:
                render = renderString("" + groceries.getJumlah());
                break;
        }
        return render;
    }

    private ArrayList<Groceries> getBelanjaan() {
        return groceries;
    }

    private ArrayList<Product> getBarang() {
        return product;
    }

    // Fungsi get Groceries melalui SN
    private Groceries getBelanjaBySN(String sn) {
        Groceries belanja = null;
        for (Groceries groceries : getBelanjaan()) {
            if (groceries.getProduk().getSn().equals(sn)) {
                belanja = groceries;
            }
        }
        return belanja;
    }

    private void updateTotalBelanja() {
        long jumlah = 0;
        for (Groceries groceries : getBelanjaan()) {
            jumlah = jumlah + (groceries.getProduk().getHarga() * groceries.getJumlah());
        }
        totalBelanja = jumlah;
    }

    private void updateStok() {
        int stok = 0;
        for (Groceries groceries : getBelanjaan()) {
            if (groceries.getJumlah() >= 0) {
                stok = stok + (groceries.getProduk().getStok() - groceries.getJumlah());
                groceries.getProduk().setStok(stok);
            }
        }
        sisaStok = stok;
    }

    public void tambah(Product produk, int jumlah) {
        Groceries belanja = getBelanjaBySN(produk.getSn());
        // jika produk sudah ada dalam keranjang
        // maka tambahkan jumlah
        if (belanja != null) {
            int prodJumlah = belanja.getJumlah() + 1;
            if (jumlah != -1) prodJumlah = jumlah;
            getBelanjaan().set(getBelanjaan().indexOf(belanja), new Groceries(produk, prodJumlah));
        } else {
            // jika tidak ada dalam keranjang
            // maka masukan ke keranjang
            if (jumlah == -1) jumlah = 1;
            getBelanjaan().add(new Groceries(produk, jumlah));
        }
        // update totalBelanja belanjaan
        updateTotalBelanja();
        updateStok();
        notifyDataSetChanged();
    }

    public void hapus(Product product) {
        // update totalBelanja belanjaan
        getBarang().remove(product);
        updateTotalBelanja();
        updateStok();
        notifyDataSetChanged();
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(14);
        return textView;
    }
}
