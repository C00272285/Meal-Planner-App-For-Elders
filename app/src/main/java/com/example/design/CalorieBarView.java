package com.example.design;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

public class CalorieBarView extends View {
    private Paint usedPaint;
    private Paint remainingPaint;
    private double totalCalories;
    private double consumedCalories;

    public CalorieBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();  // Default values
    }

    private void init() {
        consumedCalories = 0;
        totalCalories = 100;

        usedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        usedPaint.setColor(Color.RED);
        usedPaint.setStyle(Paint.Style.FILL);

        remainingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        remainingPaint.setColor(Color.GREEN);
        remainingPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        // Draw the used calories bar
        float usedWidth = (float) (getWidth() * (consumedCalories / totalCalories));
        @SuppressLint("DrawAllocation") RectF usedRect = new RectF(0, 0, usedWidth, getHeight());
        canvas.drawRect(usedRect, usedPaint);

        // Draw the remaining calories bar
        @SuppressLint("DrawAllocation") RectF remainingRect = new RectF(usedWidth, 0, getWidth(), getHeight());
        canvas.drawRect(remainingRect, remainingPaint);
    }

}
