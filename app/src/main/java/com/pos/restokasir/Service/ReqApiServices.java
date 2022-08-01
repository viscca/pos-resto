package com.pos.restokasir.Service;

import android.util.Log;

import java.io.IOException;
import okhttp3.*;

public class ReqApiServices {
    private final String TAG="Prs_Api";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded");
    public final String MainURL="https://resto.montaz.id/apps/v1";
    public final String Appkey= "P0s4pps$2o22";
    private OkHttpClient client = new OkHttpClient();
    public HttpUrl.Builder urlBuilder;
    public Request.Builder request;
    public Callback EventWhenRespon;

    public void SetAwal(){
        urlBuilder = HttpUrl.parse(MainURL).newBuilder();
    }

    public void SetAwalRequest () {
        request = new Request.Builder();
        final String sURL=urlBuilder.build().toString();
        request.header("Appkey", Appkey)
                .url(sURL);
        Log.d(TAG, "Set Awal Request. URL: "+sURL);
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
        client.newCall(request.build()).enqueue(EventWhenRespon);
    }
}