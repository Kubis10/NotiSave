package webace.notisave;

import android.app.Notification;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class MyNotificationListenerService extends NotificationListenerService {
    private NotificationDatabaseHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseHelper = new NotificationDatabaseHelper(this);
        System.out.println("Notification listener service created");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String title = sbn.getNotification().extras.getString(Notification.EXTRA_TITLE);
        String text = sbn.getNotification().extras.getString(Notification.EXTRA_TEXT);
        long timestamp = sbn.getPostTime();
        String packageName = sbn.getPackageName();

        Drawable appIcon = sbn.getNotification().extras.getParcelable(Notification.EXTRA_LARGE_ICON_BIG);

        webace.notisave.Notification notification = new webace.notisave.Notification(title, text, timestamp, packageName, appIcon);

        System.out.println("Notification posted: " + notification.getTitle() + " " + notification.getText());
        databaseHelper.insertNotification(notification);
    }
}