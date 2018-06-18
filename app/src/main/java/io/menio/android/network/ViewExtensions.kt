package io.menio.android.network

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE

/**
 * Created by Amin on 09/06/2018.
 *
 */
fun View.invisible(){
    visibility = GONE
}
fun View.visible(){
    visibility = VISIBLE
}