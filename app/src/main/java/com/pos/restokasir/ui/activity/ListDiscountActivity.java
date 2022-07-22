package com.pos.restokasir.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pos.restokasir.R;
import com.pos.restokasir.adapter.DiscountAdapter;
import com.pos.restokasir.tools.NavigationItem;

import java.util.Objects;

public class ListDiscountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_discount);

        String Judul = "Data Diskon";
        Toolbar mToolbar = findViewById(R.id.toolbar_actionbar);
        mToolbar.setTitle(Judul);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ListView lv = findViewById(R.id.list);
        DiscountAdapter discAdapter;
        discAdapter = new DiscountAdapter(this, R.layout.row_discount);
        discAdapter.add(NavigationItem.discitem("Kamis Manis",  "5%"));
        discAdapter.add(NavigationItem.discitem("Jumat Berkah",  "15%"));
        discAdapter.add(NavigationItem.discitem("Juli Nguli",  "Rp2.000"));
        lv.setAdapter(discAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.discount, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_disc) {
            Intent intent = new Intent(this, AddDiscountActivity.class);
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
