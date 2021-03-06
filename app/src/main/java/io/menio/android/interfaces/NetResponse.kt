package io.menio.android.interfaces

import android.view.View
import com.android.volley.VolleyError

/**
 * Created by Amin on 01/11/2017.
 */
interface NetResponse {
    abstract fun onResponse(response: String)
    abstract fun onError(error: VolleyError?, message: String, isOnline :Boolean)
}