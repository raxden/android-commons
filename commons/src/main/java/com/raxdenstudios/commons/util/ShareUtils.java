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
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 *
 */
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

    public static void shareViaEmail(Context context, String to) {
        shareViaEmail(context, null, to);
    }

    public static void shareViaEmail(Context context, String titleChooser, String to) {
        shareViaEmail(context, titleChooser, Uri.fromParts("mailto", to, null));
    }

    public static void shareViaEmail(Context context, String titleChooser, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        context.startActivity(titleChooser != null ? Intent.createChooser(intent, titleChooser) : intent);
    }

    public static void share(Context context, ShareContainer toShare) {
        share(context, null, toShare);
    }

	public static void share(Context context, String titleChooser, ShareContainer toShare) {
		Intent intent = new Intent(Intent.ACTION_SEND);

		if (toShare != null) {
			intent.setType(toShare.mediaType != null ? toShare.mediaType.toString() : MediaType.TEXT_PLAIN.toString());
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

        context.startActivity(titleChooser != null ? Intent.createChooser(intent, titleChooser) : intent);
	}

}
