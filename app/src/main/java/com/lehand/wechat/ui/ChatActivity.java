package com.lehand.wechat.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.lehand.imui.adapter.CommonFragmentPagerAdapter;
import com.lehand.imui.fragment.ChatEmotionFragment;
import com.lehand.imui.fragment.ChatFunctionFragment;
import com.lehand.imui.util.GlobalOnItemClickManagerUtils;
import com.lehand.imui.util.KeyboardWatcher;
import com.lehand.imui.widget.EmotionInputDetector;
import com.lehand.imui.widget.NoScrollViewPager;
import com.lehand.imui.widget.StateButton;
import com.lehand.tools.overscroll.IOverScrollDecor;
import com.lehand.tools.overscroll.IOverScrollUpdateListener;
import com.lehand.tools.overscroll.OverScrollDecoratorHelper;
import com.lehand.tools.overscroll.OverScrollViewUtils;
import com.lehand.tools.overscroll.VerticalOverScrollBounceEffectDecorator;
import com.lehand.tools.overscroll.adapters.RecyclerViewOverScrollDecorAdapter;
import com.lehand.wechat.R;
import com.lehand.wechat.base.BaseActivity;
import com.lehand.wechat.chat.ChatHelper;
import com.lehand.wechat.utils.LogUtils;

import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 聊天页面
 */
public class ChatActivity extends BaseActivity {

    @BindView(R.id.chat_list_view)
    RecyclerView chatList;
    @BindView(R.id.emotion_voice)
    ImageView emotionVoice;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.voice_text)
    TextView voiceText;
    @BindView(R.id.emotion_button)
    ImageView emotionButton;
    @BindView(R.id.emotion_add)
    ImageView emotionAdd;
    @BindView(R.id.emotion_send)
    StateButton emotionSend;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;
    @BindView(R.id.emotion_layout)
    RelativeLayout emotionLayout;

    @OnClick(R.id.huanxin)
    void hunaxinClick() {
        Toast.makeText(mContext, "hhaha", Toast.LENGTH_SHORT).show();
        if (ChatHelper.getInstance().isLogin()) {
            EMClient.getInstance().updateCurrentUserNick("bingo");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChatHelper.getInstance().register("xuebing", "123456");
            }
        }).start();

    }

    private KeyboardWatcher keyboardWatcher;//键盘监听器
    private EmotionInputDetector mDetector;
    private ArrayList<Fragment> fragments;
    private ChatEmotionFragment chatEmotionFragment;//表情fragment面板
    private ChatFunctionFragment chatFunctionFragment;//多功能fragment面板
    private CommonFragmentPagerAdapter adapter;//表情和多功能面板的viewPager 适配器
    private SlimAdapter chatAdapter;//聊天list适配器
    private LinearLayoutManager layoutManager;//聊天recyclerView 线性布局管理器
    private List<String> messageList = new ArrayList<>();
    //录音相关
    int animationRes = 0;
    int res = 0;
    AnimationDrawable animationDrawable = null;
    private ImageView animView;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        initKeyBoardWatcher();
        fragments = new ArrayList<>();
        chatEmotionFragment = new ChatEmotionFragment();
        fragments.add(chatEmotionFragment);
        chatFunctionFragment = new ChatFunctionFragment();
        fragments.add(chatFunctionFragment);
        adapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);

        mDetector = EmotionInputDetector.with(this)
                .setEmotionView(emotionLayout)
                .setViewPager(viewpager)
                .bindToContent(chatList)
                .bindToEditText(editText)
                .bindToEmotionButton(emotionButton)
                .bindToAddButton(emotionAdd)
                .bindToSendButton(emotionSend)
                .bindToVoiceButton(emotionVoice)
                .bindToVoiceText(voiceText)
                .build();

        GlobalOnItemClickManagerUtils globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance(this);
        globalOnItemClickListener.attachToEditText(editText);


        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatList.setLayoutManager(layoutManager);
        chatAdapter = SlimAdapter.create()
                .register(R.layout.item_chat_accept, new SlimInjector<String>() {

                    @Override
                    public void onInject(String data, IViewInjector injector) {
                        injector.text(R.id.chat_item_content_text, data);
                        injector.with(R.id.chat_item_header, new IViewInjector.Action<ImageView>() {

                            @Override
                            public void action(ImageView view) {
                                view.setImageResource(R.mipmap.avatar_boy);
                            }
                        });
                    }
                }).attachTo(chatList);
        chatAdapter.updateData(getMessageList());
        //设置弹性回弹
        OverScrollViewUtils.setUpRecyclerView(chatList);
    }

    public List<String> getMessageList() {
        for (int i = 0; i < 20; i++) {
            messageList.add("逗比：" + i);
        }
        return messageList;
    }

    /**
     * 键盘张开和收回的监听
     */
    private void initKeyBoardWatcher() {
        keyboardWatcher = new KeyboardWatcher(findViewById(Window.ID_ANDROID_CONTENT));
        keyboardWatcher.addSoftKeyboardStateListener(new KeyboardWatcher.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                editText.setCursorVisible(true);// 再次点击显示光标
                chatList.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onSoftKeyboardClosed() {
                editText.setCursorVisible(false);// 将编辑框的光标隐藏，提升用户的体验度
            }
        });
    }
}
