package team13.taskmanagerapp.Database;
import android.support.annotation.*;

import team13.taskmanagerapp.Item;

public class Task{
    private static long rowId=0;
    @Nullable private String description, accountId, startDate, endDate;
    @Nullable private Integer duration;
    @NonNull private String id="Stub!", title="Stub!";
    @NonNull private TaskStatus status=TaskStatus.ACTIVE;
    @NonNull private TaskType type=TaskType.BACKLOG;

    public Task(){}

    public static Task valueOf(Item item){
        rowId++;
        String date=item.getYear()+"-"+(item.getMonth()<10?"0":"")+String.valueOf(item.getMonth())
                +(item.getDayOfMonth()<10?"0":"")+item.getDayOfMonth(),
            startDate=date+(item.getBeginHour().equals("")?"":"T"+item.getBeginHour()+":"+
                    item.getBeginMin()+":00"),
            endDate=date+(item.getEndHour().equals("")?"":"T"+item.getEndHour()+":"+
                    item.getEndMin()+":00");
        return new TaskBuilder()
                .title(item.getTitle()).type(TaskType.PLANNED)
                .status((item.ifReady()?TaskStatus.COMPLETED:TaskStatus.ACTIVE))
                .id(String.valueOf(rowId)).startDate(startDate).endDate(endDate)
                .build();
    }

    @Nullable public String getBeginHour() {
        if (this.getStartDate() == null || this.getStartDate().length() < 16) return null;
        return this.getStartDate().substring(11,13);
    }
    @Nullable public String getBeginMinute() {
        if (this.getStartDate() == null || this.getStartDate().length() < 16) return null;
        return this.getStartDate().substring(14,16);
    }
    @Nullable public String getEndHour() {
        if (this.getEndDate() == null || this.getEndDate().length() < 16) return null;
        return this.getEndDate().substring(11,13);
    }
    @Nullable public String getEndMinute() {
        if (this.getEndDate() == null || this.getEndDate().length() < 16) return null;
        return this.getEndDate().substring(14,16);
    }
    public static long getRowId() {return rowId;}
    @NonNull  public String getId() {return id;}
    @NonNull  public TaskStatus getStatus() {return status;}
    @NonNull  public TaskType getType() {return type;}
    @NonNull  public String getTitle() {return title;}
    @Nullable public String getAccountId() {return accountId;}
    @Nullable public String getDescription() {return description;}
    @Nullable public String getStartDate() {return startDate;}
    @Nullable public String getEndDate() {return endDate;}
    @Nullable public Integer getDuration() {return duration;}

    public static void setRowId(int rowId) {Task.rowId = rowId;}
    public void setId(@NonNull String id) {this.id = id;}
    public void setAccountId(@NonNull String accountId) {this.accountId = accountId;}
    public void setStatus(@NonNull TaskStatus status) {this.status = status;}
    public void setType(@NonNull TaskType type) {this.type = type;}
    public void setTitle(@NonNull String title) {this.title = title;}
    public void setDescription(@Nullable String description) {this.description = description;}
    public void setStartDate(@Nullable String startDate) {this.startDate = startDate;}
    public void setEndDate(@Nullable String endDate) {this.endDate = endDate;}
    public void setDuration(@Nullable int duration) {this.duration = duration;}
}










































