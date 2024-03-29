package com.pos.restokasir.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pos.restokasir.R;
import com.pos.restokasir.tools.NavigationItem;

public class ModifierAdapter extends ArrayAdapter<NavigationItem> {
    private final int layoutResourceId;

    public ModifierAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(this.layoutResourceId,parent, false);

        NavigationItem menu = getItem(position);
        TextView nama = convertView.findViewById(R.id.txtMod);
        nama.setText(menu.getKey("nama"));
        TextView pricenew = convertView.findViewById(R.id.txtPrice);
        pricenew.setText(menu.getKey("price"));

        return convertView;
    }
}