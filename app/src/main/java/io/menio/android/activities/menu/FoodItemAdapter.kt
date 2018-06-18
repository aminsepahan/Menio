package io.menio.android.activities.menu

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.github.florent37.viewanimator.ViewAnimator
import io.menio.android.R
import io.menio.android.activities.menu.CategoryActivity.Companion.GRID_TYPE
import io.menio.android.activities.menu.CategoryActivity.Companion.LIST_TYPE
import io.menio.android.activities.menu.CategoryActivity.Companion.SHOPPING_LIST_TYPE
import io.menio.android.interfaces.CartUpdate
import io.menio.android.interfaces.OnItemClicked
import io.menio.android.models.CartUpdateEvent
import io.menio.android.models.ItemModel
import io.menio.android.network.invisible
import io.menio.android.network.visible
import io.menio.android.utilities.Constants
import kotlinx.android.synthetic.main.item_cart_options.view.*
import kotlinx.android.synthetic.main.item_food_grid.view.*
import kotlinx.android.synthetic.main.item_food_list.view.*
import kotlinx.android.synthetic.main.item_food_shopping_cart.view.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Amin on 26/11/2017.
 *
 */
class FoodItemAdapter(private val clickListener: OnItemClicked, val type: Int, val fragment: CategoryFragment1)
    : BaseFoodAdapter<FoodItemAdapter.ViewHolder>(), CartUpdate {

    var modelList: List<ItemModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemAdapter.ViewHolder {
        return when (type) {
            GRID_TYPE -> ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_food_grid, parent, false), clickListener, type, fragment)
            LIST_TYPE -> ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_food_list, parent, false), clickListener, type, fragment)
            SHOPPING_LIST_TYPE -> ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_food_shopping_cart, parent, false), clickListener, type, fragment)
            else -> {
                ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false),
                        clickListener, type, fragment)
            }
        }
    }

    override fun onBindViewHolder(holder: FoodItemAdapter.ViewHolder, position: Int) {
        when (type) {
            GRID_TYPE -> holder.bindItems(modelList!![position], type, position)
            LIST_TYPE -> holder.bindItems(modelList!![position], type, position)
            SHOPPING_LIST_TYPE -> holder.bindItems(modelList!![position], type, position)
            else -> holder.bindItems(modelList!![position], type, position)
        }
    }

    override fun getItemCount(): Int {
        return modelList!!.size
    }

    override fun update(event: CartUpdateEvent) {
        for ((i, item) in modelList!!.withIndex()) {
            if (item.id == event.id) {
                item.qty = event.qty
                notifyItemChanged(i)
            }
        }
    }

    class ViewHolder(itemView: View?, private val clickListener: OnItemClicked, type: Int, val fragment: CategoryFragment1) :
            RecyclerView.ViewHolder(itemView) {

        fun bindItems(model: ItemModel, type: Int, position: Int) {

            when (type) {
                LIST_TYPE -> {
                    itemView.title.text = model.name
                    itemView.price.text = Constants.formatPriceWithCurrency(model.price)
                    Glide.with(fragment).load(model.thumbnailUrl).into(itemView.image)
                    itemView.moreInfo.setOnClickListener { ItemDetailActivity.open(fragment.activity, position) }
                    itemView.ingredients.text = model.ingredient
                }
                GRID_TYPE -> {
                    itemView.gridTitle.text = model.name
                    itemView.gridPrice.text = Constants.formatPriceWithCurrency(model.price)
                    Glide.with(fragment).load(model.thumbnailUrl).into(itemView.gridImage)
                }
                SHOPPING_LIST_TYPE -> {
                    itemView.titleShoppingCart.text = model.name
                    itemView.priceShoppingCart.text = Constants.formatPriceWithCurrency(model.salePrice)
                    if (model.variationName != null) {
                        itemView.variationTitleShoppingCart.text = model.variationName
                        itemView.variationTitleShoppingCart.visible()
                        itemView.dividerShoppingCart.visible()
                    } else {
                        itemView.variationTitleShoppingCart.invisible()
                        itemView.dividerShoppingCart.invisible()
                    }
                    Glide.with(fragment).load(model.thumbnailUrl).into(itemView.imageShoppingCart)
                    model.id = model.menuItemId
                }
                else -> {

                }
            }
            if (model.variations != null && model.variations!!.isNotEmpty()) {
                var total = 0
                for (variation in model.variations!! ) {
                    total += variation.qty
                }
                model.qty = total
            }
            if (model.qty == 0) {
                itemView.cartTitle.visible()
                itemView.cartOptionsLay.invisible()
                itemView.cartTitle.setOnClickListener({ addToCartClicked(model, position) })
                itemView.cartIncrease.setOnClickListener { }
                itemView.cartDecrease.setOnClickListener { }
            } else {
                itemView.cartTitle.invisible()
                itemView.cartOptionsLay.visible()
                itemView.cartQty.text = model.qty.toString()
                itemView.cartTitle.setOnClickListener {}
                itemView.cartIncrease.setOnClickListener { increaseDecrease(model, true, position) }
                itemView.cartDecrease.setOnClickListener { increaseDecrease(model, false, position) }
            }
        }

        private fun addToCartClicked(model: ItemModel, position: Int) {
            model.variations?.let {
                if (it.isNotEmpty()) {
                    fragment.showVariationsDialog(model, position)
                    return
                }
            }
            model.qty = 1
            ViewAnimator.animate(itemView.cartTitle).fadeOut().andAnimate(itemView.cartOptionsLay).fadeIn().duration(200).onStart {
                itemView.cartOptionsLay.visibility = VISIBLE
                itemView.cartTitle.setOnClickListener {}
                itemView.cartQty.text = model.qty.toString()
            }.onStop {
                itemView.cartTitle.visibility = GONE
                itemView.cartIncrease.setOnClickListener { increaseDecrease(model, true, position) }
                itemView.cartDecrease.setOnClickListener { increaseDecrease(model, false, position) }
            }.start()
            fragment.addToCart(model, model.variationId)
        }

        private fun increaseDecrease(model: ItemModel, increaseDecrease: Boolean, position: Int) {
            model.variations?.let {
                if (it.isNotEmpty()) {
                    fragment.showVariationsDialog(model, position)
                    return
                }
            }
            if (increaseDecrease) {
                model.qty++
                itemView.cartQty.text = model.qty.toString()
                fragment.updateCart(model, model.qty, model.variationId)
            } else {
                model.qty--
                if (model.qty > 0) {
                    itemView.cartQty.text = model.qty.toString()
                    fragment.updateCart(model, model.qty, model.variationId)
                } else {
                    model.qty = 0
                    fragment.updateCart(model, model.qty, model.variationId)
                    ViewAnimator.animate(itemView.cartTitle).fadeIn().andAnimate(itemView.cartOptionsLay)
                            .fadeOut().duration(200).onStart {
                                itemView.cartTitle.visibility = VISIBLE
                                itemView.cartIncrease.setOnClickListener { }
                                itemView.cartDecrease.setOnClickListener { }
                            }.onStop {
                                itemView.cartOptionsLay.visibility = GONE
                                itemView.cartTitle.setOnClickListener { addToCartClicked(model, position) }
                            }.start()
                }
                EventBus.getDefault().post(CartUpdateEvent(model.id, model.qty))
            }
        }
    }
}