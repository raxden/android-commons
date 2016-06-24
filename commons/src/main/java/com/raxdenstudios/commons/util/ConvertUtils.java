package com.raxdenstudios.commons.util;

/**
 *
 * @author Angel Gomez
 */
public class ConvertUtils {

	public static int longToInt(long value) {
	    if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException (value + " cannot be cast to int without changing its value.");
	    }
	    return (int) value;
	}
	
}
