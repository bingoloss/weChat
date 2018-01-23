package com.lehand.tools.overscroll;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.lehand.tools.overscroll.adapters.RecyclerViewOverScrollDecorAdapter;

/**
 * Created by bingo on 2018/1/21.
 * 操作类
 * 注意：该工具与刷新控件有冲突，不适合同时使用
 */

public class OverScrollViewUtils {
    /**
     * 针对recyclerView 的弹性效果，其他基本可以直接使用
     * 添加RecyclerView 的滑动阻力效果，在设置LayoutManager和adapter之后调用
     * OverScrollDecoratorHelper.setUpOverScroll(chatList, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);就可以实现效果，
     * 这里对recyclerView上拉出现直接越过上面布局，可以直接从上面布局上去，遇到这种情况，需要在recyclerView布局上嵌套一层父布局，如FrameLayout即可。
     *
     * @param recyclerView
     */
    public static void setUpRecyclerView(RecyclerView recyclerView) {
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
    }

    /**
     * 重载方法，可以设置侧滑设置（swipe）
     *
     * @param recyclerView
     * @param itemTouchHelperCallback
     * @return VerticalOverScrollBounceEffectDecorator 控制器
     */
    public static VerticalOverScrollBounceEffectDecorator setUpRecyclerView(RecyclerView recyclerView, ItemTouchHelper.Callback itemTouchHelperCallback) {
        // Set-up of recycler-view's native item swiping.
        /*ItemTouchHelper.Callback itemTouchHelperCallback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT);//可以设置右滑操作
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };*/
        return new VerticalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(recyclerView, itemTouchHelperCallback));
    }

    /**
     * listView
     *
     * @param listView
     */
    public static void setUpListView(ListView listView) {
        OverScrollDecoratorHelper.setUpOverScroll(listView);
    }

    /**
     * gridView
     *
     * @param gridView
     */
    public static void setUpGridView(GridView gridView) {
        OverScrollDecoratorHelper.setUpOverScroll(gridView);
    }

    /**
     * scrollView
     *
     * @param scrollView
     */
    public static void setUpScrollView(ScrollView scrollView) {
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
    }

    /**
     * HorizontalScrollView
     *
     * @param scrollView
     */
    public static void setUpScrollView(HorizontalScrollView scrollView) {
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
    }

    /**
     * View
     *
     * @param view
     */
    public static void setUpView(View view, int orientation) {
        if (OverScrollDecoratorHelper.ORIENTATION_VERTICAL == orientation) {
            OverScrollDecoratorHelper.setUpStaticOverScroll(view, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        } else if (OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL == orientation) {
            OverScrollDecoratorHelper.setUpStaticOverScroll(view, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        }
    }
}
