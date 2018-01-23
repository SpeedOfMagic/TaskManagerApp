package team13.taskmanagerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import team13.taskmanagerapp.Database.DatabaseHelper;
import team13.taskmanagerapp.Database.Task;

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

        getActivity().setTitle("Событие"); // тут название таска
        View rootView = inflater.inflate(R.layout.view_task, container, false);

        databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());
        final String databaseID = getArguments().getString("databaseID");
        final int id = getArguments().getInt("id");

        //Каким-то образом загружаем данные о времени начала и конца
        //final Item item = DatabaseHelper.getTaskById(databaseHelper.getWritableDatabase(), databaseID);
        final Item item = new Item();

        ViewGroup layout = rootView.findViewById(R.id.begin);
        if (!item.getBeginHour().equals("") && !item.getBeginMin().equals("")) {
            layout.setVisibility(View.VISIBLE);
            ((TextView) layout.findViewById(R.id.title)).setText("Начало");
            RelativeLayout timeBox = layout.findViewById(R.id.time_box);
            ((TextView) timeBox.findViewById(R.id.hour)).setText(item.getBeginHour());
            ((TextView) timeBox.findViewById(R.id.min)).setText(item.getBeginMin());
        } else {
            layout.setVisibility(View.GONE);
        }

        layout = rootView.findViewById(R.id.end);
        if (!item.getEndHour().equals("") && !item.getEndMin().equals("")) {
            layout.setVisibility(View.VISIBLE);
            ((TextView) layout.findViewById(R.id.title)).setText("Конец");
            RelativeLayout timeBox = layout.findViewById(R.id.time_box);
            ((TextView) timeBox.findViewById(R.id.hour)).setText(item.getEndHour());
            ((TextView) timeBox.findViewById(R.id.min)).setText(item.getEndMin());
        } else {
            layout.setVisibility(View.GONE);
        }

        TextView description = rootView.findViewById(R.id.description);
        description.setText(item.getDescription());
        if (item.getDescription().equals(""))
            description.setText("Описание отсутствует");

        (rootView.findViewById(R.id.notif_title)).setVisibility(View.GONE);
        (rootView.findViewById(R.id.notif_cont)).setVisibility(View.GONE);

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

        (rootView.findViewById(R.id.change)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TasksForToday.EDIT = true;
                TasksForToday.EDIT_DATABASE_ID = databaseID;
                TasksForToday.EDIT_ID = id;
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

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
