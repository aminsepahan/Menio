package io.menio.android.activities.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
import com.android.volley.VolleyError
import com.bumptech.glide.Glide
import com.github.florent37.viewanimator.ViewAnimator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import io.menio.android.R
import io.menio.android.activities.auth.AuthActivity
import io.menio.android.interfaces.NetResponseJson
import io.menio.android.models.MenuModel
import io.menio.android.utilities.AppController
import io.menio.android.utilities.BaseActivity
import io.menio.android.utilities.Constants.*
import io.menio.android.utilities.NetworkRequests
import io.menio.android.utilities.Snippets
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.item_menu.view.*
import org.json.JSONObject

class SettingsActivity : BaseActivity() {


    private var selectedItem: View? = null;
    private var modelList: MutableList<MenuModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        tableNumber.setText(AppController.app.getSP(TABLE_NUMBER, "1"))
        downloadMenus()
        Snippets.setColorForProgress(btnProgress, resources.getColor(R.color.white))
        logout.setOnClickListener { performLogout() }
        changePassword.setOnClickListener { performChangePassword() }
    }

    private fun performChangePassword() {
        val convertView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_check_password, null, false)
        val input = convertView.findViewById<View>(R.id.input) as EditText
        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                input.error = ""
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        MaterialDialog.Builder(this).customView(convertView, true)
                .onPositive { _, _ -> run{
                    if(input.text.isNotEmpty())
                        AppController.app.setSP(USER_PASS, input.text.toString())
                    else
                        input.error = getString(R.string.wrong_password)
                } }.positiveText(getString(R.string.confirm))
                .buttonRippleColor(resources.getColor(R.color.colorPrimaryDark))
                .negativeColor(resources.getColor(R.color.colorPrimaryDark))
                .positiveColor(resources.getColor(R.color.colorPrimaryDark))
                .negativeText(getString(R.string.cancel))
                .typeface("theme.ttf", "theme_light.ttf").build().show()
    }

    private fun performLogout() {
        AppController.app.setSP(IS_LOGGED_IN, FALSE)
        setResult(Activity.RESULT_FIRST_USER)
        AuthActivity.open(this, false)
        finish()
    }

    private fun downloadMenus() {
        progress.visibility = View.VISIBLE
        NetworkRequests.getRequestJson(getAllMenusUrl(),
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
        NetworkRequests.getRequestJson(getMenuUrl(selectedItem!!.tag as String),
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

        fun open(activity: Activity) {
            val intent = Intent(activity, SettingsActivity::class.java)
            activity.startActivityForResult(intent, SETTING_REQ_CODE);
        }
    }
}
