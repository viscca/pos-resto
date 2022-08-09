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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MenuAdapter extends ArrayAdapter<NavigationItem> {
    private final int layoutResourceId;

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
        TextView ZX = convertView.findViewById(R.id.txtTitle);
        ZX.setText(getJudul(menu.Data));
        ImageView Gmb= convertView.findViewById(R.id.imgIcon);
        String url= menu.getKey("photo");
        Picasso.get().load(url).resize(100, 82).
                centerCrop().error(menu.getDrawable()).into(Gmb);
        return convertView;
    }

    private String getJudul(JSONObject dt){
        String Hsl="";
        try {
            Hsl= dt.getString("name");
            JSONObject Hrg= dt.getJSONObject("sale_delivery");
            switch(dt.getInt("JnsHrg")) {
                case 0:
                    Hsl += "\r\nRp." + Hrg.getString("dine_in");
                    break;
            }
        } catch (JSONException e) {}
        return Hsl;
    }
}