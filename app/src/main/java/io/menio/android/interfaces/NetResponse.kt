package io.menio.android.interfaces

import android.view.View

/**
 * Created by Amin on 01/11/2017.
 */
interface NetResponse {
    abstract fun onResponse(response: String)
    abstract fun onError(response: String)
}