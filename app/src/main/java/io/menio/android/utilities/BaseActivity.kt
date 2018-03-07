package io.menio.android.utilities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import java.util.*


/**
 * Created by Amin on 07/03/2018.
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setLocale()
        super.onCreate(savedInstanceState)
    }

    private fun setLocale() {
        val dm = resources.getDisplayMetrics()
        val conf = resources.getConfiguration()
        conf.setLocale(Locale(AppController.app.language!!.code.toLowerCase())) // API 17+ only.
        resources.updateConfiguration(conf, dm)
    }

}