package team13.taskmanagerapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import team13.taskmanagerapp.Database.Contract;
import team13.taskmanagerapp.Database.DatabaseHelper;

import static android.provider.BaseColumns._ID;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_DESCRIP;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_END_HOUR;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_END_MIN;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_START_HOUR;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_START_MIN;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_STATUS;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_TITLE;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.TABLE_TASK;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.test);

        final TextView text = findViewById(R.id.text);

        databaseHelper = new DatabaseHelper(this);

        (findViewById(R.id.add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("add", "I am here");
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(COL_TASK_DESCRIP, "aaaa");
                db.insert(TABLE_TASK, null, values);
                db.close();
                text.setText("add");
            }
        });

        (findViewById(R.id.see)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("see", "I am here");
                SQLiteDatabase db = databaseHelper.getReadableDatabase();

                Cursor cursor = db.query(Contract.TaskEntry.TABLE_TASK,
                        new String[]{_ID, COL_TASK_DESCRIP},
                /*COL_TASK_DATE + " = ? ", new String[] {format(kdayOfMonth) + ":" + format(kmonth) + ":" + kyear},
                        null, null, null, null, null);

                while (cursor.moveToNext()) {
                    int idx = cursor.getColumnIndex(_ID);
                    int i = cursor.getColumnIndex(COL_TASK_DESCRIP);
                    //Log.d("database", cursor.getInt(idx) + " " + cursor.getString(i));
                    text.setText(cursor.getString(i));
                }

                db.close();
                cursor.close();
            }
        });*/

        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        if (false) { // Проверка того, авторизован ли пользователь
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            View rootView = getLayoutInflater().inflate(R.layout.activity_log, (RelativeLayout) findViewById(R.id.layout), false);

            (rootView.findViewById(R.id.sendBtn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, WebLog.class);
                    startActivity(intent);
                }
            });

            builder.setView(rootView);

            AlertDialog alert = builder.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
            //alert.cancel();
        } else {
            Fragment fragment = new CalendarFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
            setTitle("Календарь");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Log.d("FragmentCount", getSupportFragmentManager().getBackStackEntryCount() + "");
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_today) {
            Fragment fragment = new TasksForToday();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
        } else if (id == R.id.nav_calendar) {
            Fragment fragment = new CalendarFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
