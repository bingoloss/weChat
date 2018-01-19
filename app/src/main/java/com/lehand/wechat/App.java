package com.lehand.wechat;

import android.app.Application;

import com.lehand.wechat.chat.ChatHelper;
import com.lehand.wechat.utils.ExceptionCrashHandler;

/**
 * Created by bingo on 2018/1/19.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ExceptionCrashHandler.getInstance().init(this);
        ChatHelper.getInstance().init(this);
    }
}
