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
            Fragment fragment = new TasksForToday();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
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
