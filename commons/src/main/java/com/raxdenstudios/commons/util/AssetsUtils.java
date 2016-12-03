package com.raxdenstudios.commons.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Properties;

/**
 * Created by Raxden on 03/12/2016.
 */

public class AssetsUtils {

    private static final String TAG = AssetsUtils.class.getSimpleName();

    public static Drawable getDrawable(Context context, String name) {
        Drawable drawable = null;
        InputStream is = null;
        try {
            is = context.getAssets().open(name);
            drawable = Drawable.createFromStream(is, name);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            StreamUtils.closeInputStream(is);
        }
        return drawable;
    }

    public static String getString(Context context, String name) {
        String value = null;
        InputStream is = null;
        try {
            is = context.getAssets().open(name);
            value = StreamUtils.readContent(is);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            StreamUtils.closeInputStream(is);
        }
        return value;
    }

    public static Properties getProperties(Context context, String name) {
        Properties properties = null;
        InputStream is = null;
        try {
            is = context.getAssets().open(name);
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            StreamUtils.closeInputStream(is);
        }
        return properties;
    }

    public static <T> T getData(Context context, String name) {
        T data = null;
        InputStream is = null;
        ObjectInputStream ois = null;
        try {
            is = context.getAssets().open(name);
            if (is != null) {
                ois = new ObjectInputStream(is);
                try {
                    data = (T)ois.readObject();
                } catch (ClassNotFoundException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            StreamUtils.closeInputStream(ois);
            StreamUtils.closeInputStream(is);
        }
        return data;
    }

}
