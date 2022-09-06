package com.pos.restokasir.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pos.restokasir.R;
import com.pos.restokasir.Service.ReqApiServices;
import com.pos.restokasir.Service.TerimaResponApi;
import com.pos.restokasir.adapter.MenuAdapter;
import com.pos.restokasir.databinding.FragmentPosBinding;
import com.pos.restokasir.tools.NavigationItem;
import com.pos.restokasir.ui.activity.ListCategoryActivity;
import com.pos.restokasir.ui.activity.ListCustomerActivity;
import com.pos.restokasir.ui.activity.ListDiscountActivity;
import com.pos.restokasir.ui.activity.MainActivity;
import com.pos.restokasir.ui.activity.PayActivity;
import com.pos.restokasir.ui.activity.ProductActivity;
import com.pos.restokasir.ui.dialog.VariantModifierDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class PosFragment extends Fragment {
    private final String TAG="Prs_FragmentPOS";
    private LayoutInflater inflater;
    private FragmentPosBinding binding;
    private TableLayout tableList, tableTotal;
    private Button btnaddcustomer, btnpay, btnproduk, btndiskon, btnkategori;
//    private TextView btnedit, btneditoff;
    private androidx.appcompat.widget.AppCompatButton btnClear;
    private LinearLayout llEdit;
    private EditText eCari;
    private GridView gvMenu;
    private Spinner spinner;
    private SharedPreferences mSettings;
    private VariantModifierDialog dlgVarMod;

    @SuppressLint("UseCompatLoadingForDrawables")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        binding = FragmentPosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();
        View root = binding.getRoot();
        eCari = binding.eCari;
        eCari.setOnKeyListener(TentangTombol);

        TextView textView = root.findViewById(R.id.toolbar_title);
        textView.setText("All Item");

        gvMenu = binding.gvproduk;
        gvMenu.setOnItemClickListener(MenuDipilih);

        spinner = binding.spin1;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.metode, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG,"Pilih Spiner");
                        Cari_Produk();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        tableList = binding.tbllist;
        tableTotal = binding.tbltotal;

        btnaddcustomer = binding.btnadd;
        btnaddcustomer.setOnClickListener(DiKlik);
        btnClear = binding.btnclear;
        btnClear.setOnClickListener(DiKlik);
        btnpay = binding.btnpay;
        btnpay.setOnClickListener(DiKlik);
        llEdit = binding.llEdit;
