package com.raxdenstudios.commons.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;

/**
 *
 * @author Angel Gomez
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
    	File cacheFile = null;
    	if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !isExternalStorageRemovable()) {
    		cacheFile = getExternalCacheDir(context, uniqueName);
    	} else {
    		cacheFile = getInternalCacheDir(context, uniqueName);
    	}
        Log.d(TAG, "diskCacheDir: " + cacheFile.getPath());
        return cacheFile;
    }	
	
    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    @TargetApi(8)
    public static File getExternalCacheDir(Context context) {
        File file = null;
	    	
    	if (Utils.hasFroyo()) {
    		file = context.getExternalCacheDir();
        }
	
    	if (file == null) {
    		// Before Froyo we need to construct the external cache dir ourselves
    		final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        	Log.d(TAG, "cacheDir: " + Environment.getExternalStorageDirectory().getPath() + cacheDir);
        	file = new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    	}
    	
        return file;
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
     * Check how much usable space is available at a given path.
     *
     * @param path The path to check
     * @return The space available in bytes
     */
    @TargetApi(9)
    public static long getUsableSpace(File path) {
        if (Utils.hasGingerbread()) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }	
	
    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     *         otherwise.
     */
    @TargetApi(9)
    public static boolean isExternalStorageRemovable() {
        if (Utils.hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }   
    
}
