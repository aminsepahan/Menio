package io.menio.android.models

import com.alibaba.fastjson.annotation.JSONField
import com.google.gson.annotations.SerializedName

data class RestaurantModel(@SerializedName("_id") @JSONField(name = "_id") val id: String,
                           val name: String,
                           val address: String,
                           val sort: Int,
                           val currencies: List<CurrencyModel>,
                           val languages: List<LanguageModel>,
                           @SerializedName("logo_url") @JSONField(name = "logo_url") val logoUrl: String,
                           @SerializedName("merchant_id") @JSONField(name = "merchant_id") val merchantId: String,
                           @SerializedName("country_id") @JSONField(name = "country_id") val countryId: String,
                           @SerializedName("country_name") @JSONField(name = "country_name") val countryName: String,
                           @SerializedName("region_id") @JSONField(name = "region_id") val regionId: String,
                           @SerializedName("region_name") @JSONField(name = "region_name") val regionName: String,
                           @SerializedName("city_id") @JSONField(name = "city_id") val cityId: String,
                           @SerializedName("city_name") @JSONField(name = "city_name") val cityName: String,
                           @SerializedName("merchant_name") @JSONField(name = "merchant_name") val merchantName: String,
                           @SerializedName("selected_languages") @JSONField(name = "selected_languages") val selectedLanguages: List<String>,
                           @SerializedName("selected_currencies") @JSONField(name = "selected_currencies") val selectedCurrencies: List<String>,
                           @SerializedName("is_active") @JSONField(name = "is_active") val isActive: Boolean)