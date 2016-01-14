package com.raxdenstudios.commons.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 *
 * @author Angel Gomez
 */
public class NotificationUtils {

    private static final String TAG = NavigationUtils.class.getSimpleName();

    public static void sendNotification(Context context, Bundle extras, int notificationId, int smallIcon, String contentTitle, String contentText, String bigText, String ticker, int defaults) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(Utils.getPackageName(context));
        if (extras != null) intent.putExtras(extras);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        sendNotification(context, intent, notificationId, smallIcon, contentTitle, contentText, bigText, ticker, defaults);
    }

    public static void sendNotification(Context context, Intent intent, int notificationId, int smallIcon, String contentTitle, String contentText, String bigText, String ticker, int defaults) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_ONE_SHOT);
        sendNotification(context, pendingIntent, notificationId, smallIcon, contentTitle, contentText, bigText, ticker, defaults);
    }

    public static void sendNotification(Context context, PendingIntent pendingIntent, int notificationId, int smallIcon, String contentTitle, String contentText, String bigText, String ticker, int defaults) {
        Notification.Builder builder = new Notification.Builder(context);
        if (smallIcon != 0) builder.setSmallIcon(smallIcon);
        if (Utils.hasValue(contentTitle)) builder.setContentTitle(contentTitle);
        if (Utils.hasValue(bigText)) builder.setStyle(new Notification.BigTextStyle().bigText(bigText));
        if (Utils.hasValue(contentText)) builder.setContentText(contentText);
        if (Utils.hasValue(ticker)) builder.setTicker(ticker);
        if (pendingIntent != null) builder.setContentIntent(pendingIntent);
        builder.setDefaults(defaults);
        sendNotification(context, notificationId, builder.build());
    }

    public static void sendNotification(Context context, int notificationId, Notification notification) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(notificationId, notification);
    }

}
