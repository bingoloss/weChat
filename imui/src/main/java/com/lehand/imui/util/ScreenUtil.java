package com.lehand.imui.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by bingo on 2018/1/19.
 */

public class ScreenUtil {
    /**
     * 获取屏幕密度
     *
     * @param context
     * @return
     */
    public static float getScreenDensity(Context context) {
        DisplayMetrics curMetrics = context.getResources().getDisplayMetrics();
        return curMetrics.density;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics curMetrics = context.getResources().getDisplayMetrics();
        return curMetrics.heightPixels;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics curMetrics = context.getResources().getDisplayMetrics();
        return curMetrics.widthPixels;
    }

}
