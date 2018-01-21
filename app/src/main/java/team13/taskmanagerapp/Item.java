package team13.taskmanagerapp;

/**
 * Created by kate on 20.01.2018.
 */

class Item {
    private final String title;
    private Integer id;
    private String begin_min = "", begin_hour = "";
    private String end_min = "", end_hour = "";

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
