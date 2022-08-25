package com.pos.restokasir.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pos.restokasir.R;
import com.pos.restokasir.tools.NavigationItem;

public class VariantAdapter extends ArrayAdapter<NavigationItem> {
    private final int layoutResourceId;

    public VariantAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(this.layoutResourceId,parent, false);

        NavigationItem menu = getItem(position);
        RadioButton nama = convertView.findViewById(R.id.col1);
        nama.setText(menu.getKey("nama"));
        TextView pricenew = convertView.findViewById(R.id.col2);
        pricenew.setText(menu.getKey("pricenew"));
        TextView priceold = convertView.findViewById(R.id.col3);
        priceold.setPaintFlags(priceold.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        priceold.setText(menu.getKey("priceold"));

        return convertView;
    }
}