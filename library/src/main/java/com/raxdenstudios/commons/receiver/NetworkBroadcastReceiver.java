package com.raxdenstudios.commons.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.raxdenstudios.commons.util.NetworkUtils;

public class NetworkBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = NetworkBroadcastReceiver.class.getSimpleName();

	public interface OnNetworkListener {
		public void onWifiAvailable(boolean available);
		public void onNetworkAvailable(boolean available);
	}

	private OnNetworkListener networkListener;
	
	public NetworkBroadcastReceiver(OnNetworkListener networkListener) {
		super();
		this.networkListener = networkListener;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive");

		if (networkListener != null) {
			networkListener.onNetworkAvailable(NetworkUtils.isNetworkAvailable(context));
			networkListener.onWifiAvailable(NetworkUtils.isWifiAvailable(context));
		}
	}

}
