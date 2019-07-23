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
package com.wyq.fast.core.asynctask;

import android.os.AsyncTask;
import android.os.Build;

import com.wyq.fast.interfaces.asynctask.OnCancelled;
import com.wyq.fast.interfaces.asynctask.OnDoInBackground;
import com.wyq.fast.interfaces.asynctask.OnPostExecute;
import com.wyq.fast.interfaces.asynctask.OnPreExecute;
import com.wyq.fast.interfaces.asynctask.OnProgressUpdate;
import com.wyq.fast.interfaces.asynctask.OnPublishProgress;

import java.util.concurrent.Executor;

/**
 * Author: WangYongQi
 * Asynchronous task
 */

public class FastAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> implements OnPublishProgress<Progress> {

    private OnPreExecute mPreExecute;
    private OnCancelled mOnCancelled;
    private OnPostExecute<Result> mPostExecute;
    private OnProgressUpdate<Progress> mProgressUpdate;
    private OnDoInBackground<Params, Result> mDoInBackground;

    private FastAsyncTask() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mPreExecute != null) {
            mPreExecute.onPreExecute();
        }
    }

    @Override
    public Result doInBackground(Params... params) {
        return (mDoInBackground != null) ? mDoInBackground.doInBackground(params) : null;
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (mPostExecute != null) {
            mPostExecute.onPostExecute(result);
        }
    }

    @SafeVarargs
    @Override
    protected final void onProgressUpdate(Progress... values) {
        super.onProgressUpdate(values);
        if (mProgressUpdate != null) {
            mProgressUpdate.onProgressUpdate(values);
        }
    }

    @Override
    public void onPublishProgress(Progress[] values) {
        this.publishProgress(values);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onCancelled(Result result) {
        super.onCancelled(result);
        if (mOnCancelled != null) {
            mOnCancelled.onCancelled(result);
        }
    }

    @SafeVarargs
    public final AsyncTask<Params, Progress, Result> start(Params... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return super.executeOnExecutor(THREAD_POOL_EXECUTOR, params);
        } else {
            return super.execute(params);
        }
    }

    @SafeVarargs
    public final AsyncTask<Params, Progress, Result> start(Executor exec, Params... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return super.executeOnExecutor(exec, params);
        } else {
            return super.execute(params);
        }
    }

    public static <Params, Progress, Result> Builder<Params, Progress, Result> newBuilder() {
        return new Builder<>();
    }

    public static class Builder<Params, Progress, Result> {

        private final FastAsyncTask<Params, Progress, Result> mAsyncTask;

        public Builder() {
            mAsyncTask = new FastAsyncTask<>();
        }

        public Builder<Params, Progress, Result> setPreExecute(OnPreExecute preExecute) {
            mAsyncTask.mPreExecute = preExecute;
            return this;
        }

        public Builder<Params, Progress, Result> setDoInBackground(OnDoInBackground<Params, Result> doInBackground) {
            mAsyncTask.mDoInBackground = doInBackground;
            return this;
        }

        public Builder<Params, Progress, Result> setPostExecute(OnPostExecute<Result> postExecute) {
            mAsyncTask.mPostExecute = postExecute;
            return this;
        }

        public Builder<Params, Progress, Result> setProgressUpdate(OnProgressUpdate<Progress> progressUpdate) {
            mAsyncTask.mProgressUpdate = progressUpdate;
            return this;
        }

        public Builder<Params, Progress, Result> setCancelled(OnCancelled<Result> cancelled) {
            mAsyncTask.mOnCancelled = cancelled;
            return this;
        }

        @SafeVarargs
        public final AsyncTask<Params, Progress, Result> start(Params... params) {
            return mAsyncTask.start(params);
        }

        @SafeVarargs
        public final AsyncTask<Params, Progress, Result> start(Executor exec, Params... params) {
            return mAsyncTask.start(exec, params);
        }

        public boolean isRunning() {
            return mAsyncTask.getStatus() == AsyncTask.Status.RUNNING;
        }

        public void cancel(boolean mayInterruptIfRunning) {
            mAsyncTask.cancel(mayInterruptIfRunning);
        }

    }

}