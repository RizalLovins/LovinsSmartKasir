package com.rizal.lovins.smartkasir.widgets;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rizal.lovins.smartkasir.activity.MainActivity;
import com.rizal.lovins.smartkasir.R;
import com.rizal.lovins.smartkasir.model.Product;

public class ProductDialog {
    @SuppressLint("SetTextI18n")
    public ProductDialog(final Context context, final Product dataSet) {
        String positiveTxt = "Tambahkan";
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams") View form = LayoutInflater.from(context).inflate(R.layout.product_dialog, null);
        final TextView nama = (TextView) form.findViewById(R.id.namaproduk);
        final TextView kodeProduk = (TextView) form.findViewById(R.id.kodeproduk);
        final TextView harga = (TextView) form.findViewById(R.id.harga);
        final TextView stok = (TextView) form.findViewById(R.id.stok);
        // Jika dataSet tidak null yang berarti itu adalah mode PEMBARUAN/EDIT
        // Maka kolom akan di isi, serta ditambabkan tombol Neutral (Hapus)
        if (dataSet != null) {
            nama.setText(dataSet.getNama());
            kodeProduk.setText(dataSet.getSn());
            harga.setText("" + dataSet.getHarga());
            stok.setText("" + dataSet.getStok());
            positiveTxt = "Perbarui";
            builder.setNeutralButton("Hapus", null);
        }
        builder.setView(form);
        builder.setTitle(positiveTxt + "Produk");
        builder.setPositiveButton(positiveTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface p1, int p2) {
                ContentValues data = new ContentValues();
                data.put("nama", nama.getText().toString());
                data.put("sn", kodeProduk.getText().toString());
                data.put("harga", Long.parseLong(harga.getText().toString()));
                data.put("stok", Integer.parseInt(stok.getText().toString()));
                // Jika mode penambahan
                if (dataSet == null) {
                    MainActivity.dataProduk.tambah(data);
                    // Jika mode EDIT
                } else {
                    MainActivity.dataProduk.perbarui(dataSet, data);
                }
            }
        });
        builder.setNegativeButton("Batal", null);
        final AlertDialog dialog = builder.create();
        dialog.show();
        // Override tombol Hapus
        @SuppressWarnings("deprecation") Button hapusBtn = dialog.getButton(AlertDialog.BUTTON3);
        hapusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {
                Toast.makeText(p1.getContext(), "Tekan lama untuk menghapus", Toast.LENGTH_LONG).show();
            }
        });
        hapusBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View p1) {
                MainActivity.dataProduk.hapus(dataSet);
                dialog.dismiss();
                Toast.makeText(p1.getContext(), "Terhapus", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // Dibawah ini adalah fungsi agar Button OK di disable jika data belum terisi atau jumlahnya kurang dari kriteria yang ditentukan
        // Gunakan textWatcher disetiap kolom
        @SuppressWarnings("deprecation") final Button btn = dialog.getButton(AlertDialog.BUTTON1);
        if (dataSet == null) btn.setEnabled(false);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
            }

            @Override
            public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                if (nama.getText().length() > 3 && kodeProduk.getText().length() > 5 && harga.getText().length() > 2)
                    btn.setEnabled(true);
                else btn.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable p1) {
            }
        };
        nama.addTextChangedListener(watcher);
        kodeProduk.addTextChangedListener(watcher);
        harga.addTextChangedListener(watcher);
    }

}
