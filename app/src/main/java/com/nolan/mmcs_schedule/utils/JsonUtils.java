package com.nolan.mmcs_schedule.utils;

import com.google.gson.Gson;

public class JsonUtils {
    private static Gson gson = new Gson();

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> String toJson(T object, Class<T> clazz) {
        return gson.toJson(object, clazz);
    }
}
