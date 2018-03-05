package io.menio.android.activities.Menu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import io.menio.android.R
import io.menio.android.models.CategoryModel
import io.menio.android.utilities.AppController
import io.menio.android.utilities.Constants
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.item_category.view.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        Glide.with(this).load(AppController.app.menu!!.backgroundUrl).into(menuBack)
        populateMenu()
    }

    private fun populateMenu() {
        for (category in AppController.app.menu!!.categories) {
            var row = LayoutInflater.from(this).inflate(R.layout.item_category, baseLinLay, false)
            row.title.text = category.name
            row.setOnClickListener({ selectCategory(category, row) })
            baseLinLay.addView(row)
        }
    }

    private fun selectCategory(category: CategoryModel, row: View) {
        AppController.app.category = category
        CategoryActivity.open(this, category.id)
    }

    companion object {

        fun open(activity: Activity, menuId: String) {
            val intent = Intent(activity, MenuActivity::class.java)
            intent.putExtra(Constants.ID, menuId)
            activity.startActivity(intent);
        }
    }
}
