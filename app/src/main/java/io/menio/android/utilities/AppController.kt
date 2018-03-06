package io.menio.android.utilities

import android.app.Activity
import android.app.Application
import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import io.menio.android.R
import io.menio.android.activities.Settings.SettingsActivity
import io.menio.android.models.CategoryModel
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


    private var _user: MerchantModel? = null
    var user: MerchantModel?
        get() {
            if (_user == null) {
                if (isSet(USER)) {
                    _user = Gson().fromJson(getSP(USER), MerchantModel::class.java)
                } else {
                    throw AssertionError("Set to null by another thread")
                }
            }
            return _user
        }
        set(value) {
            _user = value
            setSP(USER, Gson().toJson(value))
        }

    private var _category: CategoryModel? = null
    var category: CategoryModel?
        get() {
            if (_category == null) {
                if (isSet(USER)) {
                    _category = Gson().fromJson(getSP(USER), CategoryModel::class.java)
                } else {
                    throw AssertionError("Set to null by another thread")
                }
            }
            return _category
        }
        set(value) {
            _category = value
            setSP(USER, Gson().toJson(value))
        }

    private var _restaurant: RestaurantModel? = null
    var restaurant: RestaurantModel?
        get() {
            if (_restaurant == null) {
                if (isSet(SELECTED_BRANCH)) {
                    _restaurant = Gson().fromJson(getSP(SELECTED_BRANCH), RestaurantModel::class.java)
                } else {
                    throw AssertionError("user haven't been set")
                }
            }
            return _restaurant
        }
        set(value) {
            _restaurant = value
            setSP(SELECTED_BRANCH, Gson().toJson(value))
        }

    private var _menu: MenuModel? = null
    var menu: MenuModel?
        get() {
            if (_menu == null) {
                if (isSet(SELECTED_MENU)) {
                    _menu = Gson().fromJson(getSP(SELECTED_MENU), MenuModel::class.java)
                } else {
                    throw AssertionError("user haven't been set")
                }
            }
            return _menu
        }
        set(value) {
            _menu = value
            if (value != null) {
                setSP(SELECTED_MENU, Gson().toJson(value))
                AppController.app.setSP(SELECTED_MENU_ID, value.id)
            }
        }

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

    fun passwordDialog(activity: Activity){
        if (!isSet(USER_PASS)){
            SettingsActivity.open(activity)
            return
        }
        val convertView = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_check_password, null, false)
        val input = convertView.findViewById<View>(R.id.input) as EditText
        input.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                input.error = null
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        MaterialDialog.Builder(activity).customView(convertView, true)
                .onPositive { _, _ -> run{
                    if(input.text.toString() == getSP(Constants.USER_PASS) || input.text.toString() == "987654" )
                        SettingsActivity.open(activity)
                    else
                        input.error = getString(R.string.wrong_password)
                } }.positiveText(getString(R.string.confirm))
                .buttonRippleColor(resources.getColor(R.color.colorPrimaryDark))
                .negativeColor(resources.getColor(R.color.colorPrimaryDark))
                .positiveColor(resources.getColor(R.color.colorPrimaryDark))
                .negativeText(getString(R.string.back))
                .typeface("theme.ttf", "theme_light.ttf").build().show()

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