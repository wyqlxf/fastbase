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
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.wyq.fast.app.FastApp;
import com.wyq.fast.model.KeyValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: WangYongQi
 * Lightweight caching tool class
 */

public final class SPUtil {

    // Declare a SharedPreferences
    private SharedPreferences preferences;
    // Declare a SharedPreferences.Editor
    private SharedPreferences.Editor editor;
    // SPUtil object map storage
    private static final Map<String, SPUtil> spUtilMap = new HashMap<>();

    private SPUtil(String name) {
        if (FastApp.getContext() != null) {
            preferences = FastApp.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
            editor = preferences.edit();
        } else {
            LogUtil.logWarn(SPUtil.class, "context is null");
        }
    }

    /**
     * return an instance of SPUtil
     *
     * @param spName
     * @return
     */
    public static SPUtil getInstance(String spName) {
        SPUtil spUtils = spUtilMap.get(spName);
        if (spUtils == null) {
            synchronized (SPUtil.class) {
                spUtils = spUtilMap.get(spName);
                if (spUtils == null) {
                    spUtils = new SPUtil(spName);
                    spUtilMap.put(spName, spUtils);
                }
            }
        }
        return spUtils;
    }

    public void put(final String key, String value) {
        if (preferences == null || null == editor) {
            LogUtil.logWarn(SPUtil.class, "preferences or  editor is null");
            return;
        }
        if (!TextUtils.isEmpty(key)) {
            if (preferences.contains(key)) {
                editor.remove(key);
            }
            editor.putString(key, value);
            editor.commit();
        } else {
            LogUtil.logWarn(SPUtil.class, "key is null");
        }
    }

    public String getString(final String key) {
        return getString(key, "");
    }

    public String getString(final String key, final String defaultValue) {
        if (preferences == null || null == editor) {
            LogUtil.logWarn(SPUtil.class, "preferences or  editor is null");
            return "";
        }
        if (!TextUtils.isEmpty(key)) {
            if (preferences.contains(key)) {
                try {
                    return preferences.getString(key, defaultValue);
                } catch (Exception ex) {
                    LogUtil.logError(SPUtil.class, "return value failed," + ex.toString());
                    return defaultValue;
                }
            } else {
                return defaultValue;
            }
        } else {
            LogUtil.logWarn(SPUtil.class, "key is null");
            return defaultValue;
        }
    }

    public void put(final String key, int value) {
        if (preferences == null || null == editor) {
            LogUtil.logWarn(SPUtil.class, "preferences or  editor is null");
            return;
        }
        if (!TextUtils.isEmpty(key)) {
            if (preferences.contains(key)) {
                editor.remove(key);
            }
            editor.putInt(key, value);
            editor.commit();
        } else {
            LogUtil.logWarn(SPUtil.class, "key is null");
        }
    }

    public int getInt(final String key) {
        return getInt(key, -1);
    }

    public int getInt(final String key, final int defaultValue) {
        if (preferences == null || null == editor) {
            LogUtil.logWarn(SPUtil.class, "preferences or  editor is null");
            return -1;
        }
        if (!TextUtils.isEmpty(key)) {
            if (preferences.contains(key)) {
                try {
                    return preferences.getInt(key, defaultValue);
                } catch (Exception ex) {
                    LogUtil.logError(SPUtil.class, "return value failed," + ex.toString());
                    return defaultValue;
                }
            } else {
                return defaultValue;
            }
        } else {
            LogUtil.logWarn(SPUtil.class, "key is null");
            return defaultValue;
        }
    }

    public void put(final String key, boolean value) {
        if (preferences == null || null == editor) {
            LogUtil.logWarn(SPUtil.class, "preferences or  editor is null");
            return;
        }
        if (!TextUtils.isEmpty(key)) {
            if (preferences.contains(key)) {
                editor.remove(key);
            }
            editor.putBoolean(key, value);
            editor.commit();
        } else {
            LogUtil.logWarn(SPUtil.class, "key is null");
        }
    }

