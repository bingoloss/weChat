package com.lehand.wechat;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import com.lehand.wechat.chat.ChatHelper;
import com.lehand.wechat.utils.ExceptionCrashHandler;
import com.lehand.wechat.utils.LogUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by bingo on 2018/1/19.
 */

public class App extends Application {
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        ExceptionCrashHandler.getInstance().init(this);

        mContext = this;
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null || !processAppName.equalsIgnoreCase(mContext.getPackageName())) {
            LogUtils.e("enter the service process!");
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        //环信SDK初始化
        ChatHelper.getInstance().init(this);
    }

    /**
     * 获取AppName
     *
     * @param pID
     * @return
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();

        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                LogUtils.e("Error>> :" + e.getMessage());
            }
        }
        return processName;
    }
}
