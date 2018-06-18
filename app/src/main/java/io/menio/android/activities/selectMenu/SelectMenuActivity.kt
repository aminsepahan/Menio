package io.menio.android.activities.selectMenu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.android.volley.VolleyError
import com.bumptech.glide.Glide
import com.github.florent37.viewanimator.ViewAnimator
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.menio.android.R
import io.menio.android.activities.menu.MenuActivity
import io.menio.android.extensions.snack
import io.menio.android.interfaces.NetResponseJson
import io.menio.android.models.MenuModel
import io.menio.android.network.invisible
import io.menio.android.utilities.*
import io.menio.android.utilities.Constants.IS_FROM_SPLASH
import io.menio.android.utilities.Constants.getMenuUrl
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_select_menu.*
import kotlinx.android.synthetic.main.item_menu.view.*
import org.json.JSONObject

class SelectMenuActivity : BaseActivity() {

    private var selectedItem: View? = null;
    private var modelList: List<MenuModel>? = null

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
        getMenu(selectedItem!!.tag as String, Consumer {
            btnProgress.invisible()
            select.text = getString(R.string.confirm)
            AppController.app.menu = it
            populateModelList(modelList!!)
            Glide.with(this@SelectMenuActivity).download(AppController.app.menu!!.backgroundUrl)
            MenuActivity.open(this@SelectMenuActivity)
            finish()
        }, Consumer {
            btnProgress.invisible()
            select.text = getString(R.string.confirm)
            snack(message = getString(R.string.connection_error)) })

    }


    private fun downloadMenus() {
        progress.visibility = VISIBLE
        getMenus(Consumer {
            progress.invisible()
            modelList = it.data
            populateModelList(modelList!!)
        }, Consumer { progress.invisible(); snack(message = getString(R.string.connection_error)) })

    }

    private fun populateModelList(modelList: List<MenuModel>) {

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