package com.lehand.wechat.ui;

import android.view.View;

import com.lehand.wechat.R;
import com.lehand.wechat.base.BaseActivity;

import butterknife.OnClick;

/**
 * 功能列表的界面
 */
public class FunctionListActivity extends BaseActivity {

    @OnClick({R.id.custom_with_bottom_navigation, R.id.flowing_drawer})
    void btnClick(View view) {
        switch (view.getId()) {
            /*通用底部导航栏类*/
            case R.id.custom_with_bottom_navigation:
                moveTo(CommonActivity.class, false);
                break;
            case R.id.flowing_drawer:
                moveTo(FlowingDrawerActivity.class, false);
                break;
            default:
                break;
        }
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_function_list;
    }

    @Override
    protected void initView() {

    }
}
