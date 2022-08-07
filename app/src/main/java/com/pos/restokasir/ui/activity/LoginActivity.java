package com.pos.restokasir.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pos.restokasir.R;
import com.pos.restokasir.Service.ReqApiServices;
import com.pos.restokasir.Service.TerimaResponApi;
import com.pos.restokasir.db_sqlite.C_DB_Setting;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class LoginActivity extends Activity {
    private final String TAG="Prs_LoginActivity";
    Button btnOK;
    EditText eUserID, ePassword;
    private C_DB_Setting DB_Setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate " + savedInstanceState);
        setContentView(R.layout.activity_login);

        eUserID = findViewById(R.id.eUserID);
        ePassword = findViewById(R.id.ePassword);

        btnOK = findViewById(R.id.btnlogin);
        btnOK.setOnClickListener(DiKlik);

        DB_Setting = new C_DB_Setting(this);
    }

    private final TerimaResponApi Jwban1 = new TerimaResponApi() {
        @Override
        public void OnSukses(JSONObject Data) {
            try {
                if(Data.getString("code").equals("00")){
                    final JSONObject User= Data.getJSONObject("message").getJSONObject("user");
                    DB_Setting.add("HashUser",User.getString("user_key"));
                    SharedPreferences.Editor Ubah = DB_Setting.EditorPref();
                    Ubah.putString("fullName", User.getString("full_name"));
                    Ubah.apply();
                    //---------------
                    Intent newIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(newIntent);
                }
                return;
            } catch (JSONException e) {}
        }

        @Override
        public void onGagal(IOException e) {

        }
    };

    private final View.OnClickListener DiKlik= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v==btnOK) {
                SharedPreferences.Editor Ubah = DB_Setting.EditorPref();
                Ubah.putString("idUser", eUserID.getText().toString());
                Ubah.apply();
                //----
                ReqApiServices X =  new ReqApiServices();
                X.EventWhenRespon=Jwban1;
                X.SetAwal();
                X.urlBuilder.addPathSegments("login/request");
                X.SetAwalRequest();
                X.request.post(new FormBody.Builder()
                        .add("email", DB_Setting.mSettings.getString("idUser",""))
                        .add("password", ePassword.getText().toString())
                        .build());
                //kasir@test.com
                X.HitNoWait();
            }
        }
    };
}
