package webace.notisave;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class NotificationDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notification_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "notifications";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_PACKAGE_NAME = "package_name";

    private Context context;

    public NotificationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_TEXT + " TEXT, " +
                COLUMN_TIMESTAMP + " INTEGER, " +
                COLUMN_PACKAGE_NAME + " TEXT" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertNotification(Notification notification) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, notification.getTitle());
        values.put(COLUMN_TEXT, notification.getText());
        values.put(COLUMN_TIMESTAMP, notification.getTimestamp());
        values.put(COLUMN_PACKAGE_NAME, notification.getPackageName());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Notification> getAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_TIMESTAMP + " DESC");

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                String text = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT));
                long timestamp = cursor.getLong(cursor.getColumnIndex(COLUMN_TIMESTAMP));
                String packageName = cursor.getString(cursor.getColumnIndex(COLUMN_PACKAGE_NAME));

                // Retrieve the app icon based on the package name
                Drawable appIcon = getAppIconByPackageName(packageName);

                Notification notification = new Notification(title, text, timestamp, packageName, appIcon);
                notifications.add(notification);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return notifications;
    }

    private Drawable getAppIconByPackageName(String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
            return pm.getApplicationIcon(appInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
