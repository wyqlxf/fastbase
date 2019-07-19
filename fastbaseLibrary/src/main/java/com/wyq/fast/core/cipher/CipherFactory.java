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

import com.wyq.fast.core.cipher.specific.AesCipher;
import com.wyq.fast.core.cipher.specific.Des3Cipher;
import com.wyq.fast.core.cipher.specific.DesCipher;
import com.wyq.fast.core.cipher.specific.RsaCipher;
import com.wyq.fast.utils.LogUtil;

/**
 * Author: WangYongQi
 * Encryption factory class
 */

public class CipherFactory {

    /**
     * Create an encryption class
     *
     * @param type
     * @return
     */
    public static Cipher create(Cipher.EncryptType type) {
        Cipher cipher = null;
        switch (type) {
            case AES:
                cipher = new AesCipher();
                break;
            case DES:
                cipher = new DesCipher();
                break;
            case DES3:
                cipher = new Des3Cipher();
                break;
            case RSA:
                cipher = new RsaCipher();
                break;
            default:
                LogUtil.logWarn(CipherFactory.class, "Error in encryption and decryption parameters");
                break;
        }
        return cipher;
    }

    public static class Options {

        // Default 256
        public int bit;
        // String encoding
        public String encoding;
        // Output mode
        public Cipher.OutputType output;
        // Whether you need encryption Key
        public boolean isEncryptKey;

        public Options() {
            // 128、192 or 256bits
            bit = 256;
            // utf-8、gb2312、gbk or gb18030
            encoding = "UTF-8";
            // Hex 、base64
            output = Cipher.OutputType.HEX;
            // The incoming key is encrypted by default
            isEncryptKey = true;
        }

        public static Options getDefaultOptions() {
            return new Options();
        }

    }

}
