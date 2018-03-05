package io.menio.android.activities.Menu

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import io.menio.android.R
import io.menio.android.activities.Menu.CategoryActivity.Companion.GRID_TYPE
import io.menio.android.activities.Menu.CategoryActivity.Companion.LIST_TYPE
import io.menio.android.interfaces.OnItemClicked
import io.menio.android.models.ItemModel
import io.menio.android.utilities.Constants
import kotlinx.android.synthetic.main.item_food_grid.view.*
import kotlinx.android.synthetic.main.item_food_list.view.*

/**
 * Created by Amin on 26/11/2017.
 *
 */
class FoodItemAdapter(private val clickListener: OnItemClicked, val type: Int, val activity: Activity) : RecyclerView.Adapter<FoodItemAdapter.ViewHolder>() {

    var modelList: MutableList<ItemModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemAdapter.ViewHolder {
        return when (type) {
            GRID_TYPE -> ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_food_grid, parent, false), clickListener, type, activity)
            LIST_TYPE -> ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_food_list, parent, false), clickListener, type, activity)
            else -> {
                ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false),
                        clickListener, type, activity)
            }
        }
    }

    override fun onBindViewHolder(holder: FoodItemAdapter.ViewHolder, position: Int) {
        when (type) {
            GRID_TYPE -> holder.bindItemsGrid(modelList!![position], type)
            LIST_TYPE -> holder.bindItemsGrid(modelList!![position], type)
            else -> holder.bindItemsGrid(modelList!![position], type)
        }
    }

    override fun getItemCount(): Int {
        return modelList!!.size
    }

    class ViewHolder(itemView: View?, private val clickListener: OnItemClicked, type: Int, val activity: Activity) :
            RecyclerView.ViewHolder(itemView) {

        fun bindItemsGrid(model: ItemModel, type: Int) {

            if (type == LIST_TYPE){
                itemView.title.text = model.name
                itemView.price.text = Constants.formatPriceWithCurrency(model.price)
                Glide.with(activity).load(model.thumbnailUrl).into(itemView.image)
                itemView.ingredients.text = model.ingredients
            } else {
                itemView.gridTitle.text = model.name
                itemView.gridPrice.text = Constants.formatPriceWithCurrency(model.price)
                Glide.with(activity).load(model.thumbnailUrl).into(itemView.gridImage)
            }
        }
    }
}