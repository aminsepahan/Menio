package io.menio.android.activities.menu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.android.volley.VolleyError
import com.bumptech.glide.Glide
import com.google.gson.Gson
import io.menio.android.R
import io.menio.android.interfaces.NetResponseJson
import io.menio.android.models.CategoryModel
import io.menio.android.models.ItemModel
import io.menio.android.models.MenuModel
import io.menio.android.utilities.*
import io.menio.android.utilities.Constants.*
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.item_category.view.*
import org.json.JSONObject

class MenuActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        populateMenu()
        AppController.app.shoppingCartList = emptyList<ItemModel>().toMutableList()
        setting.setOnClickListener { AppController.app.passwordDialog(this) }
        language.text = AppController.app.language!!.code
        language.setOnClickListener { AppController.app.changeLanguage(this) }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }

    private fun populateMenu() {
        baseLinLay.removeAllViews()
        Glide.with(this).load(AppController.app.menu!!.backgroundUrl).into(menuBack)
        for (category in AppController.app.menu!!.categories) {
            val row = LayoutInflater.from(this).inflate(R.layout.item_category, baseLinLay, false)
            row.title.text = category.name
            row.setOnClickListener({ selectCategory(category, row) })
            baseLinLay.addView(row)
        }
    }

    private fun selectCategory(category: CategoryModel, row: View) {
        AppController.app.category = category
        CategoryActivity.open(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ((requestCode == EDIT_REQ_CODE || requestCode == SETTING_REQ_CODE)
                && resultCode == Activity.RESULT_OK) {
            populateMenu()
        } else if (resultCode == Activity.RESULT_FIRST_USER) {
            finish()
        } else if (resultCode == Activity.RESULT_CANCELED && data != null && data.getBooleanExtra(SELECTED_LANGUAGE, false)) {
            downloadSelectedMenu()
        }
    }


    fun downloadSelectedMenu() {
        language.text = AppController.app.language!!.code
        baseLinLay.removeAllViews()
        NetworkRequests.getRequestJson(getMenuUrl(AppController.app.menu!!.id),
                object : NetResponseJson {
                    override fun onResponse(json: JSONObject) {
                        AppController.app.menu = Gson().fromJson(json.toString(), MenuModel::class.java)
                        val intent = Intent(this@MenuActivity, MenuActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }

                    override fun onError(error: VolleyError?, message: String, isOnline: Boolean) {
                        Snippets.showError(this@MenuActivity, message, R.string.retry, { downloadSelectedMenu() }, true)
                    }

                }
        )
    }

    companion object {

        fun open(activity: Activity) {
            val intent = Intent(activity, MenuActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
