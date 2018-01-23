package team13.taskmanagerapp.Database;

import android.provider.BaseColumns;

/**
 * Created by kate on 22.01.2018.
 */

public class Contract {
    static final String DB_NAME = "team13.taskmanagerapp";
    static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE_TASK = "tasks";
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_DESCRIP = "description";
        public static final String COL_TASK_STATUS = "status";
        public static final String COL_TASK_DATE = "startDate";
        public static final String COL_TASK_START_HOUR = "startHour";
        public static final String COL_TASK_START_MIN = "startMin";
        public static final String COL_TASK_END_HOUR = "endHour";
        public static final String COL_TASK_END_MIN = "endMin";
        static final String DONE = "done";
        static final String NOT_DONE = "not_done";
    }

    public class UserEntry implements BaseColumns {
        public static final String TABLE_USER = "users";
        public static final String COL_USER = "user";
        public static final String COL_PASSWORD = "password";
    }

    public static String status(boolean ready) {
        if (ready)
            return TaskEntry.DONE;
        return TaskEntry.NOT_DONE;
    }

    public static boolean status(String status) {
        return status.equals(TaskEntry.DONE);
    }
}
