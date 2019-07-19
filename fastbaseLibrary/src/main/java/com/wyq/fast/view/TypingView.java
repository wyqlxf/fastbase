/**
 * MIT License
 * <p>
 * Copyright (c) 2019 wangyognqi
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.wyq.fast.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author: WangYongQi
 * Customize "Entering" animation view
 */

public class TypingView extends View {

    private Paint mPaint;
    private float bigRadius;
    private float smallRadius;
    private long delayMilliseconds;

    private float viewWidth;
    private float viewHeight;
    private float paddingLeft;

    private volatile boolean isRun;
    private volatile int position = 0;

    public TypingView(Context context) {
        super(context);
        init();
    }

    public TypingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TypingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) {
            return;
        }
        if (isRun) {
            position = position + 1;
            if (position < 0 || position > 2) {
                position = 0;
            }
            // Round distance
            final float distance = bigRadius * 2;
            // Loop drawing circle
            for (int i = 0; i < 3; i++) {
                float radius;
                if (i == position) {
                    radius = bigRadius;
                } else {
                    radius = smallRadius;
                }
                float radiusX = (viewWidth - bigRadius * 2 * 3 - distance * 2) / 2 + i * (bigRadius * 2 + distance) + paddingLeft;
                float radiusY = viewHeight / 2;
                canvas.drawCircle(radiusX, radiusY, radius, mPaint);
            }
            postInvalidateDelayed(delayMilliseconds);
        }
    }

    private void init() {
        isRun = false;
        bigRadius = 0;
        smallRadius = 0;
        viewWidth = 0;
        viewHeight = 0;
        paddingLeft = 0;
        delayMilliseconds = 300;
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setDither(true);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setColor(Color.BLACK);
        }
    }

    public void setViewSize(float viewWidth, float viewHeight) {
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
    }

    public void setBigRadius(float bigRadius) {
        this.bigRadius = bigRadius;
    }

    public void setSmallRadius(float smallRadius) {
        this.smallRadius = smallRadius;
    }

    public void setPaddingLeft(float paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public void setPaintColor(int color) {
        if (mPaint != null) {
            mPaint.setColor(color);
        }
    }

    public void setDelayMilliseconds(long delayMilliseconds) {
        this.delayMilliseconds = delayMilliseconds;
    }

    public synchronized void start() {
        if (!isRun) {
            isRun = true;
            postInvalidate();
            setVisibility(View.VISIBLE);
        }
    }

    public synchronized void stop() {
        if (isRun) {
            isRun = false;
            postInvalidate();
            setVisibility(View.GONE);
        }
    }

}