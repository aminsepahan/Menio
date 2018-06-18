package io.menio.android.extensions

import io.menio.android.utilities.AppController
import io.menio.android.utilities.Constants.SELECTED_CURRENCY_CODE
import io.menio.android.utilities.Constants.TABLE_NUMBER


/**
 * Created by Amin on 06/06/2018.
 *
 */
fun prepareQuery(vararg keyValue: Pair<String, String?>): Map<String, String> {
    val data = HashMap<String, String>()
    for (pair in keyValue) {
        pair.second?.let { data[pair.first] = pair.second!!}
    }
    AppController.app.language?.code?.let { data["locale"] = it }
    data["currency"] = AppController.app.getSP(SELECTED_CURRENCY_CODE)
    data["table_number"] = AppController.app.tableNumber.toString()
    return data
}

fun prepareQuery(): Map<String, String> {
    val data = HashMap<String, String>()
    AppController.app.language?.code?.let { data["locale"] = it }
    data["currency"] = AppController.app.getSP(SELECTED_CURRENCY_CODE)
    data["table_number"] = AppController.app.tableNumber.toString()
    return data
}
