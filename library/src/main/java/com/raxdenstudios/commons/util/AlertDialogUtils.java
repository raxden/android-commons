package com.raxdenstudios.commons.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public class AlertDialogUtils {

	public static class AlertDialogButton {
		public String title;
		public int titleId;
		public DialogInterface.OnClickListener onClickListener;
		
		public AlertDialogButton(int titleId, DialogInterface.OnClickListener onClickListener) {
			this.titleId = titleId;
			this.onClickListener = onClickListener;
		}
		
		public AlertDialogButton(String title, DialogInterface.OnClickListener onClickListener) {
			this.title = title;
			this.onClickListener = onClickListener;
		}
	}
	
	public static AlertDialog showAlertDialog(Context context, int title, int message) {
		return showAlertDialog(context, title, message, null, null, null);
	}	
	
	public static AlertDialog showAlertDialog(Context context, int title, int message, AlertDialogButton button) {
		return showAlertDialog(context, title, message, button, null);
	}	
	
	public static AlertDialog showAlertDialog(Context context, int title, int message, AlertDialogButton positiveButton, AlertDialogButton negativeButton) {
		return showAlertDialog(context, title, message, null, positiveButton, negativeButton);
	}
	
	public static AlertDialog showAlertDialog(Context context, int title, int message, View view) {
		return showAlertDialog(context, title, message, view, null);
	}
	
	public static AlertDialog showAlertDialog(Context context, int title, int message, View view, AlertDialogButton button) {
		return showAlertDialog(context, title, message, view, button, null);
	}	
	
	public static AlertDialog showAlertDialog(Context context, int title, int message, View view, AlertDialogButton positiveButton, AlertDialogButton negativeButton) {
	    return showAlertDialog(context, title != 0 ? context.getResources().getString(title) : "", message != 0 ? context.getResources().getString(message) : "", view, positiveButton, negativeButton);
	}
	
	public static AlertDialog showAlertDialog(Context context, String title, String message) {
		return showAlertDialog(context, title, message, null, null, null);
	}	
	
	public static AlertDialog showAlertDialog(Context context, String title, String message, AlertDialogButton button) {
		return showAlertDialog(context, title, message, button, null);
	}	
	
	public static AlertDialog showAlertDialog(Context context, String title, String message, AlertDialogButton positiveButton, AlertDialogButton negativeButton) {
		return showAlertDialog(context, title, message, null, positiveButton, negativeButton);
	}
	
	public static AlertDialog showAlertDialog(Context context, String title, String message, View view) {
		return showAlertDialog(context, title, message, view, null);
	}
	
	public static AlertDialog showAlertDialog(Context context, String title, String message, View view, AlertDialogButton button) {
		return showAlertDialog(context, title, message, view, button, null);
	}		
	
	public static AlertDialog showAlertDialog(Context context, String title, String message, View view, AlertDialogButton positiveButton, AlertDialogButton negativeButton) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (view != null) builder.setView(view);
		if (Utils.hasValue(title)) builder.setTitle(title);
	    if (Utils.hasValue(message)) builder.setMessage(message);
	    
	    if (positiveButton != null) {
	    	builder.setPositiveButton(Utils.hasValue(positiveButton.title) ? positiveButton.title : context.getResources().getString(positiveButton.titleId), positiveButton.onClickListener);
	    }

	    if (negativeButton != null) {
	    	builder.setNegativeButton(Utils.hasValue(negativeButton.title) ? negativeButton.title : context.getResources().getString(negativeButton.titleId), negativeButton.onClickListener);
	    }

	    return builder.show();
	}	
}
