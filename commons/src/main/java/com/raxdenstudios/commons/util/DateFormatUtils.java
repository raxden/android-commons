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

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A class that allows you parse any date easy and quickly.
 *
 */
public class DateFormatUtils {

    private static final String TAG = DateFormatUtils.class.getSimpleName();

	public static final DateFormat RSS_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
	public static final DateFormat YOUTUBE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

	/**
	 * Converts miliseconds parameter in a friendly date.
	 *
	 * @param dateInMilliseconds date in miliseconds
	 * @return friendly date
	 */
	public static String dateToString(long dateInMilliseconds) {
		return dateToString(new Date(dateInMilliseconds));
	}

	/**
	 * Converts miliseconds parameter in a friendly date through of the format date.
	 *
	 * @param dateInMilliseconds date in milliseconds
	 * @param dateFormat format date
	 * @return friendly date
	 */
	public static String dateToString(long dateInMilliseconds, String dateFormat) {
		if(Utils.hasValue(dateFormat)){
			DateFormat df =  new SimpleDateFormat(dateFormat, Locale.getDefault());
			return dateToString(new Date(dateInMilliseconds), df);
		}else{
			return dateToString(new Date(dateInMilliseconds));
		}
	}

	/**
	 * Converts miliseconds parameter in a friendly date through of the format date.
	 *
	 * @param dateInMilliseconds date in milliseconds
	 * @param dateFormat format date
	 * @return friendly date
	 */
	public static String dateToString(long dateInMilliseconds, DateFormat dateFormat) {
		return dateFormat.format(new Date(dateInMilliseconds));
	}

	/**
	 * Converts date parameter in a friendly date.
	 *
	 * @param date date
	 * @return friendly date
	 */
	public static String dateToString(Date date) {
		return dateToString(date, RSS_FORMAT);
	}

	/**
	 * Converts date parameter in a friendly date through of the format date.
	 *
	 * @param date date
	 * @param dateFormat format date
	 * @return friendly date
	 */
	public static String dateToString(Date date, String dateFormat) {
		if(Utils.hasValue(dateFormat)){
			DateFormat df =  new SimpleDateFormat(dateFormat, Locale.getDefault());
			return dateToString(date, df);
		}else{
			return dateToString(date);
		}
	}

	/**
	 * Converts date parameter in a friendly date through of the format date.
	 *
	 * @param date date
	 * @param dateFormat format date
	 * @return friendly date
	 */
	public static String dateToString(Date date, DateFormat dateFormat) {
		return dateFormat.format(date);
	}

	/**
	 * Converts friendly date in a Date object through of the format date.
	 * @param date date
	 * @param dateFormat format date
	 * @return Date object
	 */
	public static Date stringToDate(String date, String dateFormat) {
		DateFormat df = null;
		try {
			df = new SimpleDateFormat(dateFormat, Locale.getDefault());
		} catch (NullPointerException e){
			Log.e(TAG, e.getMessage(), e);
		} catch (IllegalArgumentException e){
			Log.e(TAG, e.getMessage(), e);
		} finally {
			if (df == null) {
				df = RSS_FORMAT;
			}
		}
		return stringToDate(date, df);
	}

	/**
	 * Converts friendly date in a Date object through of the format date.
	 * @param date date
	 * @param dateFormat format date
	 * @return Date object
	 */
	public static Date stringToDate(String date, DateFormat dateFormat) {
		Date dateOut = null;
		try {
			if(Utils.hasValue(date)) {
				dateOut = dateFormat.parse(date);
			}
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return dateOut;
	}

}
