package com.prasadam.kmrplayer.sharedClasses;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;

import com.prasadam.kmrplayer.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.DecimalFormat;

/*
 * Created by Prasadam Saiteja on 3/20/2016.
 */
public class ExtensionMethods {

    public static void testPing() {
        Log.d("Test", "Works");
    }

    public static void logwritter(String data) {

        Log.d("Log  :  ", data);
    }

    public static String formatIntoHHMMSS(int secsIn) {

        int hours = secsIn / 3600;
        int minutes = (secsIn % 3600) / 60;
        int seconds = (secsIn % 3600) % 60;

        if(hours == 0)
        return (minutes < 10 ? "0" : "") + minutes
                + ":" + (seconds< 10 ? "0" : "") + seconds;

        return ( (hours < 10 ? "0" : "") + hours
                + ":" + (minutes < 10 ? "0" : "") + minutes
                + ":" + (seconds< 10 ? "0" : "") + seconds );

    }

    public static String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public static boolean isLandScape(Context context){
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    public static boolean stringIsEmptyorNull(String str){
        return (str == null || str.isEmpty());
    }

    public static void setStatusBarTranslucent(Activity activity) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){

            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintColor(activity.getResources().getColor(R.color.colorPrimaryDark));
            tintManager.setStatusBarTintEnabled(true);
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


}