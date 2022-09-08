package com.pos.restokasir.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pos.restokasir.R;
import com.pos.restokasir.Service.ReqApiServices;
import com.pos.restokasir.Service.TerimaResponApi;
import com.pos.restokasir.db_sqlite.C_DB_Setting;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;

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
        public void OnSukses(ReqApiServices tool, JSONObject Data) {
            try {
                String code=Data.getString("code");
                if(code.equals("97")){
                    code=Data.getJSONObject("message").getString("error");
                    Log.d(TAG,code);
                    BuatToast(code);
                }else if(code.equals("00")){
                    final JSONObject User= Data.getJSONObject("message").getJSONObject("user");
                    DB_Setting.add("HashUser",User.getString("user_key"));
                    SharedPreferences.Editor Ubah = DB_Setting.EditorPref();
                    Ubah.putString("fullName", User.getString("full_name"));
                    Ubah.apply();
                    //---------------
                    Intent newIntent = new Intent(LoginActivity.this, MainActivity.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(newIntent);
                }
            } catch (JSONException ignored) {}
        }

        @Override
        public void onGagal(ReqApiServices tool, IOException e) {

        }
    };

    private void BuatToast(String Txt){
        Runnable UpdateUI = () -> Toast.makeText(LoginActivity.this, Txt, Toast.LENGTH_LONG ).show();
        runOnUiThread(UpdateUI);
    }

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
                final JSONObject Body = new JSONObject();
                try{
                    Body.put("email", DB_Setting.mSettings.getString("idUser",""))
                        .put("password", ePassword.getText().toString());
                    //kasir@test.com
                } catch (JSONException ignored) {}
                X.SetFormBody_Post(Body);
                X.HitNoWait();
            }
        }
    };
}
