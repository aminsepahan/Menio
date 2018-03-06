package io.menio.android.activities.Menu

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.github.florent37.viewanimator.ViewAnimator
import io.menio.android.R
import io.menio.android.activities.Menu.CategoryActivity.Companion.GRID_TYPE
import io.menio.android.activities.Menu.CategoryActivity.Companion.LIST_TYPE
import io.menio.android.activities.Menu.CategoryActivity.Companion.SHOPPING_LIST_TYPE
import io.menio.android.interfaces.CartUpdate
import io.menio.android.interfaces.OnItemClicked
import io.menio.android.models.CartUpdateEvent
import io.menio.android.models.ItemModel
import io.menio.android.utilities.Constants
import kotlinx.android.synthetic.main.item_cart_options.view.*
import kotlinx.android.synthetic.main.item_food_grid.view.*
import kotlinx.android.synthetic.main.item_food_list.view.*
import kotlinx.android.synthetic.main.item_food_shopping_cart.view.*

/**
 * Created by Amin on 26/11/2017.
 *
 */
class FoodItemAdapter(private val clickListener: OnItemClicked, val type: Int, val activity: Activity)
    : RecyclerView.Adapter<FoodItemAdapter.ViewHolder>(), CartUpdate {

    var modelList: MutableList<ItemModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemAdapter.ViewHolder {
        return when (type) {
            GRID_TYPE -> ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_food_grid, parent, false), clickListener, type, activity)
            LIST_TYPE -> ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_food_list, parent, false), clickListener, type, activity)
            SHOPPING_LIST_TYPE -> ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_food_shopping_cart, parent, false), clickListener, type, activity)
            else -> {
                ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false),
                        clickListener, type, activity)
            }
        }
    }

    override fun onBindViewHolder(holder: FoodItemAdapter.ViewHolder, position: Int) {
        when (type) {
            GRID_TYPE -> holder.bindItems(modelList!![position], type)
            LIST_TYPE -> holder.bindItems(modelList!![position], type)
            SHOPPING_LIST_TYPE -> holder.bindItems(modelList!![position], type)
            else -> holder.bindItems(modelList!![position], type)
        }
    }

    override fun getItemCount(): Int {
        return modelList!!.size
    }

    override fun update(event: CartUpdateEvent) {

    }

    class ViewHolder(itemView: View?, private val clickListener: OnItemClicked, type: Int, val activity: Activity) :
            RecyclerView.ViewHolder(itemView) {

        fun bindItems(model: ItemModel, type: Int) {

            when (type) {
                LIST_TYPE -> {
                    itemView.title.text = model.name
                    itemView.price.text = Constants.formatPriceWithCurrency(model.price)
                    Glide.with(activity).load(model.thumbnailUrl).into(itemView.image)
                    itemView.ingredients.text = model.ingredient
                }
                GRID_TYPE -> {
                    itemView.gridTitle.text = model.name
                    itemView.gridPrice.text = Constants.formatPriceWithCurrency(model.price)
                    Glide.with(activity).load(model.thumbnailUrl).into(itemView.gridImage)
                }
                SHOPPING_LIST_TYPE -> {
                    itemView.titleShoppingCart.text = model.name
                    itemView.priceShoppingCart.text = Constants.formatPriceWithCurrency(model.price)
                    Glide.with(activity).load(model.thumbnailUrl).into(itemView.imageShoppingCart)
                }
                else -> {

                }
            }
            if (model.qty == 0) {
                itemView.cartTitle.visibility = VISIBLE
                itemView.cartOptionsLay.visibility = GONE
                itemView.cartTitle.setOnClickListener({ addToCartClicked(model) })
                itemView.cartIncrease.setOnClickListener { }
                itemView.cartDecrease.setOnClickListener { }
            } else {
                itemView.cartTitle.visibility = GONE
                itemView.cartOptionsLay.visibility = VISIBLE
                itemView.cartQty.text = model.qty.toString()
                itemView.cartTitle.setOnClickListener {}
                itemView.cartIncrease.setOnClickListener { increaseDecrease(model, true) }
                itemView.cartDecrease.setOnClickListener { increaseDecrease(model, false) }

            }
        }

        private fun addToCartClicked(model: ItemModel) {
            model.qty = 1
            ViewAnimator.animate(itemView.cartTitle).fadeOut().andAnimate(itemView.cartOptionsLay).fadeIn().duration(200).onStart {
                itemView.cartOptionsLay.visibility = VISIBLE
                itemView.cartTitle.setOnClickListener {}
                itemView.cartQty.text = model.qty.toString()
            }.onStop {
                itemView.cartTitle.visibility = GONE
                itemView.cartIncrease.setOnClickListener { increaseDecrease(model, true) }
                itemView.cartDecrease.setOnClickListener { increaseDecrease(model, false) }
            }.start()
            (activity as CategoryActivity).updateCart(model, 1)
        }

        private fun increaseDecrease(model: ItemModel, increaseDecrease: Boolean) {
            if (increaseDecrease) {
                model.qty++
                itemView.cartQty.text = model.qty.toString()
                (activity as CategoryActivity).updateCart(model, model.qty)
            } else {
                model.qty--
                if (model.qty > 0) {
                    itemView.cartQty.text = model.qty.toString()
                } else {
                    model.qty = 0
                    ViewAnimator.animate(itemView.cartTitle).fadeIn().andAnimate(itemView.cartOptionsLay)
                            .fadeOut().duration(200).onStart {
                                itemView.cartTitle.visibility = VISIBLE
                                itemView.cartIncrease.setOnClickListener { }
                                itemView.cartDecrease.setOnClickListener { }
                            }.onStop {
                                itemView.cartOptionsLay.visibility = GONE
                                itemView.cartTitle.setOnClickListener { addToCartClicked(model) }
                            }.start()
                }
                (activity as CategoryActivity).updateCart(model, model.qty)
            }
        }
    }
}