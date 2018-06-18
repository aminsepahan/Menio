package io.menio.android.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import io.menio.android.R
import io.menio.android.utilities.BaseActivity

/**
 * Created by Amin on 08/06/2018.
 *
 */
fun Activity.snack(message: String, buttonTxt: Int = R.string.ok,
                   action: View.OnClickListener? = null, indefinite: Boolean = false) {
    val snackbar: Snackbar = if (indefinite) {
        Snackbar.make(window.decorView.rootView, message, Snackbar.LENGTH_INDEFINITE)
    } else {
        Snackbar.make(window.decorView.rootView, message, Snackbar.LENGTH_LONG)
    }
    snackbar.setAction(buttonTxt, action).setActionTextColor(resources.getColor(R.color.md_yellow_200))
    snackbar.show()
}

fun View.showHideFade(showOrHide: Boolean) {
    val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
    if (showOrHide && visibility == VISIBLE && alpha == 1f) return
    if (!showOrHide && visibility == GONE && alpha == 0f) return
    animate().setDuration(shortAnimTime)
            .alpha((if (showOrHide) 1 else 0).toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    visibility = if (showOrHide) VISIBLE else GONE
                }

                override fun onAnimationStart(animation: Animator?) {
                    visibility = VISIBLE
                }
            })
}

fun BaseActivity.log(message: String) {
    Log.d(cons.LOG_TAG, message)
}