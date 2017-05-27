package com.rizal.lovins.smartkasir.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rizal.lovins.smartkasir.model.DBHelper;
import com.rizal.lovins.smartkasir.model.Product;

import java.text.NumberFormat;
import java.util.ArrayList;

import de.codecrafters.tableview.TableDataAdapter;

@SuppressWarnings("unchecked")
public class ProductDataAdapter extends TableDataAdapter {
    public static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
    private ArrayList<Product> product = (ArrayList<Product>) getData();

    public ProductDataAdapter(Context context, ArrayList<Product> produk) {
        super(context, produk);
    }

    @Override
    public View getCellView(int row, int column, ViewGroup p3) {
        Product product = (Product) getRowData(row);
        View render = null;
        switch (column) {
            case 0:
                render = renderString(product.getNama());
                break;
            case 1:
                render = renderString("Rp. " + PRICE_FORMATTER.format(product.getHarga()));
                break;
            case 2:
                render = renderString("" + product.getStok());
                break;
        }
        return render;
    }

    private ArrayList<Product> getBarang() {
        return product;
    }

    private int getPost(Product produk) {
        int post = -1;
        for (Product barang : getBarang()) {
            if (barang.getSn().equals(produk.getSn())) {
                post = getBarang().indexOf(barang);
                break;
            }
        }
        return post;
    }

    public void tambah(ContentValues contentValues) {
        getBarang().add(new Product(contentValues.getAsString("nama"), contentValues.getAsString("sn"), contentValues.getAsLong("harga"), contentValues.getAsInteger("stok")));
        new DBHelper(getContext()).tambah(contentValues);
        notifyDataSetChanged();
    }

    public void hapus(Product product) {
        getBarang().remove(product);
        new DBHelper(getContext()).delete(product.getSn());
        notifyDataSetChanged();
    }

    public void perbarui(Product product, ContentValues contentValues) {
        int idx = getPost(product);
        //noinspection StatementWithEmptyBody
        if (idx >= 0) {
        } else {
            return;
        }
        getBarang().set(idx, new Product(contentValues.getAsString("nama"), contentValues.getAsString("sn"), contentValues.getAsLong("harga"), contentValues.getAsInteger("stok")));
        new DBHelper(getContext()).update(contentValues, product.getSn());
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
