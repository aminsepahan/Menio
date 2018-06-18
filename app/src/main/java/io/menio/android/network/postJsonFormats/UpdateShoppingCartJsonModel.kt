package io.menio.android.network.postJsonFormats

import com.google.gson.annotations.SerializedName

/**
 * Created by Amin on 06/06/2018.
 */
data class UpdateShoppingCartJsonModel(@SerializedName("menu_item_id") val menuItemId: String,
                                       @SerializedName("variation_id") var variationId: String?, val qty: Int)