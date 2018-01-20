package team13.taskmanagerapp;

/**
 * Created by kate on 20.01.2018.
 */

class Item {
    private final String title;
    private Integer id;

    Item(String title, int id) {
        this.title = title;
        this.id = id;
    }

    String getTitle() {
        return this.title;
    }

    Integer getId() {
        return id;
    }
}
