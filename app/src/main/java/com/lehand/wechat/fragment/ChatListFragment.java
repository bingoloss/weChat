package com.lehand.wechat.fragment;

import com.lehand.wechat.R;
import com.lehand.wechat.base.BaseFragment;
import com.lehand.wechat.ui.ChatActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by bingo on 2018/1/18.
 * 会话列表
 */

public class ChatListFragment extends BaseFragment {

    @OnClick(R.id.btn)
    void click() {
        moveTo(ChatActivity.class, false);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_chat_list;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
