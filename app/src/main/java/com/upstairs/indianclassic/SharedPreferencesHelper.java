package com.upstairs.indianclassic;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by karm on 29/6/16.
 */
public class SharedPreferencesHelper {
    //Strings to store keys of Shared Preferences
    public static final String SHARED_PREFERENCES_KEY = "IndianGamesFlags";
    public static final String DB_READY_KEY = "db_ready";
    public static final boolean DEFAULT_BOOL = false;
    public static final String FIRST_TIME = "first_time";


    public static boolean checkFlag(String key, Context appContext){
        SharedPreferences Flag = appContext.getSharedPreferences(SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE);
        return Flag.getBoolean(key,DEFAULT_BOOL);
    }
    public static void setFlag(String key, Context appContext, boolean value){
        SharedPreferences Flag = appContext.getSharedPreferences(SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = Flag.edit();
        editor.putBoolean(key,value);
        editor.commit();

    }

}