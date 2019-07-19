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

import java.util.Random;

/**
 * Author: WangYongQi
 * Random tool class , Generate various random numbers And letters
 */

public final class RandomUtil {

    /**
     * Get a random number of the specified length (pure number)
     *
     * @param length
     * @return
     */
    public static String getRandomNumber(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(new Random().nextInt(9));
        }
        return stringBuffer.toString();
    }

    /**
     * Get a random number of the specified length (number + uppercase and lowercase letters)
     *
     * @param length
     * @return
     */
    public static String getRandomNumberLetter(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        String string[] = {"a", "A", "b", "B", "c", "C", "d", "D", "e", "E", "f", "F", "g", "G", "h", "H", "i", "I",
                "j", "J", "k", "K", "l", "L", "m", "M", "n", "N", "o", "O", "p", "P", "q", "Q", "r", "R", "s", "S",
                "t", "T", "u", "U", "v", "V", "w", "W", "x", "X", "y", "Y", "z", "Z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9"};
        for (int i = 0; i < length; i++) {
            int index = new Random().nextInt(string.length);
            if (string.length > index) {
                stringBuffer.append(string[index]);
            } else {
                stringBuffer.append(getRandomNumber(1));
            }
        }
        return stringBuffer.toString();
    }

}
