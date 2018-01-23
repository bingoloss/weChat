package com.lehand.wechat.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import com.lehand.tools.view.QQBezierView;
import com.lehand.tools.widget.swipe.SwipeMenuLayout;
import com.lehand.tools.widget.swipe.SwipeRecycleView;
import com.lehand.tools.widget.toast.Toasting;
import com.lehand.wechat.R;
import com.lehand.wechat.base.BaseFragment;
import com.lehand.wechat.chat.ChatHelper;
import com.lehand.wechat.ui.ChatActivity;
import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * Created by bingo on 2018/1/18.
 * 会话列表
 */

public class ChatListFragment extends BaseFragment {
    String[] users = new String[]{"xuebing", "xiaoqiang", "xurui"};
    private List<String> list;
    @BindView(R.id.swipe_recycleview)
    SwipeRecycleView mSwipeRecycleView;

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_chat_list;
    }

    @Override
    protected void initView() {
        list = new ArrayList<>();
        list.add(users[0]);
        list.add(users[1]);
        list.add(users[2]);
        mSwipeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SlimAdapter adapter = SlimAdapter.create().register(R.layout.swipe_menu_item, new SlimInjector<String>() {

            @Override
            public void onInject(final String data, IViewInjector injector) {
                QQBezierView qqBezierView = (QQBezierView) injector.findViewById(R.id.qq_point);
                qqBezierView.setText("33");
                qqBezierView.setOnDragListener(new QQBezierView.onDragStatusListener() {
                    @Override
                    public void onDrag() {
                    }

                    @Override
                    public void onMove() {

                    }

                    @Override
                    public void onRestore() {

                    }

                    @Override
                    public void onDismiss() {
                        //bean.redNum = 0;
                    }
                });
                injector.text(R.id.tv_content, data);
                SwipeMenuLayout menuLayout = (SwipeMenuLayout) injector.findViewById(com.lehand.tools.R.id.swipe_menu);
                menuLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ChatHelper.getInstance().getCurrentLoginUser().equals(data)){
                            Toasting.showAnimToast(getActivity(), "不支持自己聊天");
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("toChat",data);
                            moveTo(ChatActivity.class,false,bundle);
                        }
                    }
                });
            }
        }).attachTo(mSwipeRecycleView);
        adapter.updateData(list);
    }

    @Override
    protected void initData() {

    }
}
