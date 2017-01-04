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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

/**
 * A class that allows you to create simple dialogs alert to display.
 *
 */
public class AlertDialogUtils {

	/**
	 * Bundle class containing the title button and listener to be invoked when the button of the dialog is pressed.
	 */
	public static class AlertDialogButton {
		public String title;
		public int titleId;
		public DialogInterface.OnClickListener listener;

		/**
		 * Creates a AlertDialogButton containing the title button and listener to be invoked when the button of the dialog is pressed.
		 *
		 * @param title the resource id to use as the title
		 * @param listener The {@link DialogInterface.OnClickListener} to use.
		 */
		public AlertDialogButton(int title, DialogInterface.OnClickListener listener) {
			this.titleId = title;
			this.listener = listener;
		}

		/**
		 * Creates a AlertDialogButton containing the title button and listener to be invoked when the button of the dialog is pressed.
		 *
		 * @param title the string to use as the title
		 * @param listener The {@link DialogInterface.OnClickListener} to use.
		 */
		public AlertDialogButton(String title, DialogInterface.OnClickListener listener) {
			this.title = title;
			this.listener = listener;
		}
	}

    /**
	 * Creates an {@link AlertDialog} with the arguments supplied and immediately displays the dialog.
	 *
     * @param context the parent context
     * @param title the resource id to use as the title
     * @param message the resource id to use as the message
     * @return AlertDialog
     */
	public static AlertDialog showAlertDialog(Context context, int title, int message) {
		return showAlertDialog(context, title, message, null, null, null);
	}

    /**
	 * Creates an {@link AlertDialog} with the arguments supplied and immediately displays the dialog.
	 *
	 * @param context the parent context
	 * @param title the resource id to use as the title
	 * @param message the resource id to use as the message
	 * @param button the button of the dialog
     * @return AlertDialog
     */
	public static AlertDialog showAlertDialog(Context context, int title, int message, AlertDialogButton button) {
		return showAlertDialog(context, title, message, button, null);
	}

    /**
	 * Creates an {@link AlertDialog} with the arguments supplied and immediately displays the dialog.
	 *
	 * @param context the parent context
	 * @param title the resource id to use as the title
	 * @param message the resource id to use as the message
     * @param positiveButton the positive button of the dialog
     * @param negativeButton the negative button of the dialog
     * @return AlertDialog
     */
	public static AlertDialog showAlertDialog(Context context, int title, int message, AlertDialogButton positiveButton, AlertDialogButton negativeButton) {
		return showAlertDialog(context, title, message, null, positiveButton, negativeButton);
	}

    /**
	 * Creates an {@link AlertDialog} with the arguments supplied and immediately displays the dialog.
	 *
	 * @param context the parent context
	 * @param title the resource id to use as the title
	 * @param message the resource id to use as the message
     * @param view the custom view to be used within the dialog
     * @return AlertDialog
     */
	public static AlertDialog showAlertDialog(Context context, int title, int message, View view) {
		return showAlertDialog(context, title, message, view, null);
	}

    /**
	 * Creates an {@link AlertDialog} with the arguments supplied and immediately displays the dialog.
	 *
	 * @param context the parent context
	 * @param title the resource id to use as the title
	 * @param message the resource id to use as the message
	 * @param view the custom view to be used within the dialog
	 * @param button the button of the dialog
     * @return AlertDialog
     */
	public static AlertDialog showAlertDialog(Context context, int title, int message, View view, AlertDialogButton button) {
		return showAlertDialog(context, title, message, view, button, null);
	}

