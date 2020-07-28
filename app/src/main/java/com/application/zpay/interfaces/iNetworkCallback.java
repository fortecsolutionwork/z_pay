package com.application.zpay.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

public interface iNetworkCallback {
    void addParameters(Map<Object, Object> params);

    void failure(VolleyError volleyError);

    void response(JSONObject response);
}
