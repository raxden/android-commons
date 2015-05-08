package com.raxdenstudios.commons.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.raxdenstudios.commons.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

	public static boolean hasValue(Object value) {
		if (value == null) { 
			return false;
		} else if (value instanceof String) {
			return StringUtils.hasText((String)value) && (!"null".equals((String)value));
		} else if (value instanceof CharSequence) {
			return StringUtils.hasText((CharSequence)value) && (!"null".equals(((CharSequence)value).toString()));
		} else if (value instanceof Long) {
			if (Long.valueOf(value.toString()) == 0) return false;
        } else if (value instanceof Float) {
            if (Float.valueOf(value.toString()) == 0) return false;
        } else if (value instanceof Double) {
            if (Double.valueOf(value.toString()) == 0) return false;
		} else if (value instanceof Number) {
			if (Integer.valueOf(value.toString()) == 0) return false;
		} else if (value instanceof Map) {
			if (((Map)value).isEmpty()) return false;
		} else if (value instanceof List) {
			if (((List)value).isEmpty()) return false;
		} else if (value instanceof TextView) {
			if (((TextView)value).getText() == null) return false;
			if (((TextView)value).getText().toString() == null) return false;
			if (((TextView)value).getText().toString().equals("")) return false;
		}
		return true;
	}

	/**
	 * Close keyboard on screen.
	 * @param context
	 * @param binder Get binder from view -> view.getWindowToken()
	 */
	public static void closeKeyboard(Context context, IBinder binder) {
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(binder, 0);
	}
	
	public static String getMainGoogleEmailAccount(Context context) {
		String email = "";
		AccountManager accountManager = AccountManager.get(context);
		Account[] accounts = accountManager.getAccountsByType("com.google");
		for (Account a: accounts) {
			if (hasValue(a.name)) {
//		    if (a.name.contains("@gmail.com")) {
		    	email = a.name;
		    	break;
		    }
		}
		return email;
	}
	
	public static List<String> getGoogleEmailAccounts(Context context) {
		List<String> emails = new ArrayList<String>();
		AccountManager accountManager = AccountManager.get(context);
		Account[] accounts = accountManager.getAccountsByType("com.google");
		for (Account a: accounts) {
			if (hasValue(a.name)) {
//			if (a.name.contains("@gmail.com")) {
		    	emails.add(a.name);
		    }
		}
		return emails;
	}	
	
	/**
	 * A 64-bit number (as a hex string) that is randomly generated on the device's first boot and should remain 
	 * constant for the lifetime of the device. (The value may change if a factory reset is performed on the device.) 
	 * 
	 * @param context
	 * @return android_id
	 */
	public static String getSecureAndroidId(Context context) {
		return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
	}
	
	@SuppressWarnings("serial")
	public static final Map<Integer,String> dpiMap = new HashMap<Integer, String>() {
		{
			put(120, "l");
			put(160, "m");
			put(240, "h");
			put(320, "xh");
			put(480, "xxh");
			put(640, "xxxh");
		}
	};
	
	public static String getDensityKeyDisplay(Context context) {
		String dpi = null;
		int value = getDensityDisplay(context);
		if (dpiMap.containsKey(value)) {
			dpi = dpiMap.get(value);
		} else {
			int rest = 999;
			for (Entry<Integer, String> entry : dpiMap.entrySet()) {
				if (rest >= Math.abs(value - entry.getKey())) {
					rest = Math.abs(value - entry.getKey());
					dpi = entry.getValue();
				}
			}				
		}
		return dpi;
	}		
	
	public static int getDensityDisplay(Context context) {
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.densityDpi;
	}	
		
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public static boolean hasVirtualMenuKeys(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return !ViewConfiguration.get(context).hasPermanentMenuKey();
		} else {
			return false;
		}
	}
	
	public static String getDeviceModel() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return StringUtils.capitalize(model);
		} else {
			return StringUtils.capitalize(manufacturer) + " " + model;
		}
	}
	
	@SuppressLint("NewApi")
	public static int[] getScreenDimension(Context context) {
		int[] dimensions = new int[2];
		
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		
		if (hasHoneycombMR2()) {
			Point size = new Point();
			display.getSize(size);
			dimensions[0] = size.x;
			dimensions[1] = size.y;
		} else {
			dimensions[0] = display.getWidth();
			dimensions[1] = display.getHeight();
		}
		
		return dimensions;
	}
	
	public static boolean isTablet(Context context) {
	    return context.getResources().getBoolean(R.bool.isTablet);
	}
	
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }
    
    public static boolean hasHoneycombMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
    }    

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }
    
    public static boolean hasKitKat() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
    
    public static boolean hasNetworkProviderEnabled(Context context) {
    	return hasProviderEnabled(context, LocationManager.NETWORK_PROVIDER);
    }    
    
    public static boolean hasGPSProviderEnabled(Context context) {
    	return hasProviderEnabled(context, LocationManager.GPS_PROVIDER);
    }
    
    private static boolean hasProviderEnabled(Context context, String provider) {
    	boolean providerEnabled = false;
    	try {
	    	LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	    	if (lm != null) {
	    		providerEnabled = lm.isProviderEnabled(provider);
	    	}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
    	return providerEnabled;
    }
    
	public static boolean findTargetAppPackage(Context context, Intent intent, String appPackage) {
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> availableApps = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		if (availableApps != null) {
			if (contains(availableApps, appPackage)) {
				return true;
			}
		}
		return false;
	}
    
	private static boolean contains(Iterable<ResolveInfo> availableApps, String targetApp) {
		for (ResolveInfo availableApp : availableApps) {
			String packageName = availableApp.activityInfo.packageName;
			if (targetApp.equals(packageName)) {
				return true;
			}
		}
		return false;
	}	 
	
	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	public static boolean checkPlayServices(Context context) {

        final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

        if (context == null || !((context instanceof FragmentActivity) || (context instanceof Activity))) {
            throw new IllegalStateException("Context must be FragmentActivity instance.");
        }
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                if (context instanceof FragmentActivity) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, (FragmentActivity) context, PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } else if (context instanceof Activity) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context, PLAY_SERVICES_RESOLUTION_REQUEST).show();
                }
            } else {
                Log.i(TAG, "This device is not supported.");
                if (context instanceof FragmentActivity) {
                    ((FragmentActivity) context).finish();
                } else if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
            return false;
        }
        return true;
    }

    public static String getApplicationName(Context context) {
        return context.getString(context.getApplicationInfo().labelRes);
    }

	public static String getPackageName(Context context) {
		String packageName = "";
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			packageName = pInfo.packageName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return packageName;
	}
	
	public static String getVersionName(Context context) {
		String versionName = "";
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			versionName = pInfo.versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return versionName;
	}
	
	public static int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			versionCode = pInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return versionCode;
	}
}
