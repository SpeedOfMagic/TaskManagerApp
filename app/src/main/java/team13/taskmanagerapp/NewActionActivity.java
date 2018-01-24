package team13.taskmanagerapp;

import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import team13.taskmanagerapp.Database.Contract;
import team13.taskmanagerapp.Database.DBMethods;
import team13.taskmanagerapp.Database.DatabaseHelper;

import static android.provider.BaseColumns._ID;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_DESCRIP;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_END_HOUR;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_END_MIN;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_START_HOUR;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_START_MIN;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_TITLE;

/**
 * Created by kate on 13.01.2018.
 */

public class NewActionActivity extends AppCompatActivity {
    /*private RecyclerView notif_container;
    private Integer next_id = 0;
    final NotificationDataSource notif = new NotificationDataSource();*/
    DatabaseHelper databaseHelper;
    TextView begin_hour, begin_min, end_hour, end_min;
    EditText description, taskName;
    ButtonListener beginListener, endListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_action);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();

        if (getIntent().hasExtra("title")) {
            setTitle(getIntent().getStringExtra("title"));
        } else {
            setTitle("Новое событие");
        }

        RelativeLayout begin_layout = findViewById(R.id.time_box_begin);
        begin_hour = begin_layout.findViewById(R.id.hour);
        begin_min = begin_layout.findViewById(R.id.min);

        RelativeLayout end_layout = findViewById(R.id.time_box_end);
        end_hour = end_layout.findViewById(R.id.hour);
        end_min = end_layout.findViewById(R.id.min);

        description = findViewById(R.id.description);

        taskName = findViewById(R.id.name_of_act);

        taskName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskName.setError(null);
            }
        });

        if (getIntent().hasExtra("databaseID")) {
            long databaseID = getIntent().getLongExtra("databaseID", 0);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.query(Contract.TaskEntry.TABLE_TASK,
                    new String[]{COL_TASK_TITLE, COL_TASK_START_HOUR, COL_TASK_START_MIN, COL_TASK_END_HOUR, COL_TASK_END_MIN, COL_TASK_DESCRIP},
                    _ID + " = ? ", new String[] {String.valueOf(databaseID)},
                    null, null, null, null);
            while (cursor.moveToNext()) {
                taskName.setText(DBMethods.getString(cursor, COL_TASK_TITLE));
                taskName.setSelection(taskName.getText().length());
                begin_hour.setText(DBMethods.getString(cursor, COL_TASK_START_HOUR));
                begin_min.setText(DBMethods.getString(cursor, COL_TASK_START_MIN));
                end_hour.setText(DBMethods.getString(cursor, COL_TASK_END_HOUR));
                end_min.setText(DBMethods.getString(cursor, COL_TASK_END_MIN));
                description.setText(DBMethods.getString(cursor, COL_TASK_DESCRIP));
            }
            db.close();
            cursor.close();
        }

        beginListener = new ButtonListener(begin_hour, begin_min);
        begin_layout.setOnClickListener(beginListener);

        endListener = new ButtonListener(end_hour, end_min);
        end_layout.setOnClickListener(endListener);

        Button reset = findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginListener.reset();
                endListener.reset();
            }
        });

        /*notif_container = findViewById(R.id.notif_cont);
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

        });*/

        /*Button add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putInt("id", next_id);
                popupWindow(args);
                next_id++;
            }
        });*/
    }

    private void save() {
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
        }

        if (taskName.getText().toString().equals("")) {
            taskName.setError("Введите название");
        }

        if (begin <= end && !taskName.getText().toString().equals("")) {
            Intent intent = new Intent();
            String title = taskName.getText().toString();
            intent.putExtra("title", title);
            intent.putExtra("id", getIntent().getIntExtra("id", -1));
            intent.putExtra("beginHour", begin_hour.getText().toString());
            intent.putExtra("beginMin", begin_min.getText().toString());
            intent.putExtra("endHour", end_hour.getText().toString());
            intent.putExtra("endMin", end_min.getText().toString());
            intent.putExtra("description", description.getText().toString());

            if (getIntent().hasExtra("databaseID")) {
                intent.putExtra("databaseID", getIntent().getLongExtra("databaseID", 0));
            }

            setResult(RESULT_OK, intent);
            NewActionActivity.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_action_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                save();
                return true;
            case R.id.cancel:
                NewActionActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class ButtonListener implements View.OnClickListener {
        private TextView text_hour;
        private TextView text_min;
        int time = (int) 1e9;

        ButtonListener(TextView text_hour, TextView text_min) {
            this.text_hour = text_hour;
            this.text_min = text_min;
            if (!text_hour.getText().equals("") && !text_min.getText().equals("")) {
                time = timeInMinutes(Integer.valueOf(text_hour.getText().toString()), Integer.valueOf(text_min.getText().toString()));
            }
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
            int hour = c.get(Calendar.HOUR_OF_DAY);
            if (!text_hour.getText().toString().equals("")) {
                hour = Integer.valueOf(text_hour.getText().toString());
            }
            int min = c.get(Calendar.MINUTE);
            if (!text_min.getText().toString().equals("")) {
                min = Integer.valueOf(text_min.getText().toString());
            }
            TimePickerDialog timePickerDialog = new TimePickerDialog(NewActionActivity.this, 10, new TimePickerDialog.OnTimeSetListener() {
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

    String format(int time) {
        if (time < 10)
            return "0" + time;
        return "" + time;
    }

    static int timeInMinutes (int hours, int minutes) {
        return hours * 60 + minutes;
    }

    /*public static void changeData(Bundle data, NotificationDataSource notif) {
        if (data != null) {
            String message = data.getString("message", "");
            int minutes = data.getInt("minutes", 0);
            int hours = data.getInt("hours", 0);
            int id = data.getInt("id");

            notif.removeNotification(id);
            Notification new_notif = new Notification(message, hours, minutes, id);
            notif.addNotification(new_notif);
        }
    }

    public void popupWindow(final Bundle data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewActionActivity.this);

        View rootView = getLayoutInflater().inflate(R.layout.edit_notification, (LinearLayout) findViewById(R.id.lin_layout), false);

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
    }*/
}
