package com.raxdenstudios.commons.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Angel Gomez
 */
public class BitmapUtils {

    private static final String TAG = BitmapUtils.class.getSimpleName();

    public static Bitmap resize(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float aspectRatio;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            aspectRatio = width / height;
            if (bitmap.getWidth() > maxSize) {
                width = maxSize;
            }
            height = Math.round(width / aspectRatio);
        } else {
            aspectRatio = height / width;
            if (bitmap.getHeight() > maxSize) {
                height = maxSize;
            }
            width = Math.round(height / aspectRatio);
        }
        bitmap = BitmapUtils.resize(bitmap, width, height);
        return resize(bitmap, width, height);
    }

    public static Bitmap resize(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    public static Bitmap decode(URL url) {
        HttpURLConnection connection = null;
        InputStream is = StreamUtils.readInputStream(url);
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            is = connection.getInputStream();
            return decode(is);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            StreamUtils.closeInputStream(is);
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public static Bitmap decode(URL url, int width, int height) {
        HttpURLConnection connection = null;
        InputStream is = StreamUtils.readInputStream(url);
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            is = connection.getInputStream();
            return decode(is, width, height);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            StreamUtils.closeInputStream(is);
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public static Bitmap decode(InputStream is) {
        return BitmapFactory.decodeStream(is);
    }

    public static Bitmap decode(InputStream is, int width, int height) {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is, null, options);
    }

    public static Bitmap decode(Context context, int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public static Bitmap decode(Context context, int resId, int width, int height) {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }

    public static Bitmap decode(Context context, Uri uri) {
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    public static Bitmap decode(Context context, Uri uri, int width, int height) {
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            return decode(is, width, height);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

	public static Bitmap rotate(Bitmap bm, int rotation) {
		if (rotation != 0) {
			Matrix matrix = new Matrix();
			matrix.postRotate(rotation);
			Bitmap bmOut = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
			return bmOut;
		}
		return bm;
	}

	public static Bitmap rounded(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

    public static Bitmap fromDrawable(Drawable drawable) {
        if (drawable == null) return null;
        if (drawable instanceof BitmapDrawable) return ((BitmapDrawable)drawable).getBitmap();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

	public static Bitmap fromView(View view) {
		int width = view.getWidth();
		int height = view.getHeight();

		if (width == 0 || height == 0) {
			throw new IllegalStateException("View must have a widht and height > 0.");
		}
		
		view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		
		Bitmap b = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas c = new Canvas(b);
		view.draw(c);
		
		return b;
	}

    public static Bitmap fromFile(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config.ARGB_8888;
        return BitmapFactory.decodeFile(file.getPath(), options);
    }

    public static Bitmap compress(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
    }

    public static Bitmap compress(Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(format, quality, out);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
    }

    public static File toFile(Context context, Bitmap bitmap, String uniqueName) {
        return toFile(context, bitmap, uniqueName, Bitmap.CompressFormat.PNG, 100, true);
    }

	public static File toFile(Context context, Bitmap bitmap, String uniqueName, Bitmap.CompressFormat format, int quality, boolean scan) {
        File imageFile = FileUtils.getDiskCacheDir(context, uniqueName);
        try {
            FileOutputStream fos = new FileOutputStream(imageFile.getPath());
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            bitmap.compress(format, quality, bos);
            bos.flush();
            fos.getFD().sync();
            bos.close();

            if (scan) {
                String mimeType = "";
                switch (format) {
                    case JPEG: mimeType = "image/jpeg"; break;
                    case PNG: mimeType = "image/png"; break;
                }
                MediaScannerConnection.scanFile(context, new String[]{imageFile.getPath()}, new String[]{mimeType}, null);
            }

            return imageFile;
        } catch (IOException e) {
        	Log.e(TAG, e.getMessage(), e);
        }
		return null;
	}
	
	public static Bitmap takeScreenShot(Activity activity) {
	    View view = activity.getWindow().getDecorView();
	    view.setDrawingCacheEnabled(true);
	    view.buildDrawingCache();
	    Bitmap b1 = view.getDrawingCache();
	    Rect frame = new Rect();
	    activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
	    int statusBarHeight = frame.top;
	    int width = activity.getWindowManager().getDefaultDisplay().getWidth();
	    int height = activity.getWindowManager().getDefaultDisplay().getHeight();
	    Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height  - statusBarHeight);
	    view.destroyDrawingCache();
	    return b;
	}

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options An options object with out* params already populated (run through a decode* method with inJustDecodeBounds==true
     * @param reqWidth The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
    	Log.d(TAG, "options(w,h): (" + options.outWidth + "," + options.outHeight + ") required(w,h): (" + reqWidth + "," + reqHeight + ")");
    	
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        
        Log.d(TAG, "inSampleSize: " + inSampleSize);
        return inSampleSize;
    }

    /**
     * Get the size in bytes of a bitmap.
     * @param bitmap
     * @return size in bytes
     */
    @TargetApi(12)
    public static int size(Bitmap bitmap) {
        if (SDKUtils.hasHoneycombMR1()) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
