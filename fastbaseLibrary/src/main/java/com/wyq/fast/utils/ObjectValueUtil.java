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

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: WangYongQi
 * Tool class that gets values based on objects
 */

public final class ObjectValueUtil {

    public static JSONObject getJSONObject(String json) {
        try {
            if (!TextUtils.isEmpty(json)) {
                return new JSONObject(json);
            } else {
                return null;
            }
        } catch (Exception e) {
            LogUtil.logError(ObjectValueUtil.class, "getJSONObject: " + e.toString());
            return null;
        }
    }

    public static JSONObject getJSONObject(JSONObject json, String key) {
        try {
            if (json != null && !TextUtils.isEmpty(key)) {
                return json.isNull(key) ? null : json.getJSONObject(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(ObjectValueUtil.class, "getJSONObject: " + e.toString());
        }
        return null;
    }

    public static JSONObject getJSONObject(JSONArray array, int index) {
        JSONObject json = null;
        try {
            if (array != null && index >= 0 && index < array.length()) {
                json = array.isNull(index) ? null : array.getJSONObject(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(ObjectValueUtil.class, "getJSONObject: " + e.toString());
        }
        return json;
    }

    public static JSONArray getJSONArray(String json) {
        try {
            if (!TextUtils.isEmpty(json)) {
                return new JSONArray(json);
            } else {
                return null;
            }
        } catch (Exception e) {
            LogUtil.logError(ObjectValueUtil.class, "getJSONArray: " + e.toString());
            return null;
        }
    }

    public static JSONArray getJSONArray(JSONObject json, String key) {
        JSONArray array = null;
        try {
            if (json != null && !TextUtils.isEmpty(key)) {
                array = json.isNull(key) ? null : json.getJSONArray(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(ObjectValueUtil.class, "getJSONArray: " + e.toString());
        }
        return array;
    }

    public static JSONArray getJSONArray(JSONArray array, int index) {
        JSONArray jsonArray = null;
        try {
            if (array != null && index >= 0 && index < array.length()) {
                jsonArray = array.isNull(index) ? null : array.getJSONArray(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(ObjectValueUtil.class, "getJSONArray: " + e.toString());
        }
        return jsonArray;
    }

    public static String getJSONValue(JSONArray array, int index) {
        String text = "";
        try {
            if (array != null && index >= 0 && index < array.length()) {
                text = array.isNull(index) ? "" : array.getString(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(ObjectValueUtil.class, "getJSONValue: " + e.toString());
        }
        return text;
    }

    public static String getJSONValue(JSONObject json, String key) {
        String text = "";
        try {
            if (json != null && !TextUtils.isEmpty(key)) {
                text = json.isNull(key) ? "" : json.getString(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(ObjectValueUtil.class, "getJSONValue: " + e.toString());
        }
        return text;
    }

    public static String getJSONValue(JSONObject json, String key, String defaultValue) {
        String text = defaultValue;
        try {
            if (json != null && !TextUtils.isEmpty(key)) {
                text = json.isNull(key) ? defaultValue : json.getString(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(ObjectValueUtil.class, "getJSONValue: " + e.toString());
        }
        return text;
    }

    public static String getString(Object object, String key) {
        String value = "";
        try {
            if (object != null && !TextUtils.isEmpty(key)) {
                if (object instanceof Intent) {
                    value = ((Intent) object).getStringExtra(key);
                } else if (object instanceof Bundle) {
                    Bundle bundle = (Bundle) object;
                    value = bundle.containsKey(key) ? bundle.getString(key) : value;
                } else if (object instanceof HashMap) {
                    HashMap hashMap = ((HashMap) object);
                    value = hashMap.containsKey(key) ? String.valueOf(hashMap.get(key)) : value;
                } else if (object instanceof Map) {
                    Map map = ((Map) object);
                    value = map.containsKey(key) ? String.valueOf(map.get(key)) : value;
                } else if (object instanceof ContentValues) {
                    ContentValues contentValues = ((ContentValues) object);
                    value = contentValues.containsKey(key) ? contentValues.getAsString(key) : value;
                } else if (object instanceof JSONObject) {
                    JSONObject jsonObject = ((JSONObject) object);
                    value = jsonObject.isNull(key) ? value : jsonObject.getString(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(ObjectValueUtil.class, "getString: " + e.toString());
        }
        return value;
    }

    public static String getString(Object object, String key, String defaultValue) {
        String value = defaultValue;
        try {
            if (object != null && !TextUtils.isEmpty(key)) {
                if (object instanceof Intent) {
                    value = ((Intent) object).getStringExtra(key);
                    if (TextUtils.isEmpty(value)) {
                        value = defaultValue;
                    }
                } else if (object instanceof Bundle) {
                    Bundle bundle = (Bundle) object;
                    value = bundle.containsKey(key) ? bundle.getString(key) : defaultValue;
                } else if (object instanceof HashMap) {
                    HashMap hashMap = ((HashMap) object);
                    value = hashMap.containsKey(key) ? String.valueOf(hashMap.get(key)) : defaultValue;
                } else if (object instanceof Map) {
                    Map map = ((Map) object);
                    value = map.containsKey(key) ? String.valueOf(map.get(key)) : defaultValue;
                } else if (object instanceof ContentValues) {
                    ContentValues contentValues = ((ContentValues) object);
                    value = contentValues.containsKey(key) ? contentValues.getAsString(key) : defaultValue;
                } else if (object instanceof JSONObject) {
                    JSONObject jsonObject = ((JSONObject) object);
                    value = jsonObject.isNull(key) ? defaultValue : jsonObject.getString(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(ObjectValueUtil.class, "getString: " + e.toString());
        }
        return value;
    }

    public static boolean getBoolean(Object object, String key) {
        boolean value = false;
        try {
            if (object != null && !TextUtils.isEmpty(key)) {
                if (object instanceof Intent) {
                    value = ((Intent) object).getBooleanExtra(key, false);
                } else if (object instanceof Bundle) {
                    Bundle bundle = (Bundle) object;
                    value = bundle.containsKey(key) ? bundle.getBoolean(key) : false;
                } else if (object instanceof HashMap) {
                    HashMap hashMap = ((HashMap) object);
                    value = (hashMap.containsKey(key) && hashMap.get(key) instanceof Boolean) ? (Boolean) hashMap.get(key) : false;
                } else if (object instanceof Map) {
                    Map map = ((Map) object);
                    value = (map.containsKey(key) && map.get(key) instanceof Boolean) ? (Boolean) map.get(key) : false;
                } else if (object instanceof ContentValues) {
                    ContentValues contentValues = ((ContentValues) object);
                    value = contentValues.containsKey(key) ? contentValues.getAsBoolean(key) : false;
                } else if (object instanceof JSONObject) {
                    JSONObject jsonObject = ((JSONObject) object);
                    value = jsonObject.isNull(key) ? false : jsonObject.getBoolean(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(ObjectValueUtil.class, "getBoolean: " + e.toString());
        }
        return value;
    }

    public static boolean getBoolean(Object object, String key, boolean defaultValue) {
        boolean value = defaultValue;
        try {
            if (object != null && !TextUtils.isEmpty(key)) {
                if (object instanceof Intent) {
                    value = ((Intent) object).getBooleanExtra(key, defaultValue);
                } else if (object instanceof Bundle) {
                    Bundle bundle = (Bundle) object;
                    value = bundle.containsKey(key) ? bundle.getBoolean(key) : defaultValue;
                } else if (object instanceof HashMap) {
                    HashMap hashMap = ((HashMap) object);
                    value = (hashMap.containsKey(key) && hashMap.get(key) instanceof Boolean) ? (Boolean) hashMap.get(key) : defaultValue;
                } else if (object instanceof Map) {
                    Map map = ((Map) object);
                    value = (map.containsKey(key) && map.get(key) instanceof Boolean) ? (Boolean) map.get(key) : defaultValue;
                } else if (object instanceof ContentValues) {
                    ContentValues contentValues = ((ContentValues) object);
                    value = contentValues.containsKey(key) ? contentValues.getAsBoolean(key) : defaultValue;
                } else if (object instanceof JSONObject) {
                    JSONObject jsonObject = ((JSONObject) object);
                    value = jsonObject.isNull(key) ? defaultValue : jsonObject.getBoolean(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(ObjectValueUtil.class, "getBoolean: " + e.toString());
        }
        return value;
    }

    public static int getInt(Object object, String key) {
        int value = -1;
        try {
            if (object != null && !TextUtils.isEmpty(key)) {
                if (object instanceof Intent) {
                    value = ((Intent) object).getIntExtra(key, value);
                } else if (object instanceof Bundle) {
                    Bundle bundle = (Bundle) object;
                    value = bundle.containsKey(key) ? bundle.getInt(key) : value;
                } else if (object instanceof HashMap) {
                    HashMap hashMap = ((HashMap) object);
                    value = (hashMap.containsKey(key) && hashMap.get(key) instanceof Integer) ? (Integer) hashMap.get(key) : value;
                } else if (object instanceof Map) {
                    Map map = ((Map) object);
                    value = (map.containsKey(key) && map.get(key) instanceof Integer) ? (Integer) map.get(key) : value;
                } else if (object instanceof ContentValues) {
                    ContentValues contentValues = ((ContentValues) object);
                    value = contentValues.containsKey(key) ? contentValues.getAsInteger(key) : value;
                } else if (object instanceof JSONObject) {
                    JSONObject jsonObject = ((JSONObject) object);
                    value = jsonObject.isNull(key) ? value : jsonObject.getInt(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(ObjectValueUtil.class, "getInt: " + e.toString());
        }
        return value;
    }

    public static int getInt(Object object, String key, int defaultValue) {
        int value = defaultValue;
        try {
            if (object != null && !TextUtils.isEmpty(key)) {
                if (object instanceof Intent) {
                    value = ((Intent) object).getIntExtra(key, defaultValue);
                } else if (object instanceof Bundle) {
                    Bundle bundle = (Bundle) object;
                    value = bundle.containsKey(key) ? bundle.getInt(key) : defaultValue;
                } else if (object instanceof HashMap) {
                    HashMap hashMap = ((HashMap) object);
                    value = (hashMap.containsKey(key) && hashMap.get(key) instanceof Integer) ? (Integer) hashMap.get(key) : defaultValue;
                } else if (object instanceof Map) {
                    Map map = ((Map) object);
                    value = (map.containsKey(key) && map.get(key) instanceof Integer) ? (Integer) map.get(key) : defaultValue;
                } else if (object instanceof ContentValues) {
                    ContentValues contentValues = ((ContentValues) object);
                    value = contentValues.containsKey(key) ? contentValues.getAsInteger(key) : defaultValue;
                } else if (object instanceof JSONObject) {
                    JSONObject jsonObject = ((JSONObject) object);
                    value = jsonObject.isNull(key) ? defaultValue : jsonObject.getInt(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(ObjectValueUtil.class, "getInt: " + e.toString());
        }
        return value;
    }

    public static double getDouble(Object object, String key) {
        double value = -1;
        try {
            if (object != null && !TextUtils.isEmpty(key)) {
                if (object instanceof Intent) {
                    value = ((Intent) object).getDoubleExtra(key, value);
                } else if (object instanceof Bundle) {
                    Bundle bundle = (Bundle) object;
                    value = bundle.containsKey(key) ? bundle.getDouble(key) : value;
                } else if (object instanceof HashMap) {
                    HashMap hashMap = ((HashMap) object);
                    value = (hashMap.containsKey(key) && hashMap.get(key) instanceof Double) ? (Double) hashMap.get(key) : value;
                } else if (object instanceof Map) {
                    Map map = ((Map) object);
                    value = (map.containsKey(key) && map.get(key) instanceof Double) ? (Double) map.get(key) : value;
                } else if (object instanceof ContentValues) {
                    ContentValues contentValues = ((ContentValues) object);
                    value = contentValues.containsKey(key) ? contentValues.getAsDouble(key) : value;
                } else if (object instanceof JSONObject) {
                    JSONObject jsonObject = ((JSONObject) object);
                    value = jsonObject.isNull(key) ? value : jsonObject.getDouble(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(ObjectValueUtil.class, "getDouble: " + e.toString());
        }
        return value;
    }

    public static double getDouble(Object object, String key, double defaultValue) {
        double value = defaultValue;
        try {
            if (object != null && !TextUtils.isEmpty(key)) {
                if (object instanceof Intent) {
                    value = ((Intent) object).getDoubleExtra(key, value);
                } else if (object instanceof Bundle) {
                    Bundle bundle = (Bundle) object;
                    value = bundle.containsKey(key) ? bundle.getDouble(key) : value;
                } else if (object instanceof HashMap) {
                    HashMap hashMap = ((HashMap) object);
                    value = (hashMap.containsKey(key) && hashMap.get(key) instanceof Double) ? (Double) hashMap.get(key) : value;
                } else if (object instanceof Map) {
                    Map map = ((Map) object);
                    value = (map.containsKey(key) && map.get(key) instanceof Double) ? (Double) map.get(key) : value;
                } else if (object instanceof ContentValues) {
                    ContentValues contentValues = ((ContentValues) object);
                    value = contentValues.containsKey(key) ? contentValues.getAsDouble(key) : value;
                } else if (object instanceof JSONObject) {
                    JSONObject jsonObject = ((JSONObject) object);
                    value = jsonObject.isNull(key) ? value : jsonObject.getDouble(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(ObjectValueUtil.class, "getDouble: " + e.toString());
        }
        return value;
    }

    public static long getLong(Object object, String key) {
        long value = -1;
        try {
            if (object != null && !TextUtils.isEmpty(key)) {
                if (object instanceof Intent) {
                    value = ((Intent) object).getLongExtra(key, value);
                } else if (object instanceof Bundle) {
                    Bundle bundle = (Bundle) object;
                    value = bundle.containsKey(key) ? bundle.getLong(key) : value;
                } else if (object instanceof HashMap) {
                    HashMap hashMap = ((HashMap) object);
                    value = (hashMap.containsKey(key) && hashMap.get(key) instanceof Long) ? (Long) hashMap.get(key) : value;
                } else if (object instanceof Map) {
                    Map map = ((Map) object);
                    value = (map.containsKey(key) && map.get(key) instanceof Long) ? (Long) map.get(key) : value;
                } else if (object instanceof ContentValues) {
                    ContentValues contentValues = ((ContentValues) object);
                    value = contentValues.containsKey(key) ? contentValues.getAsLong(key) : value;
                } else if (object instanceof JSONObject) {
                    JSONObject jsonObject = ((JSONObject) object);
                    value = jsonObject.isNull(key) ? value : jsonObject.getLong(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(ObjectValueUtil.class, "getLong: " + e.toString());
        }
        return value;
    }

    public static long getLong(Object object, String key, long defaultValue) {
        long value = defaultValue;
        try {
            if (object != null && !TextUtils.isEmpty(key)) {
                if (object instanceof Intent) {
                    value = ((Intent) object).getLongExtra(key, value);
                } else if (object instanceof Bundle) {
                    Bundle bundle = (Bundle) object;
                    value = bundle.containsKey(key) ? bundle.getLong(key) : value;
                } else if (object instanceof HashMap) {
                    HashMap hashMap = ((HashMap) object);
                    value = (hashMap.containsKey(key) && hashMap.get(key) instanceof Long) ? (Long) hashMap.get(key) : value;
                } else if (object instanceof Map) {
                    Map map = ((Map) object);
                    value = (map.containsKey(key) && map.get(key) instanceof Long) ? (Long) map.get(key) : value;
                } else if (object instanceof ContentValues) {
                    ContentValues contentValues = ((ContentValues) object);
                    value = contentValues.containsKey(key) ? contentValues.getAsLong(key) : value;
                } else if (object instanceof JSONObject) {
                    JSONObject jsonObject = ((JSONObject) object);
                    value = jsonObject.isNull(key) ? value : jsonObject.getLong(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(ObjectValueUtil.class, "getLong: " + e.toString());
        }
        return value;
    }

}
