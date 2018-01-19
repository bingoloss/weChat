package com.lehand.wechat.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.lehand.wechat.R;
import com.lehand.wechat.base.BaseActivity;
import com.lehand.wechat.fragment.CommonFragment;
import com.lehand.wechat.utils.StatusBarUtil;

import java.util.ArrayList;

import butterknife.BindView;
//import com.r0adkll.slidr.Slidr;

/**
 * 通用的带底部导航
 */
public class CommonActivity extends BaseActivity {
    @BindView(R.id.common_center_vp)
    ViewPager mViewPager;
    @BindView(R.id.common_bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;

    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected int initLayoutId() {
        return R.layout.activity_common;
    }

    @Override
    protected void initView() {
        // 设置右滑动返回
        //Slidr.attach(this);
        initViewPager();
        initBottomNavigationBar();
    }

    private void initViewPager() {
        mFragmentList.add(new CommonFragment());
        mFragmentList.add(new CommonFragment());
        mFragmentList.add(new CommonFragment());
        mFragmentList.add(new CommonFragment());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavigationBar.selectTab(position);
                /*switch (position) {
                    case 0:
                        break;
                    default:
                        Random random = new Random();
                        int color = 0xff000000 | random.nextInt(0xffffff);
                        if (mFragmentList.get(position) instanceof BaseFragment) {
                            ((SimpleFragment) mFragmentList.get(position)).setTvTitleBackgroundColor(color);
                        }
                        break;
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });
    }

    /**
     * 初始化底部导航
     */
    private void initBottomNavigationBar() {
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_favorite, "One"))
                .addItem(new BottomNavigationItem(R.drawable.ic_gavel, "Two"))
                .addItem(new BottomNavigationItem(R.drawable.ic_grade, "Three"))
                .addItem(new BottomNavigationItem(R.drawable.ic_group_work, "Four"))
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mViewPager.setCurrentItem(position);
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
        StatusBarUtil.setTranslucentForImageViewInFragment(this, null);
    }
}
