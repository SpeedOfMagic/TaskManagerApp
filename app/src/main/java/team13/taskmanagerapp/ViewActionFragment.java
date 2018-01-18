package team13.taskmanagerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kate on 18.01.2018.
 */

public class ViewActionFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Событие"); // тут название таска
        View rootView = inflater.inflate(R.layout.view_task, container, false);

        ((TextView) rootView.findViewById(R.id.description)).setText("Описание");

        //Каким-то образом загружаем данные о времени начала и конца

        if (true) { // Проверка, есть ли начало у события
            RelativeLayout layout = rootView.findViewById(R.id.begin);
            getLayoutInflater().inflate(R.layout.time, layout, true);
            ((TextView) layout.findViewById(R.id.title)).setText("Начало");
            int hour = 12;
            int minutes = 0;
            ((TextView) layout.findViewById(R.id.hour)).setText(format(hour));
            ((TextView) layout.findViewById(R.id.min)).setText(format(minutes));
        }


        if (true) { // Проверка, есть ли конец у события
            RelativeLayout layout = rootView.findViewById(R.id.end);
            getLayoutInflater().inflate(R.layout.time, layout, true);
            ((TextView) layout.findViewById(R.id.title)).setText("Конец");
            int hour = 13;
            int minutes = 0;
            ((TextView) layout.findViewById(R.id.hour)).setText(format(hour));
            ((TextView) layout.findViewById(R.id.min)).setText(format(minutes));
        }

        final List<Notification> notif = new ArrayList<>();

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

        });

        (rootView.findViewById(R.id.change)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewActionActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private class NotificationViewHolder extends RecyclerView.ViewHolder{
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
    }

    String format(int time) {
        if (time < 10)
            return "0" + time;
        return "" + time;
    }
}
