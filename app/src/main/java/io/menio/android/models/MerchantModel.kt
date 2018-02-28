package io.menio.android.models

import com.alibaba.fastjson.annotation.JSONField
import com.google.gson.annotations.SerializedName
import io.menio.android.models.RestaurantModel

data class MerchantModel(@SerializedName("_id") @JSONField(name = "_id") val id: String,
                         val name: String,
                         val type: String,
                         val username: String,
                         val mobile: String,
                         val email: String,
                         val restaurants: List<RestaurantModel>,
                         @SerializedName("is_active") @JSONField(name = "is_active") val isActive: Boolean,
                         @SerializedName("restaurants_count") @JSONField(name = "restaurants_count") val restaurantsCount: Int,
                         @SerializedName("is_root_category") @JSONField(name = "is_root_category") val isRootCategory: Boolean){
}
