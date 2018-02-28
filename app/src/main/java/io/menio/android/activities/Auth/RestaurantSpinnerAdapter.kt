package io.menio.android.activities.Auth

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import io.menio.android.R
import io.menio.android.models.RestaurantModel
import kotlinx.android.synthetic.main.row_spinner.view.*

/**
 * Created by Amin on 26/11/2017.
 *
 */
class RestaurantSpinnerAdapter(val activity: Activity, private val modelList: List<RestaurantModel>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(activity).inflate(R.layout.row_spinner, parent, false)
        view.title.text = modelList[position].name
        return view
    }

    override fun getItem(position: Int): Any {
        return modelList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return modelList.size
    }
}