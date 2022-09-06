package com.pos.restokasir.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pos.restokasir.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class ReceiptActivity extends AppCompatActivity {
    private ImageView btnClose;
    private JSONObject dtHasil, dtPayMethod;
    private TextView txt2,txt3,txt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);
        txt4 = findViewById(R.id.txt4);
        btnClose = findViewById(R.id.ivClose);
        btnClose.setOnClickListener(DiKlik);
        LoadData();
    }

    private void LoadData(){
        Bundle extras = getIntent().getExtras();
        if (extras == null) return;
        try{
            dtHasil =new JSONObject(extras.getString("dataJSON",""));
            dtPayMethod =new JSONObject(extras.getString("dataPay",""));
            final JSONObject summary= dtHasil.getJSONObject("summary");
            //-----------
            txt2.setText(dtPayMethod.getString("name"));
            double Hrg = summary.getDouble("payment");
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            txt3.setText("Bayar Rp"+formatter.format(Hrg));
            final String sid= dtPayMethod.getString("id");
            if(sid.equals("1")) {
                Hrg = -summary.getDouble("due");
            }else{
                Hrg=0;
            }
            if(Hrg>0){
                txt4.setText("Kembalian Rp"+formatter.format(Hrg));
            }else{
                txt4.setText("");
            }
        } catch (JSONException e) {}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ReceiptActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private final View.OnClickListener DiKlik= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v==btnClose) {
                Intent intent = new Intent(ReceiptActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        }
    };
}
