package io.menio.android.activities.menu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.github.florent37.viewanimator.ViewAnimator
import io.menio.android.R
import io.menio.android.activities.menu.CategoryActivity.Companion.GRID_TYPE
import io.menio.android.interfaces.OnItemClicked
import io.menio.android.models.CartUpdateEvent
import io.menio.android.models.ItemModel
import io.menio.android.utilities.AppController
import io.menio.android.utilities.Constants
import io.menio.android.utilities.ItemDecorationPaddingTop
import kotlinx.android.synthetic.main.fragment_category_1.*
import org.greenrobot.eventbus.Subscribe

/**
 * Created by Amin on 08/05/2018.
 *
 */
class CategoryFragment1 : CategoryFragmentBase(), OnItemClicked {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = (getActivity() as CategoryActivity?)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_category_1, container, false)
        headerTitle.text = AppController.app.category!!.name
        Glide.with(this).load(AppController.app.category!!.headerImageUrl).into(header)
        populateModelList(GRID_TYPE)
        updateCartView()
        setting.setOnClickListener { AppController.app.passwordDialog(activity) }
        shoppingCartBtnLay.setOnClickListener { openCloseShoppingCart() }
        language.text = AppController.app.language!!.code
        language.setOnClickListener { AppController.app.changeLanguage(activity) }
        shoppingCartRV.layoutManager = GridLayoutManager(activity, 2)
        shoppingCartRV.adapter = FoodItemAdapter(this, CategoryActivity.SHOPPING_LIST_TYPE, this)
        backToMenus.setOnClickListener { onBackPressed() }
        return view
    }

    private fun openCloseShoppingCart() {
        (shoppingCartRV.adapter as FoodItemAdapter).modelList = AppController.app.shoppingCartList
        shoppingCartRV.adapter.notifyDataSetChanged()
        if (shoppingCartCV.visibility == View.GONE) {
            if (AppController.app.shoppingCartList.isEmpty()) {
                return
            }
            ViewAnimator.animate(shoppingCartCV).alpha(0.7f, 1f).dp().translationY(+400f, 0f).onStart {
                shoppingCartCV.visibility = View.VISIBLE
                shadow.visibility = View.VISIBLE
                shadow.setOnClickListener { activity.onBackPressed() }
            }.andAnimate(shoppingCartArrow).duration(500).rotation(180f).andAnimate(shadow).fadeIn().start()
        } else {
            ViewAnimator.animate(shoppingCartCV).dp().translationY(0f, 400f).onStop {
                shoppingCartCV.visibility = View.GONE
                shadow.visibility = View.GONE
                shadow.setOnClickListener { }
            }.alpha(1f, 0.7f).andAnimate(shoppingCartArrow).duration(500).rotation(0f).andAnimate(shadow).fadeOut().start()
        }
    }

    private fun populateModelList(listType: Int) {

        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.addItemDecoration(ItemDecorationPaddingTop(60))
        recyclerView.adapter = FoodItemAdapter(this, listType, this)
        (recyclerView.adapter as FoodItemAdapter).modelList = activity.modelList
        listGridSwitch.setOnClickListener({ switchListGrid() })
    }

    private fun switchListGrid() {
        ViewAnimator.animate(recyclerView).fadeOut().onStop {
            if (type == GRID_TYPE) {
                gridSwitch.setColorFilter(ContextCompat.getColor(activity, R.color.gray_light))
                listSwitch.setColorFilter(ContextCompat.getColor(activity, R.color.colorAccent))
                recyclerView.layoutManager = LinearLayoutManager(activity)
                recyclerView.removeItemDecorationAt(0)
                recyclerView.addItemDecoration(ItemDecorationPaddingTop(60, 1))
                type = CategoryActivity.LIST_TYPE
                recyclerView.adapter = FoodItemAdapter(this, type, this)
                (recyclerView.adapter as FoodItemAdapter).modelList = activity.modelList
            } else {
                listSwitch.setColorFilter(ContextCompat.getColor(activity, R.color.gray_light))
                gridSwitch.setColorFilter(ContextCompat.getColor(activity, R.color.colorAccent))
                recyclerView.layoutManager = GridLayoutManager(activity, 2)
                recyclerView.removeItemDecorationAt(0)
                recyclerView.addItemDecoration(ItemDecorationPaddingTop(60))
                type = GRID_TYPE
                recyclerView.adapter = FoodItemAdapter(this, type, this)
                (recyclerView.adapter as FoodItemAdapter).modelList = activity.modelList
            }
            ViewAnimator.animate(recyclerView).fadeIn().accelerate().duration(300).start()
        }.duration(300).start()

    }

    fun onBackPressed() {
        if (shoppingCartCV.visibility == View.VISIBLE) {
            openCloseShoppingCart()
        } else {
            activity.onBackPressed()
        }
    }

    override fun onClick(model: ItemModel) {

    }

    public override fun updateCartView() {
        var count = 0
        var total: Double = 0.0
        AppController.app.shoppingCartList.forEach {
            count += it.qty
            total += it.qty * it.price
        }
        shoppingCartTitle.text = resources.getQuantityString(R.plurals.order_list, 1, count)
        shoppingCartTotal.text = Constants.formatPriceWithCurrency(total.toInt().toString())
        tableNumber.text = AppController.app.getSP(Constants.TABLE_NUMBER, "1")
    }

    @Subscribe
    public fun handleCartUpdate(event: CartUpdateEvent) {
        updateCartView()
    }

    fun updateCart(model: ItemModel, qty: Int) {

    }

}