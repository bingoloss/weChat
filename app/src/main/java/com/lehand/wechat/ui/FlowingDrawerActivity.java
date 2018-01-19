package com.lehand.wechat.ui;

import com.lehand.wechat.R;
import com.lehand.wechat.base.BaseActivity;
import com.lehand.wechat.utils.LogUtils;
import com.lehand.wechat.utils.StatusBarUtil;
import com.lehand.wechat.widget.flowingdrawer.ElasticDrawer;
import com.lehand.wechat.widget.flowingdrawer.FlowingDrawer;

import butterknife.BindView;

/**
 * 滑动FlowingDrawer
 */
public class FlowingDrawerActivity extends BaseActivity {

    @BindView(R.id.flowing_drawer)
    FlowingDrawer mFlowingDrawer;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_flowing_drawer;
    }

    @Override
    protected void initView() {
        mFlowingDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        mFlowingDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    LogUtils.e("Drawer STATE_CLOSED");
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                LogUtils.e("openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mFlowingDrawer.isMenuVisible()) {
            mFlowingDrawer.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(FlowingDrawerActivity.this, null);
    }
}
