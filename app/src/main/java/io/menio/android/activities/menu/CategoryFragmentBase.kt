package io.menio.android.activities.menu

import android.content.Context
import android.support.v4.app.Fragment
import io.menio.android.activities.menu.CategoryActivity.Companion.GRID_TYPE
import io.menio.android.models.CartUpdateEvent
import io.menio.android.models.ItemModel
import io.menio.android.models.VariationModel
import io.menio.android.utilities.AppController
import org.greenrobot.eventbus.Subscribe

/**
 * Created by Amin on 08/05/2018.
 *
 */
abstract class CategoryFragmentBase : Fragment() {

    val app: AppController = AppController.getInstance()
    public lateinit var activity: CategoryActivity
    var type: Int = GRID_TYPE

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = (getActivity() as CategoryActivity?)!!
    }
    @Subscribe
    abstract fun handleCartUpdate(event: CartUpdateEvent)

    abstract fun updateCart(model: ItemModel, qty: Int, variationId: String?)
    abstract fun addToCart(model: ItemModel, variationId: String?)
    abstract fun updateCartView()
    abstract fun onBackPressed() : Boolean
    abstract fun updateModelCartView()
    abstract fun showVariationsDialog(model: ItemModel, position: Int)

}