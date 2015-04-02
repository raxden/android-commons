package com.raxdenstudios.commons.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;


public class ShareUtils {

    private static final String TAG = ShareUtils.class.getSimpleName();
	
	public static class ShareContainer {
		public String subject;
		public String text;
		public Uri stream;
		public String[] mails;
		public MediaType mediaType;
		
		public ShareContainer() {}
		
		public ShareContainer(String subject) {
			this(subject, null);
		}				
		
		public ShareContainer(String subject, String text) {
			this(subject, text, MediaType.TEXT_PLAIN);
		}		
		
		public ShareContainer(String subject, String text, MediaType mediaType) {
			this(subject, text, null, mediaType);
		}
		
		public ShareContainer(String subject, String text, Uri stream, MediaType mediaType) {
			this.subject = subject;
			this.text = text;
			this.stream = stream;
			this.mediaType = mediaType;
		}
	}

	public static final void share(Context context, String titleChooser, ShareContainer toShare) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType(toShare.mediaType != null ? toShare.mediaType.toString() : MediaType.TEXT_PLAIN.toString());
		
		if (toShare != null) {
			if (Utils.hasValue(toShare.subject))
				intent.putExtra(Intent.EXTRA_SUBJECT, toShare.subject);
			if (Utils.hasValue(toShare.text))
				intent.putExtra(Intent.EXTRA_TEXT, toShare.text);
			if (Utils.hasValue(toShare.stream))
				intent.putExtra(Intent.EXTRA_STREAM, toShare.stream);
			if (Utils.hasValue(toShare.mails)) {
				intent.putExtra(Intent.EXTRA_EMAIL, toShare.mails);
			}
		}
		
		Log.d(TAG, "==============");
		Log.d(TAG, "title: "+ titleChooser);
		Log.d(TAG, "type:" +intent.getType());
		Log.d(TAG, "subject:" +intent.getExtras().getString(Intent.EXTRA_SUBJECT));
		Log.d(TAG, "text:" +intent.getExtras().getString(Intent.EXTRA_TEXT));
		Log.d(TAG, "==============");
		
		context.startActivity(Intent.createChooser(intent, titleChooser));
	}

}
