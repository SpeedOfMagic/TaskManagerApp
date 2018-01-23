package team13.taskmanagerapp;

public class Item {
    private String title = "";
    private Integer id = 0;
    private long databaseID;
    private String begin_min = "", begin_hour = "";
    private String end_min = "", end_hour = "";
    private boolean ready = false;
    private int timeInMinutes = 0;

    Item() {}

    long getDatabaseID() {
        return databaseID;
    }

    void setDatabaseID(long databaseID) {
        this.databaseID = databaseID;
    }

    Item(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    void setBegin(String hour, String min) {
        begin_hour = hour;
        begin_min = min;
        timeInMinutes = getBeginTime();
    }

    private int getBeginTime() {
        if (begin_hour == null || begin_min == null || begin_hour.equals("") || begin_min.equals(""))
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

    public String getTitle() {
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
