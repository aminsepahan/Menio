package io.menio.android.interfaces

import android.view.View
import com.android.volley.VolleyError
import org.json.JSONObject

/**
 * Created by Amin on 01/11/2017.
 */
interface NetResponseJson {
    abstract fun onResponse(json: JSONObject)
    abstract fun onError(error: VolleyError?, message: String, isOnline :Boolean)
}