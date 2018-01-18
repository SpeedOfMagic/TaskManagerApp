package team13.taskmanagerapp.Database;

import android.content.Context;
import android.database.sqlite.*;
import android.database.*;
import android.util.Log;
import android.support.annotation.*;

import java.security.InvalidParameterException;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String[] intToCol=new String[9];
    private static void initIntToCol(){
        intToCol[0]="id";
        intToCol[1]="accountId";
        intToCol[2]="title";
        intToCol[3]="description";
        intToCol[4]="status";
        intToCol[5]="type";
        intToCol[6]="startDate";
        intToCol[7]="endDate";
        intToCol[8]="duration";
    }
    public DatabaseHelper(Context context){
        super(context, "TaskDB", null, 1);
    }
    private static DatabaseHelper getInstance(Context context){
        return new DatabaseHelper(context);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        initIntToCol();
        db.rawQuery(
                "CREATE TABLE Token (value TEXT PRIMARY KEY NOT NULL);"+
                "CREATE TABLE User ("+
                        "id TEXT NOT NULL PRIMARY KEY,"+
                        "firstName TEXT NOT NULL,"+
                        " lastName TEXT NOT NULL,"+
                        "avatarUrl TEXT NOT NULL" +
                ");"+
                "CREATE TABLE Account ("+
                        "id TEXT NOT NULL PRIMARY KEY,"+
                        "accountName TEXT NOT NULL,"+
                        "rootFolderId TEXT NOT NULL,"+
                        "userId TEXT NOT NULL,"+
                        "FOREIGN KEY (userId) REFERENCES User(id)"+
                ");"+
                "CREATE TABLE Task ("+
                        "id TEXT NOT NULL PRIMARY KEY,"+
                        "accountId TEXT NOT NULL,"+
                        "title TEXT NOT NULL,"+
                        "description TEXT,"+
                        "status TEXT NOT NULL,"+
                        "type TEXT NOT NULL,"+
                        "startDate TEXT,"+
                        "endDate TEXT,"+
                        "duration INTEGER"+
                        //"-- FOREIGN KEY (accountId) REFERENCES Account(id)"+
                ")"
            ,null
        );

    }
    private Cursor executeQuery(Context context,String query){
        SQLiteDatabase db=getInstance(context).getReadableDatabase();
        return db.rawQuery(query,null);
    }
    public Task getTaskById(Context context,String taskId){
        String query=String.format("SELECT * FROM Task WHERE id=?",new String[]{taskId});
        Cursor cursor=executeQuery(context,query);
        if (cursor.getCount()==0) {
            Log.e("Database", "Task with that id hasn't been found");
            cursor.close();
            return new Task();
        }
        return getTaskFromCursor(cursor);
    }
    @NonNull
    private Task getTaskFromCursor(Cursor cursor){
        return new Task(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),
                TaskStatus.valueOf(cursor.getString(4)),TaskType.valueOf(cursor.getString(5)),
                cursor.getString(6),cursor.getString(7),cursor.getInt(8));
    }
    public void addTask(Context context,Task task){
        executeQuery(context,
                String.format(Locale.US,"INSERT INTO Task VALUES (" +
                                "%s,%s,%s,%s,%s,%s,%s,%s,%d);",
                        task.getId(),task.getAccountId(),task.getTitle(),task.getDescription(),
                        String.valueOf(task.getStatus()),String.valueOf(task.getType()),
                        task.getStartDate(),task.getEndDate(),task.getDuration()
                )
        );
    }
    public void editTask(Context context,String taskId,boolean[] colsToEdit,String[] valuesToEdit){
        if (valuesToEdit.length!=colsToEdit.length) {
            Log.e("Database","Wrong parameters: valuesToEdit.length!=colsToEdit.length",
                    new InvalidParameterException());
            return;
        }
        StringBuilder query=new StringBuilder("UPDATE Task SET ");
        boolean useful=false;
        for (int i=0;i<colsToEdit.length;i++){
            if (colsToEdit[i]){
                useful=true;
                String toAppend=intToCol[i]+" = "+valuesToEdit[i]+",";
                query.append(toAppend);
            }
        }
        if (!useful){
            Log.e("Database","There are no columns to update");
            return;
        }
        String toSend=query.substring(0,query.length()-1)+
                String.format(" WHERE Task.id = %s",taskId);
        executeQuery(context,toSend);
    }
    public void removeTask(Context context,Task task){
        removeTaskById(context,task.getId());
    }
    public void removeTaskById(Context context,String id){
        executeQuery(context,"DELETE FROM Task WHERE Task.id="+id);
    }
    public String getToken(Context context) throws TokenNotFoundException{
        Cursor cursor=executeQuery(context,"SELECT * FROM Token");
        if (cursor.getCount()==0){
            cursor.close();
            Log.e("Token","Token not found",new TokenNotFoundException());
            return "";
        }
        String token=cursor.getString(0);
        cursor.close();
        return token;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.setVersion(newVersion);
    }
}
