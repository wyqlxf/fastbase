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

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.wyq.fast.app.FastApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Author: WangYongQi
 * Xml file tool class for generating various screen resolution pixels
 */

public final class PixelXmlMarkUtil {

    /**
     * Make an XML resolution file to the phone sd card
     *
     * @param baseWidth
     * @param baseHeight
     * @param supportDimensions
     * @param isSupportNegative
     */
    public static void markXmlSaveToSdCard(int baseWidth, int baseHeight, String supportDimensions[], boolean isSupportNegative) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !PermissionUtil.isHasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            LogUtil.logWarn(PixelXmlMarkUtil.class, "No read and write permissions");
        }
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (FastApp.getContext() != null) {
                markXmlFileDefault(FastApp.getContext(), baseWidth, baseHeight, isSupportNegative);
            } else {
                LogUtil.logWarn(PixelXmlMarkUtil.class, "context is null");
            }
            if (supportDimensions != null && supportDimensions.length > 0) {
                for (int i = 0; i < supportDimensions.length; i++) {
                    String dimension = supportDimensions[i].trim();
                    if (!TextUtils.isEmpty(dimension) && dimension.contains("x")) {
                        String screenSize[] = dimension.split("x");
                        if (screenSize != null && screenSize.length == 2) {
                            // 屏幕高
                            int screenHeight = Integer.parseInt(screenSize[0]);
                            // 屏幕宽
                            int screenWidth = Integer.parseInt(screenSize[1]);
                            StringBuffer bufferWidth = getXmlContentPX(baseWidth, screenWidth, "width", isSupportNegative);
                            StringBuffer bufferHeight = getXmlContentPX(baseHeight, screenHeight, "height", isSupportNegative);
                            File fileDir = null;
                            try {
                                fileDir = new File(Environment.getExternalStorageDirectory().getCanonicalPath() + "/res/values-" + screenHeight + "x" + screenWidth + "/");
                                if (!fileDir.exists()) {
                                    fileDir.mkdirs();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                LogUtil.logError(PixelXmlMarkUtil.class, "Failed to create directory," + e.toString());
                            }
                            if (fileDir != null) {
                                File fileWidth = new File(fileDir.getAbsolutePath(), "dimens_width.xml");
                                if (fileWidth.exists()) {
                                    fileWidth.delete();
                                }
                                File fileHeight = new File(fileDir.getAbsolutePath(), "dimens_height.xml");
                                if (fileHeight.exists()) {
                                    fileHeight.delete();
                                }
                                PrintWriter pw;
                                FileOutputStream fos;
                                try {
                                    fos = new FileOutputStream(fileWidth);
                                    pw = new PrintWriter(fos);
                                    pw.print(bufferWidth.toString());
                                    pw.close();
                                    fos.close();
                                    fos = new FileOutputStream(fileHeight);
                                    pw = new PrintWriter(fos);
                                    pw.print(bufferHeight.toString());
                                    pw.close();
                                    fos.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    LogUtil.logError(PixelXmlMarkUtil.class, "Generate xml failed," + e.toString());
                                }
                            }
                        }
                    }
                }
            } else {
                LogUtil.logWarn(PixelXmlMarkUtil.class, "Please pass in a custom dimension, for example：1920x1080,1280x720,2560x1440,960x540");
            }
        } else {
            LogUtil.logError(PixelXmlMarkUtil.class, "SD card mount exception");
        }
    }

    /**
     * Make the default adaptation file
     *
     * @param context
     * @param baseWidth
     * @param baseHeight
     * @param isSupportNegative
     */
    private static void markXmlFileDefault(Context context, int baseWidth, int baseHeight, boolean isSupportNegative) {
        StringBuffer bufferWidth = getXmlContentDP(context, baseWidth, "width", isSupportNegative);
        StringBuffer bufferHeight = getXmlContentDP(context, baseHeight, "height", isSupportNegative);
        File fileDir = null;
        try {
            fileDir = new File(Environment.getExternalStorageDirectory().getCanonicalPath() + "/res/values/");
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.logError(PixelXmlMarkUtil.class, "Failed to create directory," + e.toString());
        }
        if (fileDir != null) {
            File fileWidth = new File(fileDir.getAbsolutePath(), "dimens_width.xml");
            if (fileWidth.exists()) {
                fileWidth.delete();
            }
            File fileHeight = new File(fileDir.getAbsolutePath(), "dimens_height.xml");
            if (fileHeight.exists()) {
                fileHeight.delete();
            }
            PrintWriter pw;
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(fileWidth);
                pw = new PrintWriter(fos);
                pw.print(bufferWidth.toString());
                pw.close();
                fos.close();
                fos = new FileOutputStream(fileHeight);
                pw = new PrintWriter(fos);
                pw.print(bufferHeight.toString());
                pw.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.logError(PixelXmlMarkUtil.class, "Generate xml failed," + e.toString());
            }
        }
    }

    /**
     * Get XML content (PX value)
     *
     * @param baseSize
     * @param screenSize
     * @param prefixName
     * @param isSupportNegative
     * @return
     */
    private static StringBuffer getXmlContentPX(int baseSize, int screenSize, String prefixName, boolean isSupportNegative) {
        float ratio = (float) screenSize / (float) baseSize;
        StringBuffer buffer = new StringBuffer();
        buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        buffer.append("<resources>\n");
        buffer.append("    <dimen name=\"" + prefixName + 0 + "px\">" + 0 + "px</dimen>\n");
        for (int i = 0; i < baseSize; i++) {
            // positive
            buffer.append("    <dimen name=\"" + prefixName + (i + 1) + "px\">" + (i + 1) * ratio + "px</dimen>\n");
        }
        // Whether to support negative numbers
        if (isSupportNegative) {
            // Line break layering
            buffer.append("\n");
            for (int i = 0; i < baseSize; i++) {
                // negative
                buffer.append("    <dimen name=\"" + prefixName + "_negative" + (i + 1) + "px\">-" + (i + 1) * ratio + "px</dimen>\n");
            }
        }
        buffer.append("</resources>");
        return buffer;
    }

    /**
     * Get XML content (DP value)
     *
     * @param context
     * @param baseSize
     * @param prefixName
     * @param isSupportNegative
     * @return
     */
    private static StringBuffer getXmlContentDP(Context context, int baseSize, String prefixName, boolean isSupportNegative) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        buffer.append("<resources>\n");
        buffer.append("    <dimen name=\"" + prefixName + 0 + "px\">" + 0 + "dp</dimen>\n");
        for (int i = 0; i < baseSize; i++) {
            // positive
            buffer.append("    <dimen name=\"" + prefixName + (i + 1) + "px\">" + ScreenUtil.pxToDp(i + 1) + "dp</dimen>\n");
        }
        // Whether to support negative numbers
        if (isSupportNegative) {
            // Line break layering
            buffer.append("\n");
            for (int i = 0; i < baseSize; i++) {
                // negative
                buffer.append("    <dimen name=\"" + prefixName + "_negative" + (i + 1) + "px\">-" + ScreenUtil.pxToDp(i + 1) + "dp</dimen>\n");
            }
        }
        buffer.append("</resources>");
        return buffer;
    }

}
