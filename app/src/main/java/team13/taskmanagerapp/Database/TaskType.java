package team13.taskmanagerapp.Database;

public enum TaskType {
    BACKLOG, // Task without begin or end time (Chaos zone). Only OPTIONAL duration time.
    MILESTONE, // Task is currently going. Has end time, but not begin time.
    PLANNED // Task with begin and end time, that is planned in future.
}
