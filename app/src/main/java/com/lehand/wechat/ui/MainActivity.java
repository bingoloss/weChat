package com.lehand.wechat.ui;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.lehand.wechat.R;
import com.lehand.wechat.base.BaseActivity;
import com.lehand.wechat.chat.ChatHelper;
import com.lehand.wechat.fragment.ChatListFragment;
import com.lehand.wechat.fragment.CloudFragment;
import com.lehand.wechat.fragment.SettingFragment;
import com.lehand.wechat.utils.LogUtils;
import com.lehand.wechat.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    /*外层抽屉*/
    @BindView(R.id.main_drawer_layout)
    DrawerLayout mDrawerLayout;
    /*侧边栏*/
    @BindView(R.id.navigation)
    NavigationView leftNavigationView;
    /*底部导航*/
    @BindView(R.id.main_bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_title)
    TextView mTitleTv;
    //fragment集合
    private List<Fragment> fragmentList = new ArrayList<>();
    private Fragment chatListFragment;
    private Fragment cloudFragment;
    private Fragment settingFragment;
    //标题的集合
    private String[] mTitle = new String[]{"会话", "云平台", "设置"};
    //当前tab显示的位置
    private int currentTabIndex;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mTitleTv.setText(mTitle[currentTabIndex]);
        iniLeftNavigation();
        initBottomNavigationBar();
        //requestPermission(new String[]{Manifest.permission.CAMERA});
        LogUtils.e("当前登录账号："+ChatHelper.getInstance().getCurrentLoginUser());
    }

    @Override
    protected void initData() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (chatListFragment == null) {
            chatListFragment = new ChatListFragment();
        }
        if (cloudFragment == null) {
            cloudFragment = new CloudFragment();
        }
        if (settingFragment == null) {
            settingFragment = new SettingFragment();
        }
        fragmentList.add(chatListFragment);
        fragmentList.add(cloudFragment);
        fragmentList.add(settingFragment);
        fragmentTransaction
                .add(R.id.main_content, chatListFragment, mTitle[0])
                .add(R.id.main_content, cloudFragment, mTitle[1])
                .add(R.id.main_content, settingFragment, mTitle[2])
                .hide(cloudFragment)
                .hide(settingFragment)
                .show(chatListFragment)
                .commit();
    }


    /**
     * 初始侧边栏
     */
    private void iniLeftNavigation() {
        leftNavigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * 初始化底部导航
     */
    private void initBottomNavigationBar() {
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_favorite, "会话"))
                .addItem(new BottomNavigationItem(R.drawable.ic_gavel, "云平台"))
                .addItem(new BottomNavigationItem(R.drawable.ic_grade, "设置"))
                //.addItem(new BottomNavigationItem(R.drawable.ic_group_work, "Four"))
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                if (currentTabIndex != position) {
                    FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
                    trx.hide(fragmentList.get(currentTabIndex));
                    if (!fragmentList.get(position).isAdded()) {
                        trx.add(R.id.main_content, fragmentList.get(position));
                    }
                    trx.show(fragmentList.get(position)).commit();
                }
                //设置标题
                mTitleTv.setText(mTitle[position]);
                // set current tab selected
                currentTabIndex = position;
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorForDrawerLayout(this, mDrawerLayout, getResources().getColor(R.color.colorPrimary), StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 侧边栏的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_camera:
                //功能列表
                moveTo(FunctionListActivity.class, false);
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_manage:
                break;
            default:
                break;
        }
        item.setChecked(true);
        mDrawerLayout.closeDrawers();
        return true;
    }
}
