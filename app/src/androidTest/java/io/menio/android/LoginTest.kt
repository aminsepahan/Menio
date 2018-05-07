package io.menio.android

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import io.menio.android.activities.auth.AuthActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Amin on 07/11/2017.
 */

@RunWith(AndroidJUnit4::class)
class LoginTest {

    @Rule
    @JvmField
    val activity = ActivityTestRule<AuthActivity>(AuthActivity::class.java)


    @Test
    fun testLogin() {
        onView(withId(R.id.loginBtn)).perform(typeText("09332708005"))
        onView(withId(R.id.password)).perform(typeText("123456"))
        onView(withId(R.id.userName)).perform()
    }

}