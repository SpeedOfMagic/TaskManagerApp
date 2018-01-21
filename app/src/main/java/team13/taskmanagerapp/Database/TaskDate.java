package team13.taskmanagerapp.Database;

//Доделать
public class TaskDate {
    String date;
    public void verify(String date) throws WrongDateFormatException {
        String[] yearMonthOther = date.split("-");
        String[] dayTime = yearMonthOther[2].split("T");
        String[] hoursMinutesSeconds = dayTime[1].split(":");
        String seconds=hoursMinutesSeconds[2], minutes=hoursMinutesSeconds[1],
                hours=hoursMinutesSeconds[0],day=dayTime[0],month=yearMonthOther[1],
                year=yearMonthOther[0];
        if (day.length()==2){ //Доделать
            this.date=date;
        } else throw new WrongDateFormatException();
    }
    public TaskDate(){}
    public TaskDate(String date){
        verify(date);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        verify(date);
    }
}
