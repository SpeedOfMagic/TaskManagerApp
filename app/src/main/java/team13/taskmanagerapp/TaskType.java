package team13.taskmanagerapp;

public enum TaskType {
    Backlog, // Task without begin or end time (Chaos zone). Only OPTIONAL duration time.
    Milestone, // Task is currently going. Has end time, but not begin time.
    Planned // Task with begin and end time, that is planned in future.
}
