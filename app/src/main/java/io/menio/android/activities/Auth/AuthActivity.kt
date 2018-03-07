package io.menio.android.activities.Auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.VISIBLE
import com.android.volley.VolleyError
import com.google.gson.Gson
import io.menio.android.R
import io.menio.android.interfaces.NetResponse
import io.menio.android.interfaces.NetResponseJson
import io.menio.android.models.MerchantModel
import io.menio.android.utilities.AppController
import io.menio.android.utilities.Constants.*
import io.menio.android.utilities.LocaleHelper
import io.menio.android.utilities.NetworkRequests
import io.menio.android.utilities.Snippets
import io.menio.android.utilities.Snippets.*
import kotlinx.android.synthetic.main.activity_auth.*
import org.json.JSONObject

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        loginBtn.setOnClickListener({ performLogin() })
        Snippets.setColorForProgress(progress, resources.getColor(R.color.white))
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }

    private fun performLogin() {
        loginBtn.text = ""
        progress.visibility = VISIBLE

        if (userName.text.isNullOrEmpty()){
            showError(this, R.string.username_reqiered, R.string.retry, {performLogin()}, false)
            loginBtn.text = getString(R.string.login)
            progress.visibility = View.GONE
            return
        }

        if (password.text.isNullOrEmpty()){
            showError(this, R.string.password_reqiered, R.string.retry, {performLogin()}, false)
            loginBtn.text = getString(R.string.login)
            progress.visibility = View.GONE
            return
        }

        val json = JSONObject()
        json.put("username", userName.text)
        json.put("password", password.text)

        NetworkRequests.jsonPostRequest(getLoginUrl(), object : NetResponseJson{
            override fun onResponse(json: JSONObject) {
                loginBtn.text = getString(R.string.login)
                progress.visibility = View.GONE
                AppController.app.setSP(ACCESS_TOKEN, json.getString("token"))
                (application as AppController).setCurrentUser(Gson().fromJson(json.toString(), MerchantModel::class.java))
                AppController.app.setSP(USER, json.getString("merchant"))
                AppController.app.setSP(IS_LOGGED_IN, TRUE)
                SelectBranchActivity.open(this@AuthActivity, true)
                finish()
            }

            override fun onError(error: VolleyError?, message: String, isOnline: Boolean) {
                showError(this@AuthActivity, message, R.string.retry, {performLogin()}, true)
                loginBtn.text = getString(R.string.login)
                progress.visibility = View.GONE
            }

        }, json)

    }

    companion object {
        fun open(activity: Activity, isFromSplash: Boolean) {
            val intent = Intent(activity, AuthActivity::class.java);
            intent.putExtra(IS_FROM_SPLASH, true);
            activity.startActivity(intent);
        }
    }
}