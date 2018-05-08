package io.menio.android.activities.menu

import android.content.Context
import android.support.v4.app.Fragment
import io.menio.android.activities.menu.CategoryActivity.Companion.GRID_TYPE

/**
 * Created by Amin on 08/05/2018.
 *
 */
abstract class CategoryFragmentBase : Fragment() {

    public lateinit var activity: CategoryActivity
    var type: Int = GRID_TYPE

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = (getActivity() as CategoryActivity?)!!
    }

    abstract fun updateCartView()

}