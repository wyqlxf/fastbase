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
package com.wyq.fast.core.cipher.specific;

import android.text.TextUtils;

import com.wyq.fast.core.cipher.Cipher;
import com.wyq.fast.core.cipher.CipherFactory;
import com.wyq.fast.utils.LogUtil;

import java.security.Key;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Author: WangYongQi
 * 3DES Encryption class
 */

public class Des3Cipher extends Cipher {

    private String mKey = "";
    private static final String CIPHER_MODE = "desede" + "/CBC" + "/PKCS5Padding";

    public Des3Cipher() {
        super();
    }

    @Override
    public String getFinalKey() {
        return mKey;
    }

    @Override
    public String encryptString(String key, String data) {
        return encrypt(key, data, mOptions);
    }

    @Override
    public String decryptString(String key, String data) {
        return decrypt(key, data, mOptions);
    }

    @Override
    public String encryptString(String key, String data, CipherFactory.Options options) {
        return encrypt(key, data, options);
    }

    @Override
    public String decryptString(String key, String data, CipherFactory.Options options) {
        return decrypt(key, data, options);
    }

    /**
     * Encrypted string
     *
     * @param key
     * @param data
     * @param options
     * @return
     */
    private String encrypt(String key, String data, CipherFactory.Options options) {
        try {
            if (!TextUtils.isEmpty(data)) {
                byte[] bytes = data.getBytes(options.encoding);
                bytes = encrypt(key, bytes, options);
                if (bytes != null) {
                    switch (options.output) {
                        case HEX:
                            return byteToHex(bytes);
                        case BASE64:
                            return byteToBase64(bytes);
                        default:
                            return new String(bytes, options.encoding);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Encrypted byte array
     *
     * @param key
     * @param bytes
     * @param options
     * @return
     */
    private byte[] encrypt(String key, byte[] bytes, CipherFactory.Options options) {
        try {
            if (bytes != null) {
                javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(CIPHER_MODE);
                cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, getSecretKey(key, options), new IvParameterSpec(new byte[cipher.getBlockSize()]));
                return cipher.doFinal(bytes);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * Decrypt string
     *
     * @param key
     * @param data
     * @param options
     * @return
     */
    private String decrypt(String key, String data, CipherFactory.Options options) {
        try {
            if (!TextUtils.isEmpty(data)) {
                byte[] bytes;
                switch (options.output) {
                    case HEX:
                        bytes = hexToByte(data);
                        break;
                    case BASE64:
                        bytes = base64ToByte(data);
                        break;
                    default:
                        bytes = data.getBytes();
                        break;
                }
                bytes = decrypt(key, bytes, options);
                if (bytes != null) {
                    return new String(bytes, options.encoding);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Decrypt byte array
     *
     * @param key
     * @param bytes
     * @param options
     * @return
     */
    private byte[] decrypt(String key, byte[] bytes, CipherFactory.Options options) {
        try {
            if (bytes != null) {
                javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(CIPHER_MODE);
                cipher.init(javax.crypto.Cipher.DECRYPT_MODE, getSecretKey(key, options), new IvParameterSpec(new byte[cipher.getBlockSize()]));
                return cipher.doFinal(bytes);
            }
        } catch (Exception ex) {
        }
        return bytes;
    }

    /**
     * Get the SecretKeySpec object
     *
     * @param key
     * @param options
     * @return
     */
    private Key getSecretKey(String key, CipherFactory.Options options) {
        try {
            if (options.isEncryptKey) {
                key = encryptKey(key, options.bit);
            }
            mKey = key;
            DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(key.getBytes(options.encoding));
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("desede");
            return secretKeyFactory.generateSecret(deSedeKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(Des3Cipher.class, "Failed to generate key，" + e.toString());
        }
        return null;
    }

}
