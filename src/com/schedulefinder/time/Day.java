package com.schedulefinder.time;

public class Day {

    // * IMPORTANT CLASS INFORMATION
    /*
    DAYTIME     : 6AM -> 8PM (06 -> 20)
    NIGHTTIME   : 9PM -> 5AM (21 -> 05)

    For a day to be considered "available" there needs to be 6 hours available.
     */

    private int month, day, year;
    private Hour[] hours;

    /**
     * Constructs the Day object as a date (leaving all hours unavailable)
     */
    public Day(int MM, int DD, int YYYY){
        this.month = MM;
        this.day = DD;
        this.year = YYYY;

        this.hours = new Hour[24];
        this.setUnavailable();
    }

    /**
     * Constructs the Day object as part of a week (leaving the date null, and all hours available)
     */
    public Day(){
        this.month = -1;
        this.day = -1;
        this.year = -1;

        this.hours = new Hour[24];
        for (int i = 0; i <= 23; i++) {
            this.hours[i] = new Hour(true);
        }
    }

    public void writeUnavailableTimes(int startHr, int endHr){
        for (int i = startHr; i <= endHr; i++) {
            hours[i-1].setAvailable(false);
        }
    }

    public void writeAvailableTimes(int startHr, int endHr){
        for (int i = startHr; i <= endHr; i++) {
            hours[i-1].setAvailable(true);
        }
    }

    public boolean isAvailableDuring(int periodStart, int periodEnd) {
        boolean isAvailable = true;

        for (int i = periodStart; i <= periodEnd; i++) {
            if (!hours[i - 1].isAvailable())
                isAvailable = false;

            // check for end of day before incrementing
            if (i == 23)
                i = 0;
        }

        return isAvailable;
    }

    public void setNighttimeUnavailable(){
        // nighttime is considered 9pm-5am
        for (int i = 0; i < 24; i++) {
            if(i<=5 || i>=21 ) {
                hours[i].setAvailable(false);
            }
        }
    }

    public void setUnavailable(){
        for (int i = 0; i < 24; i++) {
            if(hours[i] == null) {
                hours[i] = new Hour(false);
            } else {
                hours[i].setAvailable(false);
            }
        }
    }

    public void clearSchedule(){
        hours = new Hour[24];
    }

    /**
     * @param HH A valid hour from 1-24
     * @return if the hour given is available
     */
    public boolean isHourAvailable(int HH){
        return hours[HH-1].isAvailable();
    }

    public boolean isDayAvailable(){
        // needs 6 hours minimum to be considered available
        return isDayAvailable(6);
    }

    public String toString(){
        return month + "/"+ day + "/" + year;
    }

    private boolean isDayAvailable(int hoursNeeded){
        int hoursFound = 0;

        // 6AM to 8PM (6HR-20HR)
        for (int i = 6; i <= 20; i++) {
            if(hours[i-1].isAvailable()) hoursFound++;
        }

        return hoursFound > hoursNeeded;
    }

}
