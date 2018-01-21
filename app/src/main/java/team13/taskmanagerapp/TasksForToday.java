package team13.taskmanagerapp;

/**
 * Created by anton on 15.01.2018.
 */

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
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
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class TasksForToday extends Fragment {
    static boolean EDIT = false;
    static int EDIT_ID;
    static boolean READY;
    private int nextId = 0;
    private RecyclerView recyclerView;
    final DataSource dataSource = new DataSource();

    private static final int NEW_TASK_CODE = 1171;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (EDIT) {
            EDIT = false;
            editTask(EDIT_ID);
        }

        Calendar now = Calendar.getInstance();

        int cur_year = now.get(Calendar.YEAR), cur_month = now.get(Calendar.MONTH), cur_day = now.get(Calendar.DAY_OF_MONTH);

        int year = cur_year, month = cur_month, dayOfMonth = cur_day;
        if (getArguments() != null) {
            year = getArguments().getInt("year", cur_year);
            month = getArguments().getInt("month", cur_month);
            dayOfMonth = getArguments().getInt("dayOfMonth", cur_day);
        }

        if (year == cur_year && month == cur_month && dayOfMonth == cur_day) {
            getActivity().setTitle("Сегодня");
        } else {
            getActivity().setTitle(format(dayOfMonth) + "." + format(month + 1) + "." + year);
        }

        View rootView = inflater.inflate(R.layout.tasksfortoday, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(getActivity(), NewActionActivity.class);
              intent.putExtra("id", nextId);
              startActivityForResult(intent, NEW_TASK_CODE);
              nextId++;
          }
        });

        recyclerView = rootView.findViewById(R.id.container);
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ItemViewHolder(
                    getActivity().getLayoutInflater().inflate(R.layout.card, parent, false)
                );
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                Item item = dataSource.getItem(position);
                ((ItemViewHolder) holder).bind(item, item.getId());
            }

            @Override
            public int getItemCount() {
                return dataSource.getCount();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    String format(int time) {
        if (time < 10)
            return "0" + time;
        return "" + time;
    }

    class DataSource {

        private final List<Item> items = new ArrayList<>();

        int getCount() {
            return items.size();
        }

        Item getItem(int position) {
            return items.get(position);
        }

        void addItem(Item item) {
            items.add(item);
            final int position = items.size() - 1;
            recyclerView.getAdapter().notifyItemInserted(position);
            recyclerView.scrollToPosition(position);
        }

        void removeTask(int id) {
            for (int position = 0; position < items.size(); position++) {
                if (items.get(position).getId().equals(id)) {
                    items.remove(position);
                    recyclerView.getAdapter().notifyItemRemoved(position);
                    break;
                }
            }
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private Button dlttsk;
        private Button edttsk;
        RelativeLayout begin, end;

        ItemViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.text1);
            dlttsk = itemView.findViewById(R.id.dlttsk);
            edttsk = itemView.findViewById(R.id.edttsk);
            begin = itemView.findViewById(R.id.begin);
            end = itemView.findViewById(R.id.end);
        }

        void bind(Item item, final int id) {
            title.setText(item.getTitle());
            dlttsk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataSource.removeTask(id);
                }
            });
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new ViewActionFragment();
                    Bundle inf = new Bundle();
                    // кладем нужную информацию
                    inf.putInt("Id", id);
                    fragment.setArguments(inf);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame, fragment).addToBackStack("ViewExactTask").commit();
                }
            });
            edttsk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                editTask(id);
                }
            });
            if (!item.getBeginHour().equals("") && !item.getBeginHour().equals("")) {
                getLayoutInflater().inflate(R.layout.time_box, begin, true);
                ((TextView) begin.findViewById(R.id.hour)).setText(item.getBeginHour());
                ((TextView) begin.findViewById(R.id.min)).setText(item.getBeginMin());
            }
            if (!item.getEndHour().equals("") && !item.getEndHour().equals("")) {
                getLayoutInflater().inflate(R.layout.time_box, end, true);
                ((TextView) end.findViewById(R.id.hour)).setText(item.getEndHour());
                ((TextView) end.findViewById(R.id.min)).setText(item.getEndMin());
            }
        }
    }

    void editTask(int id) {
        Intent intent = new Intent(getActivity(), NewActionActivity.class);
        intent.putExtra("title", "Редактирование события");
        intent.putExtra("id", id);
        // добавляем нужную информацию
        startActivityForResult(intent, NEW_TASK_CODE);
    }

    @Override
    public void onActivityResult(int code, int result, Intent data) {
        Log.d("onActivityResult", code + " " + result + " " + RESULT_OK);
        if (code == NEW_TASK_CODE && result == RESULT_OK) {
            //Log.d("onActivityResult", "on");
            if (data.hasExtra("title") && data.hasExtra("id")) {
                int id = data.getIntExtra("id", 0);
                dataSource.removeTask(id);
                Item task = new Item(data.getStringExtra("title"), id);

                String beginHour = data.getStringExtra("beginHour");
                String beginMin = data.getStringExtra("beginMin");
                String endHour = data.getStringExtra("endHour");
                String endMin = data.getStringExtra("endMin");

                if (!(beginHour).equals("") && !(beginMin).equals("")) {
                    task.setBegin(beginHour, beginMin);
                }
                if (!(endHour).equals("") && !(endMin).equals("")) {
                    task.setEnd(endHour, endMin);
                }

                dataSource.addItem(task);
            }
        }
    }
}
