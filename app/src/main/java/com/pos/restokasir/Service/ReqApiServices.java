package com.pos.restokasir.Service;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.*;

public class ReqApiServices {
    private final String TAG="Prs_Api";
    private final ReqApiServices AlatIni=this;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded");
    public final String MainURL="https://resto.montaz.id/apps/v1";
    public final String Appkey= "P0s4pps$2o22";
    private OkHttpClient client = new OkHttpClient();
    public HttpUrl.Builder urlBuilder;
    public Request.Builder request;
    public Integer KodePath;
    public Integer JmlErr;
    public TerimaResponApi EventWhenRespon;

    public ReqApiServices() {
        SetKodePath_Acak(); JmlErr=0;
    }

    public void SetKodePath_Acak(){
        int I= 1000;
        KodePath= (int) (Math.random() * I + 1);
    }

    public void SetAwal(){
        urlBuilder = HttpUrl.parse(MainURL).newBuilder();
    }

    public void SetAwalRequest () {
        request = new Request.Builder();
        final String sURL=urlBuilder.build().toString();
        request.header("Appkey", Appkey)
                .url(sURL);
        Log.d(TAG, "Set Awal Request ("+KodePath+"). URL: "+sURL);
    }

    public String SendRequest() throws IOException {
        Response response = client.newCall(request.build()).execute();
        return response.body().string();
    }

    public void SetBodyJSON (String json)  {
        RequestBody body = RequestBody.create(JSON, json);
        request.post(body);
    }

    public void SetBodyFormData (String qs)  {
        RequestBody body = RequestBody.create(FORM,qs);
        request.post(body);
    }

    public void HitNoWait() {
        client.newCall(request.build()).enqueue(Jwban1);
    }

    private final Callback Jwban1 = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            if (EventWhenRespon!=null) EventWhenRespon.onGagal(AlatIni, e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();
                    Log.d(TAG, "Respon ("+KodePath+"): "+ responseString);
                    OlahRespon(responseString);
                } else {
                    Log.d(TAG, "Error ("+KodePath+"): "+ response);
                }
            } catch (IOException e) {
                Log.d(TAG, "Exception caught ("+KodePath+ "): ", e);
            }
        }
    };

    private void  OlahRespon(String Respon){
        JSONObject js;
        try {
            js = new JSONObject(Respon);
            if (EventWhenRespon!=null) EventWhenRespon.OnSukses(AlatIni,js);
        } catch (JSONException e) {}
    }

    public void SetFormBody_Post(@NonNull JSONObject data) {
        final FormBody.Builder Body = new FormBody.Builder();
        try {
            for (Iterator<String> iter = data.keys(); iter.hasNext(); ) {
                String key = iter.next();
                Body.add(key, data.getString(key));
            }
            request.post(Body.build());
            Log.d(TAG, "POST Form ("+KodePath+"): "+ data.toString());
        } catch (JSONException ignored) {}
    }

}
