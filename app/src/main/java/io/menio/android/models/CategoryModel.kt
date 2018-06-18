package io.menio.android.models

import com.alibaba.fastjson.annotation.JSONField
import com.google.gson.annotations.SerializedName

data class CategoryModel(@SerializedName("_id") val id: String,
                         val name: String,
                         val weight: Int,
                         @SerializedName("menu_id") val menuId: String,
                         @SerializedName("thumbnail_url") val thumbnailUrl: String,
                         @SerializedName("thumbnail_small_url")  val thumbnailSmallUrl: String,
                         @SerializedName("header_image_url") val headerImageUrl: String,
                         @SerializedName("menu_items")  val menuItems: List<ItemModel>,
                         @SerializedName("is_active") val isActive: Boolean)
