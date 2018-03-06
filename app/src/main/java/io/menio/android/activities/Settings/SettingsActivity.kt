package io.menio.android.activities.Settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import com.android.volley.VolleyError
import com.bumptech.glide.Glide
import com.github.florent37.viewanimator.ViewAnimator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import io.menio.android.R
import io.menio.android.activities.Menu.MenuActivity
import io.menio.android.interfaces.NetResponseJson
import io.menio.android.models.MenuModel
import io.menio.android.utilities.AppController
import io.menio.android.utilities.Constants.*
import io.menio.android.utilities.NetworkRequests
import io.menio.android.utilities.Snippets
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.item_menu.view.*
import org.json.JSONObject

class SettingsActivity : AppCompatActivity() {


    private var selectedItem: View? = null;
    private var modelList: MutableList<MenuModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        tableNumber.setText(AppController.app.getSP(TABLE_NUMBER, "1"))
        downloadMenus()
        Snippets.setColorForProgress(btnProgress, resources.getColor(R.color.white))
    }

    private fun downloadMenus() {
        progress.visibility = View.VISIBLE
        NetworkRequests.getRequestJson(getMenuUrl(AppController.app.getSP(SELECTED_LANGUAGE_CODE)),
                object : NetResponseJson {
                    override fun onResponse(json: JSONObject) {
                        progress.visibility = View.GONE
                        modelList = Gson().fromJson<MutableList<MenuModel>>(json.getString("data"),
                                object : TypeToken<List<MenuModel>>() {}.type)
                        populateModelList(modelList!!)
                    }

                    override fun onError(error: VolleyError?, message: String, isOnline: Boolean) {
                        Snippets.showError(this@SettingsActivity, message, R.string.retry, { downloadMenus() }, true)
                    }

                })
    }

    private fun populateModelList(modelList: MutableList<MenuModel>) {

        for ((index, menuModel) in modelList.withIndex()) {
            var row = LayoutInflater.from(this).inflate(R.layout.item_menu, baseLinLay, false)
            row.title.text = menuModel.name
            Picasso.with(this).load(menuModel.thumbnailUrl).into(row.image)
            row.setOnClickListener({ selectMenu(row, menuModel) })
            if (menuModel.id == AppController.app.getSP(SELECTED_MENU_ID)) {
                selectMenu(row, menuModel)
            }
            if (index < 3) {
                baseLinLay.addView(row)
            } else if (index < 6) {
                baseLinLay2.addView(row)
            } else {
                break
            }
        }

        save.setOnClickListener({
            AppController.app.setSP(TABLE_NUMBER, tableNumber.text.toString())
            if (selectedItem!!.tag as String != AppController.app.getSP(SELECTED_MENU_ID)) {
                downloadSelectedMenu()
            } else {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        })
        cancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

    }


    private fun selectMenu(selectedView: View, menu: MenuModel) {
        if (selectedItem != null && selectedItem != selectedView) {
            ViewAnimator.animate(selectedItem!!.menuItemBack1).fadeOut().duration(200).onStop {
                selectedItem = selectedView
                selectedItem!!.tag = menu.id
                ViewAnimator.animate(selectedView.menuItemBack1).fadeIn().duration(200).start()
            }.start()
        } else {
            selectedItem = selectedView
            selectedItem!!.tag = menu.id
            ViewAnimator.animate(selectedView.menuItemBack1).fadeIn().duration(200).start()
        }
    }

    private fun downloadSelectedMenu() {
        btnProgress.visibility = View.VISIBLE
        save.text = ""
        NetworkRequests.getRequestJson(getMenuUrl(selectedItem!!.tag as String,
                AppController.app.getSP(SELECTED_LANGUAGE_CODE), AppController.app.getSP(SELECTED_CURRENCY_CODE)),
                object : NetResponseJson {
                    override fun onResponse(json: JSONObject) {
                        btnProgress.visibility = View.GONE
                        save.text = getString(R.string.confirm)
                        AppController.app.menu = Gson().fromJson(json.toString(), MenuModel::class.java)
                        Glide.with(this@SettingsActivity).download(AppController.app.menu!!.backgroundUrl)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onError(error: VolleyError?, message: String, isOnline: Boolean) {
                        btnProgress.visibility = View.GONE
                        save.text = getString(R.string.confirm)
                        Snippets.showError(this@SettingsActivity, message, R.string.retry, { downloadSelectedMenu() }, true)
                    }

                }
        )
    }

    companion object {

        fun open(activity: Activity, isFromCategoryActivity: Boolean) {
            val intent = Intent(activity, SettingsActivity::class.java)
            intent.putExtra(IS_FROM_CATEGORY, isFromCategoryActivity)
            activity.startActivityForResult(intent, SETTING_REQ_CODE);
        }
    }
}
