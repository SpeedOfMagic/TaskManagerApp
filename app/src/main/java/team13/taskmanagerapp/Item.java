package team13.taskmanagerapp;

import team13.taskmanagerapp.Database.Task;

/**
 * Created by kate on 20.01.2018.
 */

public class Item{
    private String title;
    private Integer id;
    private String databaseID;
    private String begin_min = "", begin_hour = "";
    private String end_min = "", end_hour = "";

    public static Item valueOf(Task task){
        Item newItem=new Item();
        newItem.databaseID=task.getId();newItem.title=task.getTitle();
        newItem.begin_hour=task.getBeginHour();newItem.begin_min=task.getBeginMinute();
        newItem.end_hour=task.getEndHour();newItem.end_min=task.getEndMinute();
        return newItem;
    }
    private Item(){}
    Item(String title, int id) {
        this.title = title;
        this.id = id;
    }

    void setBegin(String hour, String min) {
        begin_hour = hour;
        begin_min = min;
    }

    void setEnd(String hour, String min) {
        end_hour = hour;
        end_min = min;
    }

    String getTitle() {
        return this.title;
    }

    Integer getId() {
        return id;
    }

    String getBeginHour() {
        return begin_hour;
    }

    String getBeginMin() {
        return begin_min;
    }

    String getEndHour() {
        return end_hour;
    }

    String getEndMin() {
        return end_min;
    }
}
