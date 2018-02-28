package io.menio.android.utilities;

import android.net.Uri;

import java.text.DecimalFormat;

import io.menio.android.R;

/**
 * Created by Amin on 11/16/2014.
 * be cause this app will be used for different websites, it needs different Constants too
 */
public class Constants {

    public static boolean clientOrRealtor = true;

    // LINKS AND URLs:
    public static final String API_HOST = "https://api.menio.io/api/v1/";
    public static Uri.Builder getApiHttpUrlBuilder(boolean withVersion) {
        Uri.Builder builder = Uri.parse(API_HOST).buildUpon();
        return builder;
    }

    public static String getLoginUrl() {
        Uri url = getApiHttpUrlBuilder(true)
                .appendPath("restaurant")
                .appendPath("login")
                .build();
        return url.toString();
    }

    public static String getRestaurant() {
        Uri url = getApiHttpUrlBuilder(true)
                .appendPath("restaurant")
                .appendPath("me")
                .appendPath("restaurant")
                .build();
        return url.toString();
    }

    public static String getMenuUrl(String locale) {
        Uri url = getApiHttpUrlBuilder(true)
                .appendPath("restaurant")
                .appendPath("menus")
                .appendQueryParameter("locale", locale)
                .build();
        return url.toString();
    }

    public static String getMenuUrl(String menuId,String locale, String currency) {
        Uri url = getApiHttpUrlBuilder(true)
                .appendPath("restaurant")
                .appendPath("menus")
                .appendPath(menuId)
                .appendQueryParameter("locale", locale)
                .appendQueryParameter("currency", currency)
                .build();
        return url.toString();
    }

    //AMIN: Name Strings:
    public static String LOG_TAG = "****AMIN ** DEBUG****";
    private static final String IMAGE_DIRECTORY_NAME = "Moshaver";



    //AMIN Shared Pref Keys
    public static String SP_FILE_NAME_BASE = "sp_file_base";
    public static String FALSE = "FALSE";
    public static String TRUE = "TRUE";
    public static String REGIONS = "REGIONS";
    public static String SELECTED_REGION = "SELECTED_REGION";
    public static String SELECTED_REGION_NAME = "SELECTED_REGION_NAME";
    public static String SELECTED_MENU = "SELECTED_MENU";
    public static String SELECTED_BRANCH = "SELECTED_BRANCH";
    public static String SELECTED_BRANCH_ID = "SELECTED_BRANCH_ID";
    public static String SELECTED_BRANCH_NAME = "SELECTED_BRANCH_NAME";
    public static String SELECTED_LANGUAGE = "SELECTED_LANGUAGE";
    public static String SELECTED_LANGUAGE_NAME = "SELECTED_LANGUAGE_NAME";
    public static String SELECTED_LANGUAGE_CODE = "SELECTED_LANGUAGE_CODE";
    public static String SELECTED_CURRENCY = "SELECTED_CURRENCY";
    public static String SELECTED_CURRENCY_NAME = "SELECTED_CURRENCY_NAME";
    public static String SELECTED_CURRENCY_CODE = "SELECTED_CURRENCY_CODE";
    public static String SLIDERS = "SLIDERS";
    public static String PROPERTY_FORM = "PROPERTY_FORM";
    public static String SEARCH_FORM = "SEARCH_FORM";

