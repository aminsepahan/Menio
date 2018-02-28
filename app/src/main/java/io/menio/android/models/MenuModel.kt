package io.menio.android.models

import com.alibaba.fastjson.annotation.JSONField
import com.google.gson.annotations.SerializedName

data class MenuModel(@SerializedName("_id") @JSONField(name = "_id") val id: String,
                     val name: String,
                     val weight: Int,
                     val categories: List<CategoryModel>,
                     @SerializedName("menu_type_id") @JSONField(name = "menu_type_id") val menuTypeId: String,
                     @SerializedName("background_url") @JSONField(name = "background_url") val backgroundUrl: String,
                     @SerializedName("thumbnail_url") @JSONField(name = "thumbnail_url") val thumbnailUrl: String,
                     @SerializedName("thumbnail_small_url") @JSONField(name = "thumbnail_url") val thumbnailSmallUrl: String,
                     @SerializedName("is_active") @JSONField(name = "is_active") val isActive: Boolean)
