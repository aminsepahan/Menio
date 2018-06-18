package io.menio.android.activities.menu

import android.support.v7.widget.RecyclerView
import io.menio.android.models.CartUpdateEvent
import io.menio.android.models.ItemModel

/**
 * Created by Amin on 11/06/2018.
 */
abstract class BaseFoodAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>()  {

    abstract fun update(event: CartUpdateEvent)

}