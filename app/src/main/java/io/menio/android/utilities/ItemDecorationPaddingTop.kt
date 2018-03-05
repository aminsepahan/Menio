package io.menio.android.utilities

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by Amin on 15/12/2017.
 *
 */
class ItemDecorationPaddingTop(val paddingTop: Int, val column: Int) : RecyclerView.ItemDecoration() {

    constructor(paddingTop: Int) : this(paddingTop, 2)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemCount = state.itemCount

        val itemPosition = parent.getChildAdapterPosition(view)

        // no position, leave it alone
        if (itemPosition == RecyclerView.NO_POSITION) {
            return
        }

        // first item
        if (itemPosition < column) {
            outRect.set(view.paddingLeft, Snippets.dpToPixels(paddingTop.toFloat()), view.paddingRight, view.paddingBottom)
        } else if (itemCount > 0 && itemPosition == itemCount - 1) {
            outRect.set(view.paddingLeft, view.paddingTop, view.paddingRight, Snippets.dpToPixels(50f))
        } else {
            outRect.set(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)
        }// every other item
        // last item
    }

}