package team13.taskmanagerapp.Database;

import android.database.Cursor;

/**
 * Created by kate on 22.01.2018.
 */

public class DBMethods {
    public static String getString(Cursor cursor, String column) {
        int index = cursor.getColumnIndex(column);
        return cursor.getString(index);
    }
    public static int getInt(Cursor cursor, String column) {
        int index = cursor.getColumnIndex(column);
        return cursor.getInt(index);
    }
}
