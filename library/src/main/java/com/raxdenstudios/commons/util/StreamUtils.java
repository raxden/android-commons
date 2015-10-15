package com.raxdenstudios.commons.util;

import android.content.Context;
import android.util.Log;
import android.util.Xml.Encoding;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 *
 * @author Angel Gomez
 */
public class StreamUtils {

    private static final String TAG = StreamUtils.class.getSimpleName();
	
    private static final int IO_BUFFER_SIZE = 8 * 1024;
	
	public static interface OnAmountDownloadListener {
		public void onAmountDownload(int amountComplete);
	}
    
	public static interface OnProgressDownloadListener {
		public void onProgressDownload(float progressComplete);
	}
	
	public static String readFileStream(File file) {
		return readFileStream(file, Encoding.UTF_8);
	}
	
	public static String readFileStream(File file, Encoding encoding) {
		if (file == null) return null;
		if (encoding == null) encoding = Encoding.UTF_8;
		
		String content = "";
		try {
			content = readInputStream(new FileInputStream(file), encoding);
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return content;
	}
    
	public static String readURLStream(String urlString) {
		return readURLStream(urlString, Encoding.UTF_8);
	}
	
	public static String readURLStream(String urlString, Encoding encoding) {
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage());
		}
		return readURLStream(url, encoding);
	}
	
	public static String readURLStream(URL url) {
		return readURLStream(url, Encoding.UTF_8);
	}
	
	public static String readURLStream(URL url, Encoding encoding) {
		if (url == null) return null;
		if (encoding == null) encoding = Encoding.UTF_8;
		
		InputStream is = readURLInputStream(url);
		
		return (is != null) ? readInputStream(is, encoding) : null;
	}
		
	public static InputStream readURLInputStream(String urlString) {
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage());
		}
		return readURLInputStream(url);
	}
	
	public static InputStream readURLInputStream(URL url) {
		if (url == null) return null;
		
		InputStream is = null;
		try {
			is = url.openStream();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		return is;
	}

	public static String readInputStream(InputStream is) {
		return readInputStream(is, Encoding.UTF_8);
	}
	
	public static String readInputStream(InputStream is, Encoding encoding) {
		StringBuilder sb = new StringBuilder();
		String line;

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, StringUtils.encodingToString(encoding)));
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} finally {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
		}		
		return sb.toString();
	}
				
	public static File downloadDataToFile(Context context, String filename, String urlString, Encoding encoding, OnAmountDownloadListener listener) {
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage());
		}
		return downloadDataToFile(context, filename, url, encoding, listener);
	}    
    
	public static File downloadDataToFile(Context context, String filename, URL url, Encoding encoding, OnAmountDownloadListener listener) {
		File file = null;
		HttpURLConnection urlConnection = null;
		InputStream is = null;
		try {
//			HttpClientUtils.disableConnectionReuseIfNecessary();
			urlConnection = (HttpURLConnection) url.openConnection();  
			is = urlConnection.getInputStream();
			file = downloadDataToFile(context, filename, is, encoding, listener);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		} finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
                urlConnection = null;
            }
    		closeInputStream(is);
		}
		return file;
	}    
	
	public static File downloadDataToFile(Context context, String filename, InputStream is, Encoding encoding, OnAmountDownloadListener listener) {
		File file = null;		
		try {
			file = FileUtils.getDiskCacheDir(context, filename);
			if (!downloadDataToOutputStream(is, encoding, new FileOutputStream(file), listener)) {
				file = null;
			}
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return file;
	}
	
	public static boolean downloadDataToFile(String urlString, Encoding encoding, File file, OnProgressDownloadListener listener) {		
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage());
		}
		return downloadDataToFile(url, encoding, file, listener);
	}
	
	public static boolean downloadDataToFile(URL url, Encoding encoding, File file, final OnProgressDownloadListener listener) {		
		boolean operation = false;
		HttpURLConnection urlConnection = null;
		InputStream is = null;
		try {
//			HttpClientUtils.disableConnectionReuseIfNecessary();
			urlConnection = (HttpURLConnection) url.openConnection();  
			is = urlConnection.getInputStream();
			final int contentLength = urlConnection.getContentLength();
			operation = downloadDataToOutputStream(is, encoding, new FileOutputStream(file), new OnAmountDownloadListener() {
				
				@Override
				public void onAmountDownload(int amountComplete) {
					float progress = (float)((double)amountComplete / (double)contentLength * 100.0);
					Log.d(TAG, String.format("Downloaded %s of %s bytes (%f) for file", amountComplete, contentLength, progress));
					if (listener != null) listener.onProgressDownload(progress);
				}
			});			
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		} finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
                urlConnection = null;
            }
    		closeInputStream(is);
		}
		return operation;
	}	
	
	public static boolean downloadDataToFile(InputStream is, Encoding encoding, File file, OnAmountDownloadListener listener) {
		boolean operation = false;
		try {
			operation = downloadDataToOutputStream(is, encoding, new FileOutputStream(file), listener);
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return operation;
	}

    public static boolean downloadDataToOutputStream(String urlString, Encoding encoding, OutputStream outputStream, OnProgressDownloadListener listener) {
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage());
		}
		return downloadDataToOutputStream(url, encoding, outputStream, listener);
    }		
	
	private static boolean downloadDataToOutputStream(URL url, Encoding encoding, OutputStream os, final OnProgressDownloadListener listener) {
		boolean operation = false;
		HttpURLConnection urlConnection = null;
		InputStream is = null;
		try {
//			HttpClientUtils.disableConnectionReuseIfNecessary();
			urlConnection = (HttpURLConnection) url.openConnection();  
			is = urlConnection.getInputStream();
			final int contentLength = urlConnection.getContentLength();
			operation = downloadDataToOutputStream(is, encoding, os, new OnAmountDownloadListener() {
				
				@Override
				public void onAmountDownload(int amountComplete) {
					float progress = (float)((double)amountComplete / (double)contentLength * 100.0);
					Log.d(TAG, String.format("Downloaded %s of %s bytes (%f) for file", amountComplete, contentLength, progress));
					if (listener != null) listener.onProgressDownload(progress);
				}
			});
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		} finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
                urlConnection = null;
            }
    		closeInputStream(is);
		}
		return operation;
	}

	public static boolean downloadDataToOutputStream(InputStream is, Encoding encoding, OutputStream os, OnAmountDownloadListener listener) {		
		boolean operation = false;
		BufferedOutputStream bos = null;
		try {
			BufferedInputStream bis = (is instanceof BufferedInputStream) ? (BufferedInputStream)is : new BufferedInputStream(is, IO_BUFFER_SIZE);
			bos = (os instanceof BufferedOutputStream) ? bos = (BufferedOutputStream)os : new BufferedOutputStream(os, IO_BUFFER_SIZE);
						
			int numBytesRead = 0;
			int amountComplete = 0;
			byte bufeer[] = new byte[IO_BUFFER_SIZE];
			
            while ((numBytesRead = bis.read(bufeer)) > 0) {
				amountComplete += numBytesRead;
				bos.write(bufeer, 0, numBytesRead);
				if (listener != null) listener.onAmountDownload(amountComplete);
			}
            operation = true;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		} finally {
			closeOutputStream(bos);
		}
		return operation;
	}

	public static void closeInputStream(InputStream is) {
		if (is != null) {
			if (is instanceof BufferedInputStream) {
				closeInputStream((BufferedInputStream)is);
			} else if (is instanceof FileInputStream) {
				closeInputStream((FileInputStream)is);
			} else {
				try {
					is.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
			is = null;
		}
	}
    
	public static void closeOutputStream(OutputStream os) {
		if (os != null) {
			if (os instanceof BufferedOutputStream) {
				closeOutputStream((BufferedOutputStream)os);
			} else if (os instanceof FileOutputStream) {
				closeOutputStream((FileOutputStream)os);
			} else {
				try {
					os.flush();
					os.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
			os = null;
		}
	}
	
	private static void closeInputStream(FileInputStream fis) {
		if (fis != null) {
			try {
				fis.close();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
			}
			fis = null;
		}
	}
    
	private static void closeInputStream(BufferedInputStream bis) {
		if (bis != null) {
			try {
				bis.close();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
			}
			bis = null;
		}
	}	
	
	private static void closeOutputStream(FileOutputStream fos) {
		if (fos != null) {
			try {
				fos.flush();
				fos.close();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
			}
			fos = null;
		}
	}
	
	private static void closeOutputStream(BufferedOutputStream bos) {
		if (bos != null) {
			try {
				bos.flush();
				bos.close();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
			}
			bos = null;
		}
	}
}
