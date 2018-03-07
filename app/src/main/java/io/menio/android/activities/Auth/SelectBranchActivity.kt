package io.menio.android.activities.Auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.volley.VolleyError
import com.google.gson.Gson
import io.menio.android.R
import io.menio.android.models.RestaurantModel
import io.menio.android.activities.SelectMenu.SelectMenuActivity
import io.menio.android.interfaces.NetResponseJson
import io.menio.android.utilities.AppController
import io.menio.android.utilities.AppController.Companion.app
import io.menio.android.utilities.Constants.*
import io.menio.android.utilities.LocaleHelper
import io.menio.android.utilities.NetworkRequests
import io.menio.android.utilities.Snippets
import kotlinx.android.synthetic.main.activity_select_branch.*
import org.json.JSONObject

class SelectBranchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_branch)
        showBranches()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }

    private fun downloadModel() {
        NetworkRequests.getRequestJson(getRestaurant(), object : NetResponseJson{
            override fun onResponse(json: JSONObject) {
                app.restaurant = Gson().fromJson(json.getString("restaurant"), RestaurantModel::class.java)
                app.setSP(SELECTED_BRANCH_NAME, app.restaurant!!.name)
                app.setSP(SELECTED_CURRENCY, Gson().toJson(app.restaurant!!.currencies[0]))
                app.setSP(SELECTED_CURRENCY_NAME, app.restaurant!!.currencies[0].name)
                app.setSP(SELECTED_CURRENCY_CODE, app.restaurant!!.currencies[0].code)
                app.language = app.restaurant!!.languages[0]
            }

            override fun onError(error: VolleyError?, message: String, isOnline: Boolean) {
                Snippets.showError(this@SelectBranchActivity, message, R.string.retry, {downloadModel()}, true)
            }

        })
    }

    private fun showBranches() {
        val adapter = RestaurantSpinnerAdapter(this, AppController.app.user!!.restaurants)
        branchSpinner.adapter = adapter
        branchSpinner.setSelection(0)
        select.setOnClickListener({
            downloadModel()
            AppController.app.setSP(SELECTED_BRANCH_ID, (branchSpinner.selectedItem as RestaurantModel).id)
            SelectMenuActivity.open(this, intent.getBooleanExtra(IS_FROM_SPLASH, false))
            finish()
        })
    }

    companion object {
        fun open(activity: Activity, isFromSplash: Boolean) {
            val intent = Intent(activity, SelectBranchActivity::class.java);
            intent.putExtra(IS_FROM_SPLASH, true)
            activity.startActivity(intent);
        }
    }

}