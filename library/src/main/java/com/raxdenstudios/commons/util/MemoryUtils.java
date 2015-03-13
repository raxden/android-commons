package com.raxdenstudios.commons.util;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MemoryUtils {

    private static final String TAG = MemoryUtils.class.getSimpleName();
	
	public static final int sizeOf(Object object) {
		if (object == null)
			return -1;

		// Special output stream use to write the content of an output stream to an internal byte array.
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		// Output stream that can write object
		ObjectOutputStream oos = null;
		
		try {
			oos = new ObjectOutputStream(byteArrayOutputStream);
			oos.writeObject(object);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		} finally {
			if (oos != null) {
				try {
					oos.flush();
					oos.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		}
				    
		// Get the byte array
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		return byteArray == null ? 0 : byteArray.length;
	}
}
