package io.menio.android.models

import com.alibaba.fastjson.annotation.JSONField
import com.google.gson.annotations.SerializedName

data class ItemModel(@SerializedName("_id") @JSONField(name = "_id") val id: String,
                     val name: String,
                     val type: String,
                     var ingredients: String = "گوشت، مرغ، پنیر، سس قارچ",
                     val images: List<String>,
                     val variations: List<VariationModel>,
                     val weight: Int,
                     val price: Double,
                     val inventory: InventoryModel,
                     @SerializedName("menu_type_id") @JSONField(name = "menu_type_id") val menuTypeId: String,
                     @SerializedName("menu_id") @JSONField(name = "menu_id") val menuId: String,
                     @SerializedName("thumbnail_url") @JSONField(name = "thumbnail_url") val thumbnailUrl: String,
                     @SerializedName("thumbnail_small_url") @JSONField(name = "thumbnail_url") val thumbnailSmallUrl: String,
                     @SerializedName("is_active") @JSONField(name = "is_active") val isActive: Boolean)
