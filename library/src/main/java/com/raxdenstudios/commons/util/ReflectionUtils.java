package com.raxdenstudios.commons.util;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

    private static final String TAG = ReflectionUtils.class.getSimpleName();
	
	public static Field getDeclaredField(Object object, String fieldName) {
		Field field = null;
		try {
			field = object.getClass().getDeclaredField(fieldName);
		} catch (SecurityException e) {
			Log.e(TAG, e.getMessage());
		} catch (NoSuchFieldException e) {
			Log.w(TAG, "Field \"" + field + "\" not found for " + object.getClass().toString());
		}
		return field;
	}
	
	public static boolean classExists(String className){
		boolean result = true;
		try {
			Class.forName(className);
		} catch (ClassNotFoundException e) {
			result = false;
		}
		return result;
	}	
	
	public static Class<?> getClass(Context context, String className) {
		Class<?> classDefinition = null;
		if (className.contains(context.getPackageName())) {
			classDefinition = getClass(className);
		} else {
			classDefinition = getClass(context.getPackageName()+className);
		}
		return classDefinition;
	}
	
	public static Class<?> getClass(String className) {
		Class<?> classDefinition = null;
		try {
			classDefinition = Class.forName(className);
		} catch (ClassNotFoundException e) {
            Log.w(TAG, "Class \"" + className + "\" not found");
		}
		return classDefinition;
	}
	
	public static Method getMethod(Object object, String methodName, Object[] types) {
		Method method = null;
		try {
			
			Class<?>[] typeClassArray = null;
			
			if(types != null && types.length > 0) {
				typeClassArray = new Class<?>[types.length];
				
				for (int i=0; i<types.length; i++) {
					typeClassArray[i] = (Class<?>)types[i];
				}
				
			}else{
				typeClassArray = new Class<?>[] {};
			}

			method = object.getClass().getMethod(methodName, typeClassArray);
			
		} catch (NoSuchMethodException e) {
            Log.w(TAG, "Method \"" + methodName + "\" not found for " + object.getClass().toString());
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return method;
	}
		
	public static void invokeMethod(Object object, String methodName) {
		invokeMethod(object, methodName, null, null);
	}
	
	public static void invokeMethod(Object object, String methodName, Object[] types, Object[] values) {
		
		Method method = getMethod(object, methodName, types);
		if (method != null) {
			try {
				method.invoke(object, values);
			} catch (IllegalArgumentException e) {
				Log.e(TAG, e.getMessage());
			} catch (IllegalAccessException e) {
				Log.e(TAG, e.getMessage());
			} catch (InvocationTargetException e) {
				Log.e(TAG, e.getMessage());
			}
		}
	}
		
	public static Object newInstance(String className, Object... args) {
		Object object = null;
		if(Utils.hasValue(className)) {
			try {
				Class<?> adapterClass = Class.forName(className);
				Constructor<?> ct = adapterClass.getConstructor(); 
				object = ct.newInstance();
			} catch (ClassNotFoundException e) {
				Log.w(TAG, "ClassNotFoundException on class: " + className);
			} catch (ClassCastException e) {
				Log.w(TAG, "ClassCastException on class: " + className);
			} catch (SecurityException e) {
				Log.w(TAG, "SecurityException on class: " + className);
			} catch (NoSuchMethodException e) {
				Log.w(TAG, "NoSuchMethodException on class: " + className);
			} catch (IllegalArgumentException e) {
				Log.w(TAG, "IllegalArgumentException on class: " + className);
			} catch (InstantiationException e) {
				Log.w(TAG, "InstantiationException on class: " + className);
			} catch (IllegalAccessException e) {
				Log.w(TAG, "IllegalAccessException on class: " + className);
			} catch (InvocationTargetException e) {
				Log.w(TAG, "InvocationTargetException on class: " + className);
			}
		}
		return object;
	}
}
