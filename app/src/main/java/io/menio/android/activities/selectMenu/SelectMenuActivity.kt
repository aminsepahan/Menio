package io.menio.android.activities.selectMenu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.android.volley.VolleyError
import com.bumptech.glide.Glide
import com.github.florent37.viewanimator.ViewAnimator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import io.menio.android.R
import io.menio.android.activities.menu.MenuActivity
import io.menio.android.interfaces.NetResponseJson
import io.menio.android.models.MenuModel
import io.menio.android.utilities.AppController
import io.menio.android.utilities.Constants.*
import io.menio.android.utilities.LocaleHelper
import io.menio.android.utilities.NetworkRequests
import io.menio.android.utilities.Snippets
import kotlinx.android.synthetic.main.activity_select_menu.*
import kotlinx.android.synthetic.main.item_menu.view.*
import org.json.JSONObject

class SelectMenuActivity : AppCompatActivity() {

    private var selectedItem: View? = null;
    private var modelList: MutableList<MenuModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_menu)
        downloadMenus()
        Snippets.setColorForProgress(btnProgress, resources.getColor(R.color.white))
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }

    private fun downloadSelectedMenu() {
        btnProgress.visibility = VISIBLE
        select.text = ""
        NetworkRequests.getRequestJson(getMenuUrl(selectedItem!!.tag as String),
                object : NetResponseJson {
                    override fun onResponse(json: JSONObject) {
                        btnProgress.visibility = GONE
                        select.text = getString(R.string.confirm)
                        AppController.app.menu = Gson().fromJson(json.toString(), MenuModel::class.java)
                        Glide.with(this@SelectMenuActivity).download(AppController.app.menu!!.backgroundUrl)
                        MenuActivity.open(this@SelectMenuActivity)
                        finish()
                    }

                    override fun onError(error: VolleyError?, message: String, isOnline: Boolean) {
                        btnProgress.visibility = GONE
                        select.text = getString(R.string.confirm)
                        Snippets.showError(this@SelectMenuActivity, message, R.string.retry, { downloadSelectedMenu() }, true)
                    }

                }
        )
    }


    private fun downloadMenus() {
        progress.visibility = VISIBLE
        NetworkRequests.getRequestJson(getAllMenusUrl(),
                object : NetResponseJson {
                    override fun onResponse(json: JSONObject) {
                        progress.visibility = GONE
                        modelList = Gson().fromJson<MutableList<MenuModel>>(json.getString("data"),
                                object : TypeToken<List<MenuModel>>() {}.type)
                        populateModelList(modelList!!)
                    }

                    override fun onError(error: VolleyError?, message: String, isOnline: Boolean) {
                        Snippets.showError(this@SelectMenuActivity, message, R.string.retry, { downloadMenus() }, true)
                    }

                })
    }

    private fun populateModelList(modelList: MutableList<MenuModel>) {

        for ((index, menuModel) in modelList.withIndex()) {
            var row = LayoutInflater.from(this).inflate(R.layout.item_menu, baseLinLay, false)
            row.title.text = menuModel.name
            Picasso.with(this).load(menuModel.thumbnailUrl).into(row.image)
            row.setOnClickListener({ selectMenu(row, menuModel) })
            if (index == 0) {
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

        select.setOnClickListener({
            downloadSelectedMenu()
        })

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

    companion object {
        fun open(activity: Activity, isFromSplash: Boolean) {
            val intent = Intent(activity, SelectMenuActivity::class.java)
            intent.putExtra(IS_FROM_SPLASH, isFromSplash)
            activity.startActivity(intent);
        }
    }


}