package com.rizal.lovins.smartkasir.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.rizal.lovins.smartkasir.activity.MainActivity;
import com.rizal.lovins.smartkasir.R;
import com.rizal.lovins.smartkasir.adapter.GroceriesDataAdapter;
import com.rizal.lovins.smartkasir.model.Groceries;

public class ShopDialog {
    @SuppressLint("SetTextI18n")
    public ShopDialog(Context context, final Groceries belanja, final TextView totalBelanja) {
        String nama = belanja.getProduk().getNama();
        final long harga = belanja.getProduk().getHarga();
        final int jumlah = belanja.getJumlah();
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.shop_dialog, null);
        final NumberBar nilai = (NumberBar) view.findViewById(R.id.numr);
        TextView hargaView = (TextView) view.findViewById(R.id.hargaview);
        final TextView totalView = (TextView) view.findViewById(R.id.totalview);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle(nama);
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface p1, int p2) {
                MainActivity.dataBalanjaan.tambah(belanja.getProduk(), nilai.getValue());
                totalBelanja.setText("Rp. " + GroceriesDataAdapter.PRICE_FORMATTER.format(GroceriesDataAdapter.totalBelanja));
            }
        });
//        builder.setNeutralButton("Hapus Belanjaan", null);
        builder.show();
        totalView.setText("Rp. " + GroceriesDataAdapter.PRICE_FORMATTER.format(harga * jumlah));
        hargaView.setText("Rp. " + GroceriesDataAdapter.PRICE_FORMATTER.format(harga));
        nilai.setMinValue(1);
        nilai.setMaxValue(200);
        nilai.setValue(jumlah);
        nilai.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker p1, int p2, int p3) {
                totalView.setText("Rp. " + GroceriesDataAdapter.PRICE_FORMATTER.format(harga * nilai.getValue()));
            }
        });
    }
}
