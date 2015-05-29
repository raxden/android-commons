package com.raxdenstudios.commons.util;

import android.os.Build;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Angel Gomez
 */
public class HttpClientUtils {

    private static final String TAG = HttpClientUtils.class.getSimpleName();

    public static HttpEntity initHttpEntity(Object value) {
        HttpEntity entity = null;
        if (value != null && value instanceof JSONObject) {
            entity = initHttpEntity(value.toString());
        } else if (value != null && value instanceof JSONArray) {
            entity = initHttpEntity(value.toString());
        } else if (value != null && value instanceof List && !((List)value).isEmpty() && ((List)value).get(0) instanceof BasicNameValuePair) {
            entity = initHttpEntity((List<BasicNameValuePair>)value);
        }
        return entity;
    }

    public static HttpEntity initHttpEntity(List<BasicNameValuePair> httpParams) {
        UrlEncodedFormEntity entity = null;
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(httpParams.size());
            for (BasicNameValuePair bnvp : httpParams) {
                Log.d(TAG, bnvp.getName() + ":" + bnvp.getValue());
                nameValuePairs.add(bnvp);
            }
            entity = new UrlEncodedFormEntity(nameValuePairs);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return entity;
    }

    public static HttpEntity initHttpEntity(String bodyText) {
        HttpEntity entity = null;
        try {
            if (Utils.hasValue(bodyText)) {
                entity = new StringEntity(bodyText);
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "cannot create String entity", e);
        }
        return entity;
    }

    /**
     * Workaround for bug pre-Froyo, see here for more info:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     */
    public static void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

}