package com.rizal.lovins.smartkasir.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.rizal.lovins.smartkasir.R;
import com.rizal.lovins.smartkasir.activity.MainActivity;
import com.rizal.lovins.smartkasir.adapter.ProductDataAdapter;
import com.rizal.lovins.smartkasir.model.Product;
import com.rizal.lovins.smartkasir.table.TableProduct;
import com.rizal.lovins.smartkasir.widgets.InputProductScanner;
import com.rizal.lovins.smartkasir.widgets.ProductDialog;

import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;

import static com.rizal.lovins.smartkasir.activity.MainActivity.dataBalanjaan;

@SuppressWarnings("unchecked")
public class ProductFragment extends Fragment {
    FloatingActionButton fab_addbtn;
    CoordinatorLayout coordinatorLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        fab_addbtn = (FloatingActionButton) view.findViewById(R.id.fab_addproduct);
        TableProduct tableView = (TableProduct) view.findViewById(R.id.tableView);
        tableView.setDataAdapter(MainActivity.dataProduk);
        tableView.addDataClickListener(new DataClickListener());
        tableView.addDataLongClickListener(new DataLongClickListener());
        fab_addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {
                new BottomSheet.Builder(getActivity())
                        .setSheet(R.menu.input_mode_menu)
                        .setListener(new BottomSheetListener() {

                            @Override
                            public void onSheetShown(@NonNull BottomSheet p1) {
                                // TODO: Implement this method
                            }

                            @Override
                            public void onSheetItemSelected(@NonNull BottomSheet p1, MenuItem p2) {
                                switch (p2.getItemId()) {
                                    case R.id.tambahsatuan:
                                        new ProductDialog(getActivity(), null);
                                        break;
                                    case R.id.tambahbanyak:
                                        new InputProductScanner(getActivity()).tambahkanProduk();
                                        break;
                                }
                            }

                            @Override
                            public void onSheetDismissed(@NonNull BottomSheet p1, int p2) {
                                // TODO: Implement this method
                            }
                        }).show();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayoutProduk);
    }


    private class DataClickListener implements TableDataClickListener<Product> {
        @Override
        public void onDataClicked(int rowIndex, final Product clickedData) {
            new BottomSheet.Builder(getActivity())
                    .setSheet(R.menu.popup_menu)
                    .setTitle(clickedData.getNama() + " - (Rp. " + ProductDataAdapter.PRICE_FORMATTER.format(clickedData.getHarga()) + ")")
                    .setListener(new BottomSheetListener() {
                        @Override
                        public void onSheetShown(@NonNull BottomSheet p1) {
                            // TODO: Implement this method
                        }

                        @Override
                        public void onSheetItemSelected(@NonNull BottomSheet p1, MenuItem menu) {
                            switch (menu.getItemId()) {
                                case R.id.addtoproduct:
                                    dataBalanjaan.tambah(clickedData, -1);
                                    break;
                                case R.id.menuedit:
                                    new ProductDialog(getActivity(), clickedData);
                                    break;
                                case R.id.menudelete:
                                    Snackbar.make(coordinatorLayout, "Tekan Hapus untuk mengkonfirmasi", Snackbar.LENGTH_LONG)
                                            .setAction("Hapus", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View p1) {
                                                    MainActivity.dataProduk.hapus(clickedData);
                                                    Toast.makeText(getActivity(), "Terhapus", Toast.LENGTH_SHORT).show();
                                                }
                                            }).show();
                                    break;
                            }
                        }

                        @Override
                        public void onSheetDismissed(@NonNull BottomSheet p1, int p2) {
                            // TODO: Implement this method
                        }
                    })
                    .show();
        }
    }

    private class DataLongClickListener implements TableDataLongClickListener<Product> {
        @Override
        public boolean onDataLongClicked(int rowIndex, Product clickedData) {
            new ProductDialog(getActivity(), clickedData);
            return true;
        }
    }

}
