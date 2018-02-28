package io.menio.android.utilities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import io.menio.android.R;
import io.menio.android.interfaces.NetResponse;
import io.menio.android.interfaces.NetResponseJson;

import static io.menio.android.utilities.AppController.app;
import static io.menio.android.utilities.Constants.ACCESS_TOKEN;
import static io.menio.android.utilities.Constants.DEBUG;
import static io.menio.android.utilities.Constants.LOG_TAG;
import static io.menio.android.utilities.Constants.SELECTED_BRANCH_ID;
import static io.menio.android.utilities.Constants.VOLLEY_TIME_OUT;
import static io.menio.android.utilities.Snippets.isOnline;


/**
 * Created by Amin on 20/05/2016.
 *
 */
public class NetworkRequests {

    public static StringRequest getRequest(final String url, final NetResponse listener) {
        if (DEBUG) Log.d(LOG_TAG, " url = " + url);
        // creating volley string request
        final StringRequest strReq = new StringRequest(Request.Method.GET,
                url, response -> parseResponse(response, url, listener), error -> parseError(error, url, listener)) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return provideHeaders();
            }
        };


        strReq.setRetryPolicy(new DefaultRetryPolicy(
                VOLLEY_TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        if (isOnline(app)) {
            app.addToRequestQueue(strReq, "request");
        } else {
            listener.onError(null, app.getString(R.string.you_are_offline), false);
        }
        return strReq;

    }

    public static JsonObjectRequest getRequestJson(final String url, final NetResponseJson listener) {
        if (DEBUG) Log.d(LOG_TAG, " url = " + url);
        // creating volley string request
        final JsonObjectRequest strReq = new JsonObjectRequest(url, null,
                response -> parseResponse(response, url, listener),
                error -> parseError(error, url, listener)) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return provideHeaders();
            }
        };


        strReq.setRetryPolicy(new DefaultRetryPolicy(
                VOLLEY_TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        if (isOnline(app)) {
            app.addToRequestQueue(strReq, "request");
        } else {
            listener.onError(null, app.getString(R.string.you_are_offline), false);
        }
        return strReq;

    }


    public static void deleteRequest(final String url, final NetResponse listener) {
        if (DEBUG) Log.d(LOG_TAG, " url = " + url);

        // creating volley string request
        final StringRequest strReq = new StringRequest(Request.Method.DELETE,
                url, response -> {

            parseResponse(response, url, listener);
        }, error -> parseError(error, url, listener)) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return provideHeaders();
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                VOLLEY_TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (isOnline(app)) {
            app.addToRequestQueue(strReq, "request");
        } else {
            listener.onError(null, app.getString(R.string.you_are_offline), false);
        }

    }

    public static void getRequestWithKey(final String url, final NetResponse listener, final String key) {
        getRequest(url, new NetResponse() {
            @Override
            public void onResponse(String response) {
                if (DEBUG) Log.d(LOG_TAG, " response for url " + url + " ===== " + response);
                String responseToSend = null;
                try {
                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response);
                    responseToSend = jsonObject.get(key).toString();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
                if (responseToSend != null) {
                    listener.onResponse(responseToSend);
                } else {
                    listener.onError(null, app.getString(R.string.connection_error), true);
                }
            }

            @Override
            public void onError(VolleyError error, String message, boolean isOnline) {
                listener.onError(error, message, isOnline);
            }

        });
    }

    public static void postRequest(final String url, final NetResponse listener,
                                   final String tag, final Map<String, String> postParams) {


        if (DEBUG) Log.d(LOG_TAG, tag + " url = " + url);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, response -> parseResponse(response, url, listener), error -> parseError(error, url, listener)) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return provideHeaders();
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                VOLLEY_TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        if (isOnline(app)) {
            app.addToRequestQueue(strReq, "request");
        } else {
            listener.onError(null, app.getString(R.string.you_are_offline), false);
        }

    }

    public static void jsonPostRequest(final String url, final NetResponseJson listener,
                                       final JSONObject jsonPostData) {

        if (DEBUG) Log.d(LOG_TAG, url + "   and  post json is :  " + jsonPostData.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonPostData,
                jsonObjectResponse -> {
                    parseResponse(jsonObjectResponse, url, listener);
                }, error -> parseError(error, url, listener)) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return provideHeaders();
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                VOLLEY_TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        if (isOnline(app)) {
            app.addToRequestQueue(jsonObjectRequest, "request");
        } else {
            listener.onError(null, app.getString(R.string.you_are_offline), false);
        }
    }

    public static void jsonPutRequest(final String url, final NetResponseJson listener, final JSONObject jsonPostData) {

        if (DEBUG) {
            Log.d(LOG_TAG, url + "   and  post json is :  " + jsonPostData.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonPostData,
                jsonObjectResponse -> parseResponse(jsonObjectResponse, url, listener), error -> parseError(error, url, listener)) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return provideHeaders();
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                VOLLEY_TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        if (isOnline(app)) {
            app.addToRequestQueue(jsonObjectRequest, "request");
        } else {
            listener.onError(null, app.getString(R.string.you_are_offline), false);
        }
    }

    public static void jsonPostPutRequest(int method, final String url, final NetResponseJson listener,
                                          final JSONObject jsonPostData) {

        if (DEBUG) Log.d(LOG_TAG, url + "   and  post json is :  " + jsonPostData.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url, jsonPostData,
                jsonObjectResponse -> {
                    parseResponse(jsonObjectResponse, url, listener);
                }, error -> parseError(error, url, listener)) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return provideHeaders();
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                VOLLEY_TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        if (isOnline(app)) {
            app.addToRequestQueue(jsonObjectRequest, "request");
        } else {
            listener.onError(null, app.getString(R.string.you_are_offline), false);
        }
    }


    @NonNull
    private static Map<String, String> provideHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("User-agent", System.getProperty("http.agent"));
        if (app.isSet(ACCESS_TOKEN)) {
            headers.put("Authorization", "Bearer " + app.getSP(ACCESS_TOKEN));
            if (app.isSet(SELECTED_BRANCH_ID)) {
                headers.put("Restaurant-Id", app.getSP(SELECTED_BRANCH_ID));
            }
        }
        headers.put("OS", "Android");
        try {
            PackageManager manager = app.getPackageManager();
            PackageInfo info = manager.getPackageInfo(app.getPackageName(), 0);
            headers.put("VersionCode", String.valueOf(info.versionCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return headers;
    }


    private static void parseResponse(String response, String url, NetResponse listener) {
        if (!(response.contains("ا") || response.contains("ب") || response.contains("پ") ||
                response.contains("ت") || response.contains("ث") || response.contains("ج")
                || response.contains("چ") || response.contains("ح") || response.contains("خ")
                || response.contains("د") || response.contains("ذ") || response.contains("ر")
                || response.contains("ز") || response.contains("ژ") || response.contains("س")
                || response.contains("ش") || response.contains("ص") || response.contains("ض")
                || response.contains("ط") || response.contains("ظ") || response.contains("ع")
                || response.contains("غ") || response.contains("ف") || response.contains("ق")
                || response.contains("ک") || response.contains("گ") || response.contains("ل")
                || response.contains("م") || response.contains("ن") || response.contains("و")
                || response.contains("ه") || response.contains("ی"))) {
            try {
                response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if (DEBUG) Log.d(LOG_TAG, " response for url " + url + " ===== " + response);
        listener.onResponse(response);
    }

    private static void parseResponse(JSONObject response, String url, NetResponseJson listener) {
        if (DEBUG) Log.d(LOG_TAG, " response for url " + url + " ===== " + response);
        listener.onResponse(response);
    }

    private static void parseError(VolleyError error, String url, NetResponse listener) {
        String message = "";
        if (error.networkResponse != null) {
            if (error.networkResponse.data != null) {
                message = new String(error.networkResponse.data);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonObject != null && jsonObject.has("error")) {
                    try {
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (DEBUG) {
                message = message + error.networkResponse.statusCode;
            }
            if (message.contains("html")) {
                Log.d(LOG_TAG, " error for url " + url + message);
                message = app.getString(R.string.connection_error);
            }
        } else {
            message = app.getString(R.string.connection_error);
        }
        if (DEBUG) {
            Log.d(LOG_TAG, " error for url " + url + message);
        }
        listener.onError(error, message, true);
    }

    private static void parseError(VolleyError error, String url, NetResponseJson listener) {
        parseError(error, url, new NetResponse() {
            @Override
            public void onResponse(@NotNull String response) {

            }

            @Override
            public void onError(@Nullable VolleyError error, @NotNull String message, boolean isOnline) {
                listener.onError(error, message, isOnline);
            }
        });
    }
}

