package webace.notisave;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {
    private List<Notification> notifications;

    public AppAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_notification, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView textTextView;
        private TextView timestampTextView;
        private ImageView iconImageView;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            textTextView = itemView.findViewById(R.id.textTextView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
        }

        public void bind(Notification notification) {
            titleTextView.setText(notification.getTitle());
            textTextView.setText(notification.getText());
            iconImageView.setImageDrawable(notification.getAppIcon());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String timestamp = sdf.format(new Date(notification.getTimestamp()));
            timestampTextView.setText(timestamp);
        }
    }
}
