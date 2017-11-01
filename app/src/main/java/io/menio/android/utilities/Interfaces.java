package io.menio.android.utilities;

import com.android.volley.VolleyError;

/**
 * Created by Amin on 20/05/2016.
 * this the interface between fragments and main activity
 */
public class Interfaces {


    public interface NetworkListeners {
        public void onResponse(String response);
        public void onError(VolleyError error, String message, boolean isOnline);
    }


    public interface messageOkListener {
        public void onOkClicked();
    }

    public interface CallBack {
        public void call();
    }
}
