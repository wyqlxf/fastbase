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
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Author: WangYongQi
 * Custom rectangle progress bar
 */

public class RectProgressView extends View {

    private Paint mPaint;
    private int progress = 0;
    private int screenWidth = 0;
    private float progressHeight = 0;

    public RectProgressView(Context context) {
        super(context);
        init();
    }

    public RectProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RectProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInEditMode() && canvas != null) {
            float progressWidth = screenWidth * ((float) progress / (float) 100);
            canvas.drawRect(0, 0, progressWidth, progressHeight, mPaint);
        }
    }

    private void init() {
        progress = 0;
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.WHITE);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        progressHeight = 6;
    }

    /**
     * Set height
     *
     * @param height
     */
    public void setHeight(float height) {
        progressHeight = height;
    }

    /**
     * Set color
     *
     * @param resId
     */
    public void setColor(int resId) {
        mPaint.setColor(resId);
    }

    /**
     * Set gradient color
     *
     * @param colors
     */
    public void setShader(int colors[]) {
        // Set the gradient area
        LinearGradient shader = new LinearGradient(0, 0, screenWidth, progressHeight, colors, null, Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
    }

    /**
     * Set current progress
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.progress = progress;
        if (progress == 100) {
            setVisibility(View.GONE);
            mPaint.setColor(Color.TRANSPARENT);
        } else {
            setVisibility(View.VISIBLE);
            mPaint.setColor(Color.parseColor("#ff8000"));
        }
        postInvalidate();
    }

}
