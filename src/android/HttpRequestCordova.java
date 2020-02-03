package br.com.fabrica704.http;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpRequestCordova extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args,
                           CallbackContext callbackContext) throws JSONException {

        if (action.equals("requestGet")) {
            JSONObject headers = args.getJSONObject(0).getJSONObject("headers");
            String url = args.getJSONObject(0).getString("url");
            requestApiGET(url, headers, callbackContext);
            return true;
        }

        if (action.equals("requestPost")) {
            JSONObject headers = args.getJSONObject(0).getJSONObject("headers");
            JSONObject data = args.getJSONObject(0).getJSONObject("data");
            String url = args.getJSONObject(0).getString("url");
            requestApiPOST(url, headers, data, callbackContext);
            return true;
        }


        return false; // Returning false results in a "MethodNotFound" error.
    }

    private void requestApiGET(String url, JSONObject objectHeaders, CallbackContext callbackContext) {
        try {
            Map<String, String> headers = new HashMap<>();

            Iterator<String> keys = objectHeaders.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                headers.put(key, objectHeaders.getString(key));
            }

            RequestQueue requestQueue = Volley.newRequestQueue(cordova.getContext());

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        callbackContext.success(new JSONObject(response));
                        Log.i("VOLLEY", response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callbackContext.error(error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return null;
                }
            };

            requestQueue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestApiPOST(String url, JSONObject objectHeaders, JSONObject objectData, CallbackContext callbackContext) {
        try {
            Map<String, String> headers = new HashMap<>();

            Iterator<String> keys = objectHeaders.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                headers.put(key, objectHeaders.getString(key));
            }

            final String requestBody = objectData.toString();


            RequestQueue requestQueue = Volley.newRequestQueue(cordova.getContext());

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        callbackContext.success(new JSONObject(response));
                        Log.i("VOLLEY", response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callbackContext.error(error.toString());
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}