package com.pos.restokasir.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pos.restokasir.R;
import com.pos.restokasir.tools.NavigationItem;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MenuAdapter extends ArrayAdapter<NavigationItem> {
    private final int layoutResourceId;
    private TextView priceOld, priceNew;

    public MenuAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(this.layoutResourceId,parent, false);

        NavigationItem menu = getItem(position);
        TextView title = convertView.findViewById(R.id.txtTitle);
        title.setText(getJudul(menu.Data));
        priceNew = convertView.findViewById(R.id.txtPriceNew);
        getPriceNew(menu.Data);
        priceOld = convertView.findViewById(R.id.txtPriceOld);
        getPriceOld(menu.Data);
        ImageView Gmb= convertView.findViewById(R.id.imgIcon);
        String url= menu.getKey("photo");
        Picasso.get().load(url).resize(100, 82).
                centerCrop().error(menu.getDrawable()).into(Gmb);
        return convertView;
    }

    private String getJudul(JSONObject dt){
        String Hsl="";
        try {
            Hsl = dt.getString("name");
//            Integer Hrg = dt.getInt("sale_price");
//            JSONObject sd= dt.getJSONObject("sale_delivery");
//            if (dt.getInt("JnsHrg") == 0) {
//                Hrg += sd.getInt("dine_in");
//            }
//            DecimalFormat formatter = new DecimalFormat("#,###,###");
//            Hsl += "\r\nRp"+formatter.format(Hrg);
        } catch (JSONException ignored) {}
        return Hsl;
    }

    private void getPriceNew(JSONObject dt){
        String Hsl;
        try {
            Integer Hrg = dt.getInt("sale_price");
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            Hsl = "Rp"+formatter.format(Hrg);
            priceNew.setText(Hsl);
        } catch (JSONException ignored) {}
    }

    private void getPriceOld(JSONObject dt){
        String Hsl;
        try {
            if (!dt.getString("sale_disc").equals("") || !dt.getString("sale_disc").isEmpty()) {
                Integer Hrg = dt.getInt("sale_oldprice");
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                Hsl = "Rp"+formatter.format(Hrg);
                priceOld.setText(Hsl);
                priceOld.setPaintFlags(priceOld.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                priceOld.setVisibility(View.GONE);
            }
        } catch (JSONException ignored) {}
    }
}