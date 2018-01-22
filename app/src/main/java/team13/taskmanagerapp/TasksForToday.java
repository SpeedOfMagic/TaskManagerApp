package team13.taskmanagerapp;

/**
 * Created by anton on 15.01.2018.
 */

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import team13.taskmanagerapp.Database.DatabaseHelper;

import static android.app.Activity.RESULT_OK;

public class TasksForToday extends Fragment {
    static boolean EDIT = false;
    static int EDIT_ID;
    static String EDIT_DATABASE_ID;
    private int nextId = 0;
    private RecyclerView recyclerView;
    final DataSource dataSource = new DataSource();
    DatabaseHelper databaseHelper;
    int kyear, kmonth, kdayOfMonth;

    private static final int NEW_TASK_CODE = 1171;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());

        Calendar now = Calendar.getInstance();

        int cur_year = now.get(Calendar.YEAR), cur_month = now.get(Calendar.MONTH), cur_day = now.get(Calendar.DAY_OF_MONTH);

        kyear = cur_year;
        kmonth = cur_month;
        kdayOfMonth = cur_day;
        if (getArguments() != null) {
            kyear = getArguments().getInt("year", cur_year);
            kmonth = getArguments().getInt("month", cur_month);
            kdayOfMonth = getArguments().getInt("dayOfMonth", cur_day);
        }

        final int year = kyear, month = kmonth, dayOfMonth = kdayOfMonth; // Простите за костыли

        if (EDIT) {
            EDIT = false;
            editTask(EDIT_DATABASE_ID, EDIT_ID);
        }

        if (year == cur_year && month == cur_month && dayOfMonth == cur_day) {
            getActivity().setTitle("Сегодня");
        } else {
            getActivity().setTitle(format(dayOfMonth) + "." + format(month + 1) + "." + year);
        }

        View rootView = inflater.inflate(R.layout.tasksfortoday, container, false);

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
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
                ((ItemViewHolder) holder).bind(item);
            }

            @Override
            public int getItemCount() {
                return dataSource.getCount();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*List <Item> list = DatabaseHelper.getTasksAtCurrentDate(databaseHelper.getWritableDatabase(), year, month, dayOfMonth);

        for (Item item : list) {
            item.setId(nextId);
            nextId++;
            dataSource.addItem(item);
        }*/

        return rootView;
    }

    String format(int time) {
        if (time < 10)
            return "0" + time;
        return "" + time;
    }

    private class DataSource {

        private final List<Item> items = new ArrayList<>();

        int getCount() {
            return items.size();
        }

        Item getItem(int position) {
            return items.get(position);
        }

        void addItem(Item item) {
            int position = 0;
            for ( ; position < items.size(); position++) {
                int cur_min = items.get(position).getTimeInMinutes();
                int new_min = item.getTimeInMinutes();
                if (cur_min > new_min) {
                    break;
                }
            }
            items.add(position, item);
            recyclerView.getAdapter().notifyItemInserted(position);
            recyclerView.scrollToPosition(position);
        }

        void removeTask(int id) {
            for (int position = 0; position < items.size(); position++) {
                if (items.get(position).getId().equals(id)) {
                    DatabaseHelper.eraseTaskById(databaseHelper.getWritableDatabase(), items.get(position).getDatabaseID());
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
        ViewGroup begin, end;
        private CheckBox checkBox;
        Button dash;

        ItemViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.text1);
            dlttsk = itemView.findViewById(R.id.dlttsk);
            edttsk = itemView.findViewById(R.id.edttsk);
            begin = itemView.findViewById(R.id.begin);
            end = itemView.findViewById(R.id.end);
            checkBox = itemView.findViewById(R.id.checkBox);
            dash = itemView.findViewById(R.id.dash);
        }

        void bind(final Item item) {
            title.setText(item.getTitle());
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new ViewActionFragment();
                    Bundle inf = new Bundle();
                    // кладем нужную информацию
                    inf.putString("databaseID", item.getDatabaseID());
                    inf.putInt("id", item.getId());
                    fragment.setArguments(inf);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame, fragment).addToBackStack("ViewExactTask").commit();
                }
            });
            edttsk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editTask(item.getDatabaseID(), item.getId());
                }
            });

            dlttsk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataSource.removeTask(item.getId());
                }
            });

            if (!item.getBeginHour().equals("") && !item.getBeginHour().equals("")) {
                begin.setVisibility(View.VISIBLE);
                ((TextView) begin.findViewById(R.id.hour)).setText(item.getBeginHour());
                ((TextView) begin.findViewById(R.id.min)).setText(item.getBeginMin());
            } else {
                begin.setVisibility(View.GONE);
            }

            if (!item.getEndHour().equals("") && !item.getEndHour().equals("")) {
                end.setVisibility(View.VISIBLE);
                dash.setVisibility(View.VISIBLE);
                ((TextView) end.findViewById(R.id.hour)).setText(item.getEndHour());
                ((TextView) end.findViewById(R.id.min)).setText(item.getEndMin());
            } else {
                end.setVisibility(View.GONE);
                dash.setVisibility(View.GONE);
            }

            checkBox.setChecked(item.ifReady());
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataSource.removeTask(item.getId());
                    item.setIfReady(checkBox.isChecked());
                    dataSource.addItem(item);
                }
            });
        }
    }

    void editTask(String databeseID, int id) {
        Intent intent = new Intent(getActivity(), NewActionActivity.class);
        intent.putExtra("title", "Редактирование события");
        intent.putExtra("id", id);
        intent.putExtra("databaseID", databeseID);
        startActivityForResult(intent, NEW_TASK_CODE);
    }

    @Override
    public void onActivityResult(int code, int result, Intent data) {
        Log.d("onActivityResult", code + " " + result + " " + RESULT_OK);
        if (code == NEW_TASK_CODE && result == RESULT_OK) {
            if (data.hasExtra("title") && data.hasExtra("id")) {
                int id = data.getIntExtra("id", 0);
                dataSource.removeTask(id);
                Item task = new Item(data.getStringExtra("title"), id);

                String beginHour = data.getStringExtra("beginHour");
                String beginMin = data.getStringExtra("beginMin");
                String endHour = data.getStringExtra("endHour");
                String endMin = data.getStringExtra("endMin");
                String description = data.getStringExtra("description");

                task.setBegin(beginHour, beginMin);
                task.setEnd(endHour, endMin);

                Item item = new Item();
                item.setId(id);
                item.setBegin(beginHour, beginMin);
                item.setEnd(endHour, endMin);
                item.setDescription(description);
                item.setYear(kyear);
                item.setMonth(kmonth);
                item.setDayOfMonth(kdayOfMonth);

                //DatabaseHelper.addTask(databaseHelper.getWritableDatabase(), item);

                dataSource.addItem(task);
            }
        }
    }
}
