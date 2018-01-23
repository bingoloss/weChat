package com.lehand.tools.widget.toast;

import android.content.Context;
import android.view.Gravity;

/**
 * Created by bingo on 2018/1/11.
 * 自定义吐司工具
 * 方向可自定义（可扩展）
 */

public class Toasting {

    public static TPrompt tPrompt;

    public static void showAnimToast(Context mContext, String msg) {
        if (!isFastDoubleClick()) {
            if (tPrompt == null) {
                tPrompt = new TPrompt(mContext);
            }
            tPrompt.showToast(msg);
        }
    }

    /**
     * 底部弹出
     * @param mContext
     * @param msg
     */
    public static void showBottomAnimToast(Context mContext, String msg) {
        if (!isFastDoubleClick()) {
            if (tPrompt == null) {
                tPrompt = new TPrompt(mContext, Gravity.BOTTOM);
            }
            tPrompt.showToast(msg);
        }
    }

    protected static long lastClickTime;

    protected static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
