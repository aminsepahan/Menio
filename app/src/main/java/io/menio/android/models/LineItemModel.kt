package io.menio.android.models

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class LineItemModel(@SerializedName("_id") val id: String,
                         val name: String,
                         val exists: Boolean,
                         val subtotal: Long,
                         val total: Long,
                         var qty: Int,
                         val discount: Long,
                         val type: String,
                         val prices: JSONObject,
                         @SerializedName("menu_item_id") val menuItemId: String,
                         @SerializedName("variationId") val variationId: String,
                         @SerializedName("variation_name") val variationName: String,
                         @SerializedName("has_error") val hasError: Boolean,
                         @SerializedName("is_on_sale") val isOnSale: Boolean,
                         @SerializedName("list_price") val listPrice: Long,
                         @SerializedName("sale_price") val salePrice: Long,
                         @SerializedName("thumbnail_url") val thumbnailUrl: String)
