package com.raxdenstudios.commons.util;

import android.util.Log;

import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Angel Gomez
 */
public class EncryptUtils {

    private static final String TAG = EncryptUtils.class.getSimpleName();

    public enum AlgorithmType {SHA,MD5};
        
	public static String hashKey(String key){
		return hashKey(key,AlgorithmType.MD5,HTTP.UTF_8);
	}	
	
	public static String hashKey(String key, AlgorithmType algorithmType, String encoding){ 	
		byte[] digestBytes = getDigest(key, algorithmType, encoding);
		return Base64.encodeWebSafe(digestBytes, true);
	}
	
    public static String hashKeyForDisk(String key) {
		return hashKeyForDisk(key,AlgorithmType.MD5,HTTP.UTF_8);
    }	
	
	public static String hashKeyForDisk(String key, AlgorithmType algorithmType, String encoding){ 	
		byte[] digestBytes = getDigest(key, algorithmType, encoding);
        return bytesToHexString(digestBytes);
	}    
    
	private static byte[] getDigest(String key, AlgorithmType algorithmType, String encoding) {
		byte[] digest = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(getAlgorithm(algorithmType));
    		messageDigest.update(key.getBytes(encoding));
    		digest = messageDigest.digest();
		} catch (NoSuchAlgorithmException e) {
	        Log.e(TAG, e.getMessage());
		} catch (UnsupportedEncodingException e) {
	        Log.e(TAG, e.getMessage());
		}
				
		return digest;
	}
	
	private static String getAlgorithm(AlgorithmType algorithmType){
		switch(algorithmType){
			case SHA:	return "SHA";
			case MD5:	return "MD5";
			default:  	return "MD5";
		}
	}	
	
    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    
}
