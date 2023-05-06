package com.schedulefinder.schedule;

import com.schedulefinder.time.Day;

import java.util.ArrayList;

public class Schedule {

    // weekly schedule
    Day[] week = new Day[7];

    // additional unavailable days
    ArrayList<Day> unavailableDays = new ArrayList<>();

    public Schedule(){
        // init weekly schedule with all nights marked unavailable
        for (int i = 0; i < 7; i++) {
            week[i] = new Day();
            week[i].setNighttimeUnavailable();
        }
    }

    public Day[] getWeeklySchedule(){
        return week;
    }

    public Day getDayOfWeek(int index){
        return week[index];
    }

    public void setWeek(Day[] weeklySchedule){
        this.week = weeklySchedule;
    }

    public void setDayOfWeek(int index, Day day){
        this.week[index] = day;
    }

    public void setAllNightsUnavailable(){
        for (int i = 0; i <= 6; i++) {
            this.week[i].setNighttimeUnavailable();
        }
    }

    public void setAllNightsAvailable(){
        for (int i = 0; i <= 6; i++) {
            this.week[i].setNighttimeAvailable();
        }
    }

    public void addUnavailableDay(int DD, int MM, int YYYY){
        unavailableDays.add(new Day(DD,MM,YYYY));
    }

    public ArrayList<Day> getUnavailableDays(){
        return unavailableDays;
    }

    public void setUnavailableDays(ArrayList<Day> newUnavailableDays){
        this.unavailableDays = newUnavailableDays;
    }

}
