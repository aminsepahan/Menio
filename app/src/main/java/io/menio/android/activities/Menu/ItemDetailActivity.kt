package io.menio.android.activities.Menu

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.IntDef
import io.menio.android.R
import io.menio.android.utilities.BaseActivity
import io.menio.android.utilities.Constants

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
