package com.schedulefinder.schedule;

import com.schedulefinder.person.Person;
import com.schedulefinder.time.Day;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ScheduleCheck {

    /**
     * Checks a group of People for availability on a specified date and hours needed
     *
     * @param hoursNeeded the amount of hours needed in a row where everyone is available
     * @param day         the day to check
     * @param mo          the month to check
     * @param year        the year to check
     * @param people      the people to scan through
     * @return true if there are the specified amount of hours in a row that everyone is available
     */
    public static boolean checkForGroupAvailability(int hoursNeeded, int day, int mo, int year, ArrayList<Person> people) {
        int dayIndex = getDayOfWeek(mo, day, year);
        ArrayList<Boolean> hourlyAvailability = new ArrayList<>();

        // get booleans for each hour of the day
        for (int i = 0; i < 23; i++) {
            boolean allAvailable = true;
            for (Person person : people) {
                if (!(person.getSchedule().getWeeklySchedule()[dayIndex].isHourAvailable(i) && person.getSchedule().isAvailable(day, mo, year))) {
                    allAvailable = false;
                }
            }
            hourlyAvailability.add(allAvailable);
        }

        // read through all hour results
        int count = 0;
        for (Boolean b : hourlyAvailability) {
            if (b) {
                count++;
            } else if (count >= hoursNeeded) {
                // there are enough hours
                return true;
            } else {
                count = 0;
            }
        }
        return count >= hoursNeeded;
    }

    public static ArrayList<Day> findAllAvailableDays(int hoursNeeded, int day, int mo, int year, ArrayList<Person> people) {
        int dayIndex = getDayOfWeek(mo, day, year);
        ArrayList<Boolean> hourlyAvailability = new ArrayList<>();

        // get booleans for each hour of the day
        for (int i = 0; i < 23; i++) {
            boolean allAvailable = true;
            for (Person person : people) {
                if (!(person.getSchedule().getWeeklySchedule()[dayIndex].isHourAvailable(i) && person.getSchedule().isAvailable(day, mo, year))) {
                    allAvailable = false;
                }
            }
            hourlyAvailability.add(allAvailable);
        }

        // read through all hour results
        int count = 0;
        for (Boolean b : hourlyAvailability) {
            if (b) {
                count++;
            } else if (count >= hoursNeeded) {
                // there are enough hours
                return null;
            } else {
                count = 0;
            }
        }
return null;
    }

    /**
     * Calculates the day of the week for a given date.
     *
     * @param month the month (1-12)
     * @param day   the day of the month
     * @param year  the year
     * @return the day of the week as an integer (0-6, where 0 is Sunday and 6 is Saturday)
     */
    public static int getDayOfWeek(int month, int day, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day); // Calendar months start from 0, so we subtract 1 from the input month

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return (dayOfWeek + 5) % 7; // Adjusting the index to start from 0 (Sunday) instead of 1 (Monday)
    }

}