    /**
	 * Creates an {@link AlertDialog} with the arguments supplied and immediately displays the dialog.
	 *
	 * @param context the parent context
	 * @param title the resource id to use as the title
	 * @param message the resource id to use as the message
	 * @param view the custom view to be used within the dialog
	 * @param positiveButton the positive button of the dialog
	 * @param negativeButton the negative button of the dialog
     * @return AlertDialog
     */
	public static AlertDialog showAlertDialog(Context context, int title, int message, View view, AlertDialogButton positiveButton, AlertDialogButton negativeButton) {
	    return showAlertDialog(context, title != 0 ? context.getResources().getString(title) : "", message != 0 ? context.getResources().getString(message) : "", view, positiveButton, negativeButton);
	}

    /**
	 * Creates an {@link AlertDialog} with the arguments supplied and immediately displays the dialog.
	 *
	 * @param context the parent context
	 * @param title the string to use as the title
	 * @param message the string to use as the message
     * @return AlertDialog
     */
	public static AlertDialog showAlertDialog(Context context, String title, String message) {
		return showAlertDialog(context, title, message, null, null, null);
	}

    /**
	 * Creates an {@link AlertDialog} with the arguments supplied and immediately displays the dialog.
	 *
	 * @param context the parent context
	 * @param title the string to use as the title
	 * @param message the string to use as the message
	 * @param button the button of the dialog
     * @return AlertDialog
     */
	public static AlertDialog showAlertDialog(Context context, String title, String message, AlertDialogButton button) {
		return showAlertDialog(context, title, message, button, null);
	}

    /**
	 * Creates an {@link AlertDialog} with the arguments supplied and immediately displays the dialog.
	 *
	 * @param context the parent context
	 * @param title the string to use as the title
	 * @param message the string to use as the message
	 * @param positiveButton the positive button of the dialog
	 * @param negativeButton the negative button of the dialog
     * @return AlertDialog
     */
	public static AlertDialog showAlertDialog(Context context, String title, String message, AlertDialogButton positiveButton, AlertDialogButton negativeButton) {
		return showAlertDialog(context, title, message, null, positiveButton, negativeButton);
	}

    /**
	 * Creates an {@link AlertDialog} with the arguments supplied and immediately displays the dialog.
	 *
	 * @param context the parent context
	 * @param title the string to use as the title
	 * @param message the string to use as the message
	 * @param view the custom view to be used within the dialog
     * @return AlertDialog
     */
	public static AlertDialog showAlertDialog(Context context, String title, String message, View view) {
		return showAlertDialog(context, title, message, view, null);
	}

    /**
	 * Creates an {@link AlertDialog} with the arguments supplied and immediately displays the dialog.
	 *
	 * @param context the parent context
	 * @param title the string to use as the title
	 * @param message the string to use as the message
	 * @param view the custom view to be used within the dialog
	 * @param button the button of the dialog
     * @return AlertDialog
     */
	public static AlertDialog showAlertDialog(Context context, String title, String message, View view, AlertDialogButton button) {
		return showAlertDialog(context, title, message, view, button, null);
	}

    /**
	 * Creates an {@link AlertDialog} with the arguments supplied and immediately displays the dialog.
	 *
	 * @param context the parent context
	 * @param title the string to use as the title
	 * @param message the string to use as the message
	 * @param view the custom view to be used within the dialog
	 * @param positiveButton the positive button of the dialog
	 * @param negativeButton the negative button of the dialog
     * @return AlertDialog
     */
	public static AlertDialog showAlertDialog(Context context, String title, String message, View view, AlertDialogButton positiveButton, AlertDialogButton negativeButton) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (view != null) builder.setView(view);
		if (Utils.hasValue(title)) builder.setTitle(title);
	    if (Utils.hasValue(message)) builder.setMessage(message);
	    if (positiveButton != null) builder.setPositiveButton(Utils.hasValue(positiveButton.title) ? positiveButton.title : context.getResources().getString(positiveButton.titleId), positiveButton.listener);
	    if (negativeButton != null)	builder.setNegativeButton(Utils.hasValue(negativeButton.title) ? negativeButton.title : context.getResources().getString(negativeButton.titleId), negativeButton.listener);
	    return builder.show();
	}

}
