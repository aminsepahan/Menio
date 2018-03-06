package io.menio.android.activities.Menu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import io.menio.android.R
import io.menio.android.activities.Settings.SettingsActivity
import io.menio.android.models.CategoryModel
import io.menio.android.utilities.AppController
import io.menio.android.utilities.Constants.EDIT_REQ_CODE
import io.menio.android.utilities.Constants.SETTING_REQ_CODE
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.item_category.view.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        populateMenu()
        setting.setOnClickListener { SettingsActivity.open(this, false) }
    }

    private fun populateMenu() {
        baseLinLay.removeAllViews()
//        Glide.with(this).load(AppController.app.menu!!.backgroundUrl).into(menuBack)
        Glide.with(this).load(R.drawable.menu_background_1).into(menuBack)
        for (category in AppController.app.menu!!.categories) {
            var row = LayoutInflater.from(this).inflate(R.layout.item_category, baseLinLay, false)
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
        }
    }

    companion object {

        fun open(activity: Activity) {
            val intent = Intent(activity, MenuActivity::class.java)
            activity.startActivity(intent);
        }
    }
}
