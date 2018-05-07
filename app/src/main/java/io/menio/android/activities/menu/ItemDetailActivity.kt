package io.menio.android.activities.menu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import io.menio.android.R
import io.menio.android.utilities.BaseActivity

class ItemDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
    }


    companion object {
        fun open(activity: Activity, position: Int) {
            val intent = Intent(activity, ItemDetailActivity::class.java)
            activity.startActivity(intent);
        }
    }

}
