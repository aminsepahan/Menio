package io.menio.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import io.menio.android.R;
import io.menio.android.activities.Auth.AuthActivity;
import io.menio.android.activities.Menu.MenuActivity;

import static io.menio.android.utilities.Constants.IS_LOGGED_IN;
import static io.menio.android.utilities.Snippets.isSet;

/**
 * Created by Melo on 11/10/2016.
 */
public class SplashScreen extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.splashScreenBackColor));
        openApp();

    }


    public void openApp() {
        if (isSet(IS_LOGGED_IN)) {
            MenuActivity.Companion.open(this, true);

        } else {
            AuthActivity.Companion.open(this, true);
        }
        finish();
    }
}
