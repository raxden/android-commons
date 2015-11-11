package com.raxdenstudios.commons.manager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.raxdenstudios.commons.util.StreamUtils;
import com.raxdenstudios.commons.util.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Properties;

public class ResourceManager {

    private static final String TAG = ResourceManager.class.getSimpleName();

    public enum ResourceType {DRAWABLE,PROPERTIES,OBJECT,STRING,STREAM};
		
	// ASSETS
	public static Object getAssets(Context context, String name, ResourceType resType) {
		
		if (context == null) return null;
		if (!Utils.hasValue(name)) return null;
		if (resType == null) return null;
		
		Object object = null;
		InputStream is = null;
		
		try {
			is = context.getAssets().open(name);
			if (is != null) {
				switch (resType) {
				case DRAWABLE:
					object = Drawable.createFromStream(is, name);
					break;
				case OBJECT:
					ObjectInputStream ois = null;
					try {
						ois = new ObjectInputStream(is);
						object = ois.readObject();
					} catch (IOException e) {
						Log.e(TAG, e.getMessage());
					} catch (ClassNotFoundException e) {
						Log.e(TAG, e.getMessage());
					}			
					break;
				case STRING:
					object = StreamUtils.readContent(is);
					break;
				case PROPERTIES: 
					object = new Properties();
					((Properties)object).load(is);
					break;
				case STREAM:
					object = is;
					break;
				}
			}
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} finally {
            try {
                if (is != null) {
                	is.close();
                }
            } catch (IOException e) {}
		}

		return object;
	}	
}
