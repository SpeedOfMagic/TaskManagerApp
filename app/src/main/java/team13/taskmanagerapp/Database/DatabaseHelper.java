package team13.taskmanagerapp.Database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("unused")
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String[] intToCol={
            "id","accountId","title","description","status","type","startDate","endDate","duration"
    };
    private static final HashMap<String,String> monthsToNumber=new HashMap<>();

    private void initMonthsToNumber(){
        monthsToNumber.put("Jan","01");monthsToNumber.put("Feb","02");monthsToNumber.put("Mar","03");
        monthsToNumber.put("Apr","04");monthsToNumber.put("May","05");monthsToNumber.put("Jun","06");
        monthsToNumber.put("Jul","07");monthsToNumber.put("Aug","08");monthsToNumber.put("Sep","09");
        monthsToNumber.put("Oct","10");monthsToNumber.put("Nov","11");monthsToNumber.put("Dec","12");
    }

    private DatabaseHelper(Context context){
        super(context, "TaskDB", null, 1);
    }
    private static DatabaseHelper getInstance(Context context){
        return new DatabaseHelper(context);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        initMonthsToNumber();
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
    @NonNull
    private Cursor executeQuery(Context context, @NonNull String query){
        SQLiteDatabase db=getInstance(context).getReadableDatabase();
        return db.rawQuery(query,null);
    }
    @NonNull
    public List<Task> getTasksAtCurrentDate(Context context,@NonNull Date rawDate) {
        Cursor cursor=executeQuery(context,
                String.format("SELECT * FROM Task WHERE startDate=\"%s\"",dateToDBDate(rawDate)));
        List<Task> taskList=new ArrayList<>();
        while (!cursor.isLast()){
            taskList.add(getTaskFromCursor(cursor));
            cursor.moveToNext();
        } taskList.add(getTaskFromCursor(cursor));
        cursor.close();
        return taskList;
    }
    @Nullable
    public Task getTaskById(Context context,@NonNull String taskId){
        String query=String.format("SELECT * FROM Task WHERE id=\"%s\"",taskId);
        Cursor cursor=executeQuery(context,query);
        if (cursor.getCount()==0) {
            Log.e("Database", "Task with that id hasn't been found");
            cursor.close();
            return null;
        }
        return getTaskFromCursor(cursor);
    }
    @NonNull
    private Task getTaskFromCursor(@NonNull Cursor cursor){
        return new Task(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),
                TaskStatus.valueOf(cursor.getString(4)),TaskType.valueOf(cursor.getString(5)),
                cursor.getString(6),cursor.getString(7),cursor.getInt(8));
    }
    @NonNull
    private String dateToDBDate(@NonNull Date rawDate){
        String[]partsOfDate=rawDate.toString().split(" ");
        //Date.toString => <День недели> <Месяц> <День> <ЧЧ:ММ:СС> UTC <Год+1900>
        return String.valueOf(Integer.valueOf(partsOfDate[5])-1900)+"-"
                +monthsToNumber.get(partsOfDate[1])+"-"
                + (Integer.valueOf(partsOfDate[2])<10?"0":"")+partsOfDate[2];
    }
    public void addTask(Context context,@NonNull Task task){
        Cursor cursor=executeQuery(context,
                String.format(Locale.US,"INSERT INTO Task VALUES (" +
                                "\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%d);",
                        task.getId(),task.getAccountId(),task.getTitle(),task.getDescription(),
                        String.valueOf(task.getStatus()),String.valueOf(task.getType()),
                        task.getStartDate(),task.getEndDate(),task.getDuration()
                )
        );
        cursor.close();
    }

    public void editTask(Context context,@NonNull String taskId,boolean[] colsToEdit,String[] valuesToEdit){
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
        Cursor cursor=executeQuery(context,toSend);
        cursor.close();
    }

    /*
    public void removeTask(Context context,@NonNull Task task){
        removeTaskById(context,task.getId());
    }
    public void removeTaskById(Context context,String id){
        Cursor cursor=executeQuery(context,"DELETE FROM Task WHERE Task.id="+"\""+id+"\"");
        cursor.close();
    }
    */

    @Nullable
    public String getToken(Context context) throws TokenNotFoundException{
        Cursor cursor=executeQuery(context,"SELECT * FROM Token");
        if (cursor.getCount()!=1){
            cursor.close();
            Log.e("Token","Token not found",new TokenNotFoundException());
            return null;
        }
        String token=cursor.getString(0);
        cursor.close();
        return token;
    }
    public void setToken(Context context,@NonNull String token){
        Cursor cursor=executeQuery(context,"INSERT INTO Token VALUES(\""+token+"\");");
        cursor.close();
    }
    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion){
        db.setVersion(newVersion);
    }
}
