package com.lehand.wechat.chat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMChatRoomManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMContactManager;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.lehand.wechat.ui.MainActivity;
import com.lehand.wechat.utils.LogUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by bingo on 2018/1/19.
 * 聊天工具类
 */

public class ChatHelper {
    /*工具类单例模式*/
    private static ChatHelper helper;

    private ChatHelper() {
    }

    public static ChatHelper getInstance() {
        if (helper == null) {
            synchronized (ChatHelper.class) {
                if (helper == null) {
                    helper = new ChatHelper();
                }
            }
        }
        return helper;
    }

    public Context mContext;
    public EMChatManager mChatManager;
    public EMContactManager mContactManager;
    public EMChatRoomManager mChatRoomManager;
    public EMGroupManager mGroupManager;

    public void init(Context context) {
        mContext = context;
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        //options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        //options.setAutoDownloadThumbnail(true);
        //设置自动登录
        options.setAutoLogin(false);
        //初始化
        EMClient.getInstance().init(context, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        mChatManager = EMClient.getInstance().chatManager();
        mContactManager = EMClient.getInstance().contactManager();
        mChatRoomManager = EMClient.getInstance().chatroomManager();
        mGroupManager = EMClient.getInstance().groupManager();
    }

    private void initEMChat() {
        // IntentFilter callFilter = new
        // IntentFilter(EMChatManager.getInstance()
        // .getIncomingCallBroadcastAction());
        // registerReceiver(new CallReceiver(), callFilter);
    }

    /**
     * 判断是否登录过
     *
     * @return
     */
    public boolean isLogin() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    /**
     * 注册接口
     *
     * @param userName
     * @param password
     */
    public void register(String userName, String password) {
        //注册失败会抛出HyphenateException
        try {
            EMClient.getInstance().createAccount(userName, password);//同步方法
        } catch (HyphenateException e) {
            int errorCode = e.getErrorCode();
            if (errorCode == EMError.NETWORK_ERROR) {
                LogUtils.e("网络异常，请检查网络！");
            } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                LogUtils.e("用户已存在！！");
            } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                LogUtils.e("注册失败，无权限！");
            } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                LogUtils.e("用户名不合法！");
            } else {
                LogUtils.e("注册失败！errorCode = " + errorCode);
            }
        }
    }

    /**
     * 登录接口
     *
     * @param userName
     * @param password
     */
    public void login(String userName, String password) {
        EMClient.getInstance().login(userName, password, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                LogUtils.e("登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                LogUtils.e("登录聊天服务器失败！");
            }
        });
    }

    /**
     * 退出登录，异步方法;同步方法直接调用:EMClient.getInstance().logout(true);
     */
    public void logout() {
        //如果集成了第三方的推送功能，那么方法中的第一个参数需要设置为true，目的是解绑设备token，否则的话可能出现退出之后依然能收到推送的情况。
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub

            }
        });
    }
}
