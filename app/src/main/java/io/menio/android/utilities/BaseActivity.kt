package io.menio.android.utilities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import io.menio.android.R
import io.menio.android.extensions.log
import io.menio.android.extensions.prepareQuery
import io.menio.android.extensions.snack
import io.menio.android.models.*
import io.menio.android.network.postJsonFormats.UpdateShoppingCartJsonModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import java.util.*


/**
 * Created by Amin on 07/03/2018.
 *
 */
@SuppressLint("Registered")
open class BaseActivity : FragmentActivity() {


    val app: AppController = AppController.getInstance()
    val cons = ConstantsKotlin()

    override fun onCreate(savedInstanceState: Bundle?) {
        setLocale()
        setAppTheme()
        super.onCreate(savedInstanceState)
    }

    private fun setAppTheme() {
        if (app.user == null) {
            return
        } else {
            if (app.user!!.username == "sabz") {
                setTheme(R.style.AppTheme_2)
            }
        }
    }

    private fun setLocale() {
        val dm = resources.displayMetrics
        val conf = resources.configuration
        conf.setLocale(Locale(AppController.app.language!!.code.toLowerCase())) // API 17+ only.
        resources.updateConfiguration(conf, dm)
    }

    fun updateCart(model: ItemModel, qty: Int, variation: String?) {
        app.webService.updateCart(prepareQuery(Pair("menu_id", app.menu!!.id), Pair("variation_id", variation)), UpdateShoppingCartJsonModel(model.id, model.variations?.get(0)?.id, qty))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            log(" result  ===  $result")
                            AppController.app.shoppingCart = result
                        },
                        { error ->
                            snack(error.message!!, buttonTxt = R.string.retry,
                                    action = View.OnClickListener {
                                        updateCart(model, qty, variation)
                                    })
                        }
                )
    }

    fun addToCart(model: ItemModel, variation: String?) {
        app.webService.addToShoppingCart(prepareQuery(Pair("menu_id", app.menu!!.id), Pair("variation_id", variation)), UpdateShoppingCartJsonModel(model.id, model.variations?.get(0)?.id, 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            log(" result  ===  $result")
                            AppController.app.shoppingCart = result
                            EventBus.getDefault().post(CartUpdateEvent(model.id, 1))
                        },
                        { error ->
                            snack(error.message!!, buttonTxt = R.string.retry,
                                    action = View.OnClickListener {
                                        addToCart(model, variation)
                                    })
                        }
                )
    }

    fun getMenus(onResult: Consumer<in BaseResponseModel<MenuModel>>, onError: Consumer<in Throwable>) {
        app.webService.getMenus(prepareQuery())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onResult, onError)
    }

    fun getMenu(menuId: String, onResult: Consumer<in MenuModel>, onError: Consumer<in Throwable>) {
        app.webService.getMenu(menuId, prepareQuery())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onResult, onError)
    }


    fun getShoppingcart(onResult: Consumer<ShoppingCartModel>, onError: Consumer<Throwable>) {
        AppController.app.webService.getShoppingCart(prepareQuery(Pair("menu_id", AppController.app.menu!!.id)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onResult, onError)
    }


}