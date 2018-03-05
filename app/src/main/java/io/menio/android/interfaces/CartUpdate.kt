package io.menio.android.interfaces

import io.menio.android.models.CartUpdateEvent

/**
 * Created by Amin on 18/02/2018.
 */
interface CartUpdate {

    fun update(event: CartUpdateEvent)

}