package com.rizal.lovins.smartkasir.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rizal.lovins.smartkasir.activity.MainActivity;
import com.rizal.lovins.smartkasir.R;
import com.rizal.lovins.smartkasir.adapter.GroceriesDataAdapter;
import com.rizal.lovins.smartkasir.model.Groceries;
import com.rizal.lovins.smartkasir.table.TableGroceries;
import com.rizal.lovins.smartkasir.widgets.ShopDialog;
import com.rizal.lovins.smartkasir.widgets.InputProductScanner;

import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;

@SuppressWarnings("unchecked")
public class ShopFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    public static TextView totalJumlah;
    public static BottomSheetBehavior bottomSheetBehavior;
    CoordinatorLayout coordinatorLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        TableGroceries tableView = (TableGroceries) view.findViewById(R.id.groceries);
        tableView.setDataAdapter(MainActivity.dataBalanjaan);
        tableView.addDataClickListener(new DataClickListener());
        tableView.addDataLongClickListener(new DataLongClickListener());
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayoutBelanja);
        FloatingActionButton fabshop = (FloatingActionButton) view.findViewById(R.id.fab_shopping);
        totalJumlah = (TextView) view.findViewById(R.id.totaljumlah);
        bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottomSheet));

        fabshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {
                new InputProductScanner(p1.getContext()).shoping();
            }
        });

        if (GroceriesDataAdapter.totalBelanja != 0) {
            totalJumlah.setText("Rp. " + GroceriesDataAdapter.PRICE_FORMATTER.format(GroceriesDataAdapter.totalBelanja));
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

    }

    private class DataClickListener implements TableDataClickListener<Groceries> {
        @Override
        public void onDataClicked(int p1, Groceries groceries) {
            //new InputProductScanner(getActivity()).shoping();
            new ShopDialog(getActivity(), groceries, totalJumlah);
        }
    }

    private class DataLongClickListener implements TableDataLongClickListener<Groceries> {
        @Override
        public boolean onDataLongClicked(int rowIndex, Groceries groceries) {
            //showdlg(groceries.getBelanjaan().getNama(), groceries.getBelanjaan().getHarga(), groceries.getJumlah());
            return true;
        }
    }
}
