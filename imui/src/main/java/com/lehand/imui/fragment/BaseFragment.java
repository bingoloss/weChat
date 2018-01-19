package com.lehand.imui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author: bingo
 * Time:  2017-12-20
 */
public abstract class BaseFragment extends Fragment {
    protected static final String TYPE = "type";//唯一标识
    protected Activity mActivity;
    protected View mRootView;

    protected abstract int initLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initLayoutId();
        mRootView = inflater.inflate(initLayoutId(), container, false);
        initView();
        initData();
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

    public ProgressDialog progressDialog;

    public ProgressDialog showProgressDialog() {
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("加载中");
        progressDialog.show();
        return progressDialog;
    }

    public ProgressDialog showProgressDialog(CharSequence message) {
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            // progressDialog.hide();会导致android.view.WindowLeaked
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
