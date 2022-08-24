package com.pos.restokasir.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pos.restokasir.R;
import com.pos.restokasir.tools.NavigationItem;

public class HistoryAdapter extends ArrayAdapter<NavigationItem> {
    private final int layoutResourceId;

    public HistoryAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(this.layoutResourceId,parent, false);

        NavigationItem menu = getItem(position);
        TextView tgl = convertView.findViewById(R.id.txtTgl);
        tgl.setText(menu.getKey("nama"));
        tgl = convertView.findViewById(R.id.txtMenu);
        tgl.setText(menu.getKey("hp"));
        tgl = convertView.findViewById(R.id.txtTotal);
        tgl.setText(menu.getKey("Email"));
        ((ImageView)convertView.findViewById(R.id.imgIcon)).setImageDrawable(menu.getDrawable());

        return convertView;
    }
}
