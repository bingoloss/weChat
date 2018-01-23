package com.lehand.wechat.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lehand.tools.utils.NetWorkUtils;
import com.lehand.tools.widget.PowerfulEditText;
import com.lehand.tools.widget.toast.Toasting;
import com.lehand.wechat.Constants;
import com.lehand.wechat.R;
import com.lehand.wechat.base.BaseActivity;
import com.lehand.wechat.chat.ChatHelper;
import com.lehand.wechat.chat.GreenDaoManager;
import com.lehand.wechat.chat.model.User;
import com.lehand.wechat.utils.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_username)
    PowerfulEditText userNameEt;
    @BindView(R.id.et_pwd)
    PowerfulEditText userPwdEt;

    /*点击注册*/
    @OnClick(R.id.register_btn)
    void onRegisterClick() {
        String userName = userNameEt.getText().toString().trim();
        String userPwd = userPwdEt.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            userNameEt.startShakeAnimation();
            return;
        }
        if (TextUtils.isEmpty(userPwd)) {
            userPwdEt.startShakeAnimation();
            return;
        }
        goToRegister(userName, userPwd);
    }

    /**
     * 注册账号
     *
     * @param userName
     * @param userPwd
     */
    private void goToRegister(final String userName, final String userPwd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //注册失败会抛出HyphenateException
                try {
                    EMClient.getInstance().createAccount(userName, userPwd);//同步方法需要另开线程操作
                    //GreenDaoManager.getInstance().addUser(userName);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<User> allUser = GreenDaoManager.getInstance().findAllUser();
                            for (User user : allUser) {
                                LogUtils.e("" + user.getHxid());
                            }
                            Toasting.showAnimToast(RegisterActivity.this, "注册成功！");
                            Intent intent = new Intent();
                            intent.putExtra(Constants.USER_NAME, userName);
                            intent.putExtra(Constants.USER_PWD, userPwd);
                            setResult(101, intent);
                            finish();
                        }
                    });
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int errorCode = e.getErrorCode();
                            if (errorCode == EMError.NETWORK_ERROR) {
                                LogUtils.e("网络异常，请检查网络！");
                                Toasting.showAnimToast(mContext, "网络异常，请检查网络！");
                            } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                                LogUtils.e("用户已存在！！");
                                Toasting.showAnimToast(mContext, "用户已存在！！");
                            } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                                LogUtils.e("注册失败，无权限！");
                                Toasting.showAnimToast(mContext, "注册失败，无权限！");
                            } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                                LogUtils.e("用户名不合法！");
                                Toasting.showAnimToast(mContext, "用户名不合法！");
                            } else {
                                LogUtils.e("注册失败！errorCode = " + errorCode);
                                Toasting.showAnimToast(mContext, "注册失败");
                            }
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {

    }
}
