package io.menio.android.activities.Menu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IntDef
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.github.florent37.viewanimator.ViewAnimator
import io.menio.android.R
import io.menio.android.activities.Settings.SettingsActivity
import io.menio.android.interfaces.OnItemClicked
import io.menio.android.models.ItemModel
import io.menio.android.utilities.AppController
import io.menio.android.utilities.Constants.*
import io.menio.android.utilities.ItemDecorationPaddingTop
import kotlinx.android.synthetic.main.activity_category.*

class CategoryActivity : AppCompatActivity(), OnItemClicked {

    var type: Int = GRID_TYPE

    private lateinit var modelList: MutableList<ItemModel>
    val shoppingCartList: MutableList<ItemModel> = emptyList<ItemModel>().toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        headerTitle.text = AppController.app.category!!.name
        modelList = AppController.app.category!!.menuItems
        populateModelList(GRID_TYPE)
        updateCartView()
        setting.setOnClickListener{ SettingsActivity.open(this, true)}

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


    fun updateCart(model: ItemModel, qty: Int) {
        var isInCart = false
        if (qty > 0) {
            for (itemModel in shoppingCartList) {
                if (itemModel.id == model.id) {
                    isInCart = true
                    break
                }
            }
            if (!isInCart){
                shoppingCartList.add(model)
            }
        } else {
            shoppingCartList.remove(model)
        }
        updateCartView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        updateCartView()
        if (requestCode == SETTING_REQ_CODE && resultCode == Activity.RESULT_OK){
            setResult(Activity.RESULT_OK)
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun updateCartView() {
        var count = 0
        shoppingCartList.forEach { count += it.qty }
        shoppingCartTitle.text = "لیست سفارشات: " + count + " مورد"
        tableNumber.text = AppController.app.getSP(TABLE_NUMBER,"1")
    }

    companion object {
        @Retention(AnnotationRetention.SOURCE)
        @IntDef(LIST_TYPE, GRID_TYPE)
        annotation class ListType

        const val LIST_TYPE: Int = 0
        const val GRID_TYPE: Int = 1

        fun open(activity: Activity) {
            val intent = Intent(activity, CategoryActivity::class.java)
            activity.startActivityForResult(intent, EDIT_REQ_CODE);
        }
    }

}
