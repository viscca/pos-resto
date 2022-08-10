package com.pos.restokasir.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pos.restokasir.R;
import com.pos.restokasir.Service.ReqApiServices;
import com.pos.restokasir.Service.TerimaResponApi;
import com.pos.restokasir.adapter.CustomerAdapter;
import com.pos.restokasir.adapter.MenuAdapter;
import com.pos.restokasir.db_sqlite.C_DB_Setting;
import com.pos.restokasir.tools.NavigationItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class ListCustomerActivity extends AppCompatActivity {
    public static ListCustomerActivity ObjIni;
    private EditText eCari;
    private ListView lv;
    public C_DB_Setting DB_Setting;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_customer);

        String Judul = "Daftar Pelanggan";
        Toolbar mToolbar = findViewById(R.id.toolbar_actionbar);
        mToolbar.setTitle(Judul);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ObjIni = this;
        DB_Setting=new C_DB_Setting(this);
        lv = findViewById(R.id.list);
        eCari = findViewById(R.id.eCari);
        eCari.setOnKeyListener(TentangTombol);
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.customer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_cust) {
            Intent intent = new Intent(this, AddCustomerActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        Cari_Customer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    private final View.OnKeyListener TentangTombol =new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(v == eCari){
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Cari_Customer();
                    return true;
                }
            }
            return false;
        }
    };

    private void Cari_Customer(){
        ReqApiServices X =  new ReqApiServices();
        X.EventWhenRespon=Jwban1;
        X.SetAwal();
        X.urlBuilder.addPathSegments("customer/list");
        X.SetAwalRequest();
        X.request.header("Apphash", DB_Setting.get_Key("HashUser"));
        RequestBody Body = new FormBody.Builder()
                .add("name", eCari.getText().toString())
                .add("page","1")
                .build();
        X.request.post(Body);
        X.HitNoWait();
    }

    private final TerimaResponApi Jwban1 = new TerimaResponApi() {
        @Override
        public void OnSukses(ReqApiServices tool, JSONObject Data) {
            try {
                if(Data.getString("code").equals("00")){
                    final JSONArray Prd= Data.getJSONObject("message").getJSONArray("data");
                    CustomerAdapter custAdapter = new CustomerAdapter(ObjIni, R.layout.row_customer);
                    for (int i=0; i < Prd.length(); i++) {
                        final NavigationItem Isi = NavigationItem.BuatItem(Prd.getJSONObject(i),
                                getResources().getDrawable(R.drawable.ic_person)
                        );
                        custAdapter.add(Isi);
                    }
                    Runnable UpdateUI=new Runnable() {
                        @Override
                        public void run() {
                            lv.setAdapter(custAdapter);
                        }
                    };
                    runOnUiThread(UpdateUI);
                }
                return;
            } catch (JSONException e) {}
        }

        @Override
        public void onGagal(ReqApiServices tool, IOException e) {

        }
    };

}