package org.frieling.myHygiene;

public class timelineViewModel {

    public static int NO_HHE  = 0;
    public static int YES_HHE = 1;

    public String message;
    public String date;
    public String status;
    public String HHEtime;

    timelineViewModel(String message, String date, String status, String HHEtime){

        this.message =message;
        this.HHEtime = HHEtime;
        this.date = date;
        this.status = status;

    }
}
