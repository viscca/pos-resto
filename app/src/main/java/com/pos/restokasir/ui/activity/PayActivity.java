package com.pos.restokasir.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pos.restokasir.R;
import com.pos.restokasir.Service.ReqApiServices;
import com.pos.restokasir.Service.TerimaResponApi;
import com.pos.restokasir.db_sqlite.C_DB_Setting;
import com.pos.restokasir.ui.dialog.FindCustomerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class PayActivity extends AppCompatActivity {
    private final String TAG="Prs_PaymentAct";
    private C_DB_Setting DB_Setting;
    private Button btnBayar;
    private Spinner spinner;
    private TextView txtTotal;
    private EditText eJumlah, eCariCust;
    private JSONArray dtMethod;
    private Double TotBayar;
    private LinearLayout ll_Tunai, ll_NonTunai;
    private FindCustomerDialog dlgCust;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        String Judul = "Pembayaran Transaksi";
        Toolbar mToolbar = findViewById(R.id.toolbar_actionbar);
        mToolbar.setTitle(Judul);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        spinner = findViewById(R.id.spinMethod);
        ll_Tunai = findViewById(R.id.ll_Tunai);
        ll_NonTunai = findViewById(R.id.ll_NonTunai);
        txtTotal = findViewById(R.id.txtTotal);
        eJumlah = findViewById(R.id.eJumlah);
        eCariCust = findViewById(R.id.eCariCust);
        eCariCust.setOnClickListener(DiKlik);
        btnBayar = findViewById(R.id.btnbayar);
        btnBayar.setOnClickListener(DiKlik);
        DB_Setting = new C_DB_Setting(this);
        LoadChart();
        dlgCust= new FindCustomerDialog(this);
        dlgCust.OnPilih= DiKlik;
        dlgCust.DB_Setting=DB_Setting;
    }

    private void LoadChart(){
        txtTotal.setText("");
        eJumlah.setText("");
        ReqApiServices X =  new ReqApiServices();
        X.EventWhenRespon=Jwban1;
        X.SetAwal();
        X.urlBuilder.addPathSegments("checkout/bill");
        X.SetAwalRequest();
        X.request.header("Apphash", DB_Setting.get_Key("HashUser"));
        final JSONObject Body = new JSONObject();
        try{
            Body.put("trxid",""+DB_Setting.mSettings.getInt("NoTrx",0));
        } catch (JSONException ignored) {}
        X.SetFormBody_Post(Body);
        X.HitNoWait();
    }

    private final TerimaResponApi Jwban1 = new TerimaResponApi() {
        @Override
        public void onGagal(ReqApiServices tool, IOException e) {

        }

        @Override
        public void OnSukses(ReqApiServices tool, JSONObject Data) {
            try {
                String code=Data.getString("code");
                if(code.equals("97")){
                    code=Data.getJSONObject("message").getString("error");
                    Log.d(TAG,code);
                }else if(code.equals("00")){
                    final JSONObject dt= Data.getJSONObject("message").getJSONObject("summary");
                    Runnable UpdateUI = new Runnable() {
                        @Override
                        public void run() {
                            InsertTotal(dt);
                        }
                    };
                    runOnUiThread(UpdateUI);
                }
            } catch (JSONException e) {}
        }
    };

    private void InsertTotal(@NonNull JSONObject dt){
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        try{
            TotBayar = dt.getDouble("total");
            String Hsl="Total Bayar Rp."+formatter.format(TotBayar);
            txtTotal.setText(Hsl);
            eJumlah.setText(TotBayar.toString());
            getPaymentMenthod();
        } catch (JSONException e) {}
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    private final View.OnClickListener DiKlik= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v==eCariCust) {
                dlgCust.show();
            }else if (v==btnBayar) {
                goBayar();
            }else if (v==dlgCust.btnPilih){
                try{
                    eCariCust.setText(dlgCust.Hasil.getString("name"));
                    getPaymentMenthod();
                } catch (JSONException e) {}
            }
        }
    };

    private void getPaymentMenthod(){
        ReqApiServices X =  new ReqApiServices();
        X.EventWhenRespon=Jwban2;
        X.KodePath=5;
        X.SetAwal();
        X.urlBuilder.addPathSegments("checkout/listpayment");
        X.SetAwalRequest();
        X.request.header("Apphash", DB_Setting.get_Key("HashUser"));
        final JSONObject Body = new JSONObject();
        try{
            Body.put("page","1")
                .put("name","");
        } catch (JSONException ignored) {}
        X.SetFormBody_Post(Body);
        X.HitNoWait();
    }

    private final TerimaResponApi Jwban2 = new TerimaResponApi() {
        @Override
        public void onGagal(ReqApiServices tool, IOException e) {

        }

        @Override
        public void OnSukses(ReqApiServices tool, JSONObject Data) {
            try {
                String code=Data.getString("code");
                if(code.equals("97")){
                    code=Data.getJSONObject("message").getString("error");
                    Log.d(TAG,code);
                }else if(code.equals("00")){
                    dtMethod = Data.getJSONObject("message").getJSONArray("data");
                    Runnable UpdateUI = new Runnable() {
                        @Override
                        public void run() {
                            SetSpinner();
                        }
                    };
                    runOnUiThread(UpdateUI);
                }
            } catch (JSONException e) {}
        }
    };

    private void SetSpinner(){
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_dropdown_item);
        try {
            for (int i=0; i < dtMethod.length(); i++) {
                final JSONObject Isi = dtMethod.getJSONObject(i);
                adapter.add(Isi.getString("name"));
            }
        } catch (JSONException ignored) {}
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> parent, View view, int position, long id) {
                try {
                    final JSONObject Isi = dtMethod.getJSONObject(position);
                    final String sid= Isi.getString("id");
                    if(sid.equals("1")) {
                        ll_NonTunai.setVisibility(View.GONE);
                        ll_Tunai.setVisibility(View.VISIBLE);
                    }else{
                        eJumlah.setText(TotBayar.toString());
                        ll_Tunai.setVisibility(View.GONE);
                        ll_NonTunai.setVisibility(View.VISIBLE);
                    }
                    btnBayar.setEnabled(true);
                } catch (JSONException ignored) {}
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void goBayar(){
        ReqApiServices X =  new ReqApiServices();
        X.EventWhenRespon=Jwban3;
        X.SetAwal();
        X.urlBuilder.addPathSegments("checkout/pay");
        X.SetAwalRequest();
        X.request.header("Apphash", DB_Setting.get_Key("HashUser"));
        final JSONObject Body = new JSONObject();
        try{
            Body.put("trxid",""+DB_Setting.mSettings.getInt("NoTrx",0));
            final JSONObject Isi = dtMethod.getJSONObject(spinner.getSelectedItemPosition());
            Body.put("paymethod",Isi.getString("id"))
                    .put("totalpayment",eJumlah.getText().toString())
                    .put("customer_id",dlgCust.Hasil.getString("id"));
            X.SetFormBody_Post(Body);
            X.HitNoWait();
        } catch (JSONException ignored) {}
    }

    private final TerimaResponApi Jwban3 = new TerimaResponApi() {
        @Override
        public void onGagal(ReqApiServices tool, IOException e) {

        }

        @Override
        public void OnSukses(ReqApiServices tool, JSONObject Data) {
            try {
                String code=Data.getString("code");
                if(code.equals("97")){
                    code=Data.getJSONObject("message").getString("error");
                    Log.d(TAG,code);
                }else if(code.equals("00")){
                    Intent intent = new Intent(PayActivity.this, ReceiptActivity.class);
                    intent.putExtra("dataJSON", Data.getJSONObject("message").toString());
                    intent.putExtra("dataPay", dtMethod.getJSONObject(spinner.getSelectedItemPosition()).toString());
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            } catch (JSONException e) {}
        }
    };


}
