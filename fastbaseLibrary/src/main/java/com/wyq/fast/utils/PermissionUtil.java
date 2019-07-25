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
package com.wyq.fast.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import com.wyq.fast.app.FastApp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

/**
 * Author: WangYongQi
 * Permissions tool class
 */

public final class PermissionUtil {

    /**
     * return whether you have permission
     *
     * @param permissions
     * @return
     */
    public static boolean isHasPermission(@NonNull String... permissions) {
        if (FastApp.getContext() != null) {
            return isHasPermission(FastApp.getContext(), permissions);
        } else {
            LogUtil.logWarn(PermissionUtil.class, "context is null");
            return false;
        }
    }

    /**
     * return whether you have permission
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean isHasPermission(Context context, @NonNull String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * return whether to grant permissions
     *
     * @param grantResults
     * @return
     */
    public static boolean isGrantPermission(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Do you need to explain why you apply for permission
     *
     * @param activity
     * @param permissions
     * @return
     */
    public static boolean isShouldShowRequestPermissionRationale(Activity activity, @NonNull String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

}
