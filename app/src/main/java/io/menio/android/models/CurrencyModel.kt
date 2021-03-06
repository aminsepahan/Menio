package io.menio.android.models

import com.alibaba.fastjson.annotation.JSONField
import com.google.gson.annotations.SerializedName

data class CurrencyModel(@SerializedName("_id") @JSONField(name = "_id") val id: String,
                         val name: String,
                         val code: String,
                         val symbol: String,
                         val symbol_html: String,
                         @SerializedName("is_active") @JSONField(name = "is_active") val isActive: Boolean)
