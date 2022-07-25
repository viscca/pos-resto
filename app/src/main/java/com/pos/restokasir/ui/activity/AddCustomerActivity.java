package com.pos.restokasir.ui.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pos.restokasir.R;

import java.util.Calendar;
import java.util.Objects;

public class AddCustomerActivity extends AppCompatActivity {
    private EditText eTgl;
    private int mYear, mMonth, mDay;
    private final int DATE_DIALOG_ID = 0;
    private final String[] arrMonth = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        String Judul = "Tambah Pelanggan";
        Toolbar mToolbar = findViewById(R.id.toolbar_actionbar);
        mToolbar.setTitle(Judul);
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
                    String sdate = LPad(mDay + "") + "/" + arrMonth[mMonth] + "/" + mYear;
                    eTgl.setText(sdate);
                }
            };

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
}
