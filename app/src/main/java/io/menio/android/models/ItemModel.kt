package io.menio.android.models

import com.alibaba.fastjson.annotation.JSONField
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class ItemModel(@SerializedName("_id") var id: String,
                     val name: String,
                     val type: String,
                     var ingredient: String = "گوشت، مرغ، پنیر، سس قارچ",
                     val description: String,
                     val images: List<String>,
                     var variations: List<VariationModel>?,
                     val weight: Int,
                     val price: Double,
                     val discount: Double,
                     val total: Double,
                     var qty: Int,
                     val exists: Boolean,
                     val inventory: InventoryModel,
                     @SerializedName("menu_type_id") val menuTypeId: String,
                     @SerializedName("menu_item_id") val menuItemId: String,
                     @SerializedName("list_price") val listPrice: Double,
                     @SerializedName("sale_price") val salePrice: Double,
                     @SerializedName("variation_id") val variationId: String?,
                     @SerializedName("variation_name") val variationName: String?,
                     @SerializedName("thumbnail_url") val thumbnailUrl: String,
                     @SerializedName("thumbnail_small_url") val thumbnailSmallUrl: String,
                     @SerializedName("is_active") val isActive: Boolean)
