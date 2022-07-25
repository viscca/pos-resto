package com.pos.restokasir.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pos.restokasir.R;
import com.pos.restokasir.adapter.CustomerAdapter;
import com.pos.restokasir.tools.NavigationItem;

import java.util.Objects;

public class ListCustomerActivity extends AppCompatActivity {

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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ListView lv = findViewById(R.id.list);
        CustomerAdapter custAdapter;
        custAdapter = new CustomerAdapter(this, R.layout.row_customer);
        custAdapter.add(NavigationItem.cusitem("Test",  getResources().getDrawable(R.drawable.ic_person), "081910111111", "test@test.com"));
        custAdapter.add(NavigationItem.cusitem("Abdi",  getResources().getDrawable(R.drawable.ic_person), "081910222222", "abdi@test.com"));
        lv.setAdapter(custAdapter);
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}