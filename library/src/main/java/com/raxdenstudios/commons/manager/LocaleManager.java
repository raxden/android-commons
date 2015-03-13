package com.raxdenstudios.commons.manager;

import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;

import com.raxdenstudios.commons.util.Utils;

public class LocaleManager {
	
    public static final String DEFAULT_LANGUAGE = "en_us";
    public static final String APP_LANGUAGE = "app_language";	
	
	private Context context;
	private Locale appLocale;
	
	public LocaleManager(Context context) {
		this.context = context;
	}
	
	public void initLocalization() {
		String appLanguage = getApplicationLanguage();
		if (Utils.hasValue(appLanguage)) {
			Locale appLocale = new Locale(appLanguage);
            Locale.setDefault(appLocale);
            Configuration config = new Configuration();
            config.locale = appLocale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
		} else {
			setApplicationLanguage(Locale.getDefault().toString());
		}
	}	
	
	public Locale getApplicationLocale() {
		SharedPreferences settings = getLocalePreferences(context);
		String language = settings.getString(APP_LANGUAGE, "");
		
		if (appLocale == null) {
			appLocale = new Locale(language);
		}
		
		return appLocale;
	}
	
	public String getApplicationLanguage() {
		SharedPreferences settings = getLocalePreferences(context);
		String language = settings.getString(APP_LANGUAGE, "");
		
		if (appLocale == null) {
			appLocale = new Locale(language);
		}
		
		return language;
	}
	
	public void setApplicationLocale(Locale locale) {
		SharedPreferences settings = getLocalePreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(APP_LANGUAGE, locale.toString());
		editor.commit();
		
		appLocale = locale;
	}
	
	public void setApplicationLanguage(String language) {
		SharedPreferences settings = getLocalePreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(APP_LANGUAGE, language);
		editor.commit();
		
		appLocale = new Locale(language);
	}
		
	private SharedPreferences getLocalePreferences(Context context) {
	    return ((FragmentActivity)context).getSharedPreferences(Utils.getPackageName(context), Context.MODE_PRIVATE);
	}
	
}
