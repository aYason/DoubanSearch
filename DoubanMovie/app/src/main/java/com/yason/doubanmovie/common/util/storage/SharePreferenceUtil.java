package com.yason.doubanmovie.common.util.storage;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.yason.doubanmovie.common.config.Config;
import java.util.Set;


public final class SharePreferenceUtil {

    /**
     * Activity.getPreferences(int mode)生成 Activity名.xml 用于Activity内部存储
     * PreferenceManager.getDefaultSharedPreferences(Context)生成 包名_preferences.xml
     * Context.getSharedPreferences(String name,int mode)生成name.xml
     */
    private static final SharedPreferences PREFERENCES =
            PreferenceManager.getDefaultSharedPreferences(Config.getApplicationContext());

    private static SharedPreferences getAppPreference() {
        return PREFERENCES;
    }

    public static void clearAppPreferences() {
        getAppPreference()
                .edit()
                .clear()
                .apply();
    }

    public static void saveAppBoolean(String key, boolean values) {
        getAppPreference()
                .edit()
                .putBoolean(key, values)
                .apply();
    }

    public static boolean getAppBoolean(String key) {
        return getAppPreference()
                .getBoolean(key, false);
    }

    public static void saveAppStringSet(String key, Set<String> values) {
        getAppPreference()
            .edit()
            .putStringSet(key, values)
            .apply();
    }

    public static Set<String> getAppStringSet(String key) {
        return getAppPreference()
            .getStringSet(key, null);
    }

    public static void saveAppInt(String key, int val) {
        getAppPreference()
            .edit()
            .putInt(key, val)
            .apply();
    }

    public static int getAppInt(String key) {
        return getAppPreference()
            .getInt(key, 0);
    }

    public static void saveAppString(String key, String values) {
        getAppPreference()
                .edit()
                .putString(key, values)
                .apply();
    }

    public static String getAppString(String key) {
        return getAppPreference().getString(key, "");
    }

}
