package io.menio.android.activities.Menu

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import io.menio.android.R

/**
 * Created by Amin on 02/11/2017.
 *
 */
class MenuBaseAdapter : RecyclerView.Adapter<MenuBaseAdapter.GroupViewHolder>() {
    override fun onBindViewHolder(holder: GroupViewHolder?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): GroupViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.menu_grid_item, viewGroup, false)
        return GroupViewHolder(v)
    }


    override fun getItemCount(): Int {
        return 0
    }

    class GroupViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var cv: CardView
        internal var title: TextView
        internal var image: ImageView

        init {
            cv = itemView.findViewById<View>(R.id.cv) as CardView
            title = itemView.findViewById<View>(R.id.title) as TextView
            image = itemView.findViewById<View>(R.id.image) as ImageView
        }
    }
}
