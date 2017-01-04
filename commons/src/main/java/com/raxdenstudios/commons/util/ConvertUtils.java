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

public class ConvertUtils {

	public static int longToInt(long value) {
	    if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException (value + " cannot be cast to int without changing its value.");
	    }
	    return (int) value;
	}
	
}
