package io.menio.android.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.menio.android.R
import io.menio.android.utilities.Constants

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    companion object {
        fun open(activity: Activity, isFromSplash: Boolean) {
            val intent = Intent(activity, MainActivity.javaClass);
            intent.putExtra(Constants.IS_FROM_SPLASH, true);
            activity.startActivity(intent);
        }
    }

}