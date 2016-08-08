package com.example.animation360.engine;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import com.example.animation360.view.FloatCircleView;
import com.example.animation360.view.FloatMenuView;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/8/5.
 */
public class FloatViewManager {

    private Context context;

    private FloatCircleView circleView;

    private LayoutParams params;

    private FloatMenuView floatMenuView;

    private View.OnTouchListener circleViewTouchListener = new View.OnTouchListener() {

        private float startX;
        private float startY;

        private float x0;
        private float y0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getRawX();
                    x0 = event.getRawX();
                    y0 = event.getRawY();
                    startY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = event.getRawX();
                    float y = event.getRawY();

                    float dx = x - startX;
                    float dy = y - startY;

                    params.x += dx;
                    params.y += dy;

                    circleView.setDragState(true); // 手指移动时为拖拽状态

                    windowManager.updateViewLayout(circleView, params);

                    startX = x;
                    startY = y;

                    break;
                case MotionEvent.ACTION_UP:
                    float x1 = event.getRawX();
                    // 如果在屏幕右半边区域
                    if (x1 > getScreenWidth() / 2) {
                        // 停靠在手机屏幕的右边
                        params.x = getScreenWidth() - circleView.width;
                    } else {
                        params.x = 0;
                    }

                    circleView.setDragState(false); // 手指离开时为非拖拽状态

                    windowManager.updateViewLayout(circleView, params);

                    // 如果是拖拽动作
                    if (Math.abs(x1 - x0)  > 6) {
                        return true; // 消费掉事件 解决滑动冲突
                    } else {
                        return false; // 走onClick事件
                    }

            }
            return false;
        }
    };

    public int getScreenWidth() {
        return windowManager.getDefaultDisplay().getWidth();
    }

    public int getScreenHeight() {
        return windowManager.getDefaultDisplay().getHeight();
    }

    /**
     * 通过反射的方式获取
     *
     * Class<?> c = Class.forName("com.android.internal.R$dimen");
     * Object o = c.newInstance();
     * Field field = c.getField("status_bar_height");
     * int x = (Integer) field.get(o);
     * int height = context.getResources().getDimensionPixelSize(x);
     *
     * @return
     */
    // 获取状态栏的高
    public int getStatusHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(0);
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            return 0;
        }
    }

    // 通过WindowManager来操控浮窗体的显示和隐藏
    private WindowManager windowManager;

    private FloatViewManager(final Context context) {
        this.context = context;
        windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        circleView = new FloatCircleView(context);
        circleView.setOnTouchListener(circleViewTouchListener);
        circleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "onClick", Toast.LENGTH_SHORT).show();
                // TODO
                // 隐藏circleView 显示菜单栏 开启动画
                windowManager.removeView(circleView);
                showFloatMenuView();
                floatMenuView.startAnimation();
            }
        });
        floatMenuView = new FloatMenuView(context);
    }

    private void showFloatMenuView() {
        LayoutParams params = new LayoutParams();
        params.width = getScreenWidth();
        params.height = getScreenHeight() - getStatusHeight();
        params.gravity = Gravity.BOTTOM | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        params.type = LayoutParams.TYPE_PHONE;
        params.flags = LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.format = PixelFormat.RGBA_8888;
        windowManager.addView(floatMenuView, params);
    }

    private volatile static FloatViewManager instance;

    public static FloatViewManager getInstance(Context context) {
        if (instance == null) {
            synchronized(FloatViewManager.class) {
                if (instance == null) {
                    instance = new FloatViewManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * 展示浮窗小球到窗口上
     */
    public void showFloatCircleView() {
        if (params == null) {
            params = new LayoutParams();
            params.width = circleView.width;
            params.height = circleView.height;
            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.x = 0;
            params.y = 0;
            params.type = LayoutParams.TYPE_PHONE;
            params.flags = LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL;
            params.format = PixelFormat.RGBA_8888;
        }

        windowManager.addView(circleView, params);
    }

    public void hideFloatMenuView() {
        windowManager.removeView(floatMenuView);
    }

}
