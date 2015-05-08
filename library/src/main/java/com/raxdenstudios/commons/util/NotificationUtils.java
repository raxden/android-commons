package com.raxdenstudios.commons.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by agomez on 23/04/2015.
 */
public class NotificationUtils {

    private static final String TAG = NavigationUtils.class.getSimpleName();

    private static NotificationUtils INSTANCE = null;

    private NotificationUtils() {}

    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NotificationUtils();
        }
    }

    public static NotificationUtils getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }

    public void sendNotification(Context context, int notificationId, int smallIcon, String contentTitle, String contentText, String bigText, String ticker) {
        Intent intent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_ONE_SHOT);
        sendNotification(context, pendingIntent, notificationId, smallIcon, contentTitle, contentText, bigText, ticker);
    }

    public void sendNotification(Context context, PendingIntent pendingIntent, int notificationId, int smallIcon, String contentTitle, String contentText, String bigText, String ticker) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        if (smallIcon != 0) builder.setSmallIcon(smallIcon);
        if (Utils.hasValue(contentTitle)) builder.setContentTitle(contentTitle);
        if (Utils.hasValue(bigText)) builder.setStyle(new NotificationCompat.BigTextStyle().bigText(bigText));
        if (Utils.hasValue(contentText)) builder.setContentText(contentText);
        if (Utils.hasValue(ticker)) builder.setTicker(ticker);
        if (pendingIntent != null) builder.setContentIntent(pendingIntent);
        sendNotification(context, notificationId, builder.build());
    }

    public void sendNotification(Context context, int notificationId, Notification notification) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(notificationId, notification);
    }

}
