package com.example.animation360.view;


import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.example.animation360.R;
import com.example.animation360.engine.FloatViewManager;

/**
 * Created by Administrator on 2016/8/8.
 */
public class FloatMenuView extends LinearLayout {

    private LinearLayout ll;

    private TranslateAnimation animation;

    public FloatMenuView(Context context) {
        super(context);
        View root = View.inflate(getContext(), R.layout.float_menu_view, null);
        ll = (LinearLayout) root.findViewById(R.id.ll);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(500);
        animation.setFillAfter(true);
        ll.setAnimation(animation);
        root.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FloatViewManager manager = FloatViewManager.getInstance(getContext());
                manager.hideFloatMenuView();
                manager.showFloatCircleView();
                return false;
            }
        });
        addView(root);
    }

    public void startAnimation() {
        animation.start();
    }

}
