package com.rizal.lovins.smartkasir.widgets;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.rizal.lovins.smartkasir.R;
import com.rizal.lovins.smartkasir.activity.MainActivity;
import com.rizal.lovins.smartkasir.adapter.GroceriesDataAdapter;
import com.rizal.lovins.smartkasir.fragment.ShopFragment;
import com.rizal.lovins.smartkasir.model.Product;

import java.util.ArrayList;
import java.util.List;

import static com.google.zxing.BarcodeFormat.EAN_13;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class InputProductScanner {
    private Context context;
    private DecoratedBarcodeView barcodeView;
    private InputMethodManager inputMethodManager;
    private Product product_terindentifikasi;
    private AlertDialog.Builder builder;
    private boolean flashlight;

    public InputProductScanner(Context context) {
        this.context = context;
        this.inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        builder = new AlertDialog.Builder(context);
        flashlight = false;
        product_terindentifikasi = null;
    }

    private void setupScanner() {
        barcodeView.setStatusText("Scanning...");
        final ArrayList<BarcodeFormat> formatList = new ArrayList<>();
        formatList.add(EAN_13);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory());
        // Toggle flashlight saat viewfinder barcode disentuh
        barcodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {
                if (flashlight) barcodeView.setTorchOff();
                else barcodeView.setTorchOn();
            }
        });
        barcodeView.setTorchListener(new DecoratedBarcodeView.TorchListener() {

            @Override
            public void onTorchOn() {
                flashlight = true;
            }

            @Override
            public void onTorchOff() {
                flashlight = false;
            }
        });
    }

    public void shoping() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.shopping_scanner, null);
        final TextView namaProduk = (TextView) view.findViewById(R.id.scanNamaProduk);
        final TextView hargaProduk = (TextView) view.findViewById(R.id.scanHargaProduk);
        final TextView snProduk = (TextView) view.findViewById(R.id.scanSN);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.autoaddcheck);
        barcodeView = (DecoratedBarcodeView) view.findViewById(R.id.scannershop);
        setupScanner();
        builder.setView(view);
        builder.setTitle("Pemindai Barcode");
        builder.setCancelable(false);
        builder.setNegativeButton("Selesai", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface p1, int p2) {
                barcodeView.pause();
            }
        });
        // ini hanya buat nampilin tombol "Tambahkan" pada builder
        // onclick sengaja ga di isi
        // Meng-override onClick (lihat okBtn) agar saat di klik builder kaga ngilang
        builder.setPositiveButton("Tambahkan", null);
        AlertDialog dialog = this.builder.show();
        // tombol positive (Tambahkan)
        @SuppressWarnings("deprecation") final Button okBtn = dialog.getButton(AlertDialog.BUTTON1);
        okBtn.setEnabled(false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton p1, boolean checked) {
                if (checked) okBtn.setEnabled(false);
                else okBtn.setEnabled(true);
            }
        });
        // Override onClick positiveButton pada builder,
        okBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View p1) {
                MainActivity.dataBalanjaan.tambah(product_terindentifikasi, -1);
                // Update total Jumlah di BottomSheet
                ShopFragment.totalJumlah.setText("Rp. " + GroceriesDataAdapter.PRICE_FORMATTER.format(GroceriesDataAdapter.totalBelanja));
            }
        });

        barcodeView.decodeContinuous(new BarcodeCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void barcodeResult(BarcodeResult result) {
                product_terindentifikasi = Product.getBySN(context, result.getText());
                // Jika fragment_product kedaftar di Database fragment_product
                // Kalo tidak, maka tidak melakukan apa2/diam
                if (product_terindentifikasi != null) {
                    namaProduk.setText(product_terindentifikasi.getNama());
                    hargaProduk.setText("Rp. " + GroceriesDataAdapter.PRICE_FORMATTER.format(product_terindentifikasi.getHarga()));
                    snProduk.setText(result.getText());
                    namaProduk.setVisibility(View.VISIBLE);
                    hargaProduk.setVisibility(View.VISIBLE);
                    // Jika mode otomatis (tanpa konfirm) di cek
                    if (checkBox.isChecked()) {
                        okBtn.setEnabled(false);
                        MainActivity.dataBalanjaan.tambah(product_terindentifikasi, -1);
                        // Update totalBelanja jumlah di BottomSheet
                        ShopFragment.totalJumlah.setText("Rp. " + GroceriesDataAdapter.PRICE_FORMATTER.format(GroceriesDataAdapter.totalBelanja));
                        // Pause dulu kamera jika sudah berhasil mengidentifikasi fragment_product
                        // setelah 2dtk baru di resume
                        // ini utk menghindari scan beruntun, dlm waktu 2dtk jauhkan barcode dari kamera atau aplikasi akan mengupdate status jumlah-nya
                        barcodeView.pause();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                barcodeView.resume();
                            }
                        }, 2000);
                    } else {
                        okBtn.setEnabled(true);
                    }
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> p1) {
            }
        });
        barcodeView.resume();
    }

    public void tambahkanProduk() {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(context).inflate(R.layout.input_product_scanner, null);
        final EditText namaproduk = (EditText) v.findViewById(R.id.new_namaproduk);
        final EditText hargaproduk = (EditText) v.findViewById(R.id.new_hargaproduk);
        final EditText stokproduk = (EditText) v.findViewById(R.id.new_stok);
        final Button tambahkanbtn = (Button) v.findViewById(R.id.tambahkanbtn);
        final Button cancelbtn = (Button) v.findViewById(R.id.cancelbtn);
        final Button selesaibtn = (Button) v.findViewById(R.id.selesaibtn);
        namaproduk.setEnabled(false);
        hargaproduk.setEnabled(false);
        stokproduk.setEnabled(false);
        final BarcodeCallback callback = new BarcodeCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result.getText() != null) {
                    namaproduk.setEnabled(true);
                    hargaproduk.setEnabled(true);
                    stokproduk.setEnabled(true);
                    namaproduk.requestFocus();
                    product_terindentifikasi = Product.getBySN(context, result.getText());
                    if (product_terindentifikasi != null) {
                        tambahkanbtn.setText("Perbarui");
                        namaproduk.setText(product_terindentifikasi.getNama());
                        hargaproduk.setText("" + product_terindentifikasi.getHarga());
                        stokproduk.setText("" + product_terindentifikasi.getStok());
                    } else {
                        tambahkanbtn.setText("Tambahkan");
                    }
                    inputMethodManager.showSoftInput(namaproduk, InputMethodManager.SHOW_IMPLICIT);
                    barcodeView.setStatusText(result.getText());
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
            }
        };
        barcodeView = (DecoratedBarcodeView) v.findViewById(R.id.scanner);
        setupScanner();
        builder.setView(v);
        builder.setCancelable(false);
        builder.setTitle("Tambahkan Produk");
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface p1) {
                barcodeView.pause();
            }
        });
        final AlertDialog dialog = this.builder.create();
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {
                barcodeView.pause();
                dialog.dismiss();
            }
        });
        selesaibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {
                barcodeView.pause();
                dialog.dismiss();
            }
        });

        tambahkanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {
                ContentValues data = new ContentValues();
                data.put("nama", namaproduk.getText().toString());
                data.put("sn", barcodeView.getStatusView().getText().toString());
                data.put("harga", Long.parseLong(hargaproduk.getText().toString()));
                data.put("stok", Integer.parseInt(stokproduk.getText().toString()));
                if (product_terindentifikasi == null) {
                    MainActivity.dataProduk.tambah(data);
                } else {
                    MainActivity.dataProduk.perbarui(product_terindentifikasi, data);
                }
                inputMethodManager.hideSoftInputFromWindow(namaproduk.getWindowToken(), 0);
                namaproduk.setText("");
                hargaproduk.setText("");
                stokproduk.setText("");
                namaproduk.setEnabled(false);
                hargaproduk.setEnabled(false);
                stokproduk.setEnabled(false);
                barcodeView.setStatusText("Scanning...");
            }
        });
        dialog.show();
        tambahkanbtn.setEnabled(false);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
            }

            @Override
            public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                String s = stokproduk.getText().toString();
                int stok = 0;
                if (s.length() > 0) {
                    stok = Integer.parseInt(s);
                }
                if (namaproduk.getText().length() > 3 && stok > 0 && hargaproduk.getText().length() > 2) {
                    tambahkanbtn.setEnabled(true);
                } else {
                    tambahkanbtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable p1) {
            }
        };
        namaproduk.addTextChangedListener(watcher);
        hargaproduk.addTextChangedListener(watcher);
        stokproduk.addTextChangedListener(watcher);
        barcodeView.decodeContinuous(callback);
        barcodeView.resume();
    }
}
