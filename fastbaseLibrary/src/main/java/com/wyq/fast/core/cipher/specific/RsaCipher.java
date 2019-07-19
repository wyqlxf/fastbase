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
import android.util.Base64;

import com.wyq.fast.core.cipher.Cipher;
import com.wyq.fast.core.cipher.CipherFactory;
import com.wyq.fast.utils.LogUtil;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Author: WangYongQi
 * RSA Encryption class
 */

public class RsaCipher extends Cipher {

    private String mKey = "";
    private static final String CIPHER_MODE = "RSA" + "/None" + "/PKCS1Padding";

    public RsaCipher() {
        super();
    }

    @Override
    public String getFinalKey() {
        return mKey;
    }

    @Override
    public String encryptString(String key, String data) {
        return encrypt(getRSAPublicKey(key), data, mOptions);
    }

    @Override
    public String decryptString(String key, String data) {
        return decrypt(getRSAPrivateKey(key), data, mOptions);
    }

    @Override
    public String encryptString(String key, String data, CipherFactory.Options options) {
        return encrypt(getRSAPublicKey(key), data, options);
    }

    @Override
    public String decryptString(String key, String data, CipherFactory.Options options) {
        return decrypt(getRSAPrivateKey(key), data, options);
    }

    /**
     * Randomly generate an RSA key pair
     * Key length，range ：512～2048
     *
     * @param keyLength
     * @return
     */
    public KeyPair generateRSAKeyPair(int keyLength) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(keyLength);
            return kpg.genKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Encrypted string
     *
     * @param key
     * @param data
     * @param options
     * @return
     */
    private String encrypt(RSAPublicKey key, String data, CipherFactory.Options options) {
        try {
            if (!TextUtils.isEmpty(data) && key != null) {
                byte[] bytes = data.getBytes(options.encoding);
                bytes = encrypt(key, bytes);
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
     * @return
     */
    private byte[] encrypt(RSAPublicKey key, byte[] bytes) {
        try {
            if (bytes != null && key != null) {
                javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(CIPHER_MODE);
                cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
                return cipher.doFinal(bytes);
            }
        } catch (Exception e) {
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
    private String decrypt(RSAPrivateKey key, String data, CipherFactory.Options options) {
        try {
            if (!TextUtils.isEmpty(data) && key != null) {
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
                bytes = decrypt(key, bytes);
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
     * @return
     */
    private byte[] decrypt(RSAPrivateKey key, byte[] bytes) {
        try {
            if (bytes != null && key != null) {
                javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(CIPHER_MODE);
                cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key);
                return cipher.doFinal(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * Get the public key
     *
     * @param key
     * @return
     */
    private RSAPublicKey getRSAPublicKey(String key) {
        try {
            mKey = key;
            byte[] data = Base64.decode(key, Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(data);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the private key
     *
     * @param key
     * @return
     */
    private RSAPrivateKey getRSAPrivateKey(String key) {
        try {
            mKey = key;
            byte[] data = Base64.decode(key, Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(data);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(RsaCipher.class, "Failed to generate key，" + e.toString());
        }
        return null;
    }

}
