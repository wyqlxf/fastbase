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
package com.wyq.fast.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.wyq.fast.interfaces.handler.OnHandlerListener;
import com.wyq.fast.receiver.NetworkChangeReceiver;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Author: WangYongQi
 * AppCompatActivity base class
 */

public abstract class FastBaseAppCompatActivity extends AppCompatActivity {

    // Declaration handler
    private CommonHandler mHandler;
    // Declare the Handler listener interface
    private OnHandlerListener mOnHandlerListener;
    // Monitor network changes
    private NetworkChangeReceiver mNetworkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Register Handler listener
     *
     * @param onHandlerListener
     */
    public void registerHandlerListener(OnHandlerListener onHandlerListener) {
        if (mHandler == null) {
            mHandler = new CommonHandler(this);
        }
        mOnHandlerListener = onHandlerListener;
    }

    /**
     * Registered network listener
     *
     * @param networkChangeReceiver
     */
    public void registerNetworkListener(NetworkChangeReceiver networkChangeReceiver) {
        mNetworkChangeReceiver = networkChangeReceiver;
    }

    /**
     * Send a message
     *
     * @param msg
     */
    public void sendMessage(Message msg) {
        if (mHandler != null) {
            mHandler.sendMessage(msg);
        }
    }

    /**
     * Send a delayed message
     *
     * @param msg
     * @param delayMillis
     */
    public void sendMessageDelayed(Message msg, long delayMillis) {
        if (mHandler != null) {
            mHandler.sendMessageDelayed(msg, delayMillis);
        }
    }

    /**
     * Send an empty message
     *
     * @param what
     */
    public void sendEmptyMessage(int what) {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(what);
        }
    }

    /**
     * Delay sending an empty message
     *
     * @param what
     * @param delayMillis
     */
    public void sendEmptyMessageDelayed(int what, long delayMillis) {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(what, delayMillis);
        }
    }

    /**
     * Post a thread that can update the UI
     *
     * @param runnable
     */
    public void post(Runnable runnable) {
        if (mHandler != null) {
            mHandler.post(runnable);
        }
    }

    /**
     * Delay Post a thread, you can update the UI
     *
     * @param runnable
     * @param delayMillis
     */
    public void postDelayed(Runnable runnable, long delayMillis) {
        if (mHandler != null) {
            mHandler.postDelayed(runnable, delayMillis);
        }
    }

    /**
     * Remove message
     *
     * @param what
     */
    public void removeMessages(int what) {
        if (mHandler != null) {
            mHandler.removeMessages(what);
        }
    }

    /**
     * Remove all messages and callbacks
     *
     * @param object
     */
    public void removeCallbacksAndMessages(Object object) {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(object);
        }
    }

    /**
     * Get the default Handler
     *
     * @return
     */
    public Handler getDefaultHandler() {
        if (mHandler == null) {
            mHandler = new CommonHandler(this);
        }
        return mHandler;
    }

    /**
     * In Java, non-static inner classes and anonymous inner classes implicitly hold
     * references to their outer classes. Static inner classes do not hold references to external classes.
     */
    private static class CommonHandler extends Handler {

        private WeakReference<FastBaseAppCompatActivity> mWeakReference;

        public CommonHandler(FastBaseAppCompatActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final FastBaseAppCompatActivity activity = mWeakReference.get();
            if (activity != null && activity.mOnHandlerListener != null && msg != null) {
                activity.mOnHandlerListener.onHandleMessage(msg);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNetworkChangeReceiver != null) {
            registerReceiver(mNetworkChangeReceiver, mNetworkChangeReceiver.getIntentFilter());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNetworkChangeReceiver != null) {
            unregisterReceiver(mNetworkChangeReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            // Delete all handler messages and callback functions to avoid memory leaks
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        mNetworkChangeReceiver = null;
    }

}
