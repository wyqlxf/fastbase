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

/**
 * Author: WangYongQi
 * Encryption class
 */

public abstract class Cipher extends BaseCipher {

    // Type of encryption
    public enum EncryptType {
        AES,
        DES,
        DES3,
        RSA
    }

    // Output type
    public enum OutputType {
        HEX,
        BASE64
    }

    // Default option
    protected CipherFactory.Options mOptions = CipherFactory.Options.getDefaultOptions();

    // Get the final KEY
    public abstract String getFinalKey();

    // Encrypted string
    public abstract String encryptString(String key, String data);

    // Decrypt string
    public abstract String decryptString(String key, String data);

    // Encrypted string
    public abstract String encryptString(String key, String data, CipherFactory.Options options);

    // Decrypt string
    public abstract String decryptString(String key, String data, CipherFactory.Options options);

    public static String sha1(String string) {
        return encryptData(string, "SHA-1");
    }

    public static String sha224(String string) {
        return encryptData(string, "SHA-224");
    }

    public static String sha256(String string) {
        return encryptData(string, "SHA-256");
    }

    public static String sha384(String string) {
        return encryptData(string, "SHA-384");
    }

    public static String sha512(String string) {
        return encryptData(string, "SHA-512");
    }

    public static String md5(String string) {
        return encryptData(string, "MD5");
    }

    public static String base64Encrypt(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        return Base64.encodeToString(string.getBytes(), Base64.DEFAULT);
    }

    public static String base64Decrypt(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        return new String(Base64.decode(string.getBytes(), Base64.DEFAULT));
    }

    public static String xorEncrypt(String string) {
        return xorEncryptData(string);
    }

    public static String xorDecrypt(String string) {
        return xorDecryptData(string);
    }

}
