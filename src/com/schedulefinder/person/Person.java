package com.schedulefinder.person;

import com.schedulefinder.schedule.Schedule;
import com.schedulefinder.time.Day;

public class Person {

    private String name;
    private Schedule schedule;

    public Person(String name){
        this.name = name;
        this.schedule = new Schedule();
    }

    public String getName(){
        return name;
    }

    public Schedule getSchedule(){
        return schedule;
    }

    public boolean equals(String object){
        if(object.equals(this.name)){
            return true;
        }
        return false;
    }

}
