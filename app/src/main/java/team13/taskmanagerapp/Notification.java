package team13.taskmanagerapp;

/**
 * Created by kate on 14.01.2018.
 */

class Notification {
    private String message;
    private int minutes, hours;
    private Integer id;
    Notification(String message, int hours, int minutes, int id) {
        this.message = message;
        this.minutes = minutes;
        this.hours = hours;
        this.id = id;
    }
    void setMessage(String message) {
        this.message = message;
    }
    void setMinutes(int minutes) {
        this.minutes = minutes;
    }
    void setHours(int hours) {
        this.hours = hours;
    }
    String getMessage() {
        return this.message;
    }
    int getMinutes() {
        return this.minutes;
    }
    int getHours() {
        return this.hours;
    }
    Integer getId() {
        return this.id;
    }
}
