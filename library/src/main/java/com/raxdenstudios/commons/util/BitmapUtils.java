package com.raxdenstudios.commons.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Angel Gomez
 */
public class BitmapUtils {

    private static final String TAG = BitmapUtils.class.getSimpleName();

	public static Bitmap createBitmapFromView(View view) {
		int width = view.getWidth();
		int height = view.getHeight();

		if (width == 0 || height == 0) {
			throw new IllegalStateException("View must have a widht and height > 0.");
		}
		
		view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		
		Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		view.draw(c);
		
		return b;
	}
	
	public static String saveBitmap(Context context, Bitmap bitmap, String uniqueName) {
		File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), uniqueName+".png");
		try {
            FileOutputStream fos = new FileOutputStream(imageFile.getPath());
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            bitmap.compress(Bitmap.CompressFormat.PNG, 90, bos);
            bos.flush();
            fos.getFD().sync();
            bos.close();

            MediaScannerConnection.scanFile(context, new String[]{imageFile.getPath()}, new String[]{"image/png"}, null);

            if (!bitmap.isRecycled()) {
            	bitmap.recycle();
            }

            return imageFile.getPath();
            
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
    
	public static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
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
     * @param value
     * @return size in bytes
     */
    @TargetApi(12)
    public static int getBitmapSize(BitmapDrawable value) {
    	Bitmap bitmap = value.getBitmap();
    	return getBitmapSize(bitmap);
    }
    
    /**
     * Get the size in bytes of a bitmap.
     * @param bitmap
     * @return size in bytes
     */
    @TargetApi(12)
    public static int getBitmapSize(Bitmap bitmap) {
        if (Utils.hasHoneycombMR1()) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
