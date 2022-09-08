package com.pos.restokasir.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.pos.restokasir.R;
import com.pos.restokasir.Service.ReqApiServices;
import com.pos.restokasir.Service.TerimaResponApi;
import com.pos.restokasir.adapter.CustomerAdapter;
import com.pos.restokasir.db_sqlite.C_DB_Setting;
import com.pos.restokasir.tools.NavigationItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class FindCustomerDialog extends Dialog {
    private final String TAG = "Prs_DlgCustomer";
    public C_DB_Setting DB_Setting;
    public JSONObject Hasil;
    public Button btnPilih;
    public View.OnClickListener OnPilih;
    private Context OrgTua;
    private EditText eCari;
    private ListView lv;

    public FindCustomerDialog(@NonNull Context context) {
        super(context);
        OrgTua=context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_list_customer);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(0));
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.90);
        getWindow().setLayout(width, height);

        btnPilih = findViewById(R.id.btn_pilih);
        btnPilih.setOnClickListener(DiKlik);
        TextView btnTutup = findViewById(R.id.btn_Tutup);
        btnTutup.setOnClickListener(DiKlik);
        lv = findViewById(R.id.list);
        lv.setOnItemClickListener(CustDipilih);
        eCari = findViewById(R.id.eCari);
        eCari.setOnKeyListener(TentangTombol);
    }

    private final View.OnClickListener DiKlik = v -> {
        if (v == btnPilih) {

        } else {
            dismiss();
        }
    };

    @Override
    public void show() {
        super.show();
        Cari_Customer();
    }

    private final View.OnKeyListener TentangTombol = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (v == eCari) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Cari_Customer();
                    return true;
                }
            }
            return false;
        }
    };

    private void Cari_Customer() {
        ReqApiServices X = new ReqApiServices();
        X.EventWhenRespon = Jwban1;
        X.SetAwal();
        X.urlBuilder.addPathSegments("customer/list");
        X.SetAwalRequest();
        X.request.header("Apphash", DB_Setting.get_Key("HashUser"));
        final JSONObject Body = new JSONObject();
        try{
            Body.put("name", eCari.getText().toString())
                .put("page", "1");
        } catch (JSONException ignored) {}
        X.SetFormBody_Post(Body);
        X.HitNoWait();
    }

    private final TerimaResponApi Jwban1 = new TerimaResponApi() {
        @Override
        public void OnSukses(ReqApiServices tool, JSONObject Data) {
            try {
                if (Data.getString("code").equals("00")) {
                    final JSONArray Prd = Data.getJSONObject("message").getJSONArray("data");
                    CustomerAdapter custAdapter = new CustomerAdapter(OrgTua, R.layout.row_customer);
                    for (int i = 0; i < Prd.length(); i++) {
                        @SuppressLint("UseCompatLoadingForDrawables") final NavigationItem Isi = NavigationItem.BuatItem(Prd.getJSONObject(i),
                                OrgTua.getResources().getDrawable(R.drawable.ic_person)
                        );
                        custAdapter.add(Isi);
                    }
                    Runnable UpdateUI = () -> lv.setAdapter(custAdapter);
                    ((Activity) OrgTua).runOnUiThread(UpdateUI);
                }
            } catch (JSONException ignored) {
            }
        }

        @Override
        public void onGagal(ReqApiServices tool, IOException e) {

        }
    };

    private final AdapterView.OnItemClickListener CustDipilih = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (view.getParent() == lv) {
                NavigationItem item = (NavigationItem) parent.getAdapter().getItem(position);
                try{
                    Hasil=new JSONObject(item.Data.toString());
                    dismiss();
                    OnPilih.onClick(btnPilih);
                } catch (JSONException ignored) {
                    BuatToast("Error:"+ignored.getMessage());
                }
            }
        }
    };

    private void BuatToast(String Txt) {
        Toast.makeText(OrgTua, Txt, Toast.LENGTH_LONG).show();
    }
}