//        btnedit = root.findViewById(R.id.txtedit);
//        btnedit.setOnClickListener(DiKlik);
//        btneditoff = root.findViewById(R.id.txteditoff);
//        btneditoff.setOnClickListener(DiKlik);
        btnproduk = binding.btnaddproduct;
        btnproduk.setOnClickListener(DiKlik);
        btndiskon = binding.btnadddisc;
        btndiskon.setOnClickListener(DiKlik);
        btnkategori = binding.btnaddcat;
        btnkategori.setOnClickListener(DiKlik);
    }

    @Override
    public void onResume() {
        super.onResume();
        llEdit.setVisibility(View.GONE);
//        btnedit.setVisibility(View.VISIBLE);
//        btneditoff.setVisibility(View.GONE);
        SetNoTrxID();
        LoadChart(false);
    }

    private void SetNoTrxID(){
        mSettings= MainActivity.ObjIni.DB_Setting.mSettings;
        final String Key= "NoTrx";
        if(mSettings.getInt(Key,0)==0){
            SharedPreferences.Editor Ubah = mSettings.edit();
            int I= 1000;
            I= (int) (Math.random() * I + 1);
            Ubah.putInt(Key, I);
            Ubah.apply();
            Log.d(TAG, Key+": "+I);
        }
    }

    @SuppressLint("SetTextI18n")
    private void InsertTotal(@NonNull JSONObject dt){
        tableTotal.removeAllViews();
        TableRow tr = new TableRow(getActivity());
        View v = getLayoutInflater().inflate(R.layout.row_list, tr, false);
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        Integer Hrg;
        try {
            ((TextView)v.findViewById(R.id.col1)).setText("Sub Total");
            ((TextView)v.findViewById(R.id.col1)).setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
            ((TextView)v.findViewById(R.id.col1)).setTextColor(getResources().getColor(R.color.darkgrey));
            ((TextView)v.findViewById(R.id.col2)).setText("");
            Hrg= dt.getInt("totalsubtotal");
            ((TextView)v.findViewById(R.id.col3)).setText("Rp"+formatter.format(Hrg));
            ((TextView)v.findViewById(R.id.col3)).setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
            ((TextView)v.findViewById(R.id.col3)).setTextColor(getResources().getColor(R.color.darkgrey));
            v.findViewById(R.id.txtX).setVisibility(View.GONE);
            tableTotal.addView(v);
        } catch (JSONException ignored) {}

        tr = new TableRow(getActivity());
        v = getLayoutInflater().inflate(R.layout.row_list, tr, false);
        try {
            ((TextView)v.findViewById(R.id.col1)).setText("Total Disc");
            ((TextView)v.findViewById(R.id.col1)).setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
            ((TextView)v.findViewById(R.id.col1)).setTextColor(getResources().getColor(R.color.grey_200));
            ((TextView)v.findViewById(R.id.col2)).setText("");
            Hrg= dt.getInt("totaldisc");
            ((TextView)v.findViewById(R.id.col3)).setText("Rp"+formatter.format(Hrg));
            ((TextView)v.findViewById(R.id.col3)).setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
            ((TextView)v.findViewById(R.id.col3)).setTextColor(getResources().getColor(R.color.grey_200));
            v.findViewById(R.id.txtX).setVisibility(View.GONE);
            tableTotal.addView(v);
        } catch (JSONException ignored) {}

        tr = new TableRow(getActivity());
        v = getLayoutInflater().inflate(R.layout.row_list, tr, false);
        try {
            ((TextView)v.findViewById(R.id.col1)).setText("Pajak");
            ((TextView)v.findViewById(R.id.col1)).setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
            ((TextView)v.findViewById(R.id.col1)).setTextColor(getResources().getColor(R.color.grey_200));
            ((TextView)v.findViewById(R.id.col2)).setText("");
            Hrg= dt.getInt("totaltax");
            ((TextView)v.findViewById(R.id.col3)).setText("Rp"+formatter.format(Hrg));
            ((TextView)v.findViewById(R.id.col3)).setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
            ((TextView)v.findViewById(R.id.col3)).setTextColor(getResources().getColor(R.color.grey_200));
            v.findViewById(R.id.txtX).setVisibility(View.GONE);
            tableTotal.addView(v);
        } catch (JSONException ignored) {}

        tr = new TableRow(getActivity());
        v = getLayoutInflater().inflate(R.layout.row_list, tr, false);
        try {
            ((TextView)v.findViewById(R.id.col1)).setText("Total");
            ((TextView)v.findViewById(R.id.col1)).setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
            ((TextView)v.findViewById(R.id.col1)).setTextColor(getResources().getColor(R.color.darkgrey));
            ((TextView)v.findViewById(R.id.col2)).setText("");
            Hrg= dt.getInt("total");
            SetHarga_BtnPay(Hrg);
            ((TextView)v.findViewById(R.id.col3)).setText("Rp"+formatter.format(Hrg));
            ((TextView)v.findViewById(R.id.col3)).setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
            ((TextView)v.findViewById(R.id.col3)).setTextColor(getResources().getColor(R.color.darkgrey));
            v.findViewById(R.id.txtX).setVisibility(View.GONE);
            tableTotal.addView(v);
        } catch (JSONException ignored) {}
    }

    private final View.OnClickListener DiKlik= v-> {
        if (v==btnClear) {
            LoadChart(true);
        }else if (v==btnaddcustomer) {
            Intent intent = new Intent(getActivity(), ListCustomerActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(0, 0);
        } else if (v==btnpay) {
            Intent intent = new Intent(getActivity(), PayActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(0, 0);
//            } else if (v==btnedit) {
//                llEdit.setVisibility(View.VISIBLE);
//                btnedit.setVisibility(View.GONE);
//                btneditoff.setVisibility(View.VISIBLE);
//            } else if (v==btneditoff) {
//                llEdit.setVisibility(View.GONE);
//                btnedit.setVisibility(View.VISIBLE);
//                btneditoff.setVisibility(View.GONE);
        } else if (v==btnproduk) {
            Intent intent = new Intent(getActivity(), ProductActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(0, 0);
        } else if (v==btndiskon) {
            Intent intent = new Intent(getActivity(), ListDiscountActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(0, 0);
        } else if (v==btnkategori) {
            Intent intent = new Intent(getActivity(), ListCategoryActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(0, 0);
        } else if (v==dlgVarMod.btnPilih) {
            addToCart();
        }
    };

    private final View.OnKeyListener TentangTombol =new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(v == eCari){
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Cari_Produk();
                    return true;
                }
            }
            return false;
        }
    };

    private void Cari_Produk(){
        ReqApiServices X =  new ReqApiServices();
        X.EventWhenRespon=Jwban1;
        X.SetAwal();
        X.urlBuilder.addPathSegments("product/list");
        X.SetAwalRequest();
        X.request.header("Apphash", MainActivity.ObjIni.DB_Setting.get_Key("HashUser"));
        RequestBody Body = new FormBody.Builder()
                .add("name", eCari.getText().toString())
                .add("code","")
                .add("description","")
                .add("category_id","")
                .add("page","1")
                .add("showprice",KodePriceTipe())
                .build();
        X.request.post(Body);
        X.HitNoWait();
    }

    private String KodePriceTipe(){
        final String[] Kd = {"dine_in","take_away","GoSend","Grab"};
        final int Pilih= spinner.getSelectedItemPosition();
        if(Kd.length<=Pilih) return "";
        return Kd[Pilih];
    }

    private final TerimaResponApi Jwban1 = new TerimaResponApi() {
        @Override
        public void OnSukses(ReqApiServices tool, JSONObject Data) {
            try {
                if(Data.getString("code").equals("00")){
                    final JSONArray Prd= Data.getJSONObject("message").getJSONArray("data");
                    MenuAdapter trxAdapter= new MenuAdapter(inflater.getContext(), R.layout.list_menu);
                    for (int i=0; i < Prd.length(); i++) {
                        @SuppressLint("UseCompatLoadingForDrawables") final NavigationItem Isi = NavigationItem.BuatItem(Prd.getJSONObject(i),
                                getResources().getDrawable(R.drawable.food)
                        );
                        Isi.Data.put("JnsHrg", KodePriceTipe());
                        trxAdapter.add(Isi);
                    }
                    Runnable UpdateUI= () -> gvMenu.setAdapter(trxAdapter);
                    MainActivity.ObjIni.runOnUiThread(UpdateUI);
                }
            } catch (JSONException ignored) {}
        }

        @Override
        public void onGagal(ReqApiServices tool, IOException e) {

        }
    };

    private  void SetHarga_BtnPay(double Hrg){
        final boolean Bisa =Hrg>=0;
        btnpay.setEnabled(Bisa);
        String Hsl = "Blm ada yg dibayar";
        if(Bisa) {
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            Hsl = "Bayar Rp" + formatter.format(Hrg);
        }
        btnpay.setText(Hsl);
    }

    private void LoadChart(boolean clear){
        SetHarga_BtnPay(-1);
        btnClear.setEnabled(false);
        ReqApiServices X =  new ReqApiServices();
        X.EventWhenRespon=Jwban2;
        X.KodePath=5;
        X.SetAwal();
        String url ="cart/";
        if(clear) url+="clear";else url+="detail";
        X.urlBuilder.addPathSegments(url);
        X.SetAwalRequest();
        X.request.header("Apphash", MainActivity.ObjIni.DB_Setting.get_Key("HashUser"));
        RequestBody Body = new FormBody.Builder()
                .add("trxid",""+mSettings.getInt("NoTrx",0))
                .build();
        X.request.post(Body);
        X.HitNoWait();
    }

    private final AdapterView.OnItemClickListener MenuDipilih= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(view.getParent()==gvMenu){
                NavigationItem item = (NavigationItem) parent.getAdapter().getItem(position);
                dlgVarMod= new VariantModifierDialog(item.Data,MainActivity.ObjIni);
                dlgVarMod.OnPilih= DiKlik;
                dlgVarMod.show();
                Log.d(TAG,"Pilih: "+item.Data.toString());
             }
        }
    };

    private void addToCart(){
        final JSONObject dt = dlgVarMod.Hasil;
        Log.d(TAG,"Hasil:"+dt.toString());
        try {
            String Code = dt.getString("id");
            ReqApiServices X =  new ReqApiServices();
            X.KodePath=25;
            X.EventWhenRespon=Jwban2;
            X.SetAwal();
            X.urlBuilder.addPathSegments("cart/add");
            X.SetAwalRequest();
            X.request.header("Apphash", MainActivity.ObjIni.DB_Setting.get_Key("HashUser"));
            RequestBody Body = new FormBody.Builder()
                    .add("trxid",""+mSettings.getInt("NoTrx",0))
                    .add("modifier_id", dt.getString("modifier_id"))
                    .add("note", dt.getString("note"))
                    .add("product_id", Code)
                    .add("pricetype",KodePriceTipe())
                    .add("qty",""+dt.getInt("qty"))
                    .build();
            X.request.post(Body);
            SetHarga_BtnPay(-1);
            X.HitNoWait();
        } catch (JSONException ignored) {}
    }

    private final TerimaResponApi Jwban2 = new TerimaResponApi() {
        @Override
        public void onGagal(ReqApiServices tool, IOException e) {

        }

        @Override
        public void OnSukses(ReqApiServices tool, JSONObject Data) {
            try {
                if (Data.getString("code").equals("00")) {
                    final JSONObject dt= Data.getJSONObject("message").getJSONObject("cart  ");
                    final JSONArray cart= dt.getJSONArray("cart");
                    Runnable UpdateUI = () -> {
                        InsertTotal(dt);
                        InsertRow(cart);
                    };
                    MainActivity.ObjIni.runOnUiThread(UpdateUI);
                }
            } catch (JSONException ignored) {}
        }
    };


    @SuppressLint("SetTextI18n")
    private void InsertRow(@NonNull JSONArray dt){
        tableList.removeAllViews();
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        Integer Hrg;
        try {
            for (int i=0; i < dt.length(); i++) {
                TableRow tr = new TableRow(getActivity());
                View v = getLayoutInflater().inflate(R.layout.row_list, tr, false);
                final JSONObject Isi = dt.getJSONObject(i);
                TextView Obj= v.findViewById(R.id.col1);
                Obj.setText(Isi.getString("product"));
                Hrg= Isi.getInt("qty");
                Obj=v.findViewById(R.id.col2);Obj.setText("x"+Hrg);
                Hrg= Isi.getInt("total");
                Obj=v.findViewById(R.id.col3);Obj.setText("Rp"+formatter.format(Hrg));
                Obj=v.findViewById(R.id.txtX);
                Obj.setOnClickListener(view -> HapusCartMin(Isi));
                tableList.addView(v);
            }
            btnpay.setEnabled(dt.length()>0);
            btnClear.setEnabled(dt.length()>0);
        } catch (JSONException ignored) {}
    }

    private void HapusCartMin(@NonNull JSONObject dt){
        Log.d(TAG,"Kurangi Qty Cart: "+ dt);
        try{
            ReqApiServices X = new ReqApiServices();
            X.EventWhenRespon=Jwban2;
            X.SetAwal();
            X.urlBuilder.addPathSegments("cart/delete");
            X.SetAwalRequest();
            X.request.header("Apphash", MainActivity.ObjIni.DB_Setting.get_Key("HashUser"));
            RequestBody Body = new FormBody.Builder()
                    .add("trxid",""+mSettings.getInt("NoTrx",0))
                    .add("cart_id",""+dt.getInt("id"))
                    .build();
            X.request.post(Body);
            X.HitNoWait();
        } catch (JSONException ignored) {}
    }

}