package team13.taskmanagerapp;

/**
 * Created by kate on 20.01.2018.
 */

class Item {
    private final String title;
    private Integer id;
    private String begin_min = "", begin_hour = "";
    private String end_min = "", end_hour = "";
    private boolean ready = false;
    private int timeInMinutes = 0;

    Item(String title, int id) {
        this.title = title;
        this.id = id;
    }

    void setBegin(String hour, String min) {
        begin_hour = hour;
        begin_min = min;
        timeInMinutes = getBeginTime();
    }

    private int getBeginTime() {
        if (begin_hour.equals("") || begin_min.equals(""))
            return 0;
        return Integer.valueOf(begin_hour) * 60 + Integer.valueOf(begin_min);
    }

    void setEnd(String hour, String min) {
        end_hour = hour;
        end_min = min;
    }

    void setIfReady(boolean ready) {
        this.ready = ready;
    }

    Boolean ifReady() {
        return ready;
    }

    String getTitle() {
        return title;
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

    int getTimeInMinutes() {
        int res = timeInMinutes;
        if (ready)
            res += 1e9;
        return res;
    }
}
