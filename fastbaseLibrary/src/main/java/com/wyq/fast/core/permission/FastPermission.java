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
package com.wyq.fast.core.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.wyq.fast.config.Config;
import com.wyq.fast.interfaces.permission.OnPermissionListener;
import com.wyq.fast.utils.PermissionUtil;
import com.wyq.fast.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

/**
 * Author: WangYongQi
 * Permissions Obtain class
 */

public class FastPermission {

    private Activity mActivity;

    private String reason;
    private int requestCodeTemp;
    private int requestCodeAlways;

    public FastPermission(Activity activity) {
        this.mActivity = activity;
        this.requestCodeTemp = 10101;
        this.requestCodeAlways = 10102;
    }

    /**
     * Reason description
     *
     * @param reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Set permission request code (temporary)
     *
     * @param requestCodeTemp
     */
    public void setRequestCodeTemp(int requestCodeTemp) {
        this.requestCodeTemp = requestCodeTemp;
    }

    /**
     * Set the permission request code (always)
     *
     * @param requestCodeAlways
     */
    public void setRequestCodeAlways(int requestCodeAlways) {
        this.requestCodeAlways = requestCodeAlways;
    }

    /**
     * request for permission
     *
     * @param permissions
     */
    public void requestPermission(@NonNull final String... permissions) {
        if (PermissionUtil.isShouldShowRequestPermissionRationale(mActivity, permissions)) {
            if (!TextUtils.isEmpty(reason)) {
                // Not the first time to apply for permission, you should first explain the purpose of this permission
                new AlertDialog.Builder(mActivity)
                        .setMessage(reason)
                        .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(mActivity, permissions, requestCodeTemp);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(mActivity, permissions, requestCodeTemp);
            }
        } else {
            // First time to apply for permission, or the user has selected the "Don't ask again" option
            ActivityCompat.requestPermissions(mActivity, permissions, requestCodeAlways);
        }
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, OnPermissionListener listener) {
        if (PermissionUtil.isGrantPermission(grantResults)) {
            // Allow permission
            if (listener != null) {
                listener.onPermissionAllow(requestCode);
            }
        } else {
            final List<String> list = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    // Permission denied
                    if (requestCode == requestCodeAlways) {
                        if (SPUtil.getInstance(Config.SP_PERMISSION_NAME).getBoolean(permissions[i], true)) {
                            // Reject permission for the first time
                            SPUtil.getInstance(Config.SP_PERMISSION_NAME).put(permissions[i], false);
                        } else {
                            // The user has selected the "Don't ask again" option and needs to be processed.
                            list.add(permissions[i]);
                        }
                    } else if (requestCode == requestCodeTemp) {
                        // User temporarily denied permission
                        list.add(permissions[i]);
                    }
                }
            }
            if (listener != null && list != null && list.size() > 0) {
                final String names[] = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    names[i] = list.get(i);
                }
                if (requestCode == requestCodeAlways) {
                    // Permissions are always denied
                    listener.onPermissionAlwaysReject(requestCode, names);
                } else if (requestCode == requestCodeTemp) {
                    // Permission is temporarily denied
                    listener.onPermissionTempReject(requestCode, names);
                }
            }
        }
    }

}
