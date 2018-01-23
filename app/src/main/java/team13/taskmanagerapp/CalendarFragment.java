package team13.taskmanagerapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

/**
 * Created by kate on 16.01.2018.
 */

public class CalendarFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Календарь");
        View rootView = inflater.inflate(R.layout.calendar, container, false);
        rootView.setBackgroundColor(getResources().getColor(R.color.white));
        CalendarView calendar = rootView.findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Вызов Fragment с делами на выбранный день
                Fragment fragment = new TasksForToday();
                Bundle date = new Bundle();
                date.putInt("year", year);
                date.putInt("month", month);
                date.putInt("dayOfMonth", dayOfMonth);
                fragment.setArguments(date);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame, fragment).addToBackStack("ViewTasksForDay").commit();
            }
        });
        return rootView;
    }
}
