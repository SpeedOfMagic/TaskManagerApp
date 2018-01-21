package team13.taskmanagerapp.Database;
import android.support.annotation.*;

public class Task{
    @Nullable private String description;
    @Nullable private String startDate;
    @Nullable private String endDate;
    @Nullable private int duration;
    @NonNull private String id;
    @NonNull private String accountId;
    @NonNull private String title;
    @NonNull private TaskStatus status;
    @NonNull private TaskType type;

    public Task(){}
    public Task(String id,String accountId,String title,String description,TaskStatus status,
                TaskType taskType,String startDate,String endDate,int duration){
        this.id = id;
        this.accountId = accountId;
        this.title = title;
        this.description = description;
        this.status = status;
        type = taskType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    @Nullable public String   getBeginHour(){return this.getStartDate().substring(13,15);}
    @Nullable public String getBeginMinute(){return this.getStartDate().substring(15,17);}
    @Nullable public String     getEndHour(){return this.getEndDate().substring(13,15);}
    @Nullable public String   getEndMinute(){return this.getEndDate().substring(15,17);}
    @NonNull  public String getAccountId() {return accountId;}
    @NonNull  public TaskStatus getStatus() {return status;}
    @NonNull  public TaskType getType() {return type;}
    @NonNull  public String getTitle() {return title;}
    @Nullable public String getDescription() {return description;}
    @NonNull  public String getId() {return id;}
    @Nullable public String getStartDate() {return startDate;}
    @Nullable public String getEndDate() {return endDate;}
    @Nullable public int getDuration() {return duration;}
    public void setAccountId(@NonNull String accountId) {this.accountId = accountId;}
    public void setStatus(@NonNull TaskStatus status) {this.status = status;}
    public void setType(@NonNull TaskType type) {this.type = type;}
    public void setTitle(@NonNull String title) {this.title = title;}
    public void setDescription(@Nullable String description) {this.description = description;}
    public void setStartDate(@Nullable String startDate) {this.startDate = startDate;}
    public void setEndDate(@Nullable String endDate) {this.endDate = endDate;}
    public void setDuration(@Nullable int duration) {this.duration = duration;}
    public void setId(@NonNull String id) {this.id = id;}



    public static class TaskBuilder {
        private Task task;
        public Task build() {return this.task;}
        public TaskBuilder(){}
        public TaskBuilder(Task task){this.task=task;}
        public TaskBuilder id(String tid){
            task.id=tid;
            return this;
        }
        public TaskBuilder description(String tdescription){
            task.description=tdescription;
            return this;
        }
        public TaskBuilder duration(int tduration){
            task.duration=tduration;
            return this;
        }
        public TaskBuilder title(String ttitle){
            task.title=ttitle;
            return this;
        }
        public TaskBuilder status(TaskStatus tstatus){
            task.status=tstatus;
            return this;
        }
        public TaskBuilder type(TaskType ttype){
            task.type=ttype;
            return this;
        }
        public TaskBuilder startDate(String tstartDate){
            task.startDate=tstartDate;
            return this;
        }
        public TaskBuilder endDate(String tendDate){
            task.endDate=tendDate;
            return this;
        }
        public TaskBuilder accountId(String taccountId){
            task.accountId=taccountId;
            return this;
        }
        
    }
}










































