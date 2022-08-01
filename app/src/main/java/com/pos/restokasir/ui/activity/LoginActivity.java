package com.pos.restokasir.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pos.restokasir.R;
import com.pos.restokasir.Service.ReqApiServices;
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
    C_DB_Setting DB_Setting = new C_DB_Setting(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate " + savedInstanceState);
        setContentView(R.layout.activity_login);

        eUserID = findViewById(R.id.eUserID);
        ePassword = findViewById(R.id.ePassword);

        btnOK = findViewById(R.id.btnlogin);
        btnOK.setOnClickListener(DiKlik);
    }

    private final Callback Jwban1 = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();
                    OlahRespon(responseString);
                } else {
                    Log.d(TAG, "Error "+ response);
                }
            } catch (IOException e) {
                Log.d(TAG, "Exception caught : ", e);
            }
        }
    };

    private void  OlahRespon(String Respon){
        JSONObject js;
        Log.d(TAG, "Respon: "+ Respon);
        try {
            js = new JSONObject(Respon);
            if(js.getString("code").equals("00")){
                String x= js.getJSONObject("message").getJSONObject("user").getString("user_key");
                DB_Setting.add("HashUser",x);

                Intent newIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(newIntent);
            }
        } catch (JSONException e) {
            return;
        }
    }

    private final View.OnClickListener DiKlik= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v==btnOK) {
                ReqApiServices X =  new ReqApiServices();
                X.EventWhenRespon=Jwban1;
                X.SetAwal();
                X.urlBuilder.addPathSegments("login/request");
                X.SetAwalRequest();
                X.request.post(new FormBody.Builder()
                        .add("email", eUserID.getText().toString())
                        .add("password", ePassword.getText().toString())
                        .build());
                //kasir@test.com
                X.HitNoWait();
            }
        }
    };
}
