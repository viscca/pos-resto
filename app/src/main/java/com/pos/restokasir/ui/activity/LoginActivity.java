package com.pos.restokasir.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pos.restokasir.R;

public class LoginActivity extends Activity {
    private final String TAG="Prs_LoginActivity";
    Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate " + savedInstanceState);
        setContentView(R.layout.activity_login);

        btnOK = findViewById(R.id.btnlogin);
        btnOK.setOnClickListener(DiKlik);
    }

    private final View.OnClickListener DiKlik= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v==btnOK) {
                Intent newIntent = new Intent(LoginActivity.this, MainActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(newIntent);
            }
        }
    };
}
