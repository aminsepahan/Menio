package io.menio.android.utilities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import io.menio.android.R
import java.util.*
import io.menio.android.utilities.AppController.*
import io.menio.android.utilities.AppController.Companion.app


/**
 * Created by Amin on 07/03/2018.
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setLocale()
        setAppTheme()
        super.onCreate(savedInstanceState)
    }

    private fun setAppTheme() {
        if (app.user == null){
            return
        } else {
            if (app.user!!.username == "sabz"){
                setTheme(R.style.AppTheme_2)
            }
        }
    }

    private fun setLocale() {
        val dm = resources.getDisplayMetrics()
        val conf = resources.getConfiguration()
        conf.setLocale(Locale(AppController.app.language!!.code.toLowerCase())) // API 17+ only.
        resources.updateConfiguration(conf, dm)
    }

}