    //AMIN INTENT KEYS:
    public static String PARENT_ID = "PARENT_ID";
    public static String POSITION_MENU = "POSITION_MENU";
    public static String TITLE = "TITLE";
    public static String QUERY = "QUERY";
    public static String TYPE = "TYPE";
    public static String PRICE = "PRICE";
    public static String SALE_PRICE = "SALE_PRICE";
    public static String SALE = "SALE";
    public static String SLOGAN = "SLOGAN";
    public static String LOGO = "LOGO";
    public static String HEADER = "HEADER";
    public static String OFFLINE_MODE = "OFFLINE_MODE";
    public static String BOOKMARK_LIST = "BOOKMARK_LIST";
    public static String DISTRICT_LIST = "DISTRICT_LIST";
    public static String BOOKMARK_NUM = "BOOKMARK_NUM";
    public static String LINK = "LINK";
    public static String POSITION = "POSITION";
    public static String COUNT = "COUNT";
    public static String MAX_NUMBER = "MAX_NUMBER";
    public static String IMAGE = "IMAGE";
    public static String MODEL = "MODEL";
    public static String CATEGORIES = "CATEGORIES";
    public static String USER = "USER";
    public static String PRODUCT_GROUP_ID = "PRODUCT_GROUP_ID";
    public static String BRAND_ID = "BRAND_ID";
    public static String BRANCH_ID = "BRANCH_ID";
    public static String SORT = "SORT";
    public static String SHOW_BRANDS = "SHOW_BRANDS";
    public static String SEARCH_HISTORY = "SEARCH_HISTORY";
    public static String LATEST_SENT_VERSION = "LATEST_SENT_VERSION";
    public static String IS_LOGGED_IN = "IS_LOGGED_IN";
    public static String LIKED = "LIKED";
    public static String IS_FROM_SPLASH = "IS_FROM_SPLASH";
    public static String IS_SHOPPING_LIST = "IS_SHOPPING_LIST";
    public static String USER_ID = "USER_ID";
    public static String USER_NAME = "USER_NAME";
    public static String TEMP_USER_MOBILE = "TEMP_USER_MOBILE";
    public static String TEMP_USER_PASS = "TEMP_USER_PASS";
    public static String USER_MOBILE = "USER_MOBILE";
    public static String USER_PASS = "USER_PASS";
    public static String AGENCY_NAME = "AGENCY_NAME";
    public static String LOGIN_COMPLETE = "LOGIN_COMPLETE";
    public static String ID = "ID";
    public static String SECOND = "SECOND";
    public static String IS_FROM_SIGN_UP = "IS_FROM_SIGN_UP";
    public static String CHANGE_OR_CHOOSE = "CHANGE_OR_CHOOSE";
    public static String SPLASH_SCREEN = "SPLASH_SCREEN";
    public static String IS_EDIT = "IS_EDIT";
    public static String FRESH_START = "FRESH_START";
    public static String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static String FIREBASE_TOKEN = "ACCESS_TOKEN";
    public static String APP_INSTALLATION_ID = "APP_INSTALLATION_ID";
    public static String SALE_OR_RENT = "SALE_OR_RENT";
    public static String ADD_ACCOUNT = "ADD_ACCOUNT";
    public static String PLAY_SERVICES_ON_OR_OFF = "PLAY_SERVICES_ON_OR_OFF";
    public static String LOCATION_PERMISSION = "LOCATION_PERMISSION";
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int GALLERY_PICK_IMAGE_REQUEST_CODE = 101;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;


    //AMIN Numbers:
    public static final int EDIT_REQ_CODE = 100;
    public static final int NEW_REQ_CODE = 101;
    public static final int PRODUCT_ADD_TO_CART = 102;
    public static final int COMPLETE_ORDER = 103;
    public static final int LOGIN_SIGNUP = 104;
    public static final int GPS_SETTING_REQUEST_CODE = 9002;
    public static final int VOLLEY_TIME_OUT = 90000;
    public static final int ITEM_IN_PAGE_COUNT = 10;
    public static final int NOTIFICATION_ID = 900;
    public static final int NOTIFICATION_CHECK_PERIOD = 30 * 60 * 1000;
    public static boolean DEBUG = true;

    public static final String themeFont = "fonts/theme.ttf";
    public static final String themeFontBold = "fonts/theme_bold.ttf";
    public static final String themeFontLight = "fonts/theme_light.ttf";


    public static String formatPrice(String num) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(Long.valueOf(num));
    }

    public static String formatPrice(double num) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(num);
    }

    public static String formatPrice(Long num) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(num);
    }

    public static String formatPriceWithCurrency(Long num) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(num) + " " + AppController.app.getString(R.string.currency_name);
    }

    public static String formatPriceWithCurrency(double num) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(num) + " " + AppController.app.getString(R.string.currency_name);
    }

    public static String formatPriceWithCurrency(String num) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(Long.valueOf(num)) + " " + AppController.app.getString(R.string.currency_name);
    }
}
