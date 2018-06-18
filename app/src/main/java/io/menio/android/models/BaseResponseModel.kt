package io.menio.android.models

import com.alibaba.fastjson.annotation.JSONField
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class BaseResponseModel<T>(val data: List<T>)
