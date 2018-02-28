package io.menio.android.models

import com.alibaba.fastjson.annotation.JSONField
import com.google.gson.annotations.SerializedName

data class InventoryModel(@SerializedName("is_in_stock") @JSONField(name = "is_in_stock") val isInStock: Boolean)
