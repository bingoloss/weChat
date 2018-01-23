package com.lehand.wechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lehand.tools.utils.LoadingUtil;
import com.lehand.tools.widget.PowerfulEditText;
import com.lehand.tools.widget.toast.Toasting;
import com.lehand.wechat.Constants;
import com.lehand.wechat.R;
import com.lehand.wechat.base.BaseActivity;
import com.lehand.wechat.chat.ChatHelper;
import com.lehand.wechat.utils.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_username)
    PowerfulEditText usernameEt;
    @BindView(R.id.et_pwd)
    PowerfulEditText passwordEt;

    /*登录按钮操作*/
    @OnClick(R.id.btn_login)
    void login() {
        String name = usernameEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            usernameEt.startShakeAnimation();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEt.startShakeAnimation();
            return;
        }
        goToLogin(name, password);
        //moveTo(MainActivity.class, true);
    }

    @OnClick(R.id.btn_register)
    void register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 101) {
            if (data != null) {
                String name = data.getStringExtra(Constants.USER_NAME);
                String pwd = data.getStringExtra(Constants.USER_PWD);
                usernameEt.setText(name);
                passwordEt.setText(pwd);
            }
        }
    }

    /**
     * 登录环信账号
     *
     * @param name
     * @param password
     */
    private void goToLogin(String name, String password) {
        LoadingUtil.show(this,"");
        EMClient.getInstance().login(name, password, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                LoadingUtil.dismiss();
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                LogUtils.e("登录聊天服务器成功！");
                moveTo(MainActivity.class,true);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //UI操作要放到主线程中
                        Toasting.showAnimToast(LoginActivity.this,"登录成功");
                    }
                });

            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                LoadingUtil.dismiss();
                LogUtils.e("登录聊天服务器失败！ code = " + code);
            }
        });
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ChatHelper.getInstance().isLoggedIn()) {
            moveTo(MainActivity.class,true);
            return;
        }
        setContentView(initLayoutId());
        usernameEt.setText(ChatHelper.getInstance().getCurrentLoginUser());
        LogUtils.e("onCreate loginUser = " +ChatHelper.getInstance().getCurrentLoginUser());
    }
    @Override
    protected void initView() {

    }
}
