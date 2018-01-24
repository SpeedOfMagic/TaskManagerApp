package team13.taskmanagerapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
 * Created by kate on 18.01.2018.
 */

public class ViewActionFragment extends Fragment {

    DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() == null) {
            Log.d("ViewActionFragment", "No databaseID");
            return null;
        }

        getActivity().setTitle("Просмотр события");

        View rootView = inflater.inflate(R.layout.view_task, container, false);
        rootView.setBackgroundColor(getResources().getColor(R.color.white));

        databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());
        final long databaseID = getArguments().getLong("databaseID", 0);

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(Contract.TaskEntry.TABLE_TASK,
                new String[]{COL_TASK_TITLE, COL_TASK_START_HOUR, COL_TASK_START_MIN, COL_TASK_END_HOUR, COL_TASK_END_MIN, COL_TASK_DESCRIP},
                _ID + " = ? ", new String[] {String.valueOf(databaseID)},
                null, null, null, null);

        String beginMin = "", beginHour = "", endMin = "", endHour = "";
        String descript = "", title = "";
        while (cursor.moveToNext()) {
            beginHour = DBMethods.getString(cursor, COL_TASK_START_HOUR);
            beginMin = DBMethods.getString(cursor, COL_TASK_START_MIN);
            endHour = DBMethods.getString(cursor, COL_TASK_END_HOUR);
            endMin = DBMethods.getString(cursor, COL_TASK_END_MIN);
            descript = DBMethods.getString(cursor, COL_TASK_DESCRIP);
            title = DBMethods.getString(cursor, COL_TASK_TITLE);
        }

        db.close();
        cursor.close();

        ((TextView) rootView.findViewById(R.id.title)).setText(title);

        ViewGroup layout = rootView.findViewById(R.id.begin);
        if (!beginHour.equals("") && !beginMin.equals("")) {
            layout.setVisibility(View.VISIBLE);
            ((TextView) layout.findViewById(R.id.title)).setText("Начало");
            RelativeLayout timeBox = layout.findViewById(R.id.time_box);
            ((TextView) timeBox.findViewById(R.id.hour)).setText(beginHour);
            ((TextView) timeBox.findViewById(R.id.min)).setText(beginMin);
        } else {
            layout.setVisibility(View.GONE);
        }

        layout = rootView.findViewById(R.id.end);
        if (!endHour.equals("") && !endMin.equals("")) {
            layout.setVisibility(View.VISIBLE);
            ((TextView) layout.findViewById(R.id.title)).setText("Конец");
            RelativeLayout timeBox = layout.findViewById(R.id.time_box);
            ((TextView) timeBox.findViewById(R.id.hour)).setText(endHour);
            ((TextView) timeBox.findViewById(R.id.min)).setText(endMin);
        } else {
            layout.setVisibility(View.GONE);
        }

        TextView description = rootView.findViewById(R.id.description);
        description.setText(descript);
        description.setMovementMethod(new ScrollingMovementMethod());
        if (descript.equals(""))
            description.setText("Описание отсутствует");

        (rootView.findViewById(R.id.change)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TasksForToday.EDIT = true;
                TasksForToday.EDIT_DATABASE_ID = databaseID;
                TasksForToday.EDIT_ID = getArguments().getInt("id", 0);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        /*final List<Notification> notif = new ArrayList<>();

        //Каким-то образом загружаем данные об уведомлениях
        notif.add(new Notification("First", 12, 0, 0));
        notif.add(new Notification("Second", 12, 30, 1));

        RecyclerView notif_container = rootView.findViewById(R.id.notif_cont);
        notif_container.setLayoutManager(new LinearLayoutManager(getActivity()));
        notif_container.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new NotificationViewHolder(getLayoutInflater().inflate(R.layout.view_notification, parent, false));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                Notification notification = notif.get(position);
                ((NotificationViewHolder) holder).bind(notification);
            }

            @Override
            public int getItemCount() {
                return notif.size();
            }

        }); */

        return rootView;
    }

    /*private class NotificationViewHolder extends RecyclerView.ViewHolder{
        private final TextView message;
        private TextView hours;
        private TextView minutes;

        NotificationViewHolder(final View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.notif_message);
            hours = itemView.findViewById(R.id.hour);
            minutes = itemView.findViewById(R.id.min);
        }

        void bind(final Notification notification) {
            message.setText(notification.getMessage());
            hours.setText(format(notification.getHours()));
            minutes.setText(format(notification.getMinutes()));
        }
    }*/
}
