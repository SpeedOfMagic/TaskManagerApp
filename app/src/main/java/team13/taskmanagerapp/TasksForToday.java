package team13.taskmanagerapp;

/**
 * Created by anton on 15.01.2018.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import team13.taskmanagerapp.Database.Contract;
import team13.taskmanagerapp.Database.DBMethods;
import team13.taskmanagerapp.Database.DatabaseHelper;

import static android.app.Activity.RESULT_OK;
import static android.provider.BaseColumns._ID;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_DATE;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_DESCRIP;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_END_HOUR;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_END_MIN;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_START_HOUR;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_START_MIN;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_STATUS;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_TITLE;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.TABLE_TASK;

public class TasksForToday extends Fragment {
    static boolean EDIT = false;
    static int EDIT_ID;
    static long EDIT_DATABASE_ID;
    private int nextId = 0;
    private RecyclerView recyclerView;
    final DataSource dataSource = new DataSource();
    DatabaseHelper databaseHelper;
    int kyear, kmonth, kdayOfMonth;

    private static final int NEW_TASK_CODE = 1171;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tasksfortoday, container, false);
        rootView.setBackgroundColor(getResources().getColor(R.color.white));

        databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());

        Calendar now = Calendar.getInstance();

        int cur_year = now.get(Calendar.YEAR), cur_month = now.get(Calendar.MONTH), cur_day = now.get(Calendar.DAY_OF_MONTH);

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);

        kyear = cur_year;
        kmonth = cur_month;
        kdayOfMonth = cur_day;
        if (getArguments() != null) {
            kyear = getArguments().getInt("year", cur_year);
            kmonth = getArguments().getInt("month", cur_month);
            kdayOfMonth = getArguments().getInt("dayOfMonth", cur_day);
        }

        final int year = kyear, month = kmonth, dayOfMonth = kdayOfMonth; // Простите за костыли

        if (EDIT) { // Простите за костыли
            EDIT = false;
            editTask(EDIT_DATABASE_ID, EDIT_ID);
        }

        if (year == cur_year && month == cur_month && dayOfMonth == cur_day) {
            getActivity().setTitle("Сегодня");
        } else if (year == tomorrow.get(Calendar.YEAR) && month == tomorrow.get(Calendar.MONTH) && dayOfMonth == tomorrow.get(Calendar.DAY_OF_MONTH)) {
            getActivity().setTitle("Завтра");
        } else if (year == yesterday.get(Calendar.YEAR) && month == yesterday.get(Calendar.MONTH) && dayOfMonth == yesterday.get(Calendar.DAY_OF_MONTH)) {
            getActivity().setTitle("Вчера");
        } else{
            getActivity().setTitle(format(dayOfMonth) + " " + getResources().getStringArray(R.array.months)[month] + " " + year);
        }

        final FloatingActionButton fab = rootView.findViewById(R.id.fab);
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 && fab.isShown())
                    fab.hide();
                if (dy < 0)
                    fab.show();
            }
        });

        /*List <Item> list = DatabaseHelper.getTasksAtCurrentDate(databaseHelper.getWritableDatabase(), year, month, dayOfMonth);

        for (Item item : list) {
            item.setId(nextId);
            nextId++;
            dataSource.addItem(item);
        }*/

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(Contract.TaskEntry.TABLE_TASK,
                new String[]{_ID, COL_TASK_TITLE, COL_TASK_STATUS, COL_TASK_START_HOUR, COL_TASK_START_MIN, COL_TASK_END_HOUR, COL_TASK_END_MIN},
            COL_TASK_DATE + " = ? ", new String[] {format(kdayOfMonth) + ":" + format(kmonth) + ":" + kyear},
                null, null, null, null);

        while (cursor.moveToNext()) {
            Item task = new Item(DBMethods.getString(cursor, COL_TASK_TITLE), nextId);
            nextId++;

            task.setBegin(DBMethods.getString(cursor, COL_TASK_START_HOUR), DBMethods.getString(cursor, COL_TASK_START_MIN));
            task.setEnd(DBMethods.getString(cursor, COL_TASK_END_HOUR), DBMethods.getString(cursor, COL_TASK_END_MIN));
            task.setIfReady(Contract.status(DBMethods.getString(cursor, COL_TASK_STATUS)));

            task.setDatabaseID(DBMethods.getInt(cursor, _ID));

            dataSource.addItem(task);
        }

        cursor.close();
        db.close();

        return rootView;
    }

    String format(int time) {
        if (time < 10)
            return "0" + time;
        return "" + time;
    }

    private class DataSource {

        private final List<Item> items = new ArrayList<>();
        private final List<Long> databaseIDs = new ArrayList<>();

        int getCount() {
            return items.size();
        }

        Item getItem(int position) {
            return items.get(position);
        }

        void addItem(Item item) {
            for (long id : databaseIDs) {
                if (id == item.getDatabaseID())
                    return;
            }
            int position = 0;
            for ( ; position < items.size(); position++) {
                int cur_min = items.get(position).getTimeInMinutes();
                int new_min = item.getTimeInMinutes();
                if (cur_min > new_min) {
                    break;
                }
            }
            items.add(position, item);
            databaseIDs.add(item.getDatabaseID());
            recyclerView.getAdapter().notifyItemInserted(position);
            recyclerView.scrollToPosition(position);
        }

        void removeTask(int id) {
            for (int position = 0; position < items.size(); position++) {
                if (items.get(position).getId().equals(id)) {
                    databaseIDs.remove(items.get(position).getDatabaseID());
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
                    inf.putLong("databaseID", item.getDatabaseID());
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
                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                    db.delete(TABLE_TASK, _ID + " = ? ", new String[] {String.valueOf(item.getDatabaseID())});
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
                ContentValues values = new ContentValues();
                values.put(COL_TASK_STATUS, Contract.status(item.ifReady()));
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                db.update(TABLE_TASK, values, _ID + " = ? ", new String[] {String.valueOf(item.getDatabaseID())});
                db.close();
                }
            });
        }
    }

    void editTask(long databaseID, int id) {
        Intent intent = new Intent(getActivity(), NewActionActivity.class);
        intent.putExtra("title", "Редактирование");
        intent.putExtra("id", id);
        intent.putExtra("databaseID", databaseID);
        startActivityForResult(intent, NEW_TASK_CODE);
    }

    @Override
    public void onActivityResult(int code, int result, Intent data) {
        if (code == NEW_TASK_CODE && result == RESULT_OK) {
            if (data.hasExtra("title") && data.hasExtra("id")) {
                int id = data.getIntExtra("id", -1);
                dataSource.removeTask(id);

                String title = data.getStringExtra("title");
                String beginHour = data.getStringExtra("beginHour");
                String beginMin = data.getStringExtra("beginMin");
                String endHour = data.getStringExtra("endHour");
                String endMin = data.getStringExtra("endMin");
                String description = data.getStringExtra("description");

                Item task = new Item(title, id);
                task.setBegin(beginHour, beginMin);
                task.setEnd(endHour, endMin);

                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(COL_TASK_TITLE, title);
                values.put(COL_TASK_DATE, format(kdayOfMonth) + ":" + format(kmonth) + ":" + kyear);
                values.put(COL_TASK_START_HOUR, beginHour);
                values.put(COL_TASK_START_MIN, beginMin);
                values.put(COL_TASK_END_HOUR, endHour);
                values.put(COL_TASK_END_MIN, endMin);
                values.put(COL_TASK_STATUS, Contract.status(false));
                values.put(COL_TASK_DESCRIP, description);

                if (data.hasExtra("databaseID")) {
                    long databaseID = data.getLongExtra("databaseID", 0);
                    db.update(TABLE_TASK, values, _ID + " = ? ", new String[] {String.valueOf(databaseID)});
                    task.setDatabaseID(databaseID);
                } else {
                    task.setDatabaseID(db.insert(Contract.TaskEntry.TABLE_TASK,null, values));
                }

                db.close();
                dataSource.addItem(task);
            }
        }
    }
}
