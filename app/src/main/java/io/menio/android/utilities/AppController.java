package io.menio.android.utilities;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/**
 * Created By Amin 11/1/2017
 */

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();
    public static AppController mInstance;

    private RequestQueue mRequestQueue;
    public static Context applicationContext = null;


    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        mInstance = this;
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(com.android.volley.Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
