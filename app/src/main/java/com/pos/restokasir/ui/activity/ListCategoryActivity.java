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
import com.pos.restokasir.adapter.CategoryAdapter;
import com.pos.restokasir.tools.NavigationItem;

import java.util.Objects;

public class ListCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);

        String Judul = "Data Kategori";
        Toolbar mToolbar = findViewById(R.id.toolbar_actionbar);
        mToolbar.setTitle(Judul);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ListView lv = findViewById(R.id.list);
        CategoryAdapter categoryAdapter;
        categoryAdapter = new CategoryAdapter(this, R.layout.row_category);
        categoryAdapter.add(NavigationItem.catitem("Makanan",  "Makanan yang berat-berat"));
        categoryAdapter.add(NavigationItem.catitem("Minuman",  "Minuman yang ringan-ringan"));
        categoryAdapter.add(NavigationItem.catitem("Promo",  "Promo Agustus"));
        lv.setAdapter(categoryAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_cat) {
            Intent intent = new Intent(this, AddCategoryActivity.class);
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