package io.menio.android.network

import io.menio.android.models.BaseResponseModel
import io.menio.android.models.MenuModel
import io.menio.android.models.ShoppingCartModel
import io.menio.android.network.postJsonFormats.UpdateShoppingCartJsonModel
import io.menio.android.utilities.AppController
import io.menio.android.utilities.AppController.Companion.app
import io.menio.android.utilities.Constants.ACCESS_TOKEN
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * Created by Amin on 30/05/2018.
 *
 */
interface Webservice {

    @POST("restaurant/shopping-cart/add-to-cart")
    fun addToShoppingCart(@QueryMap query: Map<String, String>, @Body post: UpdateShoppingCartJsonModel):
            Observable<ShoppingCartModel>

    @PUT("restaurant/shopping-cart/update-qty")
    fun updateCart(@QueryMap query: Map<String, String>, @Body post: UpdateShoppingCartJsonModel):
            Observable<ShoppingCartModel>

    @GET("restaurant/shopping-cart")
    fun getShoppingCart(@QueryMap query: Map<String, String>):
            Observable<ShoppingCartModel>

    @GET("restaurant/menus")
    fun getMenus(@QueryMap query: Map<String, String>):
            Observable<BaseResponseModel<MenuModel>>

    @GET("restaurant/menus/{menu_id}")
    fun getMenu(@Path("menu_id") menuId: String, @QueryMap query: Map<String, String>):
            Observable<MenuModel>

    companion object {
        fun create(): Webservice {
            val okHttpClientBuilder = OkHttpClient.Builder()
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
            okHttpClientBuilder.addInterceptor({ chain ->
                val request = chain.request().newBuilder()
                if (app.isSet(app.cons.ACCESS_TOKEN)) {
                    request.addHeader("Authorization", "Bearer " + app.getSP(app.cons.ACCESS_TOKEN))
                    if (app.isSet(app.cons.SELECTED_BRANCH_ID)) {
                        request.addHeader("Restaurant-Id", app.getSP(app.cons.SELECTED_BRANCH_ID))
                    }
                }
                request.addHeader("OS", "Android")
                chain.proceed(request.build())
            })
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClientBuilder.build())
                    .baseUrl("https://api.menio.io/api/v1/")
                    .build()

            return retrofit.create(Webservice::class.java)
        }
    }
}