package com.raxdenstudios.commons.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author agomez
 *
 *  <?xml version="1.0" encoding="utf-8"?>
 *	<manifest xmlns:android="http://schemas.android.com/apk/res/android"...>
 *    <uses-sdk android:minSdkVersion="14" />
 *    <uses-permission android:name="android.permission.READ_CALENDAR" />
 *    <uses-permission android:name="android.permission.WRITE_CALENDAR" />
 *	    ...
 *	</manifest>
 */
public class CalendarUtils {

    private static final String TAG = CalendarUtils.class.getSimpleName();
	
	public static class CalendarEvent {
		public long calendarId;
		public String title;
		public String description;
		public String location;
		public String timeZone;
		public long eventStart;
		public long eventEnd;
		public boolean alarm;
		private List<CalendarReminder> reminders = new ArrayList<CalendarReminder>();

		public CalendarEvent(Context context, long eventStart, long eventEnd) {
			this(getCalendarId(context), eventStart, eventEnd);
		}
		
		private CalendarEvent(long calendarId, long eventStart, long eventEnd) {
			this.calendarId = calendarId;
			this.eventStart = eventStart;
			this.eventEnd = eventEnd;
		}
				
		public ContentValues getContentValues() {
			ContentValues values = new ContentValues();
			values.put("calendar_id", calendarId);
			values.put("title", Utils.hasValue(title) ? title.replaceAll("'", "") : "");
			values.put("description", Utils.hasValue(description) ? description.replaceAll("'", "") : "");
			values.put("eventLocation", Utils.hasValue(location) ? location : "");
			values.put("eventTimezone", Utils.hasValue(timeZone) ? timeZone : Calendar.getInstance().getTimeZone().getDisplayName());
			values.put("dtstart", eventStart);
			values.put("dtend", eventEnd);
			values.put("hasAlarm", alarm ? 1 : 0);
			return values;
		}
		
		public void addReminders(CalendarReminder reminder) {
			reminder.calendarId = calendarId;
			reminders.add(reminder);
		}
	}

	public static class CalendarReminder {
		private long calendarId;
		public int method;
		public int minutes;
		
		public CalendarReminder(int method, int minutes) {
			this.method = method;
			this.minutes = minutes;
		}
		
		public ContentValues getContentValues() {
			ContentValues values = new ContentValues();
            values.put("event_id", calendarId);
            values.put("method", method);
            values.put("minutes", minutes);
            return values;
		}
	}
	
	public static final boolean saveCalendarEvent(Context context, CalendarEvent calendarEvent) {
		boolean operation = false;
		
		if (calendarEvent != null) {
			
			Uri event = context.getContentResolver().insert(getEventsContentUri(), calendarEvent.getContentValues());
			
			if (event != null) {
				
				// get the event ID that is the last element in the Uri
				long eventID = Long.parseLong(event.getLastPathSegment());
				
				for (CalendarReminder reminder : calendarEvent.reminders) {
					context.getContentResolver().insert(getRemindersContentUri(), reminder.getContentValues());
				}				
				operation = true;
			}
		}

		return operation;
	}
	
	@SuppressLint("NewApi")
	public static final long getCalendarId(Context context) {
		long calId = 0;
		
		Uri calendars;
		if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			calendars = CalendarContract.Calendars.CONTENT_URI;
		}else{
			calendars = Uri.parse(getContentUri()+"/calendars");
		}
				
		Cursor managedCursor = context.getContentResolver().query(calendars, new String[] { "_id", "name" }, null, null, null);
		if(managedCursor.moveToFirst()){
			calId = managedCursor.getLong(managedCursor.getColumnIndex("_id")); 
		}
		managedCursor.close();
		
		return calId;
	}
	
	@SuppressLint("NewApi")
	private static String getContentUri(){
		String AUTHORITY = "";
		if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO){
			AUTHORITY = "com.android.calendar";
		}else{
			AUTHORITY = "calendar";
		}
		return "content://"+AUTHORITY;
	}
	
	@SuppressLint("NewApi")
	private static Uri getEventsContentUri(){
		if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			return CalendarContract.Events.CONTENT_URI;
		}else{
			return Uri.parse(getContentUri()+"/events");
		}
	}
	
	@SuppressLint("NewApi")
	private static Uri getRemindersContentUri(){
		if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			return CalendarContract.Reminders.CONTENT_URI;
		}else{
			return Uri.parse(getContentUri()+"/reminders");
		}
	}		
}