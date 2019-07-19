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
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author: WangYongQi
 * Custom circular jitter (zoom) view
 */

public class CircleShakeView extends View {

    public CircleShakeView(Context context) {
        super(context);
        init();
    }

    public CircleShakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleShakeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // Outer circle radius
    private float outerRadius;
    // Inner circle radius (less than the outer circle radius)
    private float insideRadius;
    // Jitter scaling distance
    private float shakeDistance;
    // Current jitter zoom distance
    private float curDistance;

    // Outer circle color
    private int outerColor;
    // Inner circle color
    private int insideColor;

    // definition paint
    private Paint mPaint;

    // delay time
    private long delayTime;
    // is allow drawing
    private boolean isDraw;
    // is allow run
    private volatile boolean isRun;
    // definition thread
    private Thread mThread;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) {
            return;
        }
        if (!isDraw) {
            return;
        }
        if (outerRadius > insideRadius) {
            // Draw the outer circle (the center of the circle is unchanged and the radius changes)
            RectF outerRectF = new RectF();
            outerRectF.left = curDistance;
            outerRectF.right = outerRadius * 2 - curDistance;
            outerRectF.top = curDistance;
            outerRectF.bottom = outerRadius * 2 - curDistance;
            mPaint.setColor(outerColor);
            canvas.drawRoundRect(outerRectF, outerRadius, outerRadius, mPaint);
            // Draw the inner circle (the inner center and the outer center maintain the same center coordinates, and the radius changes)
            float size = outerRadius - insideRadius;
            RectF insideRectF = new RectF();
            insideRectF.left = outerRectF.left + size;
            insideRectF.right = outerRectF.right - size;
            insideRectF.top = outerRectF.top + size;
            insideRectF.bottom = outerRectF.bottom - size;
            mPaint.setColor(insideColor);
            canvas.drawRoundRect(insideRectF, outerRadius, outerRadius, mPaint);
        } else {
            // Draw the inner circle (the center of the circle is unchanged and the radius changes)
            RectF rectF = new RectF();
            rectF.left = curDistance;
            rectF.right = insideRadius * 2 - curDistance;
            rectF.top = curDistance;
            rectF.bottom = insideRadius * 2 - curDistance;
            mPaint.setColor(insideColor);
            canvas.drawRoundRect(rectF, insideRadius, insideRadius, mPaint);
        }
    }

    private void init() {
        isDraw = false;
        isRun = false;
        curDistance = 0;
        delayTime = 0;
        insideRadius = 0;
        outerRadius = 0;
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setDither(true);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
        }
    }

    public synchronized void start() {
        if (isRun == false) {
            isRun = true;
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (delayTime > 0) {
                        // 如果有延迟显示时间
                        try {
                            Thread.sleep(delayTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        delayTime = 0;
                    }
                    isDraw = true;
                    // 如果不想优先显示最大抖动缩放距离，可省略此句
                    curDistance = shakeDistance;
                    while (isRun) {
                        postInvalidate();
                        if (curDistance > 0) {
                            curDistance = 0;
                        } else {
                            curDistance = shakeDistance;
                        }
                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            mThread.start();
        }
    }

    public synchronized void stop() {
        try {
            isRun = false;
            isDraw = false;
            // If the current Thread is not empty and is alive and has not been interrupted
            if (mThread != null && mThread.isAlive() && !mThread.isInterrupted()) {
                mThread.interrupt();
            }
        } catch (Exception ex) {
        } finally {
            mThread = null;
        }
    }

    public void setOuterRadius(float outerRadius) {
        this.outerRadius = outerRadius;
    }

    public void setInsideRadius(float insideRadius) {
        this.insideRadius = insideRadius;
    }

    public void setShakeDistance(float shakeDistance) {
        this.shakeDistance = shakeDistance;
    }

    public void setOuterColor(int outerColor) {
        this.outerColor = outerColor;
    }

    public void setInsideColor(int insideColor) {
        this.insideColor = insideColor;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

}