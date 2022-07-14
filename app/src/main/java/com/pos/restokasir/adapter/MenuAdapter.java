package com.pos.restokasir.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pos.restokasir.tools.NavigationItem;
import com.pos.restokasir.R;

public class MenuAdapter extends ArrayAdapter<NavigationItem> {
    private final int layoutResourceId;
    private String ItemDipilih;
    private final boolean dgGmbr;

    public MenuAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        this.dgGmbr=true;
        this.ItemDipilih="";
        this.layoutResourceId = layoutResourceId;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(this.layoutResourceId,parent, false);

        NavigationItem menu = getItem(position);
        TextView ZX=(TextView) convertView.findViewById(R.id.txtTitle);
        ZX.setText(menu.getText());
        if(dgGmbr)
            ((ImageView)convertView.findViewById(R.id.imgIcon)).setImageDrawable(menu.getDrawable());

        return convertView;
    }
}