package team13.taskmanagerapp;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import team13.taskmanagerapp.Database.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private DatabaseHelper dbHelper=new DatabaseHelper(InstrumentationRegistry.getTargetContext());
    private SQLiteDatabase readableDB=dbHelper.getReadableDatabase();
    private SQLiteDatabase writableDB=dbHelper.getWritableDatabase();

    private void clearDatabase(){
        DatabaseHelper.eraseAllTasks(writableDB);
        DatabaseHelper.eraseToken(writableDB);
    }

    //Subtask #1 - Token
    @Test
    public void getTokenSuccessfully() throws Exception{
        clearDatabase();
        DatabaseHelper.setToken(writableDB,"test");
        String token=DatabaseHelper.getToken(writableDB);
        assertEquals(token,"test");
    }
    @Test
    public void getTokenNotFoundException1() throws Exception{ //#1 - 0 tokens
        clearDatabase();
        @Nullable String token=null;
        try{
            token=DatabaseHelper.getToken(readableDB);
            fail();
        } catch (TokenNotFoundException exception){
            assertNull(token);
        }
    }
    @Test
    public void getTokenNotFoundException2()throws Exception{ //#2 - token was removed
        clearDatabase();
        DatabaseHelper.setToken(writableDB,"test");
        @Nullable String token=null;
        DatabaseHelper.eraseToken(writableDB);
        try{
            token=DatabaseHelper.getToken(readableDB);
            fail();
        } catch (TokenNotFoundException exception){
            assertNull(token);
        }
    }
    @Test
    public void getTokenNotFoundException3()throws Exception{ //#3 - multiple tokens
        clearDatabase();
        @Nullable String token=null;
        DatabaseHelper.setToken(writableDB,"test1");
        DatabaseHelper.setToken(writableDB,"test2");
        try{
            token=DatabaseHelper.getToken(readableDB);
            fail();
        } catch (TokenNotFoundException exception){
            assertNull(token);
        }
    }

    //Subtask #2 - Task
    @Test
    public void addTask() throws Exception{
        clearDatabase();
        Task task=new TaskBuilder().id("id").title("title").status(TaskStatus.ACTIVE)
                .type(TaskType.BACKLOG).build();
        DatabaseHelper.addTask(writableDB,task);
    }
    @Test
    public void getTaskProperty1() throws Exception{ //#1 - 1 task
        clearDatabase();
        Task task1=new TaskBuilder().id("id").accountId("accountId").title("title").duration(300)
                .description("description").status(TaskStatus.COMPLETED).type(TaskType.PLANNED)
                .startDate("2018-01-25T09:30:00").endDate("2018-01-25T09:35:00").build();
        DatabaseHelper.addTask(writableDB,task1);
        Task task2=DatabaseHelper.getTaskById(readableDB,"id");
        assertNotNull(task2);
        assertEquals(task2.getDescription(),"description");
    }
    @Test
    public void getTaskProperty2() throws Exception{ //#2 - 2 tasks
        clearDatabase();
        Task task1=new TaskBuilder().id("id1").accountId("accountId").title("title").duration(300)
                .description("description").status(TaskStatus.COMPLETED).type(TaskType.PLANNED)
                .startDate("2018-01-25T09:30:00").endDate("2018-01-25T09:35:00").build(),
             task2=new TaskBuilder().id("id2").accountId("accountId").title("title").duration(300)
                     .description("description").status(TaskStatus.COMPLETED).type(TaskType.PLANNED)
                     .startDate("2018-01-25T09:30:00").endDate("2018-01-25T09:45:00").build();
        DatabaseHelper.addTask(writableDB,task1);
        DatabaseHelper.addTask(writableDB,task2);
        Task task3=DatabaseHelper.getTaskById(readableDB,"id2");
        assertNotNull(task3);
        assertEquals(task3.getEndMinute(),"45");
    }
    @Test
    public void getTaskProperty3() throws Exception{ //#3 - 2 tasks,1 delete/replacement
        clearDatabase();
        Task task1=new TaskBuilder().id("id1").accountId("accountId").title("title").duration(300)
                .description("description").status(TaskStatus.COMPLETED).type(TaskType.PLANNED)
                .startDate("2018-01-25T09:30:00").endDate("2018-01-25T09:35:00").build(),
                task2=new TaskBuilder().id("id2").accountId("accountId2").title("title2").duration(600)
                        .description("description2").status(TaskStatus.COMPLETED).type(TaskType.PLANNED)
                        .startDate("2018-01-25T09:35:00").endDate("2018-01-25T09:45:00").build(),
                task3=new TaskBuilder().id("id2").accountId("accountId3").title("title3").duration(1500)
                        .description("description3").status(TaskStatus.COMPLETED).type(TaskType.PLANNED)
                        .startDate("2018-01-25T09:35:00").endDate("2018-01-25T10:00:00").build();
        DatabaseHelper.addTask(writableDB,task1);
        DatabaseHelper.addTask(writableDB,task2);

        Task task4=DatabaseHelper.getTaskById(readableDB,"id2");

        assertNotNull(task4);assertNotNull(task4.getDuration());
        assertEquals(task4.getDuration().longValue(),600);

        DatabaseHelper.eraseTask(writableDB,task2);
        DatabaseHelper.eraseTaskById(writableDB,"id1");
        DatabaseHelper.addTask(writableDB,task3);

        task4=DatabaseHelper.getTaskById(readableDB,"id2");

        assertNotNull(task4);assertNotNull(task4.getDuration());
        assertEquals(task4.getDuration().longValue(),1500);
    }
    /*
    @Test
    public void getProperty4() throws Exception{ //#4 - 2 tasks, 1 replacement
        Task task1=new TaskBuilder().id("id1").accountId("accountId").title("title").duration(300)
                .description("description").status(TaskStatus.COMPLETED).type(TaskType.PLANNED)
                .startDate("2018-01-25T09:30:00").endDate("2018-01-25T09:35:00").build(),
                task2=new TaskBuilder().id("id2").accountId("accountId2").title("title").duration(300)
                        .description("description").status(TaskStatus.COMPLETED).type(TaskType.PLANNED)
                        .startDate("2018-01-25T09:30:00").endDate("2018-01-25T09:45:00").build();
        DatabaseHelper.addTask(writableDB,task1);
        DatabaseHelper.addTask(writableDB,task2);
        Task task3=DatabaseHelper.getTaskById(readableDB,"id1");

        assertNotNull(task3);
        assertEquals(task3.getAccountId(),"accountId");

        DatabaseHelper.editTask(writableDB,"id","01",new String[]{"","accountId3"});

        assertNotNull(task3);
        assertEquals(task3.getAccountId(),"accountId3");
    }
    */
    //Subtask #3 - List of tasks
    @Test
    public void getListOfItems1() throws Exception{ //#1 - 2 tasks with that date
        clearDatabase();
        Task task1=new TaskBuilder().id("id").title("title").status(TaskStatus.ACTIVE)
                .type(TaskType.BACKLOG).startDate("2018-01-21T22:30:00").build(),
             task2=new TaskBuilder().id("id2").title("title2").endDate("2018-01-21T23:30:00")
                     .status(TaskStatus.ACTIVE).type(TaskType.BACKLOG).build();
        DatabaseHelper.addTask(writableDB,task1);
        DatabaseHelper.addTask(writableDB,task2);
        List<Item> items=DatabaseHelper.getTasksAtCurrentDate(readableDB,2018,1,21);
        assertEquals(items.get(0).getBeginHour(),"22");
        assertEquals(items.get(1).getEndHour(),"23");
    }
    public void getListOfEmptyIDs1() throws Exception { //#1 - 0 tasks
        clearDatabase();
        assertEquals(DatabaseHelper.getTasksAtCurrentDate(readableDB,0,0,0),new ArrayList<Item>());
    }
    @Test
    public void getListOfEmptyIDs2() throws Exception { //#2 - all tasks' date does not match
        clearDatabase();

    }
}
