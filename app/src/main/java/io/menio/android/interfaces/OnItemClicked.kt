package io.menio.android.interfaces

import io.menio.android.models.ItemModel

/**
 * Created by Amin on 01/11/2017.
 */
interface OnItemClicked {
    abstract fun onClick(model: ItemModel)
}