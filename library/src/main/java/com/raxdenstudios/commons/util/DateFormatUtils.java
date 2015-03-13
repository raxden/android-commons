package com.raxdenstudios.commons.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatUtils {

    private static final String TAG = DateFormatUtils.class.getSimpleName();
	
	public static final DateFormat DEFAULT_RSS = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
	public static final DateFormat YOUTUBE_RSS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());	
	
	public static String dateToString(long time) {
		return dateToString(new Date(time));
	}
	
	public static String dateToString(long time, String dateFormat) {
		if(Utils.hasValue(dateFormat)){
			DateFormat df =  new SimpleDateFormat(dateFormat, Locale.getDefault());
			return dateToString(new Date(time), df);
		}else{
			return dateToString(new Date(time));
		}
	}
	
	public static String dateToString(long time, DateFormat sdf) {
		return sdf.format(new Date(time));
	}
	
	public static String dateToString(Date date) {
		return dateToString(date, DEFAULT_RSS);
	}
	
	public static String dateToString(Date date, String dateFormat) {
		if(Utils.hasValue(dateFormat)){
			DateFormat df =  new SimpleDateFormat(dateFormat, Locale.getDefault());
			return dateToString(date, df);
		}else{
			return dateToString(date);
		}
	}	
	
	public static String dateToString(Date date, DateFormat sdf) {
		return sdf.format(date);
	}
	
	public static Date stringToDate(String dateString) {
		return stringToDate(dateString, DEFAULT_RSS);
	}
	
	public static Date stringToDate(String dateString, String dateFormat) {
		DateFormat df = null;
		try {
			df = new SimpleDateFormat(dateFormat, Locale.getDefault());
		} catch (NullPointerException e){
			Log.e(TAG, e.getMessage(), e);
		} catch (IllegalArgumentException e){
			Log.e(TAG, e.getMessage(), e);
		} finally {
			if (df == null) {
				df = DEFAULT_RSS;
			}
		}
		return stringToDate(dateString,df);
	}
	
	public static Date stringToDate(String dateString, DateFormat dateFormat) {
		Date dateOut = null;
		try {
			if(Utils.hasValue(dateString)) {
				dateOut = dateFormat.parse(dateString);
			}
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return dateOut;
	}		
}
