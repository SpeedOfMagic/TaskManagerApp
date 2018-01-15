package team13.taskmanagerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kate on 13.01.2018.
 */

public class NewActionActivity extends Activity {
    private RecyclerView notif_container;
    private volatile Integer next_id = 0;
    final NotificationDataSource notif = new NotificationDataSource();
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_action);

        title = findViewById(R.id.title1);

        notif_container = findViewById(R.id.notif_cont);;

        notif_container.setLayoutManager(new LinearLayoutManager(this));

        notif_container.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new NotificationViewHolder(getLayoutInflater().inflate(R.layout.notification, parent, false));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                Notification notification = notif.getNotification(position);
                ((NotificationViewHolder) holder).bind(notification, notification.getId());
            }

            @Override
            public int getItemCount() {
                return notif.getCount();
            }

        });

        Button add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notif.addNotification(new Notification("Текст уведомления", 0, 0, next_id));
                Intent intent = new Intent(NewActionActivity.this, EditNotificationActivity.class);
                startActivityForResult(intent, next_id);
                next_id++;
            }
        });
    }

    @Override
    protected void onActivityResult(int id, int resultCode, Intent data) {
        super.onActivityResult(id, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == RESULT_OK) {
            if (data.hasExtra("message") && data.hasExtra("minutes") && data.hasExtra("hours")) {
                notif.changeNotification(id, data.getStringExtra("message"), data.getIntExtra("minutes", 0), data.getIntExtra("hours", 0));
            }
        }
    }


    private class NotificationDataSource {
        private final List<Notification> items = new ArrayList<>();

        int getCount() {
            return items.size();
        }

        Notification getNotification(int position) {
            return items.get(position);
        }

        void addNotification(Notification item) {
            items.add(item);
            final int position = items.size() - 1;
            notif_container.getAdapter().notifyItemInserted(position);
            notif_container.scrollToPosition(position);
        }

        void removeNotification(int id) {
            for (int ind = 0; ind < items.size(); ind++) {
                if (items.get(ind).getId().equals(id)) {
                    items.remove(ind);
                    notif_container.getAdapter().notifyItemRemoved(ind);
                    break;
                }
            }
        }

        void changeNotification(int id, String new_message, int new_min, int new_hour) {
            for (int ind = 0; ind < items.size(); ind++) {
                if (items.get(ind).getId().equals(id)) {
                    items.get(ind).setMessage(new_message);
                    items.get(ind).setMinutes(new_min);
                    items.get(ind).setHours(new_hour);
                    notif_container.getAdapter().notifyItemChanged(ind);
                    break;
                }
            }
        }
    }

    private class NotificationViewHolder extends RecyclerView.ViewHolder{
        private final TextView message;
        private TextView hours;
        private TextView minutes;
        private Button delete;
        private Button edit;
        private int id;

        NotificationViewHolder(final View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.notif_message);
            hours = itemView.findViewById(R.id.hour);
            minutes = itemView.findViewById(R.id.min);
            delete = itemView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notif.removeNotification(id);
                }
            });
            edit = itemView.findViewById(R.id.edit);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NewActionActivity.this, EditNotificationActivity.class);
                    startActivityForResult(intent, id);
                }
            });
        }

        private String format(int time) {
            if (time < 10)
                return "0" + time;
            return "" + time;
        }

        void bind(Notification notification, final int id) {
            message.setText(notification.getMessage());
            hours.setText(format(notification.getHours()));
            minutes.setText(format(notification.getMinutes()));
            this.id = id;
        }
    }
}
