package io.menio.android.models

import com.google.gson.annotations.SerializedName

data class ShoppingCartModel(@SerializedName("_id") val id: String,
                             val total: Double = 0.0,
                             val discount: Double,
                             val tax: Double,
                             val shipping: Double,
                             val subtotal: Double,
                             val currency: String,
                             @SerializedName("items_count") val itemsCount: Int = 0,
                             @SerializedName("line_items") val lineItems: List<ItemModel>?,
                             @SerializedName("line_items_total") val lineItemsTotal: Double,
                             @SerializedName("line_items_discount") val lineItemsDiscount: Double,
                             @SerializedName("coupon_discount") val couponDiscount: Double,
                             @SerializedName("table_number") val table_number: Int,
                             @SerializedName("has_error") val hasError: Boolean,
                             @SerializedName("error_message") val hasChild: String?,
                             @SerializedName("restaurant_id") val restaurantId: String,
                             @SerializedName("menu_id") val menuId: String)
