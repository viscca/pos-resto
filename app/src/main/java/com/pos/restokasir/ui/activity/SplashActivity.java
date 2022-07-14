package com.pos.restokasir.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.pos.restokasir.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {
    private static final long SPLASH_TIMEOUT = 2000;
    private boolean gone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initiateUI(savedInstanceState);
    }

    private void initiateUI(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        Display getOrient = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        getOrient.getSize(size);
        ConstraintLayout layout = findViewById(R.id.layout);
        if (size.x < size.y) {
            layout.setBackgroundResource(R.drawable.splash);
        } else {
            layout.setBackgroundResource(R.drawable.splash_ls);
        }

        Button goButton = findViewById(R.id.goButton);
        goButton.setOnClickListener(v -> go());
        new Handler().postDelayed(() -> {
            if (!gone) go();
        }, SPLASH_TIMEOUT);
    }

    private void go() {
        gone = true;
        Intent newActivity = new Intent(SplashActivity.this,
                LoginActivity.class);
        startActivity(newActivity);
        SplashActivity.this.finish();
    }
}
