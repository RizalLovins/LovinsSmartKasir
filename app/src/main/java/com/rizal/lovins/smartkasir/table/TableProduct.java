package com.rizal.lovins.smartkasir.table;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.rizal.lovins.smartkasir.R;
import com.rizal.lovins.smartkasir.model.Product;

import java.util.Comparator;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;

public class TableProduct extends SortableTableView<Product> {
    public TableProduct(Context context) {
        super(context, null);
    }

    public TableProduct(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public TableProduct(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);
        setColumnCount(3);
        SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(context, "Produk", "Harga", "Stok");
        simpleTableHeaderAdapter.setTextColor(ContextCompat.getColor(context, R.color.table_header_text));
        final int rowColorEven = ContextCompat.getColor(context, R.color.table_data_row_even);
        final int rowColorOdd = ContextCompat.getColor(context, R.color.table_data_row_odd);
        setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(rowColorEven, rowColorOdd));
        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());
        setHeaderAdapter(simpleTableHeaderAdapter);
        setColumnComparator(1, new HargaProdukComparator());
        setColumnComparator(0, new NamaProdukComparator());
        setColumnComparator(2, new StokProdukComparator());
    }

    private static class HargaProdukComparator implements Comparator<Product> {
        @Override
        public int compare(Product prod1, Product prod2) {
            if (prod1.getHarga() < prod2.getHarga()) return -1;
            if (prod1.getHarga() > prod2.getHarga()) return 1;
            return 0;
        }
    }

    private static class StokProdukComparator implements Comparator<Product> {
        @Override
        public int compare(Product prod1, Product prod2) {
            if (prod1.getStok() < prod2.getStok()) return -1;
            if (prod1.getStok() > prod2.getStok()) return 1;
            return 0;
        }
    }

    private static class NamaProdukComparator implements Comparator<Product> {
        @Override
        public int compare(final Product prod1, final Product prod2) {
            return prod1.getNama().compareTo(prod2.getNama());
        }
    }
}
