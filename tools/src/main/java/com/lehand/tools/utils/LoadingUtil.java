package com.lehand.tools.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import com.lehand.tools.R;
import com.lehand.tools.widget.kprogresshud.KProgressHUD;

/**
 * Created by bingo on 2017/12/25.
 */

public class LoadingUtil {

    private static KProgressHUD kProgressHUD;

    /**
     * 显示loading
     */
    public static void show(Context context, String lable) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(context);
        }
        if (kProgressHUD.isShowing()) {
            return;
        }
        show(context, lable, "");
    }

    public static void show(Context context, String lable, String detail) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(context);
        }
        if (kProgressHUD.isShowing()) {
            return;
        }
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        if (!TextUtils.isEmpty(lable)) {
            kProgressHUD.setLabel(lable);//标题类
        }
        if (!TextUtils.isEmpty(detail)) {
            kProgressHUD.setDetailsLabel(detail);//详情
        }
        kProgressHUD.show();
    }

    public static void showImage(Context context, int imageRes) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(imageRes);
        kProgressHUD.setCustomView(imageView)
                .setLabel("This is a custom view")
                .show();
    }

    public static void dismiss() {
        if (kProgressHUD == null) {
            LogUtils.e("kProgressHUD = null");
            return;
        }
        if (kProgressHUD.isShowing()) {
            kProgressHUD.dismiss();
        }
        kProgressHUD = null;
    }

    /**
     * 自定义方法
     *
     * @param context
     * @param resId   资源id
     * @param label
     */
    public static void showCustom(Context context, int resId, String label) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(context);
        }
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(resId);//静态页面
        kProgressHUD.setCustomView(imageView);
        if (!TextUtils.isEmpty(label)) {
            kProgressHUD.setLabel(label);
        }
        kProgressHUD.show();
    }

    /**
     * 判断是否正在显示
     *
     * @return
     */
    public static boolean isShow() {
        if (kProgressHUD != null) {
            if (kProgressHUD.isShowing()) {
                return true;
            }
        }
        return false;
    }
}
