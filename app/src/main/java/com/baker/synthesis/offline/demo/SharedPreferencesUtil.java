package com.baker.synthesis.offline.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.UUID;

/**
 * Create by hsj55
 * 2020/4/23
 */
public class SharedPreferencesUtil {
    //保存在手机里面的文件名
    private static final String FILE_NAME = "bakerMouldInfo";
    private static final String FIELD_NAME_QUERY_ID = "query_id";
    private static final String FIELD_NAME_CLIENT_ID = "client_id";
    private static final String FIELD_NAME_CLIENT_SECRET = "client_secret";

    /**
     * 将mould存在手机本地，方便体验自己的声音模型。
     */
    public static String getQueryId() {
        SharedPreferences sp = BakerApplication.getInstance()
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String queryId = sp.getString(FIELD_NAME_QUERY_ID, null);
        if (TextUtils.isEmpty(queryId)) {
            queryId = UUID.randomUUID().toString();
            sp.edit().putString(FIELD_NAME_QUERY_ID, queryId).apply();
            return queryId;
        } else {
            return queryId;
        }
    }

    public static String getClientId() {
        return BakerApplication.getInstance()
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .getString(FIELD_NAME_CLIENT_ID, null);
    }

    public static String getClientSecret() {
        return BakerApplication.getInstance()
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .getString(FIELD_NAME_CLIENT_SECRET, null);
    }

    public static void saveClientId(String clientId) {
        if (!TextUtils.isEmpty(clientId)) {
            SharedPreferences sp = BakerApplication.getInstance()
                    .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            sp.edit().putString(FIELD_NAME_CLIENT_ID, clientId).apply();
        }
    }

    public static void saveClientSecret(String clientId) {
        if (!TextUtils.isEmpty(clientId)) {
            SharedPreferences sp = BakerApplication.getInstance()
                    .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            sp.edit().putString(FIELD_NAME_CLIENT_SECRET, clientId).apply();
        }
    }
}
