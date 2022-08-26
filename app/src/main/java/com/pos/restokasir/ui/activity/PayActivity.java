package com.pos.restokasir.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pos.restokasir.R;

import java.util.Objects;

public class PayActivity extends AppCompatActivity {
    private Button btnBayar;

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

        btnBayar = findViewById(R.id.btnbayar);
        btnBayar.setOnClickListener(DiKlik);
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
