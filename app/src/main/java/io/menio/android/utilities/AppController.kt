package io.menio.android.utilities

import android.app.Activity
import android.app.Application
import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import io.menio.android.R
import io.menio.android.activities.menu.CategoryActivity
import io.menio.android.activities.menu.MenuActivity
import io.menio.android.activities.settings.SettingsActivity
import io.menio.android.models.*
import io.menio.android.network.Webservice
import io.menio.android.utilities.Constants.*
import org.greenrobot.eventbus.EventBus
import kotlin.properties.Delegates


/**
 * Created by Amin on 25/02/2018.
 *
 */
class AppController : Application() {


    val cons by lazy { ConstantsKotlin() }
    val webService by lazy {
        Webservice.create()
    }

    private var mRequestQueue: RequestQueue? = null

    private var _language: LanguageModel? = null
    var language: LanguageModel?
        get() {
            if (_language == null) {
                if (isSet(SELECTED_LANGUAGE)) {
                    _language = Gson().fromJson(getSP(SELECTED_LANGUAGE), LanguageModel::class.java)
                } else {
                    return null
                }
            }
            return _language
        }
        set(value) {
            _language = value
            setSP(SELECTED_LANGUAGE, Gson().toJson(value))
        }

    private var _user: MerchantModel? = null
    var user: MerchantModel?
        get() {
            if (_user == null) {
                if (isSet(USER)) {
                    _user = Gson().fromJson(getSP(USER), MerchantModel::class.java)
                } else {
                    return null
                }
            }
            return _user
        }
        set(value) {
            _user = value
            setSP(USER, Gson().toJson(value))
        }

    private var _branchId: String? = null
    var branchId: String?
        get() {
            if (_branchId == null) {
                if (isSet(SELECTED_BRANCH_ID)) {
                    _branchId = getSP(SELECTED_BRANCH_ID)
                } else {
                    return null
                }
            }
            return _branchId
        }
        set(value) {
            _branchId = value
            setSP(BRANCH_ID, value!!)
        }

