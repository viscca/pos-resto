package com.pos.restokasir.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pos.restokasir.R;
import com.pos.restokasir.tools.NavigationItem;

import java.util.Objects;

public class CustomerAdapter extends ArrayAdapter<NavigationItem> {
    private final int layoutResourceId;

    public CustomerAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(this.layoutResourceId,parent, false);

        NavigationItem menu = getItem(position);
        TextView nama = convertView.findViewById(R.id.txtNama);
        nama.setText(menu.getKey("name"));
        TextView phone = convertView.findViewById(R.id.txtHP);
        if (Objects.equals(menu.getKey("phone"), "null")) {
            phone.setVisibility(View.GONE);
        } else {
            Log.i("prs_phone", menu.getKey("phone"));
            phone.setText(menu.getKey("phone"));
        }
        TextView email = convertView.findViewById(R.id.txtEmail);
        email.setText(menu.getKey("email"));
        ((ImageView)convertView.findViewById(R.id.imgIcon)).setImageDrawable(menu.getDrawable());

        return convertView;
    }
}