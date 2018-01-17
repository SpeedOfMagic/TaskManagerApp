package team13.taskmanagerapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;
import team13.taskmanagerapp.TokenNotFoundException;
public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context){
        super(context, "TaskDB", null, 1);
    }
    private static DatabaseHelper getInstance(Context context){
        return new DatabaseHelper(context);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.rawQuery(
                ""+
                "CREATE TABLE Token (value TEXT PRIMARY KEY NOT NULL);"+
                "CREATE TABLE Account ("+
                        "id TEXT NOT NULL PRIMARY KEY,"+
                        "accountName TEXT NOT NULL,"+
                        "rootFolderId TEXT NOT NULL"+
                ");"+
                "CREATE TABLE Task ("+
                        "id TEXT NOT NULL PRIMARY KEY,"+
                        "accountId TEXT NOT NULL,"+
                        "title TEXT NOT NULL,"+
                        "description TEXT,"+
                        "status TEXT NOT NULL,"+
                        "taskType TEXT NOT NULL,"+
                        "startDate TEXT,"+
                        "endDate TEXT,"+
                        "duration INTEGER,"+
                        "FOREIGN KEY (accountId) REFERENCES Account(id)"+
                ")"

            ,null
        );

    }

    public String getToken(Context context) throws TokenNotFoundException{
        SQLiteDatabase db=getInstance(context).getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM Token;",null);
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
