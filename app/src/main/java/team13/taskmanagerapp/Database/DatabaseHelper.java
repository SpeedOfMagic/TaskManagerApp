package team13.taskmanagerapp.Database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import team13.taskmanagerapp.Item;

@SuppressWarnings("unused")
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String[] intToCol={
            "id","accountId","title","description","status","type","startDate","endDate","duration"
    };
    public DatabaseHelper(Context context){
        super(context, "TaskDB", null, 1);
    }
    private static DatabaseHelper getInstance(Context context){return new DatabaseHelper(context);}
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS Token (value TEXT PRIMARY KEY NOT NULL);");
        db.execSQL("CREATE TABLE IF NOT EXISTS User ("+
                        "id TEXT NOT NULL PRIMARY KEY,"+
                        "firstName TEXT NOT NULL,"+
                        " lastName TEXT NOT NULL,"+
                        "avatarUrl TEXT NOT NULL" +
                ");"
        );
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS Account ("+
                        "id TEXT NOT NULL PRIMARY KEY,"+
                        "accountName TEXT NOT NULL,"+
                        "rootFolderId TEXT NOT NULL,"+
                        "userId TEXT NOT NULL,"+
                        "FOREIGN KEY (userId) REFERENCES User(id)"+
                ");"
        );
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS Task ("+
                        "id TEXT NOT NULL PRIMARY KEY,"+
                        "accountId TEXT /*NOT NULL*/,"+
                        "title TEXT NOT NULL,"+
                        "description TEXT,"+
                        "status TEXT NOT NULL,"+
                        "type TEXT NOT NULL,"+
                        "startDate TEXT,"+
                        "endDate TEXT,"+
                        "duration INTEGER"+
                        //"-- FOREIGN KEY (accountId) REFERENCES Account(id)"+
                ")"
        );
    }
    @NonNull
    private static Cursor executeSelectQuery(SQLiteDatabase db, @NonNull String query){
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        return cursor;
    }
    private static void executeChangeQuery(SQLiteDatabase db, @NonNull String query){
        db.execSQL(query);
    }
    @NonNull
    public static List<Item> getTasksAtCurrentDate(SQLiteDatabase db,int years,int months,int days) {
        String date=years+"-"+(months<10?"0":"")+months+"-"+(days<10?"0":"")+days+"*";
        Cursor cursor=executeSelectQuery(db,
                String.format(
                 "SELECT * FROM Task WHERE startDate GLOB \"%s\" OR endDate GLOB \"%s\""
                        ,date,date));
        cursor.moveToFirst();
        List<Item> taskList=new ArrayList<>();
        if (cursor.getCount()==0)return taskList;
        do {
            taskList.add(Item.valueOf(getTaskFromCursor(cursor)));
        } while (cursor.moveToNext());
        cursor.close();
        return taskList;
    }
    @Nullable
    public static Task getTaskById(SQLiteDatabase db,@NonNull String taskId){
        String query=String.format("SELECT * FROM Task WHERE id=\"%s\"",taskId);
        Cursor cursor=executeSelectQuery(db,query);
        if (cursor.getCount()==0) {
            Log.e("Database", "Task with that id hasn't been found");
            cursor.close();
            return null;
        }
        return getTaskFromCursor(cursor);
    }
    @NonNull
    private static Task getTaskFromCursor(@NonNull Cursor cursor){
        return new TaskBuilder()
                .id(cursor.getString(0))
                .accountId(cursor.getString(1))
                .title(cursor.getString(2))
                .description(cursor.getString(3))
                .status(TaskStatus.valueOf(cursor.getString(4)))
                .type(TaskType.valueOf(cursor.getString(5)))
                .startDate(cursor.getString(6))
                .endDate(cursor.getString(7))
                .duration(cursor.getInt(8))
                .build();
    }
    public static long addTask(SQLiteDatabase db,@NonNull Item item){
        addTask(db,Task.valueOf(item));
        return Task.getRowId();
    }
    public static void addTask(SQLiteDatabase db,@NonNull Task task){
        executeChangeQuery(db,
                String.format(Locale.US,"INSERT INTO Task VALUES (" +
                                "\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%d);",
                        task.getId(),task.getAccountId(),task.getTitle(),task.getDescription(),
                        String.valueOf(task.getStatus()),String.valueOf(task.getType()),
                        task.getStartDate(),task.getEndDate(),task.getDuration()
                )
        );
    }
    /*
    public static void editTask(SQLiteDatabase db,@NonNull String taskId,
                                String colsToEdit,String[] valuesToEdit){
        if (valuesToEdit.length!=colsToEdit.length()) {
            Log.e("Database","Wrong parameters: valuesToEdit.length!=colsToEdit.length",
                    new InvalidParameterException());
            return;
        }
        StringBuilder query=new StringBuilder("UPDATE Task SET ");
        boolean useful=false;
        for (int i=0;i<colsToEdit.length();i++){
            if (colsToEdit.charAt(i)=='1'){
                useful=true;
                String toAppend=intToCol[i]+" = \""+valuesToEdit[i]+"\",";
                query.append(toAppend);
            }
        }
        if (!useful){
            Log.e("Database","There are no columns to update");
            return;
        }
        String toSend=query.substring(0,query.length()-1)+
                String.format(" WHERE Task.id = \"%s\"",taskId);
        executeChangeQuery(db,toSend);
    }
    */

    public static void eraseTask(SQLiteDatabase db,@NonNull Task task){
        eraseTaskById(db,task.getId());
    }
    public static void eraseTaskById(SQLiteDatabase db,String id){
        executeChangeQuery(db,"DELETE FROM Task WHERE Task.id="+"\""+id+"\"");
    }
    public static void eraseAllTasks(SQLiteDatabase db){executeChangeQuery(db,"DELETE FROM Task");}
    @Nullable
    public static String getToken(SQLiteDatabase db) throws TokenNotFoundException{
        Cursor cursor=executeSelectQuery(db,"SELECT value FROM Token");
        if (cursor.getCount()!=1) {
            cursor.close();
            Log.e("Token", "Token not found");
            throw new TokenNotFoundException();
        }
        String token=cursor.getString(0);
        cursor.close();
        return token;
    }
    public static void setToken(SQLiteDatabase db,@NonNull String token){
        executeChangeQuery(db,"INSERT INTO Token VALUES(\""+token+"\");");
    }
    public static void eraseToken(SQLiteDatabase db){executeChangeQuery(db,"DELETE FROM Token");}
    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion){
        db.setVersion(newVersion);
    }
}
