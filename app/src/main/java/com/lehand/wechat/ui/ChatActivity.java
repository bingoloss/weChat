package com.lehand.wechat.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.library.BaseMultiItemAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.entity.MultiItemEntity;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
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
import com.lehand.wechat.chat.model.Message;
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
public class ChatActivity extends BaseActivity implements EmotionInputDetector.SendContentListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_title)
    TextView mTitleTv;

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

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.chat_layout)
    FrameLayout chatFrameLayout;
    private BaseMultiItemAdapter chatAdapter;

    private KeyboardWatcher keyboardWatcher;//键盘监听器
    private EmotionInputDetector mDetector;
    private ArrayList<Fragment> fragments;
    private ChatEmotionFragment chatEmotionFragment;//表情fragment面板
    private ChatFunctionFragment chatFunctionFragment;//多功能fragment面板
    private CommonFragmentPagerAdapter adapter;//表情和多功能面板的viewPager 适配器
    //private SlimAdapter chatAdapter;//聊天list适配器
    private LinearLayoutManager layoutManager;//聊天recyclerView 线性布局管理器
    //private List<String> messageList = new ArrayList<>();
    //录音相关
    int animationRes = 0;
    int res = 0;
    AnimationDrawable animationDrawable = null;
    private ImageView animView;

    private List<Message> messageList = new ArrayList<>();//聊天数据
    private String toChatUser;
    @Override
    protected int initLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        if (getIntent()!=null){
            toChatUser = getIntent().getBundleExtra("data").getString("toChat");
            mTitleTv.setText(toChatUser);
            LogUtils.e("---------->"+toChatUser);
        }

        initKeyBoardWatcher();
        iniRefreshLayout();
        initChatListener();
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

        mDetector.setListener(this);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatList.setLayoutManager(layoutManager);
        chatList.setAdapter(chatAdapter = new BaseMultiItemAdapter<Message>(ChatActivity.this, messageList) {

            @Override
            protected void convert(BaseViewHolder helper, Message item) {
                switch (helper.getItemViewType()) {
                    case Message.FROM_TEXT:
                        helper.setImageResource(R.id.chat_item_header, R.mipmap.avatar_boy);
                        helper.setText(R.id.chat_item_content_text, item.getBody());
                        break;
                    case Message.SEND_TEXT:
                        helper.setImageResource(R.id.chat_item_header, R.mipmap.avatar_girl);
                        helper.setText(R.id.chat_item_content_text, item.getBody());
                        break;
                    default:
                        break;
                }
            }

            @Override
            protected void addItemLayout() {
                addItemType(Message.FROM_TEXT, R.layout.item_chat_accept);
                addItemType(Message.SEND_TEXT, R.layout.item_chat_send);
            }
        });
        chatAdapter.openLoadAnimation(true);
        /*chatAdapter = SlimAdapter.create()
                .register(R.layout.item_chat_accept, new SlimInjector<EMMessage>() {

                    @Override
                    public void onInject(EMMessage data, IViewInjector injector) {
                        if (EMClient.getInstance().getCurrentUser() != data.getFrom()) {
                            injector.text(R.id.chat_item_content_text, data.getBody().toString());
                            injector.with(R.id.chat_item_header, new IViewInjector.Action<ImageView>() {

                                @Override
                                public void action(ImageView view) {
                                    view.setImageResource(R.mipmap.avatar_boy);
                                }
                            });
                        }
                    }
                }).attachTo(chatList);*/
        //chatAdapter.updateData(getMessageList());
        //设置弹性回弹
        //OverScrollViewUtils.setUpView(chatFrameLayout,OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
    }

    /**
     * 初始化消息监听
     */
    private void initChatListener() {
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    LogUtils.e(getMessageLog(message));
                    final Message msg = new Message();
                    msg.setFrom(message.getFrom());
                    msg.setTo(message.getTo());
                    msg.setBody(message.getBody().toString());
                    if (EMClient.getInstance().getCurrentUser() != message.getFrom()) {
                        msg.itemType = Message.FROM_TEXT;
                    } else {
                        msg.itemType = Message.SEND_TEXT;
                    }
                    // 主线程中刷新
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chatAdapter.add(msg);
                            if (chatAdapter.getData().size()>1){
                                chatList.scrollToPosition(chatAdapter.getData().size() - 1);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {

            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> messages) {

            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
        });
    }

    /**
     * 初始化刷新控件
     */
    private void iniRefreshLayout() {
        //上面的方法已经废弃
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE,
                Color.GREEN,
                Color.YELLOW,
                Color.RED);


        // 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeRefreshLayout.setDistanceToTriggerSync(300);
        // 设定下拉圆圈的背景
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        // 设置圆圈的大小
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);

        //设置下拉刷新的监听
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //显示或隐藏刷新进度条
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
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
                if (chatAdapter.getData().size()>1){
                    chatList.scrollToPosition(chatAdapter.getData().size() - 1);
                }
            }

            @Override
            public void onSoftKeyboardClosed() {
                editText.setCursorVisible(false);// 将编辑框的光标隐藏，提升用户的体验度
            }
        });
    }

    /**
     * 获取消息的所有信息
     *
     * @param message
     * @return
     */
    public String getMessageLog(EMMessage message) {
        return "EMMessage [" + "\n" +
                "getMsgId=" + message.getMsgId() + "\n" +
                "type=" + message.getType() + "\n" +
                "chatType=" + message.getChatType() + "\n" +
                "getFrom =" + message.getFrom() + "\n" +
                "getTo=" + message.getTo() + "\n" +
                "getUserName=" + message.getUserName() + "\n" +
                "getBody =" + message.getBody() + "\n" +
                "direct=" + message.direct() + "\n" +
                "describeContents=" + message.describeContents() + "\n" +
                "getMsgTime=" + message.getMsgTime() + "\n" +
                "isAcked=" + message.isAcked() + "\n" +
                "isDelivered=" + message.isDelivered() + "\n" +
                "isListened=" + message.isListened() + "\n" +
                "isUnread=" + message.isUnread() + "\n" +
                "localTime=" + message.localTime() + "\n" +
                "progress=" + message.progress() + "\n" +
                "status=" + message.status() + "\n" +
                "ext=" + message.ext() + "\n" +
                "]";
    }

    /**
     * 键盘的文本内容回调
     *
     * @param content
     */
    @Override
    public void onSendText(String content) {
        LogUtils.e(content);
        ChatHelper.getInstance().sendTextMessage(content, toChatUser);
        Message msg = new Message();
        msg.setFrom(ChatHelper.getInstance().getCurrentLoginUser());
        msg.setTo(toChatUser);
        msg.setBody(content);
        msg.itemType = Message.SEND_TEXT;
        chatAdapter.add(msg);
        chatList.scrollToPosition(chatAdapter.getData().size() - 1);
    }
}
