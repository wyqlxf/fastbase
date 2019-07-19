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
package com.wyq.fast.core.cipher;

import android.text.TextUtils;
import android.util.Base64;

import com.wyq.fast.utils.LogUtil;

import java.security.MessageDigest;

/**
 * Author: WangYongQi
 * Encryption base class
 */

public abstract class BaseCipher {

    protected static String xorEncryptData(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        byte[] bytes = string.getBytes();
        int len = bytes.length;
        int key = 0x12;
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (bytes[i] ^ key);
            key = bytes[i];
        }
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    protected static String xorDecryptData(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        byte[] bytes = Base64.decode(string, Base64.DEFAULT);
        int len = bytes.length;
        int key = 0x12;
        for (int i = len - 1; i > 0; i--) {
            bytes[i] = (byte) (bytes[i] ^ bytes[i - 1]);
        }
        bytes[0] = (byte) (bytes[0] ^ key);
        return new String(bytes);
    }

    protected static String encryptData(String string, String algorithm) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            byte[] bytes = messageDigest.digest(string.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                builder.append(temp);
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(CipherFactory.class, algorithm + "加密失败，" + e.toString());
        }
        return "";
    }

    /**
     * Convert byte array to hex string
     *
     * @param bytes
     * @return
     */
    protected String byteToHex(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            // 整数转成十六进制表示
            String temp = (Integer.toHexString(b & 0XFF));
            if (temp.length() == 1) {
                builder.append("0");
            }
            builder.append(temp);
        }
        return builder.toString().toUpperCase();
    }

    /**
     * Convert a hex string to a byte array
     *
     * @param data
     * @return
     */
    protected byte[] hexToByte(String data) {
        if (data == null || data.length() < 2) {
            return new byte[0];
        }
        data = data.toLowerCase();
        int length = data.length() / 2;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            String temp = data.substring(2 * i, 2 * i + 2);
            bytes[i] = (byte) (Integer.parseInt(temp, 16) & 0xFF);
        }
        return bytes;
    }

    /**
     * Convert byte array to base64
     *
     * @param bytes
     * @return
     */
    protected String byteToBase64(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * Convert base64 string to byte array
     *
     * @param data
     * @return
     */
    protected byte[] base64ToByte(String data) {
        if (TextUtils.isEmpty(data)) {
            return new byte[0];
        }
        return Base64.decode(data, Base64.DEFAULT);
    }

    /**
     * Process the user-entered key again
     * The length of the key here must be 128 or 192 or 256 bits, which is 16 or 24 or 32 bytes, otherwise an error will be reported.
     *
     * @param key
     * @return
     */
    protected String encryptKey(String key, int bit) {
        if (key == null) {
            key = "";
        }
        int capacity;
        switch (bit) {
            case 128:
                capacity = 128 / 8;
                break;
            case 196:
                capacity = 196 / 8;
                break;
            case 256:
            default:
                capacity = 256 / 8;
                break;
        }
        StringBuilder builder = new StringBuilder(capacity);
        builder.append(key);
        while (builder.length() < capacity) {
            builder.append("0");
        }
        if (builder.length() > capacity) {
            builder.setLength(capacity);
        }
        // The final key is generated using the MD algorithm.
        String value = encryptData(builder.toString(), "MD5");
        if (!TextUtils.isEmpty(value) && value.length() == 32) {
            String lastKey;
            switch (capacity) {
                case 16:
                    lastKey = value.substring(8, 24);
                    break;
                case 24:
                    lastKey = value.substring(4, 28);
                    break;
                case 32:
                default:
                    lastKey = value;
                    break;
            }
            return lastKey;
        } else {
            return builder.toString();
        }
    }

}
