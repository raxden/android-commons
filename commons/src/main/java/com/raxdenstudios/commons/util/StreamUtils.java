package com.raxdenstudios.commons.util;

import android.util.Log;
import android.util.Xml.Encoding;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
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
	
	public interface OnAmountWritenListener {
		void onAmountWriten(int amountWriten);
	}
    
	public interface OnProgressDownloadListener {
		void onProgressDownload(float progressComplete);
	}

	/* ========================================================================================== */

	public static String readContent(File file) {
		if (file == null) return "";
		return readContent(file, Encoding.UTF_8);
	}

	public static String readContent(String source) {
		if (source == null) return "";
		return readContent(source, Encoding.UTF_8);
	}

	public static String readContent(URL url) {
		if (url == null) return "";
		return readContent(url, Encoding.UTF_8);
	}

	public static String readContent(InputStream is) {
		if (is == null) return "";
		return readContent(is, Encoding.UTF_8);
	}

	/* ========================================================================================== */

	public static String readContent(File file, Encoding encoding) {
		if (file == null) return "";
		if (encoding == null) encoding = Encoding.UTF_8;
		return readContent(readInputStream(file), encoding);
	}

	public static String readContent(String source, Encoding encoding) {
		if (source == null) return "";
		if (encoding == null) encoding = Encoding.UTF_8;

		String content = "";
		try {
			content = readContent(new URL(source), encoding);
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage());
		}
		return content;
	}

	public static String readContent(URL url, Encoding encoding) {
		if (url == null) return "";
		if (encoding == null) encoding = Encoding.UTF_8;
		return readContent(readInputStream(url), encoding);
	}

	public static String readContent(InputStream is, Encoding encoding) {
		if (is == null) return "";
		if (encoding == null) encoding = Encoding.UTF_8;

		String encodingString = StringUtils.encodingToString(encoding);
		StringBuilder sb = new StringBuilder();
		String line;

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, encodingString));
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} finally {
			try {
				if (is != null) is.close();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
		}

		return sb.toString();
	}

	/* ========================================================================================== */

	public static InputStream readInputStream(File file) {
		InputStream is = null;
		try {
			if (file != null) is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return is;
	}

	public static InputStream readInputStream(String source) {
		InputStream is = null;
		try {
			is = readInputStream(new URL(source));
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage());
		}
		return is;
	}
	
	public static InputStream readInputStream(URL url) {
		InputStream is = null;
		try {
			is = url.openStream();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		return is;
	}

    /* ========================================================================================== */

    public static boolean downloadData(byte[] data, File file, OnProgressDownloadListener listener) {
        boolean operation = false;
        try {
            operation = downloadData(data, new FileOutputStream(file), listener);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return operation;
    }

    public static boolean downloadData(String source, File file, OnProgressDownloadListener listener) {
        boolean operation = false;
        try {
            operation = downloadData(new URL(source), file, listener);
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
        }
        return operation;
    }

    public static boolean downloadData(URL url, File file, OnProgressDownloadListener listener) {
        boolean operation = false;
        try {
            operation = downloadData(url, new FileOutputStream(file), listener);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return operation;
    }

    public static boolean downloadData(final byte[] data, OutputStream os, final OnProgressDownloadListener listener) {
        return writeData(data, os, new OnAmountWritenListener() {

            @Override
            public void onAmountWriten(int amountWriten) {
                float progress = (float) ((double) amountWriten / (double) data.length * 100.0);
                Log.d(TAG, String.format("Downloaded %s of %s bytes (%f) for file", amountWriten, data.length, progress));
                if (listener != null) listener.onProgressDownload(progress);
            }
        });
    }

    public static boolean downloadData(String source, OutputStream os, OnProgressDownloadListener listener) {
        boolean operation = false;
        try {
            operation = downloadData(new URL(source), os, listener);
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
        }
        return operation;
    }

    private static boolean downloadData(URL url, OutputStream os, final OnProgressDownloadListener listener) {
        boolean operation = false;
        HttpURLConnection urlConnection = null;
        InputStream is = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            is = urlConnection.getInputStream();
            final int contentLength = urlConnection.getContentLength();
            operation = writeData(is, os, new OnAmountWritenListener() {

                @Override
                public void onAmountWriten(int amountWriten) {
                    float progress = (float) ((double) amountWriten / (double) contentLength * 100.0);
                    Log.d(TAG, String.format("Downloaded %s of %s bytes (%f) for file", amountWriten, contentLength, progress));
                    if (listener != null) listener.onProgressDownload(progress);
                }
            });
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            closeInputStream(is);
        }
        return operation;
    }

	/* ========================================================================================== */

    public static boolean writeData(byte[] data, File file, OnAmountWritenListener listener) {
        return writeData(new ByteArrayInputStream(data), file, listener);
    }

    public static boolean writeData(byte[] data, OutputStream os, OnAmountWritenListener listener) {
        return writeData(new ByteArrayInputStream(data), os, listener);
    }

    public static boolean writeData(InputStream is, File file, OnAmountWritenListener listener) {
        boolean operation = false;
        try {
            operation = writeData(is, new FileOutputStream(file), listener);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return operation;
    }

	public static boolean writeData(InputStream is, OutputStream os, OnAmountWritenListener listener) {
        BufferedInputStream bis = (is instanceof BufferedInputStream) ? (BufferedInputStream)is : new BufferedInputStream(is, IO_BUFFER_SIZE);
        BufferedOutputStream bos = (os instanceof BufferedOutputStream) ? (BufferedOutputStream)os : new BufferedOutputStream(os, IO_BUFFER_SIZE);
        return writeData(bis, bos, listener);
	}

    public static boolean writeData(BufferedInputStream bis, BufferedOutputStream bos, OnAmountWritenListener listener) {
        boolean operation = false;
        try {
            int numBytesRead = 0;
            int amountComplete = 0;
            byte bufeer[] = new byte[IO_BUFFER_SIZE];

            while ((numBytesRead = bis.read(bufeer)) > 0) {
                amountComplete += numBytesRead;
                bos.write(bufeer, 0, numBytesRead);
                if (listener != null) listener.onAmountWriten(amountComplete);
            }
            operation = true;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            closeOutputStream(bos);
        }
        return operation;
    }

	/* ========================================================================================== */

	public static void closeInputStream(InputStream is) {
		if (is != null) {
			if (is instanceof BufferedInputStream) {
				closeInputStream((BufferedInputStream)is);
			} else if (is instanceof FileInputStream) {
				closeInputStream((FileInputStream) is);
			} else if (is instanceof ObjectInputStream) {
				closeInputStream((ObjectInputStream) is);
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

	private static void closeInputStream(ObjectInputStream ois) {
		if (ois != null) {
			try {
				ois.close();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
			}
			ois = null;
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
