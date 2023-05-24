package com.schedulefinder.person;

import com.schedulefinder.ConsoleColors;
import com.schedulefinder.ScheduleFinder;
import com.schedulefinder.schedule.Schedule;
import com.schedulefinder.time.Conversion;
import com.schedulefinder.time.Day;
import com.schedulefinder.ScheduleFinder.*;
import com.virtualconsole.app.VConsole;

import java.awt.*;
import java.io.Serializable;

public class Person implements Serializable {

    private String name;
    private Schedule schedule;

    public Person(String name){
        this.name = name;
        this.schedule = new Schedule();
    }

    public String getName(){
        return name;
    }

    public void setName(String newName) {this.name = newName;}

    public Schedule getSchedule(){
        return schedule;
    }

    public void setSchedule(Schedule newSchedule){
        this.schedule = newSchedule;
    }

    public boolean equals(String object){
        if(object.equals(this.name)){
            return true;
        }
        return false;
    }

    public void printSchedule(VConsole parentConsole,ScheduleFinder sf){
        if(parentConsole == null) {
            System.out.println("\t\t" + this.getName().toUpperCase() + "'S WEEKLY SCHEDULE");
            System.out.print("\t\t");
            for (int hour = 1; hour <= 24; hour++) {
                System.out.print(hour - 1 + "\t");
            }

            System.out.println();
            for (int day = 0; day < 7; day++) {
                System.out.print(Conversion.day(day) + ":\t");
                for (int hour = 1; hour <= 24; hour++) {
                    boolean isHrAvailable = this.getSchedule().getDayOfWeek(day).isHourAvailable(hour);
                    if (isHrAvailable) {
                        System.out.print(ConsoleColors.GREEN);
                        System.out.print("O\t");
                        System.out.print(ConsoleColors.RESET);
                    } else {
                        System.out.print(ConsoleColors.RED);
                        System.out.print("X\t");
                        System.out.print(ConsoleColors.RESET);
                    }
                }
                ScheduleFinder.println("");
            }
            ScheduleFinder.println("ADDITIONAL UNAVAILABLE DAYS:\t" + this.getSchedule().getUnavailableDays());
            ScheduleFinder.println("=====================================");
        } else {
            parentConsole.println("\t\t" + this.getName().toUpperCase() + "'S WEEKLY SCHEDULE\n",Color.WHITE);
            parentConsole.print("\t\t");
            for (int hour = 1; hour <= 24; hour++) {
                parentConsole.print(hour - 1 + "\t");
            }

            parentConsole.println();
            for (int day = 0; day < 7; day++) {
                parentConsole.print(Conversion.day(day) + ":\t",Color.WHITE);
                for (int hour = 1; hour <= 24; hour++) {
                    boolean isHrAvailable = this.getSchedule().getDayOfWeek(day).isHourAvailable(hour);
                    if (isHrAvailable) {
                        parentConsole.print("O\t",Color.GREEN);
                        sf.setPrinterColor(ConsoleColors.RESET);
                    } else {
                        parentConsole.print("X\t", Color.RED);
                        sf.setPrinterColor(ConsoleColors.RESET);
                    }
                }
                parentConsole.println();
            }
            parentConsole.println("ADDITIONAL UNAVAILABLE DAYS:\t" + this.getSchedule().getUnavailableDays(),Color.WHITE);
            parentConsole.println("=====================================",Color.WHITE);
        }
    }

}
