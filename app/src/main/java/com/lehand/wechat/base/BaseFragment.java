package com.lehand.wechat.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lehand.wechat.utils.StatusBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: bingo
 * Time:  2017-12-20
 */
public abstract class BaseFragment extends Fragment {
    protected static final String TYPE = "type";//唯一标识
    protected BaseActivity mActivity;
    protected View mRootView;
    protected Unbinder mUnbinder;

    protected abstract int initLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initLayoutId();
        mRootView = inflater.inflate(initLayoutId(), container, false);
        mUnbinder = ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }

    /**
     * 跳转到指定的Activity
     *
     * @param clazz
     * @param isFinish 是否销毁当前页面
     */
    protected void moveTo(Class<?> clazz, boolean isFinish) {
        startActivity(new Intent(getActivity(), clazz));
        if (isFinish) {
            this.getActivity().finish();
        }
    }

    /**
     * Toast
     *
     * @param msg
     */
    protected void showing(String msg) {
        //Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        //Toasting.showBottomAnimToast(getActivity(), msg);
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }
}
