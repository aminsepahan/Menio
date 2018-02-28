package io.menio.android.utilities

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import io.menio.android.models.MenuModel
import io.menio.android.models.MerchantModel
import io.menio.android.models.RestaurantModel
import io.menio.android.utilities.Constants.*

/**
 * Created by Amin on 25/02/2018.
 *
 */
class AppController : Application() {

    val TAG = AppController::class.java
            .simpleName
    private var mRequestQueue: RequestQueue? = null
    var user: MerchantModel?
        get() {
            if (isSet(USER)) {
                return Gson().fromJson(getSP(USER), MerchantModel::class.java)
            } else {
                throw AssertionError("Set to null by another thread")
            }
        }
    set(value) { setSP(USER, Gson().toJson(value))}

    var restaurant: RestaurantModel?
        get() {
            if (isSet(SELECTED_BRANCH)) {
                return Gson().fromJson(getSP(SELECTED_BRANCH), RestaurantModel::class.java)
            } else {
                throw AssertionError("user haven't been set")
            }
        }
    set(value) { setSP(SELECTED_BRANCH, Gson().toJson(value))}

    var menu: MenuModel?
        get() {
            if (isSet(SELECTED_MENU)) {
                return Gson().fromJson(getSP(SELECTED_MENU), MenuModel::class.java)
            } else {
                throw AssertionError("user haven't been set")
            }
        }
    set(value) { setSP(SELECTED_MENU, Gson().toJson(value))}

    override fun onCreate() {
        super.onCreate()
        app = applicationContext as AppController
    }

    fun getRequestQueue(): RequestQueue? {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(applicationContext)
        }
        return mRequestQueue
    }

    fun <T> addToRequestQueue(req: com.android.volley.Request<T>, tag: String) {
        // set the default tag if tag is empty
        req.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        getRequestQueue()!!.add(req)
    }

    fun cancelPendingRequests(tag: Any) {
        if (mRequestQueue != null) {
            mRequestQueue!!.cancelAll(tag)
        }
    }

    fun setCurrentUser(user: MerchantModel) {
        setSP(USER, Gson().toJson(user))
    }


    fun getSP(key: String, defaultValue: String?): String? {
        val sp = getSharedPreferences(SP_FILE_NAME_BASE, Context.MODE_PRIVATE)
        return sp.getString(key, defaultValue)
    }

    fun getSP(key: String): String? {
        return getSP(key, FALSE)
    }

    fun isSet(key: String): Boolean {
        return getSP(key, FALSE) != FALSE
    }

    fun getSPBoolean(key: String): Boolean {
        val sp = getSharedPreferences(SP_FILE_NAME_BASE, Context.MODE_PRIVATE)
        return sp.getString(key, FALSE) == TRUE
    }

    fun getSPInt(key: String): Int {
        val sp = getSharedPreferences(SP_FILE_NAME_BASE, Context.MODE_PRIVATE)
        return sp.getInt(key, 0)
    }

    fun setSP(key: String, value: String) {
        val sp = getSharedPreferences(SP_FILE_NAME_BASE, Context.MODE_PRIVATE)
        val spe = sp.edit()
        spe.putString(key, value)
        spe.apply()
    }

    fun setSPInt(key: String, value: Int) {
        val sp = getSharedPreferences(SP_FILE_NAME_BASE, Context.MODE_PRIVATE)
        val spe = sp.edit()
        spe.putInt(key, value)
        spe.apply()
    }

    companion object {

        lateinit var app: AppController

        @Synchronized
        fun getInstance(): AppController {
            return app
        }
    }
}