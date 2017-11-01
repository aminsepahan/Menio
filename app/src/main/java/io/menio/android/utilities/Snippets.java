package io.menio.android.utilities;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.github.florent37.viewanimator.ViewAnimator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.menio.android.R;

import static io.menio.android.utilities.Constants.FALSE;
import static io.menio.android.utilities.Constants.SP_FILE_NAME_BASE;
import static io.menio.android.utilities.Constants.TRUE;
import static io.menio.android.utilities.Constants.themeFont;
import static io.menio.android.utilities.Constants.themeFontBold;
import static io.menio.android.utilities.Constants.themeFontLight;


/**
 * Created by Amin on 3/29/2015.
 * this class is place to save all those methods that are
 * going to be used multiple time in application and in different activities
 */
public class Snippets {

    //md5 converter
    public static String md5(String in) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
this function is used to download an file from URL
and save it to the base location of app, with a folder name taken from string.xml
*/
    public static void downloadFile(String URL, String name, Context context, Activity activity) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        , 100);
            }
            return;
        }

        File direct = new File(Environment.getExternalStorageDirectory()
                + context.getResources().getString(R.string.app_name));
        //check if the folder already exists
        Boolean b = direct.exists();

        if (!b) {
            //if not, try to create it
            b = direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(URL);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(true).setTitle(name + " " + URL)
                .setDescription(name)
                .setVisibleInDownloadsUi(true)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir("/" + context.getResources().getString(R.string.app_name),
                        name + URL)
                .allowScanningByMediaScanner();
        mgr.enqueue(request);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    public static void showMessageDialog(Activity activity, String message,
                                         final Interfaces.messageOkListener okListener,
                                         final Interfaces.messageOkListener noListener,
                                         boolean showNegativeBtn) {

        hideKeyboard(activity, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    if (okListener != null) {
                        okListener.onOkClicked();
                    }
                });
        if (showNegativeBtn) {
            builder.setNegativeButton(R.string.no, (dialogInterface, i) -> {
                if (noListener != null) {
                    noListener.onOkClicked();
                }
            });
        }

        AlertDialog dialog = builder.show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        Typeface face = Typeface.createFromAsset(activity.getAssets(), Constants.themeFont);
        textView.setTypeface(face);
        textView = (TextView) dialog.findViewById(android.R.id.button1);
        face = Typeface.createFromAsset(activity.getAssets(), themeFontBold);
        textView.setTypeface(face);
        textView = (TextView) dialog.findViewById(android.R.id.button2);
        face = Typeface.createFromAsset(activity.getAssets(), themeFontBold);
        textView.setTypeface(face);

    }

    public static void showDismissDialog(final Activity activity, String message, final Interfaces.messageOkListener listener) {

        showMessageDialog(activity, message, activity.getString(R.string.thanks), listener, null, null, false, true);
    }


    public static void showMessageDialog(Activity activity, String message, String okBtnText,
                                         final Interfaces.messageOkListener okListener, String noBtnText,
                                         final Interfaces.messageOkListener noListener,
                                         boolean showNegativeBtn) {

        showMessageDialog(activity, message, okBtnText, okListener, noBtnText, noListener, showNegativeBtn, true);

    }

    public static void showMessageDialog(Activity activity, String message, String okBtnText,
                                         final Interfaces.messageOkListener okListener, String noBtnText,
                                         final Interfaces.messageOkListener noListener,
                                         boolean showNegativeBtn, boolean isCancelable) {

        hideKeyboard(activity, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(okBtnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (okListener != null) {
                            okListener.onOkClicked();
                        }
                    }
                });
        if (showNegativeBtn) {
            builder.setNegativeButton(noBtnText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (noListener != null) {
                        noListener.onOkClicked();
                    }
                }
            });
        }
        builder.setCancelable(isCancelable);

        AlertDialog dialog = builder.show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        Typeface face = Typeface.createFromAsset(activity.getAssets(), themeFont);
        textView.setTypeface(face);
        textView = (TextView) dialog.findViewById(android.R.id.button1);
        face = Typeface.createFromAsset(activity.getAssets(), themeFontBold);
        textView.setTypeface(face);
        textView = (TextView) dialog.findViewById(android.R.id.button2);
        face = Typeface.createFromAsset(activity.getAssets(), themeFontBold);
        textView.setTypeface(face);

    }


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public static boolean verifyStoragePermissions(Activity activity, int requestCode) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    requestCode
            );
            return false;
        } else {
            return true;
        }
    }

    public static boolean isDeviceSupportCamera() {
        // this device has a camera
        // no camera on this device
        return AppController.getInstance().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static void changeTabsFont(TabLayout tabLayout) {

        Typeface tf = Typeface.createFromAsset(tabLayout.getContext().getAssets(), themeFontBold);

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(tf);
                }
            }
        }
    }

    public static String getSP(String key, String defaultValue) {
        SharedPreferences sp
                = AppController
                .applicationContext
                .getSharedPreferences(SP_FILE_NAME_BASE, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static String getSP(String key) {
        return getSP(key, FALSE);
    }

    public static boolean isSet(String key) {
        return !getSP(key, FALSE).equals(FALSE);
    }

    public static boolean getSPBoolean(String key) {
        SharedPreferences sp
                = AppController
                .applicationContext
                .getSharedPreferences(SP_FILE_NAME_BASE, Context.MODE_PRIVATE);
        return sp.getString(key, FALSE).equals(TRUE);
    }

    public static int getSPInt(String key) {
        SharedPreferences sp
                = AppController
                .applicationContext
                .getSharedPreferences(SP_FILE_NAME_BASE, Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    public static void setSP(String key, String value) {
        SharedPreferences sp
                = AppController
                .applicationContext
                .getSharedPreferences(SP_FILE_NAME_BASE, Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sp.edit();
        spe.putString(key, value);
        spe.apply();
    }

    public static void setSPInt(String key, int value) {
        SharedPreferences sp
                = AppController
                .applicationContext
                .getSharedPreferences(SP_FILE_NAME_BASE, Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt(key, value);
        spe.apply();
    }

    public static int dpToPixels(float dp) {
        final float scale = AppController.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int getDisplayWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static void setFontForActivity(View view) {

        Typeface tf = Typeface.createFromAsset(view.getContext().getAssets(), themeFont);
        Typeface tfb = Typeface.createFromAsset(view.getContext().getAssets(), themeFontBold);
        Typeface tfl = Typeface.createFromAsset(view.getContext().getAssets(), themeFontLight);
        //Set up touch listener for non-text cv views to hide keyboard.
        setFontRecursive(view, tf, tfb, tfl);

    }

    public static void setFontForActivity(Activity activity) {

        setFontForActivity(activity.findViewById(R.id.root));

    }

    private static void setFontRecursive(View view, Typeface tf, Typeface tfb, Typeface tfl) {
        if (view instanceof TextView) {
            if (view.getTag() != null && view.getTag().equals("bold")) {
                ((TextView) view).setTypeface(tfb);
            } else {
                if (view.getTag() != null && view.getTag().equals("light")) {
                    ((TextView) view).setTypeface(tfl);
                } else {
                    ((TextView) view).setTypeface(tf);
                }
            }
        } else if (view instanceof TextInputLayout) {
            if (view.getTag() != null && view.getTag().equals("bold")) {
                ((TextInputLayout) view).setTypeface(tfb);
            } else {
                if (view.getTag() != null && view.getTag().equals("light")) {
                    ((TextInputLayout) view).setTypeface(tfl);
                } else {
                    ((TextInputLayout) view).setTypeface(tf);
                }
            }
        } else {
            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                    View innerView = ((ViewGroup) view).getChildAt(i);

                    setFontRecursive(innerView, tf, tfb, tfl);
                }
            }
        }
    }

    public static void setAutoCompleteList(String SPKey, AutoCompleteTextView autoCompleteTextView) {
        List<String> autoCompleteList = new ArrayList<>();
        if (!getSP(SPKey, null).equals(FALSE)) {
            try {
                autoCompleteList = JSON.parseArray(getSP(SPKey, null), String.class);
            } catch (Exception ignored) {
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AppController.getInstance()
                , android.R.layout.simple_spinner_dropdown_item, autoCompleteList);

        autoCompleteTextView.setDropDownBackgroundResource(R.color.black);

        autoCompleteTextView.setAdapter(adapter);
    }

    public static void saveAutoCompleteListEntry(String SPKey, String newEntry) {

        List<String> autoCompleteList = new ArrayList<>();
        boolean found = false;
        if (getSP(SPKey, null) != null) {
            try {
                autoCompleteList = JSON.parseArray(getSP(SPKey, null), String.class);
            } catch (Exception ignored) {
            }
        }
        if (autoCompleteList.size() > 0) {
            for (String s : autoCompleteList) {
                if (s.equals(newEntry)) {
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            autoCompleteList.add(newEntry);
            setSP(SPKey, JSON.toJSONString(autoCompleteList));
        }
    }

    public static void showFade(View view, boolean showOrHide, int duration) {
        if (showOrHide) {
            ViewAnimator.animate(view).alpha(0, 1).duration(duration).start();
        } else {
            ViewAnimator.animate(view).alpha(1, 0).duration(duration).start();
        }
    }

    public static void hideKeyboard(Activity activity, boolean showOrHide) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            if (showOrHide) {
                inputMethodManager.showSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
                activity.getCurrentFocus().clearFocus();
            } else {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }

        }
    }

    public static void setupUI(final Activity activity, final View view) {

        //Set up touch listener for non-text cv views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (activity.getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                        activity.getCurrentFocus().clearFocus();
                    }
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(activity, innerView);
            }
        }
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    public static String revertPersianNumbers(String num) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < num.length(); i++) {
            switch (num.charAt(i)) {
                case '٠':
                case '۰':
                    builder.append(0);
                    break;
                case '١':
                case '۱':
                    builder.append(1);
                    break;
                case '٢':
                case '۲':
                    builder.append(2);
                    break;
                case '٣':
                case '۳':
                    builder.append(3);
                    break;
                case '٤':
                case '۴':
                    builder.append(4);
                    break;
                case '٥':
                case '۵':
                    builder.append(5);
                    break;
                case '٦':
                case '۶':
                    builder.append(6);
                    break;
                case '٧':
                case '۷':
                    builder.append(7);
                    break;
                case '٨':
                case '۸':
                    builder.append(8);
                    break;
                case '٩':
                case '۹':
                    builder.append(9);
                    break;

                default:
                    builder.append(num.charAt(i));
            }
        }
        return builder.toString();
    }


    public static HashMap<String, String> jsonToMap(String t) throws JSONException {

        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = jObject.getString(key);
            map.put(key, value);

        }

        return map;
    }


    public static void setColorFilter(int iColor, Drawable drawable) {
        int red = (iColor & 0xFF0000) / 0xFFFF;
        int green = (iColor & 0xFF00) / 0xFF;
        int blue = iColor & 0xFF;

        float[] matrix = {0, 0, 0, 0, red,
                0, 0, 0, 0, green,
                0, 0, 0, 0, blue,
                0, 0, 0, 1, 0};
        drawable.setColorFilter(new ColorMatrixColorFilter(matrix));
    }

    public static void setColorFilter(int iColor, ImageView image) {
        setColorFilter(iColor, image.getDrawable());
    }

    public static void setColorFilter(int iColor, View view) {
        if (view instanceof ImageView) {
            setColorFilter(iColor, ((ImageView) view).getDrawable());
        } else {
            setColorFilter(iColor, view.getBackground());
        }
    }


    public static String clearPriceString(String priceTemp) {
//        priceTemp = priceTemp.replaceAll(",", "");
//        priceTemp = priceTemp.replaceAll("٬", "");
//        priceTemp = priceTemp.replaceAll("\\.", "");
//        priceTemp = priceTemp.replaceAll("", "");
        priceTemp = revertPersianNumbers(priceTemp);
        priceTemp = priceTemp.replaceAll("\\D", "");
        return priceTemp;
    }

    public static int getWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static int getHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }


    public static void setActivityTheme(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        window.setNavigationBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
    }

    public static void showError(Activity activity, String message, String action, final Interfaces.CallBack callBack) {
        showError(activity, message, action, callBack, false);
    }

    public static void showError(Activity activity, String message, String action,
                                 final Interfaces.CallBack callBack, boolean indefinite) {
        if (activity.findViewById(R.id.root) == null) {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            return;
        }
        Snackbar snackbar;
        if (indefinite) {
            snackbar = Snackbar.make(activity.findViewById(R.id.root), message, Snackbar.LENGTH_INDEFINITE);
        } else {
            snackbar = Snackbar.make(activity.findViewById(R.id.root), message, Snackbar.LENGTH_LONG);
        }
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) {
                    callBack.call();
                }
            }
        }).setActionTextColor(activity.getResources().getColor(R.color.gold));
        snackbar.getView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setFontForActivity(snackbar.getView());
        snackbar.show();
    }

    public static void showToolbarProgress(Activity activity, boolean showOrHide) {
//        if (activity != null && activity.findViewById(R.id.toolbar) != null
//                && activity.findViewById(R.id.toolbar).findViewById(R.id.toolbarProgressBar) != null){
//            ((ProgressBar) activity.findViewById(R.id.toolbar).findViewById(R.id.toolbarProgressBar)).getIndeterminateDrawable().setColorFilter(
//                    activity.getResources().getColor(R.color.white),
//                    android.graphics.PorterDuff.Mode.SRC_IN);
//            if (showOrHide){
//                ViewAnimator.animate(activity.findViewById(R.id.toolbar).findViewById(R.id.toolbarProgressBar))
//                        .fadeIn().scale(0.6f,1f).duration(500).descelerate().start();
//            } else {
//                ViewAnimator.animate(activity.findViewById(R.id.toolbar).findViewById(R.id.toolbarProgressBar))
//                        .fadeOut().scale(1f,0.8f).duration(500).descelerate().start();
//            }
//        }
    }

    public static String convertTimestampToText(long timestamp) {

        Calendar calendar = Calendar.getInstance();
        timestamp = calendar.getTimeInMillis() - timestamp;

        if (TimeUnit.MILLISECONDS.toDays(timestamp) > 31) {
            return TimeUnit.MILLISECONDS.toDays(timestamp) % 31 + " ماه پیش";
        } else if (TimeUnit.MILLISECONDS.toDays(timestamp) > 0) {
            return TimeUnit.MILLISECONDS.toDays(timestamp) + " روز پیش";
        } else if (TimeUnit.MILLISECONDS.toHours(timestamp) > 0) {
            return TimeUnit.MILLISECONDS.toHours(timestamp) + " ساعت پیش";
        } else if (TimeUnit.MILLISECONDS.toMinutes(timestamp) > 0) {
            return TimeUnit.MILLISECONDS.toMinutes(timestamp) + " دقیقه پیش";
        } else {
            return "کمتر از یک دقیقه پیش";
        }
    }
}
