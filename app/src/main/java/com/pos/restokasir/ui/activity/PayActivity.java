package com.pos.restokasir.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pos.restokasir.R;
import com.pos.restokasir.Service.ReqApiServices;
import com.pos.restokasir.Service.TerimaResponApi;
import com.pos.restokasir.db_sqlite.C_DB_Setting;

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
    private TextView txtTotal;
    private EditText eJumlah;

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

        txtTotal = findViewById(R.id.txtTotal);
        eJumlah = findViewById(R.id.eJumlah);
        btnBayar = findViewById(R.id.btnbayar);
        btnBayar.setOnClickListener(DiKlik);
        DB_Setting = new C_DB_Setting(this);
        LoadChart();
    }


    private void LoadChart(){
        txtTotal.setText("");
        eJumlah.setText("");
        btnBayar.setEnabled(false);
        ReqApiServices X =  new ReqApiServices();
        X.EventWhenRespon=Jwban2;
        X.KodePath=5;
        X.SetAwal();
        X.urlBuilder.addPathSegments("cart/detail");
        X.SetAwalRequest();
        X.request.header("Apphash", DB_Setting.get_Key("HashUser"));
        RequestBody Body = new FormBody.Builder()
                .add("trxid",""+DB_Setting.mSettings.getInt("NoTrx",0))
                .build();
        X.request.post(Body);
        X.HitNoWait();
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
                    Log.d(TAG,"Sukses Load Cart. "+tool.KodePath);
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
            Integer Hrg= dt.getInt("total");
            String Hsl="Total Bayar Rp."+formatter.format(Hrg);
            txtTotal.setText(Hsl);
            eJumlah.setText(Hrg.toString());
            btnBayar.setEnabled(true);
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
            if (v==btnBayar) {
                Intent intent = new Intent(PayActivity.this, ReceiptActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        }
    };
}
