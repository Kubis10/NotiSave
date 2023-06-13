package webace.notisave;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

public class Notification {
    private String title;
    private String text;
    private long timestamp;
    private String packageName;
    private byte[] appIconBytes;

    public Notification(String title, String text, long timestamp, String packageName, Drawable appIcon) {
        this.title = title;
        this.text = text;
        this.timestamp = timestamp;
        this.packageName = packageName;
        this.appIconBytes = convertDrawableToByteArray(appIcon);
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPackageName() {
        return packageName;
    }

    public Drawable getAppIcon() {
        return convertByteArrayToDrawable(appIconBytes);
    }

    private byte[] convertDrawableToByteArray(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        Bitmap bitmap = drawableToBitmap(drawable);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    private Drawable convertByteArrayToDrawable(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return new BitmapDrawable(Resources.getSystem(), bitmap);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


}