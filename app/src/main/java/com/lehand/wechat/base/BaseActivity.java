package com.lehand.wechat.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.lehand.tools.utils.AppManager;
import com.lehand.wechat.R;
import com.lehand.wechat.utils.CheckPermissionsUtil;
import com.lehand.wechat.utils.StatusBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by bingo on 2018/1/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    protected Unbinder mUnbinder;
    private InputMethodManager manager;

    protected abstract int initLayoutId();

    protected void initView() {
    }

    protected void initData() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        super.onCreate(savedInstanceState);
        setContentView(initLayoutId());
        mContext = this;
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initData();
        initView();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mUnbinder = ButterKnife.bind(this);
        setStatusBar();
    }

    /**
     * 跳转到指定的Activity
     *
     * @param clazz
     * @param isFinish 是否销毁当前页面
     */
    protected void moveTo(Class<?> clazz, boolean isFinish) {
        startActivity(new Intent(this, clazz));
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        if (isFinish) {
            this.finish();
        }
    }

    /**
     * 设置状态栏颜色
     */
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    /**
     * 隐藏软键盘
     */
    protected void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 点击空白区域收起/隐藏键盘.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (this.getCurrentFocus() != null) {
                if (this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }
    //==============================↓权限处理 start ↓==========================================//
    /**
     * 权限工具
     */
    protected CheckPermissionsUtil permissionsUtil;

    /**
     * 请求授权
     *
     * @param permissions
     */
    protected void requestPermission(String[] permissions) {
        permissionsUtil = CheckPermissionsUtil.getInstance();
        permissionsUtil.requestPermission(this, permissions);
    }

    /**
     * 手动授权的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CheckPermissionsUtil.REQUEST_CODE) {
            if (!permissionsUtil.verifyPermissions(grantResults)) {
                permissionsUtil.showFaiingDialog();
            }
        }
    }

    /**
     * 防止用户点击设置，并未给定权限时的返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CheckPermissionsUtil.REQUEST_CODE_FROM_SETTING) {
            if (permissionsUtil.getDeniedList() != null) {
                permissionsUtil.requestPermission(this, permissionsUtil.getDeniedList());
            }
        }
    }

    //==================================↑ 权限处理 end ↑=========================================//

    @Override
    protected void onStart() {
        super.onStart();
        AppManager.getInstance().addActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideKeyboard();
        // 极端情况下，系统会杀死APP进程，并不执行onDestroy()，
        // 因此需要使用onStop()来释放资源，从而避免内存泄漏。
        AppManager.getInstance().removeActivity(this);
    }
    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }
}
