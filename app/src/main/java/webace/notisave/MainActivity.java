package webace.notisave;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AppAdapter appAdapter;
    private NotificationDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isNotificationAccessGranted()) {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        }

        startService(new Intent(this, MyNotificationListenerService.class));

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        appAdapter = new AppAdapter(new ArrayList<>());
        recyclerView.setAdapter(appAdapter);

        databaseHelper = new NotificationDatabaseHelper(this);

        // Load and display the notifications
        List<Notification> notifications = databaseHelper.getAllNotifications();
        System.out.println("Notifications: " + notifications);
        appAdapter = new AppAdapter(notifications);
        recyclerView.setAdapter(appAdapter);

        Button refreshButton = findViewById(R.id.button);

        refreshButton.setOnClickListener(v -> {
            List<Notification> notifications1 = databaseHelper.getAllNotifications();
            System.out.println("Notifications: " + notifications1);
            appAdapter = new AppAdapter(notifications1);
            recyclerView.setAdapter(appAdapter);
        });
    }

    private boolean isNotificationAccessGranted() {
        String packageName = getPackageName();
        String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (flat != null) {
            String[] names = flat.split(":");
            for (String name : names) {
                ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null && TextUtils.equals(packageName, cn.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }

}