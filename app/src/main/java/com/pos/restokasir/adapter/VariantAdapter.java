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

import androidx.annotation.NonNull;

import com.pos.restokasir.R;
import com.pos.restokasir.tools.NavigationItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class VariantAdapter extends ArrayAdapter<NavigationItem> {
    private final int layoutResourceId;
    private TextView pricenew, priceold;

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
        nama.setText(menu.getKey("name"));
        pricenew = convertView.findViewById(R.id.col2);
        priceold = convertView.findViewById(R.id.col3);
        priceold.setPaintFlags(priceold.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        formatUang(menu.Data);

        return convertView;
    }

    private void formatUang(@NonNull JSONObject dt){
        try {
            Integer Hrg = dt.getInt("price");
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String Hsl = "Rp"+formatter.format(Hrg);
            pricenew.setText(Hsl);
            Hsl="";
            priceold.setText(Hsl);
        } catch (JSONException ignored) {}
    }

}