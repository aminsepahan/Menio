package io.menio.android.activities.menu

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.github.florent37.viewanimator.ViewAnimator
import io.menio.android.R
import io.menio.android.activities.menu.CategoryActivity.Companion.GRID_TYPE
import io.menio.android.extensions.log
import io.menio.android.extensions.showHideFade
import io.menio.android.extensions.snack
import io.menio.android.interfaces.OnItemClicked
import io.menio.android.models.CartUpdateEvent
import io.menio.android.models.ItemModel
import io.menio.android.models.VariationModel
import io.menio.android.network.invisible
import io.menio.android.network.visible
import io.menio.android.utilities.AppController
import io.menio.android.utilities.Constants
import io.menio.android.utilities.ItemDecorationPaddingTop
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.dialog_choose_variation.*
import kotlinx.android.synthetic.main.dialog_choose_variation.view.*
import kotlinx.android.synthetic.main.fragment_category_1.*
import kotlinx.android.synthetic.main.fragment_category_1.view.*
import kotlinx.android.synthetic.main.item_cart_options.view.*
import kotlinx.android.synthetic.main.item_variation_list.view.*
import org.greenrobot.eventbus.EventBus
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
        view.headerTitle.text = AppController.app.category!!.name
        Glide.with(this).load(AppController.app.category!!.headerImageUrl).into(view.header)
        updateCartView()
        view.setting.setOnClickListener { AppController.app.passwordDialog(activity) }
        view.shoppingCartBtnLay.setOnClickListener { openCloseShoppingCart() }
        view.language.text = AppController.app.language!!.code
        view.language.setOnClickListener { AppController.app.changeLanguage(activity) }
        view.shoppingCartRV.layoutManager = GridLayoutManager(activity, 2)
        view.shoppingCartRV.adapter = FoodItemAdapter(this, CategoryActivity.SHOPPING_LIST_TYPE, this)
        view.backToMenus.setOnClickListener { onBackPressed() }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        populateModelList(GRID_TYPE)
        updateCartView()
        EventBus.getDefault().register(this)
    }

    private fun openCloseShoppingCart() {
        if (shoppingCartCV.visibility == View.GONE) {
            view?.cartAVProgress?.showHideFade(true)
            view?.shoppingCartTitle?.showHideFade(false)
            activity.getShoppingcart(Consumer {
                activity.log(" result  ===  $it")
                app.shoppingCart = it
                (shoppingCartRV.adapter as FoodItemAdapter).modelList = it.lineItems
                showOrHideShoppingCart(true)
            }, Consumer {
                activity.snack(it.message!!, buttonTxt = R.string.retry,
                        action = View.OnClickListener {
                            openCloseShoppingCart()
                        })
            }
            )
        } else {
            showOrHideShoppingCart(false)
        }
    }

    private fun showOrHideShoppingCart(showOrHide: Boolean) {
        if (showOrHide) {
            ViewAnimator.animate(shoppingCartCV).alpha(0.7f, 1f).dp().translationY(+400f, 0f).onStart {
                view?.cartAVProgress?.showHideFade(false)
                view?.shoppingCartTitle?.showHideFade(true)
                shoppingCartCV.visible()
                shadow.visible()
                shadow.setOnClickListener { onBackPressed() }
            }.andAnimate(shoppingCartArrow).duration(500).rotation(180f).andAnimate(shadow).fadeIn().start()
        } else {
            ViewAnimator.animate(shoppingCartCV).dp().translationY(0f, 400f).onStop {
                shoppingCartCV.invisible()
                shadow.invisible()
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

    override fun onBackPressed(): Boolean {
        return if (shoppingCartCV.visibility == VISIBLE) {
            openCloseShoppingCart()
            false
        } else if (variationDialog.visibility == VISIBLE) {
            closeVariationDialog()
            false
        } else
            true

    }

    override fun onClick(model: ItemModel) {

    }

    public override fun updateCartView() {
        view?.let {
            if (app.shoppingCart != null) {
                it.shoppingCartTitle?.text = resources.getQuantityString(R.plurals.order_list, 1, app.shoppingCart!!.itemsCount)
                it.shoppingCartTotal?.text = Constants.formatPriceWithCurrency(app.shoppingCart!!.total)
            } else {
                it.shoppingCartTitle?.text = resources.getQuantityString(R.plurals.order_list, 1, 0)
                it.shoppingCartTotal?.text = Constants.formatPriceWithCurrency(0)
            }
            it.tableNumber?.text = app.tableNumber.toString()
            it.cartAVProgress?.showHideFade(false)
            it.shoppingCartTitle?.showHideFade(true)
        }
    }

    @Subscribe
    public override fun handleCartUpdate(event: CartUpdateEvent) {
        if (event.id.isNotEmpty()) {
            (recyclerView.adapter as FoodItemAdapter).update(event)
            updateModelCartView()
        } else {
            updateCartView()
        }
    }

    override fun updateCart(model: ItemModel, qty: Int, variation: String?) {
        activity.updateCart(model, qty, variation)
    }


    override fun updateModelCartView() {

    }

    override fun addToCart(model: ItemModel, variation: String?) {
        activity.addToCart(model, variation)
    }


    override fun showVariationsDialog(model: ItemModel, position: Int) {
        variationDialog.variationDialogTitle.text = model.name
        variationDialog.variationDialogClose.setOnClickListener { onBackPressed() }
        variationDialog.baseLinLay.removeAllViews()
        ViewAnimator.animate(variationDialog).fadeIn().duration(200).andAnimate(shadow).fadeIn().onStart {
            shadow.visible()
            variationDialog.visible()
            variationDialog.bringToFront()
        }.onStop {
            for (variation in model.variations!!) {
                val row = LayoutInflater.from(activity).inflate(R.layout.item_variation_list, variationDialog.baseLinLay, false)
                row.variationTitle.text = variation.name
                row.variationPrice.text = variation.price.toString()
                if (variation.qty == 0) {
                    row.cartTitle.visibility = VISIBLE
                    row.cartOptionsLay.visibility = View.GONE
                    row.cartTitle.setOnClickListener({ addToCartClicked(model, variation, row) })
                    row.cartIncrease.setOnClickListener { }
                    row.cartDecrease.setOnClickListener { }
                } else {
                    row.cartTitle.visibility = View.GONE
                    row.cartOptionsLay.visibility = VISIBLE
                    row.cartQty.text = variation.qty.toString()
                    row.cartTitle.setOnClickListener {}
                    row.cartIncrease.setOnClickListener { increaseDecrease(model, true, row, variation) }
                    row.cartDecrease.setOnClickListener { increaseDecrease(model, false, row, variation) }
                }
                variationDialog.baseLinLay.addView(row)
                shadow.setOnClickListener { onBackPressed() }
            }
        }.start()

    }

    private fun closeVariationDialog() {
        ViewAnimator.animate(variationDialog).fadeOut().duration(200).andAnimate(shadow).fadeOut().onStop {
            variationDialog.baseLinLay.removeAllViews()
            variationDialog.invisible()
            shadow.invisible()
        }.start()
    }

    private fun addToCartClicked(model: ItemModel, variation: VariationModel, itemView: View) {
        variation.qty = 1
        model.qty++
        ViewAnimator.animate(itemView.cartTitle).fadeOut().andAnimate(itemView.cartOptionsLay).fadeIn().duration(200).onStart {
            itemView.cartOptionsLay.visibility = VISIBLE
            itemView.cartTitle.setOnClickListener {}
            itemView.cartQty.text = variation.qty.toString()
        }.onStop {
            itemView.cartTitle.visibility = View.GONE
            itemView.cartIncrease.setOnClickListener { increaseDecrease(model, true, itemView, variation) }
            itemView.cartDecrease.setOnClickListener { increaseDecrease(model, false, itemView, variation) }
        }.start()
        addToCart(model, variation.id)
    }

    private fun increaseDecrease(model: ItemModel, increaseDecrease: Boolean, itemView: View, variation: VariationModel) {
        if (increaseDecrease) {
            variation.qty++
            model.qty++
            itemView.cartQty.text = variation.qty.toString()
            updateCart(model, variation.qty, variation.id)
        } else {
            variation.qty--
            model.qty--
            if (variation.qty > 0) {
                itemView.cartQty.text = variation.qty.toString()
                updateCart(model, model.qty, variation.id)
            } else {
                variation.qty = 0
                updateCart(model, variation.qty, variation.id)
                ViewAnimator.animate(itemView.cartTitle).fadeIn().andAnimate(itemView.cartOptionsLay)
                        .fadeOut().duration(200).onStart {
                            itemView.cartTitle.visibility = VISIBLE
                            itemView.cartIncrease.setOnClickListener { }
                            itemView.cartDecrease.setOnClickListener { }
                        }.onStop {
                            itemView.cartOptionsLay.visibility = View.GONE
                            itemView.cartTitle.setOnClickListener { addToCartClicked(model, variation, itemView) }
                        }.start()
            }
            EventBus.getDefault().post(CartUpdateEvent(model.id, model.qty))
        }
    }


    companion object {
        fun newInstance(): CategoryFragment1 {
            return CategoryFragment1()
        }
    }

}