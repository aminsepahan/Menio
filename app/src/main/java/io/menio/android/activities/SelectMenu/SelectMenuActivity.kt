package io.menio.android.activities.SelectMenu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github.florent37.viewanimator.ViewAnimator
import io.menio.android.R
import io.menio.android.utilities.Constants
import kotlinx.android.synthetic.main.activity_select_menu.*

class SelectMenuActivity : AppCompatActivity() {

    var selectedItem: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_menu)
        select.setOnClickListener({

        })
        menuLay1.setOnClickListener({
            selectMenu(1, menuItem1Back1)
        })
        menuLay2.setOnClickListener({
            selectMenu(2, menuItem2Back1)
        })
        menuLay3.setOnClickListener({
            selectMenu(3, menuItem3Back1)
        })
    }

    private fun selectMenu(position: Int, selectedView: View) {
        if (selectedItem != 0 && selectedItem != position) {
            val animateView: View
            when (selectedItem) {
                1 -> animateView = menuItem1Back1
                2 -> animateView = menuItem2Back1
                3 -> animateView = menuItem3Back1
                else -> {
                    animateView = menuItem1Back1
                }
            }
            ViewAnimator.animate(animateView).fadeOut().duration(200).onStop {
                selectedItem = position
                ViewAnimator.animate(selectedView).fadeIn().duration(200).start()
            }.start()
        } else {
            selectedItem = position
            ViewAnimator.animate(selectedView).fadeIn().duration(200).start()
        }
    }

    companion object {
        fun open(activity: Activity, isFromSplash: Boolean) {
            val intent = Intent(activity, SelectMenuActivity::class.java);
            intent.putExtra(Constants.IS_FROM_SPLASH, true);
            activity.startActivity(intent);
        }
    }


}