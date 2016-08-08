package com.example.animation360.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.animation360.R;

/**
 * Created by Administrator on 2016/8/7.
 */
public class FloatCircleView extends View {

    public int width = 150;
    public int height = 150;

    private Paint circlePaint;
    private Paint textPaint;

    private String text = "50%";

    // 是否正在被拖拽 默认为未拖拽
    private boolean drag = false;

    private Bitmap bitmap;

    public FloatCircleView(Context context) {
        super(context);
//        initPaints();
    }

    public FloatCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
        initPaints();
    }

    private void initPaints() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.GRAY);
        circlePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setTextSize(25);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zimo);

        Bitmap.createScaledBitmap(bitmap, width, height, true);

    }

    // Descent处于基线的下方，为正数
    // Ascent处于基线的上方，为负数
    @Override
    protected void onDraw(Canvas canvas) {
        if (drag) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        } else {
            canvas.drawCircle(width / 2, height / 2, width / 2, circlePaint);
            float textWidth = textPaint.measureText(text);
            float x = width / 2 - textWidth / 2;
            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            float dy = -(metrics.descent + metrics.ascent) / 2;
            float y = height / 2 + dy;
            canvas.drawText(text, x, y, textPaint);
        }
    }

    public void setDragState(boolean b) {
        drag = b;
        invalidate();
    }

}
