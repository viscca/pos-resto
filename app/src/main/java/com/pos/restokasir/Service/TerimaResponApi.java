package com.pos.restokasir.Service;

import org.json.JSONObject;
import java.io.IOException;

public interface TerimaResponApi {
    void OnSukses(ReqApiServices tool, JSONObject Data);
    void onGagal(ReqApiServices tool, IOException e);
}
