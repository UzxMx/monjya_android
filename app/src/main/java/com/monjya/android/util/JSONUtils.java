package com.monjya.android.util;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by xuemingxiang on 16-11-14.
 */

public class JSONUtils {

    private static final String TAG = "JSONUtils";

    private static ObjectMapper createObjectMapper(Class<?> clazz) {
        ObjectMapper mapper = new ObjectMapper();

        return mapper;
    }

    public static JSONObject toJSONObject(String json) {
        try {
            return new JSONObject(json);
        } catch (Throwable e) {
        }
        return null;
    }

    public static <T> T toObject(JSONObject json, Class<T> clazz) {
        if (json == null) {
            return null;
        }

        ObjectMapper mapper = createObjectMapper(clazz);
        try {
            return mapper.readValue(json.toString(), clazz);
        } catch (IOException e) {
            if (LogManager.isErrorEnabled()) {
                LogManager.e(TAG, Log.getStackTraceString(e));
            }
        }

        return null;
    }

    public static <T> T toObject(String str, Class<T> clazz) {
        return toObject(toJSONObject(str), clazz);
    }

    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (json == null)
            return null;

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, TypeFactory.defaultInstance()
                    .constructCollectionType(List.class, clazz));
        } catch (Throwable e) {
            if (LogManager.isErrorEnabled()) {
                LogManager.e(TAG, Log.getStackTraceString(e));
            }
        }

        return null;
    }

    public static String toJsonString(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject generateJSONObject(Object... list) {
        return fillJSONObject(new JSONObject(), list);
    }

    public static JSONObject fillJSONObject(JSONObject jsonObject, Object... list) {
        for (int i = 0; i < list.length; i += 2) {
            try {
                jsonObject.put(list[i].toString(), list[i + 1]);
            } catch (JSONException e) {
            }
        }
        return jsonObject;
    }
}
