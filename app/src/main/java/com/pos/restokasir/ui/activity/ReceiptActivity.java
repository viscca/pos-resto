package com.pos.restokasir.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.pos.restokasir.R;

public class ReceiptActivity extends AppCompatActivity {
    private ImageView btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        btnClose = findViewById(R.id.ivClose);
        btnClose.setOnClickListener(DiKlik);
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
