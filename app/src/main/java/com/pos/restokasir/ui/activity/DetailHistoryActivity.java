package com.pos.restokasir.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pos.restokasir.R;

import java.util.Objects;

public class DetailHistoryActivity extends AppCompatActivity {
    private TableLayout list;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);

        String Judul = "34FDSDW";
        Toolbar mToolbar = findViewById(R.id.toolbar_actionbar);
        mToolbar.setTitle(Judul);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        list = findViewById(R.id.tbllist);
        InsertRow();
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void InsertRow(){
        TableRow tr = new TableRow(this);
        View v = getLayoutInflater().inflate(R.layout.row_detail_history, tr, false);
        View v2 = getLayoutInflater().inflate(R.layout.row_detail_history, tr, false);
        View v3 = getLayoutInflater().inflate(R.layout.row_detail_history, tr, false);
        View v4 = getLayoutInflater().inflate(R.layout.row_detail_history, tr, false);
        View v5 = getLayoutInflater().inflate(R.layout.row_detail_history, tr, false);
        //want to get childs of row for example TextView, get it like this:
        ((ImageView)v.findViewById(R.id.imgIcon)).setImageDrawable(getResources().getDrawable(R.drawable.food));
        ((TextView)v.findViewById(R.id.txtKet)).setText("Nasi Goreng (Reguler)");
        ((TextView)v.findViewById(R.id.txtJumlah)).setText("Rp20.000");
        list.addView(v);
        ((ImageView)v2.findViewById(R.id.imgIcon)).setImageDrawable(getResources().getDrawable(R.drawable.white));
        ((TextView)v2.findViewById(R.id.txtKet)).setText("Subtotal");
        ((TextView)v2.findViewById(R.id.txtJumlah)).setText("Rp20.000");
        list.addView(v2);
        ((ImageView)v3.findViewById(R.id.imgIcon)).setImageDrawable(getResources().getDrawable(R.drawable.white));
        ((TextView)v3.findViewById(R.id.txtKet)).setText("Total");
        ((TextView)v3.findViewById(R.id.txtJumlah)).setText("Rp20.000");
        list.addView(v3);
        ((ImageView)v4.findViewById(R.id.imgIcon)).setImageDrawable(getResources().getDrawable(R.drawable.white));
        ((TextView)v4.findViewById(R.id.txtKet)).setText("Jumlah Bayar");
        ((TextView)v4.findViewById(R.id.txtJumlah)).setText("Rp50.000");
        list.addView(v4);
        ((ImageView)v5.findViewById(R.id.imgIcon)).setImageDrawable(getResources().getDrawable(R.drawable.white));
        ((TextView)v5.findViewById(R.id.txtKet)).setText("Kembalian");
        ((TextView)v5.findViewById(R.id.txtJumlah)).setText("Rp30.000");
        list.addView(v5);
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
}
