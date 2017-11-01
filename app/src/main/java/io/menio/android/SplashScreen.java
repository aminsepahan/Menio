package io.menio.android;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.iranfmcg.dokan.activities.AddressAndRegion.ChooseRegionIdActivity;
import com.iranfmcg.dokan.activities.Authentication.LoginActivity;
import com.iranfmcg.dokan.model.DataCenter;
import com.iranfmcg.dokan.utilities.Interfaces;
import com.iranfmcg.dokan.utilities.NetworkRequests;
import com.iranfmcg.dokan.utilities.Snippets;
import com.iranfmcg.dokan.utilities.StaticData;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import io.menio.android.utilities.Constants;

import static com.iranfmcg.dokan.utilities.Constants.IS_LOGGED_IN;
import static com.iranfmcg.dokan.utilities.Constants.LOGIN_SIGNUP;
import static com.iranfmcg.dokan.utilities.Constants.getCheckForNewVersionUrl;
import static com.iranfmcg.dokan.utilities.Snippets.isSet;
import static io.menio.android.utilities.Constants.*;

/**
 * Created by Melo on 11/10/2016.
 *
 */
public class SplashScreen extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashScreenBackColor));

        checkForNewVersion();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_SIGNUP && resultCode == RESULT_OK) {
            openApp();
        }
    }


    public void openApp() {
        if (isSet(IS_LOGGED_IN)) {
            DataCenter.setHasToken(true);
            if (StaticData.getRegionId() != 0) {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                ChooseRegionIdActivity.open(this, false, true);
            }
        } else {
            LoginActivity.open(this, false, false, null, null);
            finish();
        }
    }
}
