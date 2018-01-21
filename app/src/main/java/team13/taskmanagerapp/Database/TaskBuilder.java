package team13.taskmanagerapp.Database;

public class TaskBuilder {
    private Task task=new Task();
    public Task build() {return this.task;}
    public TaskBuilder(){}
    //public TaskBuilder(Task task){this.task=task;}
    public TaskBuilder id(String id){
        task.setId(id);
        return this;
    }
    public TaskBuilder accountId(String accountId){
        task.setAccountId(accountId);
        return this;
    }
    public TaskBuilder description(String description){
        task.setDescription(description);
        return this;
    }
    public TaskBuilder duration(int duration){
        task.setDuration(duration);
        return this;
    }
    public TaskBuilder title(String title){
        task.setTitle(title);
        return this;
    }
    public TaskBuilder status(TaskStatus status){
        task.setStatus(status);
        return this;
    }
    public TaskBuilder type(TaskType type){
        task.setType(type);
        return this;
    }
    public TaskBuilder startDate(String startDate){
        task.setStartDate(startDate);
        return this;
    }
    public TaskBuilder endDate(String endDate){
        task.setEndDate(endDate);
        return this;
    }


}
