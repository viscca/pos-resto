package com.pos.restokasir.ui.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.pos.restokasir.R;
import com.pos.restokasir.Service.ReqApiServices;
import com.pos.restokasir.Service.TerimaResponApi;
import com.pos.restokasir.db_sqlite.C_DB_Setting;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import okhttp3.FormBody;

public class AddCustomerActivity extends AppCompatActivity {
    private int mYear, mMonth, mDay;
    private final int DATE_DIALOG_ID = 0;
    private C_DB_Setting DB_Setting;
    private EditText eHP, eNama, eEmail, eTgl, eAlamat;
    private AppCompatButton btnsimpan;
    private Toolbar mToolbar;
    private Integer idCust=0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        DB_Setting=new C_DB_Setting(this);

        mToolbar = findViewById(R.id.toolbar_actionbar);
        mToolbar.setTitle("Tambah Pelanggan");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Laki-laki");
        adapter.add("Perempuan");
        adapter.add("Jenis Kelamin");
        Spinner spinner = findViewById(R.id.spinJK);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount()); //display hint

        eTgl = findViewById(R.id.eTgl);
        eNama = findViewById(R.id.eNama);
        eAlamat = findViewById(R.id.eAlamat);
        eEmail = findViewById(R.id.eEmail);
        eHP = findViewById(R.id.eHP);
        btnsimpan = findViewById(R.id.btnsimpan);
        btnsimpan.setOnClickListener(DiKlik);
        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        eTgl.setOnTouchListener((arg0, arg1) -> {
            // TODO Auto-generated method stub
            showDialog(DATE_DIALOG_ID);
            return true;
        });

        LoadData();
    }

    private void LoadData(){
        Bundle extras = getIntent().getExtras();
        if (extras == null) return;
        try{
            JSONObject dt =new JSONObject(extras.getString("dataJSON",""));
            idCust= dt.getInt("id");
            if(idCust>0) mToolbar.setTitle("Edit Pelanggan");
            eNama.setText(dt.getString("name"));
            eEmail.setText(dt.getString("email"));
            eAlamat.setText(dt.getString("address"));
            ParsingTgl(dt.getString("date_of_birth"));
        } catch (JSONException e) {}
    }

    private void ParsingTgl(String tgl){
        String[] separated = tgl.split("-");
        if (separated.length==3){
            try {
                mYear = Integer.parseInt(separated[0]);
                mMonth = Integer.parseInt(separated[1]);
                mMonth--;
                mDay = Integer.parseInt(separated[2]);
                CetakTgl();
            } catch(NumberFormatException nfe) {}
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATE_DIALOG_ID) {
            return new DatePickerDialog(
                    this, mDateSetListener, mYear, mMonth, mDay);
        }
        return null;
    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    CetakTgl();
                }
            };

    private void CetakTgl(){
        final String[] arrMonth = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        String sdate = LPad(mDay + "") + "/" + arrMonth[mMonth] + "/" + mYear;
        eTgl.setText(sdate);
    }

    private static String LPad(String schar) {
        StringBuilder sret = new StringBuilder(schar);
        for (int i = sret.length(); i < 2; i++) {
            sret.insert(0, "0");
        }
        return sret.toString();
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
            if (v == btnsimpan) {
                ReqApiServices X =  new ReqApiServices();
                X.EventWhenRespon=Jwban1;
                X.SetAwal();
                String S="create"; if (idCust>0) S="update";
                X.urlBuilder.addPathSegments("customer/"+S);
                X.SetAwalRequest();
                X.request.header("Apphash", DB_Setting.get_Key("HashUser"));
                FormBody.Builder Body = new FormBody.Builder()
                        .add("name", eNama.getText().toString())
                        .add("email", eEmail.getText().toString())
                        .add("address", eAlamat.getText().toString());
                if (idCust>0) Body.add("id",idCust.toString());
                X.request.post(Body.build());
                X.HitNoWait();
            }
        }
    };

    private final TerimaResponApi Jwban1 = new TerimaResponApi() {
        @Override
        public void OnSukses(ReqApiServices tool, JSONObject Data) {
            try {
                if(Data.getString("code").equals("00")){
                    Log.d("Prs_x","ok");
                }
            } catch (JSONException e) {}
        }

        @Override
        public void onGagal(ReqApiServices tool, IOException e) {

        }
    };

}
