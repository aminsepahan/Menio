package io.menio.android.activities.Auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.menio.android.R
import io.menio.android.activities.SelectMenu.SelectMenuActivity
import io.menio.android.utilities.Constants
import io.menio.android.utilities.Constants.*
import kotlinx.android.synthetic.main.activity_select_branch.*

class SelectBranchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_branch)
        select.setOnClickListener( {
            SelectMenuActivity.open(this, intent.getBooleanExtra(IS_FROM_SPLASH, false))
        })
    }
    companion object {
        fun open(activity: Activity, isFromSplash: Boolean) {
            val intent = Intent(activity, SelectBranchActivity::class.java);
            intent.putExtra(IS_FROM_SPLASH, true);
            activity.startActivity(intent);
        }
    }


}