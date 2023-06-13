package webace.notisave;

import android.app.Notification;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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

        Drawable appIcon = getAppIconByPackageName(packageName);

        webace.notisave.Notification notification = new webace.notisave.Notification(title, text, timestamp, packageName, appIcon);

        System.out.println("Notification posted: " + notification.getTitle() + " " + notification.getText());
        databaseHelper.insertNotification(notification);
    }

    private Drawable getAppIconByPackageName(String packageName) {
        try {
            PackageManager pm = getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
            return pm.getApplicationIcon(appInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}