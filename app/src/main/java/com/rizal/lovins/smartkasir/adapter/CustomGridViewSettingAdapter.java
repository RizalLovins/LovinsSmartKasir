package com.rizal.lovins.smartkasir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rizal.lovins.smartkasir.R;

public class CustomGridViewSettingAdapter extends BaseAdapter {
    private Context context;
    private final String[] gridViewString;
    private final int[] gridViewImageId;

    public CustomGridViewSettingAdapter(Context context, String[] gridViewString, int[] gridViewImageId) {
        this.context = context;
        this.gridViewImageId = gridViewImageId;
        this.gridViewString = gridViewString;
    }

    @Override
    public int getCount() {
        return gridViewString.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_grid_view_settings, parent, false);
        } else {
            view = convertView;
        }
        TextView text = (TextView) view.findViewById(R.id.android_gridview_text);
        text.setText(gridViewString[i]);
        ImageView image = (ImageView) view.findViewById(R.id.android_gridview_image);
        image.setImageResource(gridViewImageId[i]);
        return view;
    }
}
