/*
 * Copyright 2014 Ángel Gómez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.raxdenstudios.commons.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 *  All Android devices have two file storage areas: "internal" and "external" storage.
 *  These names come from the early days of Android, when most devices offered built-in
 *  non-volatilememory (internal storage), plus a removable storage medium such as a micro SD card
 *  (external storage). Some devices divide the permanent storage space into "internal" and
 *  "external" partitions, so even without a removable storage medium, there are always two
 *  storage spaces and the API behavior is the same whether the external storage is removable
 *  or not. The following lists summarize the facts about each storage space.
 *
 *  ================= Internal storage =============================================================
 *
 *  Internal storage is best when you want to be sure that neither the user nor other apps can
 *  access your files.
 *
 *  - It's always available.
 *  - Files saved here are accessible by only your app.
 *  - When the user uninstalls your app, the system removes all your app's files from internal
 *    storage.
 *
 *  ================= External storage =============================================================
 *
 *  External storage is the best place for files that don't require access restrictions and for
 *  files that you want to share with other apps or allow the user to access with a computer. To
 *  write to the external storage, you must request the WRITE_EXTERNAL_STORAGE permission in
 *  your manifest file:
 *
 *  - It's not always available, because the user can mount the external storage as USB storage
 *  and in some cases remove it from the device.
 *  - It's world-readable, so files saved here may be read outside of your control.
 *  - When the user uninstalls your app, the system removes your app's files from here only if you
 *  save them in the directory from getExternalFilesDir().
 *
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir otherwise use internal cache dir
    	File cacheFile;
    	if (isExternalStorageWritable() || !isExternalStorageRemovable()) {
    		cacheFile = getExternalCacheDir(context, uniqueName);
    	} else {
    		cacheFile = getInternalCacheDir(context, uniqueName);
    	}
        Log.d(TAG, "diskCacheDir: " + cacheFile.getPath());
        return cacheFile;
    }

    // External Cache ==============================================================================

    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    public static File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir();
    }
    
    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The external cache dir
     */
    public static File getExternalCacheDir(Context context, String uniqueName) {
        return new File(getExternalCacheDir(context).getPath() + File.separator + uniqueName);
    }

    // Internal Cache ==============================================================================

    /**
     * Get the internal app cache directory.
     *
     * @param context The context to use
     * @return The internal cache dir
     */
    public static File getInternalCacheDir(Context context) {
    	return context.getCacheDir();
    }
    
    /**
     * Get the internal app cache directory.
     *
     * @param context The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The internal cache dir
     */
    public static File getInternalCacheDir(Context context, String uniqueName) {
        return new File(getInternalCacheDir(context).getPath() + File.separator + uniqueName);
    }

    /**
     * Get the internal app temporal cache directory.
     *
     * @param context The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The internal cache dir
     */
    public static File getTemporalCacheDir(Context context, String uniqueName) {
        File file = null;
        try {
            file = File.createTempFile(uniqueName, null, getInternalCacheDir(context));
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return file;
    }

    /**
     * Get the internal app temporal cache directory.
     *
     * @param context The context to use
     * @param uri A unique directory name to append to the cache dir
     * @return The internal cache dir
     */
    public static File getTemporalCacheDir(Context context, Uri uri) {
        return getTemporalCacheDir(context, uri.getLastPathSegment());
    }

    // Internal files ==============================================================================

    /**
     * Get the internal app directory.
     *
     * @param context The context to use
     * @return The internal file dir
     */
    public static File getInternalDir(Context context) {
        return context.getFilesDir();
    }

    /**
     * Get the internal app directory.
     *
     * @param context The context to use
     * @param uniqueName A unique directory name to append to the file dir
     * @return The internal file dir
     */
    public static File getInternalDir(Context context, String uniqueName) {
        return new File(getInternalDir(context), uniqueName);
    }

    // External files ==============================================================================

    /**
     * Get the public external app directory. Files that should be freely available to other apps
     * and to the user. When the user uninstalls your app, these files should remain available to
     * the user. For example, photos captured by your app or other downloaded files.
     *
     * @param type The type of storage directory to return. Should be one of
     *            {@link android.os.Environment#DIRECTORY_MUSIC},
     *            {@link android.os.Environment#DIRECTORY_PODCASTS},
     *            {@link android.os.Environment#DIRECTORY_RINGTONES},
     *            {@link android.os.Environment#DIRECTORY_ALARMS},
     *            {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *            {@link android.os.Environment#DIRECTORY_PICTURES},
     *            {@link android.os.Environment#DIRECTORY_MOVIES},
     *            {@link android.os.Environment#DIRECTORY_DOWNLOADS},
     *            {@link android.os.Environment#DIRECTORY_DCIM}, or
     *            {@link android.os.Environment#DIRECTORY_DOCUMENTS}. May not be null.
     * @return The internal file dir
     */
    public static File getExternalPublicDir(String type) {
        return Environment.getExternalStoragePublicDirectory(type);
    }

    /**
     * Get the public external app directory. Files that should be freely available to other apps
     * and to the user. When the user uninstalls your app, these files should remain available to
     * the user. For example, photos captured by your app or other downloaded files.
     *
     * @param type The type of storage directory to return. Should be one of
     *            {@link android.os.Environment#DIRECTORY_MUSIC},
     *            {@link android.os.Environment#DIRECTORY_PODCASTS},
     *            {@link android.os.Environment#DIRECTORY_RINGTONES},
     *            {@link android.os.Environment#DIRECTORY_ALARMS},
     *            {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *            {@link android.os.Environment#DIRECTORY_PICTURES},
     *            {@link android.os.Environment#DIRECTORY_MOVIES},
     *            {@link android.os.Environment#DIRECTORY_DOWNLOADS},
     *            {@link android.os.Environment#DIRECTORY_DCIM}, or
     *            {@link android.os.Environment#DIRECTORY_DOCUMENTS}. May not be null.
     * @param uniqueName A unique directory name to append to the file dir
     * @return The internal file dir
     */
    public static File getExternalPublicDir(String type, String uniqueName) {
        return new File(getExternalPublicDir(type), uniqueName);
    }

    /**
     * Get the private external app directory. Files that rightfully belong to your app and should
     * be deleted when the user uninstalls your app. Although these files are technically
     * accessible by the user and other apps because they are on the external storage, they are
     * files that realistically don't provide value to the user outside your app. When the user
     * uninstalls your app, the system deletes all files in your app's external private directory.
     *
     * @param context The context to use
     * @param type The type of storage directory to return. Should be one of
     *            {@link android.os.Environment#DIRECTORY_MUSIC},
     *            {@link android.os.Environment#DIRECTORY_PODCASTS},
     *            {@link android.os.Environment#DIRECTORY_RINGTONES},
     *            {@link android.os.Environment#DIRECTORY_ALARMS},
     *            {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *            {@link android.os.Environment#DIRECTORY_PICTURES},
     *            {@link android.os.Environment#DIRECTORY_MOVIES},
     *            {@link android.os.Environment#DIRECTORY_DOWNLOADS},
     *            {@link android.os.Environment#DIRECTORY_DCIM}, or
     *            {@link android.os.Environment#DIRECTORY_DOCUMENTS}. May not be null.
     * @return The internal file dir
     */
    public static File getExternalPrivateDir(Context context, String type) {
        return context.getExternalFilesDir(type);
    }

    /**
     * Get the private external app directory. Files that rightfully belong to your app and should
     * be deleted when the user uninstalls your app. Although these files are technically
     * accessible by the user and other apps because they are on the external storage, they are
     * files that realistically don't provide value to the user outside your app. When the user
     * uninstalls your app, the system deletes all files in your app's external private directory.
     *
     * @param context The context to use
     * @param type The type of storage directory to return. Should be one of
     *            {@link android.os.Environment#DIRECTORY_MUSIC},
     *            {@link android.os.Environment#DIRECTORY_PODCASTS},
     *            {@link android.os.Environment#DIRECTORY_RINGTONES},
     *            {@link android.os.Environment#DIRECTORY_ALARMS},
     *            {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *            {@link android.os.Environment#DIRECTORY_PICTURES},
     *            {@link android.os.Environment#DIRECTORY_MOVIES},
     *            {@link android.os.Environment#DIRECTORY_DOWNLOADS},
     *            {@link android.os.Environment#DIRECTORY_DCIM}, or
     *            {@link android.os.Environment#DIRECTORY_DOCUMENTS}. May not be null.
     * @param uniqueName A unique directory name to append to the file dir
     * @return The internal file dir
     */
    public static File getExternalPrivateDir(Context context, String type, String uniqueName) {
        return new File(getExternalPrivateDir(context, type), uniqueName);
    }

    // Utils methods ===============================================================================

    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     *         otherwise.
     */
    public static boolean isExternalStorageRemovable() {
        return Environment.isExternalStorageRemovable();
    }

    /**
     * Checks if external storage is available to at least read
     *
     * @return True if external storage is readable, false
     *         otherwise.
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if external storage is available for read and write
     *
     * @return True if external storage is writable, false
     *         otherwise.
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
