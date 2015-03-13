package com.raxdenstudios.commons.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
		
	public static boolean isNetworkAvailable(Context context) {
		return isNetworkAvailable((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
	}
	
	public static boolean isNetworkAvailable(ConnectivityManager cm) {
		return 	(cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting()) ||
				(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting()) ||
				(cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET) != null && cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET).isConnectedOrConnecting());
	}
	
	public static boolean isWifiAvailable(Context context) {
		boolean isWifiAvailable = false;
		NetworkInfo networkInfo = getNetworkInfo(context);
		if (networkInfo != null) {
			isWifiAvailable = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
		}
		return isWifiAvailable;
	}
	
	public static NetworkInfo getNetworkInfo(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}
	
}
