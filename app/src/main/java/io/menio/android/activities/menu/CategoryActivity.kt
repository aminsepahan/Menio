package io.menio.android.activities.menu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IntDef
import io.menio.android.R
import io.menio.android.models.ItemModel
import io.menio.android.utilities.AppController
import io.menio.android.utilities.BaseActivity
import io.menio.android.utilities.Constants.*

class CategoryActivity : BaseActivity() {


    public lateinit var modelList: List<ItemModel>
    lateinit var fragment: CategoryFragmentBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        modelList = AppController.app.category!!.menuItems
        fragment = CategoryFragment1.newInstance()
        supportFragmentManager
                .beginTransaction()
                .add(R.id.frame, fragment)
                .commit()

    }

    override fun onBackPressed() {
        if (fragment.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fragment.updateCartView()
        if (requestCode == SETTING_REQ_CODE &&
                (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_FIRST_USER)) {
            setResult(resultCode)
            finish()
        }

    }


    fun languageChanged() {
        val data = Intent()
        data.putExtra(SELECTED_LANGUAGE, true)
        setResult(Activity.RESULT_CANCELED, data)
        finish()
    }


    companion object {
        @Retention(AnnotationRetention.SOURCE)
        @IntDef(LIST_TYPE, GRID_TYPE, SHOPPING_LIST_TYPE)
        annotation class ListType

        const val LIST_TYPE: Int = 0
        const val GRID_TYPE: Int = 1
        const val SHOPPING_LIST_TYPE: Int = 2

        fun open(activity: Activity) {
            val intent = Intent(activity, CategoryActivity::class.java)
            activity.startActivityForResult(intent, EDIT_REQ_CODE);
        }
    }




}