    private var _category: CategoryModel? = null
    var category: CategoryModel?
        get() {
            if (_category == null) {
                if (isSet(SELECTED_CATEGORY)) {
                    _category = Gson().fromJson(getSP(SELECTED_CATEGORY), CategoryModel::class.java)
                } else {
                    return null
                }
            }
            return _category
        }
        set(value) {
            _category = value
            setSP(SELECTED_CATEGORY, Gson().toJson(value))
        }
    var shoppingCart by Delegates.observable<ShoppingCartModel?>(null) { _, old, new ->
        new?.lineItems?.let {
            for (item in new.lineItems) {
                for (cat in menu!!.categories) {
                    for (menuItem in cat.menuItems) {
                        if (menuItem.id == item.menuItemId) {
                            if (menuItem.qty != item.qty) {
                                if (item.variationId != null && menuItem.variations != null) {
                                    for (variation in menuItem.variations!!) {
                                        if (variation.id == item.variationId) {
                                            variation.qty = item.qty
                                            break
                                        }
                                    }
                                } else {
                                    menuItem.qty = item.qty
                                }
                                EventBus.getDefault().post(CartUpdateEvent(item.menuItemId, item.qty))
                            }
                        }
                    }
                }
            }
            EventBus.getDefault().post(CartUpdateEvent("", new.itemsCount))
        }
        old?.lineItems?.let {
            if (new?.lineItems != null) {
                for (item in old.lineItems) {
                    var found = false
                    for (newItem in new.lineItems) {
                        if (newItem.menuItemId == item.menuItemId) found = true
                    }
                    if (!found) {
                        EventBus.getDefault().post(CartUpdateEvent(item.menuItemId, 0))
                        for (cat in menu!!.categories) {
                            for (menuItem in cat.menuItems) {
                                if (menuItem.id == item.menuItemId) {
                                    if (item.variationId != null) {
                                        for (variation in menuItem.variations!!) {
                                            menuItem.qty = menuItem.qty - variation.qty
                                            variation.qty = 0
                                        }
                                    } else {
                                        menuItem.qty = 0
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                EventBus.getDefault().post(CartUpdateEvent("", 0))
                for (item in old.lineItems) {
                    for (cat in menu!!.categories) {
                        for (menuItem in cat.menuItems) {
                            if (menuItem.id == item.menuItemId) {
                                if (item.variationId != null) {
                                    for (variation in menuItem.variations!!) {
                                        menuItem.qty = menuItem.qty - variation.qty
                                        variation.qty = 0
                                    }
                                } else {
                                    menuItem.qty = 0
                                }
                            }
                        }
                    }
                    EventBus.getDefault().post(CartUpdateEvent(item.menuItemId, 0))
                }
            }
        }
    }


    private var _restaurant: RestaurantModel? = null
    var restaurant: RestaurantModel?
        get() {
            if (_restaurant == null) {
                if (isSet(SELECTED_BRANCH)) {
                    _restaurant = Gson().fromJson(getSP(SELECTED_BRANCH), RestaurantModel::class.java)
                } else {
                    return null
                }
            }
            return _restaurant
        }
        set(value) {
            _restaurant = value
            setSP(SELECTED_BRANCH, Gson().toJson(value))
        }

    var tableNumber: Int
        get() {
            return if (getSP(cons.TABLE_NUMBER) != FALSE) {
                getSP(cons.TABLE_NUMBER).toInt()
            } else {
                1
            }
        }
        set(value) {
            setSP(cons.TABLE_NUMBER, value.toString())
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
        Log.d(cons.LOG_TAG + "   token   =", getSP(ACCESS_TOKEN))
    }

    override fun attachBaseContext(base: Context?) {

        val sp = base!!.getSharedPreferences(SP_FILE_NAME_BASE, Context.MODE_PRIVATE)
        if (sp.getString(SELECTED_LANGUAGE, FALSE) != FALSE) {
            _language = Gson().fromJson(sp.getString(SELECTED_LANGUAGE, FALSE), LanguageModel::class.java)
            super.attachBaseContext(LocaleHelper.onAttach(base, language!!.code))
        } else {
            super.attachBaseContext(base)
        }
    }

    fun getRequestQueue(): RequestQueue? {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(applicationContext)
        }
        return mRequestQueue
    }

    fun <T> addToRequestQueue(req: com.android.volley.Request<T>, tag: String) {
        // set the default tag if tag is empty
        req.tag = if (TextUtils.isEmpty(tag)) cons.LOG_TAG else tag
        getRequestQueue()!!.add(req)
    }

    fun cancelPendingRequests(tag: Any) {
        if (mRequestQueue != null) {
            mRequestQueue!!.cancelAll(tag)
        }
    }

    fun passwordDialog(activity: Activity) {
        if (!isSet(USER_PASS)) {
            SettingsActivity.open(activity)
            return
        }
        val convertView = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_check_password, null, false)
        val input = convertView.findViewById<View>(R.id.input) as EditText
        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                input.error = null
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        MaterialDialog.Builder(activity).customView(convertView, true)
                .onPositive { _, _ ->
                    run {
                        if (input.text.toString() == getSP(Constants.USER_PASS) || input.text.toString() == "987654")
                            SettingsActivity.open(activity)
                        else
                            input.error = getString(R.string.wrong_password)
                    }
                }.positiveText(getString(R.string.confirm))
                .buttonRippleColor(resources.getColor(R.color.colorPrimaryDark))
                .negativeColor(resources.getColor(R.color.colorPrimaryDark))
                .positiveColor(resources.getColor(R.color.colorPrimaryDark))
                .negativeText(getString(R.string.back))
                .typeface("theme.ttf", "theme_light.ttf").build().show()

    }

    fun setCurrentUser(user: MerchantModel) {
        setSP(USER, Gson().toJson(user))
    }


    fun getSP(key: String, defaultValue: String?): String {
        val sp = getSharedPreferences(SP_FILE_NAME_BASE, Context.MODE_PRIVATE)
        defaultValue?.let { return sp.getString(key, defaultValue) }
        return sp.getString(key, FALSE)
    }

    fun getSP(key: String): String {
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

    fun changeLanguage(activity: Activity) {
        val prevLanguage = language!!.name
        val languageList: MutableList<String> = emptyList<String>().toMutableList()
        restaurant!!.languages.forEach { languageList.add(it.name) }
        MaterialDialog.Builder(activity)
                .title(R.string.select_your_language)
                .items(languageList)
                .itemsCallbackSingleChoice(-1, MaterialDialog.ListCallbackSingleChoice { dialog, view, which, text ->
                    language = restaurant!!.languages.find { it.name == languageList[which] }
                    if (prevLanguage != language!!.name) {
                        if (activity is CategoryActivity) {
                            activity.languageChanged()
                        } else if (activity is MenuActivity) {
                            activity.downloadSelectedMenu()
                        }
                    }
                    true
                })
                .positiveText(R.string.choose_en)
                .negativeText(R.string.back_en)
                .show()
    }
}