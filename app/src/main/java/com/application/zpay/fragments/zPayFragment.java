package com.application.zpay.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.application.zpay.R;
import com.application.zpay.interfaces.iNetworkCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gaganjot Singh on 26/06/2020.
 */
public class zPayFragment extends BaseFragment {

    ProgressDialog progress;

    public void showProgressing(Activity context) {
        if (this.progress != null && this.progress.isShowing()) {
            this.progress.dismiss();
            this.progress = null;
        }
        this.progress = new ProgressDialog(context);
        this.progress.setCanceledOnTouchOutside(false);
        this.progress.setCancelable(false);
        this.progress.setTitle(context.getString(R.string.loading));
        this.progress.setMessage(context.getString(R.string.please_wait));
        this.progress.show();
    }

    public void hideProgressing() {
        if (this.progress != null && this.progress.isShowing()) {
            this.progress.dismiss();
        }
    }

    public void callService(String url, final iNetworkCallback networkCallback) {
        Map<Object, Object> params = new HashMap<>();
        networkCallback.addParameters(params);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,url, new JSONObject(params), new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {
                    networkCallback.response(response);
                    VolleyLog.v("Response:%n %s", response.toString(4));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        Log.e("Obj",obj.toString());
                        networkCallback.response(obj);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
                else{
                    networkCallback.failure(error);

                  /*  Log.d("Helo", "Error: " + error
                            + "\nStatus Code " + error.networkResponse.statusCode
                            + "\nResponse Data " + Arrays.toString(error.networkResponse.data)
                            + "\nCause " + error.getCause()
                            + "\nmessage" + error.getMessage());*/
                }

            }

        }){
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        req.setRetryPolicy(new DefaultRetryPolicy(35000, 0, 1.0f));
        requestQueue.add(req);
        requestQueue.getCache().clear();

    }

    public void callServiceGet(String url, final iNetworkCallback networkCallback) {
        Map<Object, Object> params = new HashMap<>();
        networkCallback.addParameters(params);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,url, new JSONObject(params), new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {
                    networkCallback.response(response);
                    VolleyLog.v("Response:%n %s", response.toString(4));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        Log.e("Obj",obj.toString());
                        networkCallback.response(obj);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
                else{
                    networkCallback.failure(error);

                  /*  Log.d("Helo", "Error: " + error
                            + "\nStatus Code " + error.networkResponse.statusCode
                            + "\nResponse Data " + Arrays.toString(error.networkResponse.data)
                            + "\nCause " + error.getCause()
                            + "\nmessage" + error.getMessage());*/
                }





             /*   networkCallback.failure(error);
                VolleyLog.e("Error: ", error.getMessage());*/
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        req.setRetryPolicy(new DefaultRetryPolicy(35000, 0, 1.0f));
        requestQueue.add(req);
        requestQueue.getCache().clear();

    }

    public void response(JSONObject response) {
    }

    public void failure(VolleyError error) {
    }

    public void addParameters(Map<String, String> map) {
    }

}
