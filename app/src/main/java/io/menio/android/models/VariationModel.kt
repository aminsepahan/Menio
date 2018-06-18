package io.menio.android.models

import com.alibaba.fastjson.annotation.JSONField
import com.google.gson.annotations.SerializedName

data class VariationModel(@SerializedName("_id") @JSONField(name = "_id") val id: String,
                          val name: String,
                          val price: Double,
                          val inventory: InventoryModel,
                          var qty: Int,
                          @SerializedName("is_active") @JSONField(name = "is_active") val isActive: Boolean)