    public boolean getBoolean(final String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(final String key, final boolean defaultValue) {
        if (preferences == null || null == editor) {
            LogUtil.logWarn(SPUtil.class, "preferences or  editor is null");
            return false;
        }
        if (!TextUtils.isEmpty(key)) {
            if (preferences.contains(key)) {
                try {
                    return preferences.getBoolean(key, defaultValue);
                } catch (Exception ex) {
                    LogUtil.logError(SPUtil.class, "return value failed," + ex.toString());
                    return defaultValue;
                }
            } else {
                return defaultValue;
            }
        } else {
            LogUtil.logWarn(SPUtil.class, "key is null");
            return defaultValue;
        }
    }

    public void put(final String key, float value) {
        if (preferences == null || null == editor) {
            LogUtil.logWarn(SPUtil.class, "preferences or  editor is null");
            return;
        }
        if (!TextUtils.isEmpty(key)) {
            if (preferences.contains(key)) {
                editor.remove(key);
            }
            editor.putFloat(key, value);
            editor.commit();
        } else {
            LogUtil.logWarn(SPUtil.class, "key is null");
        }
    }

    public float getFloat(final String key) {
        return getFloat(key, -1);
    }

    public float getFloat(final String key, final float defaultValue) {
        if (preferences == null || null == editor) {
            LogUtil.logWarn(SPUtil.class, "preferences or  editor is null");
            return -1;
        }
        if (!TextUtils.isEmpty(key)) {
            if (preferences.contains(key)) {
                try {
                    return preferences.getFloat(key, defaultValue);
                } catch (Exception ex) {
                    LogUtil.logError(SPUtil.class, "return value failed," + ex.toString());
                    return defaultValue;
                }
            } else {
                return defaultValue;
            }
        } else {
            LogUtil.logWarn(SPUtil.class, "key is null");
            return defaultValue;
        }
    }

    public void put(final String key, long value) {
        if (preferences == null || null == editor) {
            LogUtil.logWarn(SPUtil.class, "preferences or  editor is null");
            return;
        }
        if (!TextUtils.isEmpty(key)) {
            if (preferences.contains(key)) {
                editor.remove(key);
            }
            editor.putLong(key, value);
            editor.commit();
        } else {
            LogUtil.logWarn(SPUtil.class, "key is null");
        }
    }

    public long getLong(final String key) {
        return getLong(key, -1);
    }

    public long getLong(final String key, final long defaultValue) {
        if (preferences == null || null == editor) {
            LogUtil.logWarn(SPUtil.class, "preferences or  editor is null");
            return -1;
        }
        if (!TextUtils.isEmpty(key)) {
            if (preferences.contains(key)) {
                try {
                    return preferences.getLong(key, defaultValue);
                } catch (Exception ex) {
                    LogUtil.logError(SPUtil.class, "return value failed," + ex.toString());
                    return defaultValue;
                }
            } else {
                return defaultValue;
            }
        } else {
            LogUtil.logWarn(SPUtil.class, "key is null");
            return defaultValue;
        }
    }

    public void put(final String key, Set<String> value) {
        if (preferences == null || null == editor) {
            LogUtil.logWarn(SPUtil.class, "preferences or  editor is null");
            return;
        }
        if (!TextUtils.isEmpty(key)) {
            if (preferences.contains(key)) {
                editor.remove(key);
            }
            editor.putStringSet(key, value);
            editor.commit();
        } else {
            LogUtil.logWarn(SPUtil.class, "key is null");
        }
    }

    public Set<String> getStringSet(final String key) {
        return getStringSet(key, null);
    }

    public Set<String> getStringSet(final String key, final Set<String> defaultValue) {
        if (preferences == null || editor == null) {
            LogUtil.logWarn(SPUtil.class, "preferences or  editor is null");
            return null;
        }
        if (!TextUtils.isEmpty(key)) {
            if (preferences.contains(key)) {
                try {
                    return preferences.getStringSet(key, defaultValue);
                } catch (Exception ex) {
                    LogUtil.logError(SPUtil.class, "return value failed," + ex.toString());
                    return defaultValue;
                }
            } else {
                return defaultValue;
            }
        } else {
            LogUtil.logWarn(SPUtil.class, "key is null");
            return defaultValue;
        }
    }

    public void put(final List<KeyValue> list) {
        if (preferences == null || editor == null) {
            LogUtil.logWarn(SPUtil.class, "preferences or  editor is null");
            return;
        }
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                KeyValue keyValue = list.get(i);
                if (keyValue != null) {
                    String key = keyValue.getKey();
                    if (!TextUtils.isEmpty(key)) {
                        if (preferences.contains(key)) {
                            editor.remove(key);
                        }
                        Object value = keyValue.getValue();
                        if (value != null) {
                            if (value instanceof String) {
                                editor.putString(key, (String) value);
                            } else if (value instanceof Integer) {
                                editor.putInt(key, (int) value);
                            } else if (value instanceof Boolean) {
                                editor.putBoolean(key, (boolean) value);
                            } else if (value instanceof Float) {
                                editor.putFloat(key, (float) value);
                            } else if (value instanceof Long) {
                                editor.putLong(key, (long) value);
                            } else if (value instanceof Set) {
                                editor.putStringSet(key, (Set<String>) value);
                            }
                            editor.apply();
                        }
                    }
                }
            }
            editor.commit();
        }
    }

    /**
     * return all data collections
     *
     * @return
     */
    public Map<String, ?> getAll() {
        if (preferences == null) {
            return null;
        }
        return preferences.getAll();
    }

    /**
     * remove the corresponding value based on the passed key
     *
     * @param key
     */
    public void remove(String key) {
        if (preferences == null || null == editor) {
            return;
        }
        if (preferences.contains(key)) {
            editor.remove(key);
            editor.commit();
        }
    }

    /**
     * clear data
     */
    public void clear() {
        if (null != editor) {
            editor.clear();
            editor.commit();
        }
    }

}
