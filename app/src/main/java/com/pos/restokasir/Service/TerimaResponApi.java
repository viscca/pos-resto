package com.pos.restokasir.Service;

import org.json.JSONObject;
import java.io.IOException;

public interface TerimaResponApi {
    void OnSukses(JSONObject Data);
    void onGagal(IOException e);
}
