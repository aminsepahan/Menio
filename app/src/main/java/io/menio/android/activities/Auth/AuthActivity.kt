package io.menio.android.activities.Auth

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil.bind
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import io.menio.android.R
import io.menio.android.utilities.Constants
import io.menio.android.utilities.Constants.*
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        loginBtn.setOnClickListener({
            SelectBranchActivity.open(this, intent.getBooleanExtra(IS_FROM_SPLASH, false)) })
    }
    companion object {
        fun open(activity: Activity, isFromSplash: Boolean) {
            val intent = Intent(activity, AuthActivity::class.java);
            intent.putExtra(IS_FROM_SPLASH, true);
            activity.startActivity(intent);
        }
    }


}