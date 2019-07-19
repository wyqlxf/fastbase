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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.wyq.fast.app.FastApp;

/**
 * Author: WangYongQi
 * Network tools class
 */

public final class NetWorkUtil {

    public final static String NETWORK_2G = "2G";
    public final static String NETWORK_3G = "3G";
    public final static String NETWORK_4G = "4G";
    public final static String NETWORK_WIFI = "wifi";
    public final static String NETWORK_UNKNOWN = "unknown";
    public final static String NETWORK_ETHERNET = "ethernet";

    /**
     * returns the network type of a string
     *
     * @return
     */
    public static String getNetworkType() {
        if (isEthernet()) {
            return NETWORK_ETHERNET;
        }
        ConnectivityManager cm = (ConnectivityManager) FastApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return NETWORK_UNKNOWN;
        }
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return NETWORK_2G;

                    case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return NETWORK_3G;

                    case TelephonyManager.NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return NETWORK_4G;

                    default:
                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            return NETWORK_3G;
                        }
                }
            }
        }
        return NETWORK_UNKNOWN;
    }

    /**
     * return whether it is an Ethernet network
     *
     * @return
     */
    private static boolean isEthernet() {
        if (FastApp.getContext() != null) {
            ConnectivityManager cm = (ConnectivityManager) FastApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) {
                return false;
            }
            NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
            if (info == null) {
                return false;
            }
            NetworkInfo.State state = info.getState();
            if (null == state) {
                return false;
            }
            return state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING;
        } else {
            LogUtil.logWarn(NetWorkUtil.class, "context is null");
            return true;
        }
    }

    /**
     * return whether it is a wifi network
     *
     * @return
     */
    public static boolean isWifiConnected() {
        if (FastApp.getContext() != null) {
            ConnectivityManager cm = (ConnectivityManager) FastApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) {
                return false;
            }
            NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && info.getType() == ConnectivityManager.TYPE_WIFI;
        } else {
            LogUtil.logWarn(NetWorkUtil.class, "context is null");
            return true;
        }
    }

    /**
     * returns whether the current wifi status is enabled
     *
     * @return
     */
    public static boolean isWifiEnabled() {
        if (FastApp.getContext() != null) {
            WifiManager manager = (WifiManager) FastApp.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (manager == null) {
                return false;
            }
            return manager.isWifiEnabled();
        } else {
            LogUtil.logWarn(NetWorkUtil.class, "context is null");
            return true;
        }
    }

    /**
     * set the current wifi status based on the incoming value
     *
     * @param enabled
     */
    public static void setWifiEnabled(final boolean enabled) {
        if (FastApp.getContext() != null) {
            WifiManager manager = (WifiManager) FastApp.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (manager == null) {
                return;
            }
            // if the current state of wifi is not equal to the value passed in
            if (manager.isWifiEnabled() != enabled) {
                manager.setWifiEnabled(enabled);
            }
        } else {
            LogUtil.logWarn(NetWorkUtil.class, "context is null");
        }
    }

    /**
     * Whether to connect to the network
     *
     * @return
     */
    public static boolean isNetworkConnected() {
        if (FastApp.getContext() != null) {
            ConnectivityManager cm = (ConnectivityManager) FastApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) {
                return false;
            }
            NetworkInfo info = cm.getActiveNetworkInfo();
            return (info != null) && (info.isConnectedOrConnecting());
        } else {
            LogUtil.logWarn(NetWorkUtil.class, "context is null");
            return true;
        }
    }

    /**
     * return is wifi proxy
     *
     * @return
     */
    public static boolean isWifiProxy() {
        if (FastApp.getContext() != null) {
            int proxyPort = -1;
            String proxyAddress = "";
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    proxyAddress = System.getProperty("http.proxyHost");
                    String portStr = System.getProperty("http.proxyPort");
                    proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
                } else {
                    proxyAddress = android.net.Proxy.getHost(FastApp.getContext());
                    proxyPort = android.net.Proxy.getPort(FastApp.getContext());
                }
            } catch (Exception ex) {
            }
            return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
        } else {
            LogUtil.logWarn(NetWorkUtil.class, "context is null");
            return false;
        }
    }

}
