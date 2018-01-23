package com.lehand.wechat.fragment;

import android.widget.Button;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lehand.tools.utils.AppManager;
import com.lehand.tools.utils.LoadingUtil;
import com.lehand.tools.widget.toast.Toasting;
import com.lehand.wechat.R;
import com.lehand.wechat.base.BaseFragment;
import com.lehand.wechat.chat.ChatHelper;
import com.lehand.wechat.ui.LoginActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by bingo on 2018/1/18.
 * 设置页面
 */

public class SettingFragment extends BaseFragment {
    @BindView(R.id.setting_quit_btn)
    Button quitBtn;

    /**
     * 退出按钮的操作
     */
    @OnClick(R.id.setting_quit_btn)
    void quitClick() {
        LoadingUtil.show(getActivity(),"");
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                LoadingUtil.dismiss();
                Toasting.showAnimToast(getActivity(),"退出成功");
                AppManager.getInstance().keepLastAtivity();
                moveTo(LoginActivity.class, true);
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
                LoadingUtil.dismiss();
                Toasting.showAnimToast(getActivity(),message);
            }
        });
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
