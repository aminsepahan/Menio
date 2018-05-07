package io.menio.android.activities.menu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IntDef
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View.GONE
import android.view.View.VISIBLE
import com.bumptech.glide.Glide
import com.github.florent37.viewanimator.ViewAnimator
import io.menio.android.R
import io.menio.android.interfaces.OnItemClicked
import io.menio.android.models.CartUpdateEvent
import io.menio.android.models.ItemModel
import io.menio.android.utilities.AppController
import io.menio.android.utilities.BaseActivity
import io.menio.android.utilities.Constants
import io.menio.android.utilities.Constants.*
import io.menio.android.utilities.ItemDecorationPaddingTop
import kotlinx.android.synthetic.main.activity_category.*
import org.greenrobot.eventbus.Subscribe

class CategoryActivity : BaseActivity(), OnItemClicked {

    var type: Int = GRID_TYPE

    private lateinit var modelList: MutableList<ItemModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        headerTitle.text = AppController.app.category!!.name
        Glide.with(this).load(AppController.app.category!!.headerImageUrl).into(header)
        modelList = AppController.app.category!!.menuItems
        populateModelList(GRID_TYPE)
        updateCartView()
        setting.setOnClickListener { AppController.app.passwordDialog(this) }
        shoppingCartBtnLay.setOnClickListener { openCloseShoppingCart() }
        language.text = AppController.app.language!!.code
        language.setOnClickListener { AppController.app.changeLanguage(this) }
        shoppingCartRV.layoutManager = GridLayoutManager(this, 2)
        shoppingCartRV.adapter = FoodItemAdapter(this, SHOPPING_LIST_TYPE, this)
        backToMenus.setOnClickListener { onBackPressed() }

    }

    private fun openCloseShoppingCart() {
        (shoppingCartRV.adapter as FoodItemAdapter).modelList = AppController.app.shoppingCartList
        shoppingCartRV.adapter.notifyDataSetChanged()
        if (shoppingCartCV.visibility == GONE) {
            if (AppController.app.shoppingCartList.isEmpty()) {
                return
            }
            ViewAnimator.animate(shoppingCartCV).alpha(0.7f, 1f).dp().translationY(+400f, 0f).onStart {
                shoppingCartCV.visibility = VISIBLE
                shadow.visibility = VISIBLE
                shadow.setOnClickListener { onBackPressed() }
            }.andAnimate(shoppingCartArrow).duration(500).rotation(180f).andAnimate(shadow).fadeIn().start()
        } else {
            ViewAnimator.animate(shoppingCartCV).dp().translationY(0f, 400f).onStop {
                shoppingCartCV.visibility = GONE
                shadow.visibility = GONE
                shadow.setOnClickListener {  }
            }.alpha(1f, 0.7f).andAnimate(shoppingCartArrow).duration(500).rotation(0f).andAnimate(shadow).fadeOut().start()
        }
    }

    override fun onBackPressed() {
        if (shoppingCartCV.visibility == VISIBLE) {
            openCloseShoppingCart()
        } else {
            super.onBackPressed()
        }
    }

    private fun populateModelList(listType: Int) {
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.addItemDecoration(ItemDecorationPaddingTop(60))
        recyclerView.adapter = FoodItemAdapter(this, listType, this)
        (recyclerView.adapter as FoodItemAdapter).modelList = modelList
        listGridSwitch.setOnClickListener({ switchListGrid() })
    }

    private fun switchListGrid() {
        ViewAnimator.animate(recyclerView).fadeOut().onStop {
            if (type == GRID_TYPE) {
                gridSwitch.setColorFilter(ContextCompat.getColor(this, R.color.gray_light))
                listSwitch.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent))
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.removeItemDecorationAt(0)
                recyclerView.addItemDecoration(ItemDecorationPaddingTop(60, 1))
                type = LIST_TYPE
                recyclerView.adapter = FoodItemAdapter(this, type, this)
                (recyclerView.adapter as FoodItemAdapter).modelList = modelList
            } else {
                listSwitch.setColorFilter(ContextCompat.getColor(this, R.color.gray_light))
                gridSwitch.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent))
                recyclerView.layoutManager = GridLayoutManager(this, 2)
                recyclerView.removeItemDecorationAt(0)
                recyclerView.addItemDecoration(ItemDecorationPaddingTop(60))
                type = GRID_TYPE
                recyclerView.adapter = FoodItemAdapter(this, type, this)
                (recyclerView.adapter as FoodItemAdapter).modelList = modelList
            }
            ViewAnimator.animate(recyclerView).fadeIn().accelerate().duration(300).start()
        }.duration(300).start()

    }

    override fun onClick(model: ItemModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        updateCartView()
        if (requestCode == SETTING_REQ_CODE &&
                (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_FIRST_USER)) {
            setResult(resultCode)
            finish()
        }

    }

    private fun updateCartView() {
        var count = 0
        var total: Double = 0.0
        AppController.app.shoppingCartList.forEach {
            count += it.qty
            total += it.qty * it.price
        }
        shoppingCartTitle.text = resources.getQuantityString(R.plurals.order_list, 1, count)
        shoppingCartTotal.text = Constants.formatPriceWithCurrency(total.toInt().toString())
        tableNumber.text = AppController.app.getSP(TABLE_NUMBER, "1")
    }


    fun languageChanged() {
        val data = Intent()
        data.putExtra(SELECTED_LANGUAGE, true)
        setResult(Activity.RESULT_CANCELED, data)
        finish()
    }

    @Subscribe
    public fun handleCartUpdate(event: CartUpdateEvent){
        updateCartView()
    }

    companion object {
        @Retention(AnnotationRetention.SOURCE)
        @IntDef(LIST_TYPE, GRID_TYPE, SHOPPING_LIST_TYPE)
        annotation class ListType

        const val LIST_TYPE: Int = 0
        const val GRID_TYPE: Int = 1
        const val SHOPPING_LIST_TYPE: Int = 2

        fun open(activity: Activity) {
            val intent = Intent(activity, CategoryActivity::class.java)
            activity.startActivityForResult(intent, EDIT_REQ_CODE);
        }
    }


}
