package team13.taskmanagerapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by kate on 13.01.2018.
 */

public class NewActionActivity extends AppCompatActivity {
    private RecyclerView notif_container;
    private Integer next_id = 0;
    final NotificationDataSource notif = new NotificationDataSource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_action);

        setTitle("Новое событие");

        final TextView begin_hour = findViewById(R.id.begin_hour);
        final TextView begin_min = findViewById(R.id.begin_min);
        Button btn = findViewById(R.id.btn1);
        final ButtonListener beginListener = new ButtonListener(begin_hour, begin_min);
        btn.setOnClickListener(beginListener);

        final TextView end_hour = findViewById(R.id.end_hour);
        final TextView end_min = findViewById(R.id.end_min);
        btn = findViewById(R.id.btn2);
        final ButtonListener endListener = new ButtonListener(end_hour, end_min);
        btn.setOnClickListener(endListener);

        notif_container = findViewById(R.id.notif_cont);
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

        Button reset = findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginListener.reset();
                endListener.reset();
            }
        });

        Button add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putInt("id", next_id);
                popupWindow(args);
                next_id++;
            }
        });

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewActionActivity.this.finish();
            }
        });

        final Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int begin = beginListener.getTime();
                int end = endListener.getTime();
                if (begin > end) {
                    int color = getResources().getColor(R.color.lightRed);
                    begin_hour.setBackgroundColor(color);
                    begin_min.setBackgroundColor(color);
                    end_hour.setBackgroundColor(color);
                    end_min.setBackgroundColor(color);
                    color = getResources().getColor(R.color.darkRed);
                    begin_hour.setTextColor(color);
                    begin_min.setTextColor(color);
                    end_hour.setTextColor(color);
                    end_min.setTextColor(color);
                } else {
                    // Запоминаем событие
                    NewActionActivity.this.finish();
                }
            }
        });
    }

    class ButtonListener implements View.OnClickListener {
        private TextView text_hour;
        private TextView text_min;
        int time = (int) 1e9;

        ButtonListener(TextView text_hour, TextView text_min) {
            this.text_hour = text_hour;
            this.text_min = text_min;
        }

        int getTime() {
            return time;
        }

        void reset() {
            time = (int) 1e9;
            text_hour.setText("");
            text_min.setText("");
            int color = getResources().getColor(R.color.colorPrimaryLight);
            text_hour.setBackgroundColor(color);
            text_min.setBackgroundColor(color);
            color = getResources().getColor(R.color.colorPrimaryDark);
            text_hour.setTextColor(color);
            text_min.setTextColor(color);
        }

        @Override
        public void onClick(View view) {
            final Calendar c = Calendar.getInstance();
            final int hour = c.get(Calendar.HOUR_OF_DAY);
            int min = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(NewActionActivity.this, 3, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                    text_hour.setText(format(hours));
                    text_min.setText(format(minutes));
                    time = timeInMinutes(hours, minutes);
                }
            }, hour, min, true);
            timePickerDialog.show();
        }
    }

    public static void changeData(Bundle data, NotificationDataSource notif) {
        if (data != null) {
            String message = data.getString("message", "");
            int minutes = data.getInt("minutes", 0);
            int hours = data.getInt("hours", 0);
            int id = data.getInt("id");

            if (notif.NotificationExists(id)) {
                notif.changeNotification(id, message, minutes, hours);
            } else {
                Notification new_notif = new Notification(message, hours, minutes, id);
                notif.addNotification(new_notif);
            }
        }
    }

    public void popupWindow(final Bundle data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewActionActivity.this);

        View rootView = getLayoutInflater().inflate(R.layout.edit_notification, null);

        final TimePicker timePicker = rootView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(data.getInt("current_hours", 0));
        timePicker.setCurrentMinute(data.getInt("current_minutes", 0));

        final EditText message = rootView.findViewById(R.id.editText);
        message.setText(data.getString("current_message"));

        builder.setView(rootView)
                .setNegativeButton("Готово", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final Bundle new_data = new Bundle();
                        new_data.putInt("id", data.getInt("id"));
                        new_data.putString("message", message.getText().toString());
                        new_data.putInt("hours", timePicker.getCurrentHour());
                        new_data.putInt("minutes", timePicker.getCurrentMinute());
                        NewActionActivity.changeData(new_data, notif);
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    static int timeInMinutes (int hours, int minutes) {
        return hours * 60 + minutes;
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
            int position = 0;
            for ( ; position < items.size(); position++) {
                int cur_min = timeInMinutes(items.get(position).getHours(), items.get(position).getMinutes());
                int new_min = timeInMinutes(item.getHours(), item.getMinutes());
                if (cur_min > new_min) {
                    break;
                }
            }
            items.add(position, item);
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
            for (int position = 0; position < items.size(); position++) {
                if (items.get(position).getId().equals(id)) {
                    Notification notification = items.get(position);
                    items.remove(position);
                    notif_container.getAdapter().notifyItemRemoved(position);
                    notification.setMessage(new_message);
                    notification.setMinutes(new_min);
                    notification.setHours(new_hour);
                    addNotification(notification);
                    break;
                }
            }
        }

        boolean NotificationExists(int id) {
            for (int position = 0; position < items.size(); position++) {
                if (items.get(position).getId() == id)
                    return true;
            }
            return false;
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
        }

        void bind(final Notification notification, final int id) {
            message.setText(notification.getMessage());
            hours.setText(format(notification.getHours()));
            minutes.setText(format(notification.getMinutes()));
            this.id = id;
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putInt("id", notification.getId());
                    args.putInt("current_hours", notification.getHours());
                    args.putInt("current_minutes", notification.getMinutes());
                    args.putString("current_message", notification.getMessage());
                    popupWindow(args);
                }
            });
        }
    }

    String format(int time) {
        if (time < 10)
            return "0" + time;
        return "" + time;
    }
}
