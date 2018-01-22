package team13.taskmanagerapp;

import android.util.Log;

import team13.taskmanagerapp.Database.Task;
import team13.taskmanagerapp.Database.TaskStatus;

public class Item {
    private String title;
    private Integer id;
    private String databaseID;
    private String begin_min = "", begin_hour = "";
    private String end_min = "", end_hour = "";
    private int year, month, dayOfMonth;
    private String description;
    private boolean ready = false;
    private int timeInMinutes = 0;

    public static Item valueOf(Task task){
        Item newItem = new Item();
        newItem.databaseID = task.getId();
        newItem.title = task.getTitle();
        newItem.setDescription(task.getDescription());
        if (task.getStartDate()!=null&&task.getStartDate().length()>=16)
            newItem.setBegin(task.getBeginHour(), task.getBeginMinute());
        if (task.getEndDate()!=null&&task.getEndDate().length()>=16)
            newItem.setEnd(task.getEndHour(), task.getEndMinute());
        newItem.setIfReady(task.getStatus().equals(TaskStatus.COMPLETED));
        if (task.getStartDate()==null||task.getStartDate().equals("null")){
            if (task.getEndDate()!=null&&!task.getEndDate().equals("null")){
                newItem.setDayOfMonth(Integer.valueOf(task.getEndDate().substring(8,10)));
                newItem.setMonth(Integer.valueOf(task.getEndDate().substring(5,7)));
                newItem.setYear(Integer.valueOf(task.getEndDate().substring(0,4)));
            }
        } else {
            newItem.setDayOfMonth(Integer.valueOf(task.getStartDate().substring(8,10)));
            newItem.setMonth(Integer.valueOf(task.getStartDate().substring(5,7)));
            newItem.setYear(Integer.valueOf(task.getStartDate().substring(0,4)));
        }
        return newItem;
    }

    private Item() {}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public Item(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBegin(String hour, String min) {
        begin_hour = hour;
        begin_min = min;
        timeInMinutes = getBeginTime();
    }

    private int getBeginTime() {
        if (begin_hour == null || begin_min == null || begin_hour.equals("") || begin_min.equals(""))
            return 0;
        return Integer.valueOf(begin_hour) * 60 + Integer.valueOf(begin_min);
    }

    public void setEnd(String hour, String min) {
        end_hour = hour;
        end_min = min;
    }

    public void setIfReady(boolean ready) {
        this.ready = ready;
    }

    public Boolean ifReady() {
        return ready;
    }

    public String getTitle() {
        return title;
    }

    Integer getId() {
        return id;
    }

    public String getBeginHour() {
        return begin_hour;
    }

    public String getBeginMin() {
        return begin_min;
    }

    public String getEndHour() {
        return end_hour;
    }

    public String getEndMin() {
        return end_min;
    }

    int getTimeInMinutes() {
        int res = timeInMinutes;
        if (ready)
            res += 1e9;
        return res;
    }
}
