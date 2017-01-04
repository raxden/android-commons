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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *
 */
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
