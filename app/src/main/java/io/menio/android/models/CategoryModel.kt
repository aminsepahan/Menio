package io.menio.android.models

import com.alibaba.fastjson.annotation.JSONField
import com.google.gson.annotations.SerializedName

data class CategoryModel(@SerializedName("_id") @JSONField(name = "_id") val id: String,
                         val name: String,
                         val weight: Int,
                         @SerializedName("menu_type_id") @JSONField(name = "menu_type_id") val menuTypeId: String,
                         @SerializedName("menu_id") @JSONField(name = "menu_id") val menuId: String,
                         @SerializedName("thumbnail_url") @JSONField(name = "thumbnail_url") val thumbnailUrl: String,
                         @SerializedName("thumbnail_small_url") @JSONField(name = "thumbnail_url") val thumbnailSmallUrl: String,
                         @SerializedName("menu_items") @JSONField(name = "menu_items") val menuItems: List<ItemModel>,
                         @SerializedName("is_active") @JSONField(name = "is_active") val isActive: Boolean)
