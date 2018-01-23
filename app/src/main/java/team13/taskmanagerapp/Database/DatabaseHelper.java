package team13.taskmanagerapp.Database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static team13.taskmanagerapp.Database.Contract.DB_NAME;
import static team13.taskmanagerapp.Database.Contract.DB_VERSION;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_DATE;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_DESCRIP;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_END_HOUR;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_END_MIN;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_START_HOUR;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_START_MIN;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_STATUS;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.COL_TASK_TITLE;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.DONE;
import static team13.taskmanagerapp.Database.Contract.TaskEntry.TABLE_TASK;
import static team13.taskmanagerapp.Database.Contract.UserEntry.COL_PASSWORD;
import static team13.taskmanagerapp.Database.Contract.UserEntry.COL_USER;
import static team13.taskmanagerapp.Database.Contract.UserEntry.TABLE_USER;


public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.d("Database", "--- onCreate database ---");
        db.execSQL(
                "CREATE TABLE " + TABLE_TASK + " ( " +
                        Contract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_TASK_STATUS + " TEXT NOT NULL, " +
                        COL_TASK_TITLE + " TEXT NOT NULL, " +
                        COL_TASK_START_HOUR + " TEXT NOT NULL, " +
                        COL_TASK_START_MIN + " TEXT NOT NULL, " +
                        COL_TASK_END_HOUR + " TEXT NOT NULL, " +
                        COL_TASK_END_MIN + " TEXT NOT NULL, " +
                        COL_TASK_DATE + " TEXT NOT NULL, " +
                        COL_TASK_DESCRIP + " TEXT NOT NULL ); "
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_USER + " ( " +
                        Contract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_USER + " TEXT NOT NULL, " +
                        COL_PASSWORD + " TEXT NOT NULL ); "
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.TaskEntry.TABLE_TASK);
        onCreate(db);
    }
}
