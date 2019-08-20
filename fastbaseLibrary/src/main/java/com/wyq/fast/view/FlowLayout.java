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
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyq.fast.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author:WangYongQi
 * Custom FlowLayout
 */

public class FlowLayout extends ViewGroup {

    private Context mContext;

    private List<Integer> mLineHeight = new ArrayList<>();
    private List<List<View>> mAllViews = new ArrayList<>();

    private int textColor = -1;
    private int unit = -1;
    private float textSize = -1;
    private int resId = -1;
    private int textViewWidth = LayoutParams.WRAP_CONTENT;
    private int textViewHeight = LayoutParams.WRAP_CONTENT;
    private boolean singleLine = false;

    private float leftMargin = 0;
    private float topMargin = 0;
    private float rightMargin = 0;
    private float bottomMargin = 0;

    private float leftPadding = 0;
    private float topPadding = 0;
    private float rightPadding = 0;
    private float bottomPadding = 0;

    public FlowLayout(Context context) {
        super(context, null);
        this.mContext = context;
    }

    /**
     * @param key
     * @param list
     * @param listener
     */
    public void update(String key, final ArrayList<HashMap<String, String>> list, final OnItemClickListener listener) {
        if (list != null) {
            removeAllViews();
            for (int i = 0; i < list.size(); i++) {
                final int position = i;
                final HashMap<String, String> item = list.get(i);
                String name = item.containsKey(key) ? item.get(key) : "";
                TextView textView = getTextView();
                textView.setText(name);
                if (textViewWidth < 0) {
                    final int width = (int) (leftPadding + rightPadding + textView.getTextSize() * name.length());
                    textView.setWidth(width);
                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(0, 0, 0, 0);
                }
                textView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onClick(v, item, position);
                        }
                    }
                });
                addView(textView);
            }
        }
    }

    public void setTextViewWidth(int pixels) {
        this.textViewWidth = pixels;
    }

    public void setTextViewHeight(int pixels) {
        this.textViewHeight = pixels;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTextSize(int unit, float textSize) {
        this.unit = unit;
        this.textSize = textSize;
    }

    public void setTextSingleLine(boolean singleLine) {
        this.singleLine = singleLine;
    }

    public void setTextBackgroundResource(int resId) {
        this.resId = resId;
    }

    public void setTextViewMargin(float left, float top, float right, float bottom) {
        this.leftMargin = left;
        this.topMargin = top;
        this.rightMargin = right;
        this.bottomMargin = bottom;
    }

    public void setTextViewPadding(float left, float top, float right, float bottom) {
        this.leftPadding = left;
        this.topPadding = top;
        this.rightPadding = right;
        this.bottomPadding = bottom;
    }

    /**
     * Return a TextView
     *
     * @return
     */
    private TextView getTextView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, textViewHeight);
        TextView textView = new TextView(mContext);
        layoutParams.leftMargin = (int) leftMargin;
        layoutParams.topMargin = (int) topMargin;
        layoutParams.rightMargin = (int) rightMargin;
        layoutParams.bottomMargin = (int) bottomMargin;
        textView.setLayoutParams(layoutParams);
        textView.setWidth(textViewWidth);
        textView.setHeight(textViewHeight);
        textView.setPadding((int) leftPadding, (int) topPadding, (int) rightPadding, (int) bottomPadding);
        if (textColor > 0) {
            textView.setTextColor(textColor);
        }
        if (unit > 0 && textSize > 0) {
            textView.setTextSize(unit, textSize);
        }
        if (resId > 0) {
            textView.setBackgroundResource(resId);
        }
        textView.setSingleLine(singleLine);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int width = 0;
        int height = 0;
        int lineWidth = 0;
        int lineHeight = 0;
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            if (child != null) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
                if (lineWidth + childWidth > sizeWidth) {
                    width = Math.max(lineWidth, childWidth);
                    lineWidth = childWidth;
                    height += lineHeight;
                    lineHeight = childHeight;
                } else {
                    lineWidth += childWidth;
                    lineHeight = Math.max(lineHeight, childHeight);
                }
                if (i == cCount - 1) {
                    width = Math.max(width, lineWidth);
                    height += lineHeight;
                }
            }
        }
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width, (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();
        int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;
        List<View> lineViews = new ArrayList<>();
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width) {
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);
                lineWidth = 0;
                lineViews = new ArrayList<>();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);
        }
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);
        int left = 0;
        int top = 0;
        for (int i = 0; i < mAllViews.size(); i++) {
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();
                child.layout(lc, tc, rc, bc);
                left += child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
            }
            left = 0;
            top += lineHeight;
        }
    }

}
