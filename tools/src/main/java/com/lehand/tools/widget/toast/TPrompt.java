package com.lehand.tools.widget.toast;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lehand.tools.R;

/**
 * author：bingo
 * data：2018-1-11 10:43:26
 */
public class TPrompt extends Toast {
    private Context mContext;
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private TextView chapterNameTV;
    private LinearLayout rl_root;
    private Handler handler = new Handler();
    //默认显示的在中心
    private int DEFAULT_GRAVITY = Gravity.CENTER;

    public TPrompt(Context context) {
        super(context);
        this.mContext = context;
        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(mContext, R.anim.modal_in);
        mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(mContext, R.anim.modal_out);
        initView(DEFAULT_GRAVITY);
    }

    public TPrompt(Context context, int gravity) {
        super(context);
        this.mContext = context;
        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(mContext, R.anim.modal_in);
        mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(mContext, R.anim.modal_out);
        initView(gravity);
    }

    /*外层布局*/
    private View view;

    protected void initView(int gravity) {
        int height = 0;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.layout_toast, null);
        rl_root = (LinearLayout) view.findViewById(R.id.rl_root);
        chapterNameTV = (TextView) view.findViewById(R.id.chapterName);
        if (DEFAULT_GRAVITY != gravity) {
            Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
            // 获取屏幕高度
            height = display.getHeight();
        }
        setGravity(gravity, 0, height / 4);
        setDuration(Toast.LENGTH_LONG);
        setView(view);
    }

    /**
     * @param v
     * @param l
     * @param t
     * @param r
     * @param b
     */
   /* public void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }*/

    /**
     * 文字提示
     *
     * @param msg
     */
    public void showToast(String msg) {
        if (chapterNameTV != null) {
            show();
            chapterNameTV.setText(msg);
            //mModalInAnim.setDuration(300);
            rl_root.setVisibility(View.VISIBLE);
            //rl_root.startAnimation(mModalInAnim);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rl_root.setVisibility(View.INVISIBLE);
                    rl_root.startAnimation(mModalOutAnim);
                }
            }, 1000);
        }
    }
}
