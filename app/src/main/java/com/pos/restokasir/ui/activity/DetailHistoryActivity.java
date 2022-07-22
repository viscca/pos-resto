package com.pos.restokasir.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pos.restokasir.R;
import com.pos.restokasir.adapter.DetailHistoryAdapter;
import com.pos.restokasir.tools.NavigationItem;

import java.util.Objects;

public class DetailHistoryActivity extends AppCompatActivity {

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

        ListView lv = findViewById(R.id.list);
        DetailHistoryAdapter histAdapter;
        histAdapter = new DetailHistoryAdapter(this, R.layout.row_detail_history);
        histAdapter.add(NavigationItem.dethistItem("Nasi Goreng (Reguler)", getResources().getDrawable(R.drawable.food),  "Rp20.000"));
        histAdapter.add(NavigationItem.dethistItem("Subtotal", getResources().getDrawable(R.drawable.white),  "Rp20.000"));
        histAdapter.add(NavigationItem.dethistItem("Total", getResources().getDrawable(R.drawable.white),  "Rp20.000"));
        histAdapter.add(NavigationItem.dethistItem("Jumlah Bayar", getResources().getDrawable(R.drawable.white),  "Rp50.000"));
        histAdapter.add(NavigationItem.dethistItem("Kembalian", getResources().getDrawable(R.drawable.white),  "Rp30.000"));
        lv.setAdapter(histAdapter);
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
