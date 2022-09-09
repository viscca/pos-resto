package com.pos.restokasir.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.pos.restokasir.R;
import com.pos.restokasir.Service.ReqApiServices;
import com.pos.restokasir.Service.TerimaResponApi;
import com.pos.restokasir.db_sqlite.C_DB_Setting;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {
    private final String TAG="Prs_SplashActivity";
    private final long SPLASH_TIMEOUT = 2000;
    private String HashUser;
    private C_DB_Setting DB_Setting ;

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
        setContentView(R.layout.activity_splash);
        Display getOrient = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        getOrient.getSize(size);
        ConstraintLayout layout = findViewById(R.id.layout);
        if (size.x < size.y) {
            layout.setBackgroundResource(R.drawable.splash);
        } else {
            layout.setBackgroundResource(R.drawable.splash_ls);
        }

        //Button goButton = findViewById(R.id.goButton);
        //goButton.setOnClickListener(v -> go());
        DB_Setting = new C_DB_Setting(this);
        HashUser = DB_Setting.get_Key("HashUser");
        if(DB_Setting.mSettings.getString("idUser","").equals("") || HashUser.equals(""))
            goPageLogin();else testProfile();
    }

    private void goPageLogin() {
        new Handler().postDelayed(() -> {
            Intent newActivity = new Intent(SplashActivity.this,
                    LoginActivity.class);
            startActivity(newActivity);
            finish();
        }, SPLASH_TIMEOUT);
    }

    private void testProfile() {
        ReqApiServices X =  new ReqApiServices();
        X.EventWhenRespon=Jwban1;
        X.SetAwal();
        X.urlBuilder.addPathSegments("profile/info");
        X.SetAwalRequest();
        X.request.header("Apphash", HashUser);
        final JSONObject Body = new JSONObject();
        X.SetFormBody_Post(Body);
        X.HitNoWait();
    }

    private final TerimaResponApi Jwban1 = new TerimaResponApi() {
        @Override
        public void OnSukses(ReqApiServices tool, JSONObject Data) {
            try {
                if(Data.getString("code").equals("00")){
                    final JSONObject User= Data.getJSONObject("message").getJSONObject("user");
                    DB_Setting.add("HashUser",User.getString("user_key"));
                    SharedPreferences.Editor Ubah = DB_Setting.EditorPref();
                    Ubah.putString("fullName", User.getString("full_name"));
                    Ubah.apply();
                    //---------------
                    Intent newIntent = new Intent(SplashActivity.this, MainActivity.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(newIntent);
                    overridePendingTransition(0, 0);
                    finish();
                }
                return;
            } catch (JSONException ignored) {}
            goPageLogin();
        }

        @Override
        public void onGagal(ReqApiServices tool, IOException e) {

        }
    };
}
