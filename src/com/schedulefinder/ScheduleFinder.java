package com.schedulefinder;

import com.schedulefinder.person.Person;
import com.schedulefinder.schedule.Schedule;
import org.jetbrains.annotations.Contract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.util.ArrayList;

import static com.schedulefinder.ConsoleColors.*;

public class ScheduleFinder {

    ArrayList<Person> people = new ArrayList<>();
    private boolean run = true;
    public static final String CANCEL = "escape";

    // region Startup Methods

    public void start(){
        setPrinterColor(ConsoleColors.CYAN);
        System.out.println("> SCHEDULE FINDER RUNNING");
        println("Please enter command below...");
        resetPrinter();

        boolean addTestPeople = true;
        if(addTestPeople){
            TestOne test = new TestOne();
            test.setupNoahSchedule();
            people.add(test.noah);
        }

        startReadingInput();
    }

    private void startReadingInput(){
        while(run) {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));

            // Reading data using readLine
            String input = null;
            try {
                input = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            read(input);
        }
    }

    // endregion

    // region Command Operations

    private void help(){
        setPrinterColor(ConsoleColors.PURPLE);
        print("\tCOMMAND GUIDE\n");
        print("\t>\thelp command - /help\n" +
                "\t>\tadd person - /addperson\n" +
                "\t>\tedit person - /editperson\n" +
                "\t>\tview person - /viewperson\n" +
                "\t>\texport directory - /export\n" +
                "\t>\timport directory - /import\n");
        setPrinterColor(ConsoleColors.RESET);
    }

    private void addPerson(){
        setPrinterColor(ConsoleColors.RESET);
        println("ADD NEW PERSON: Enter the full name of the person you'd like to add.");
        String name = getInputFromConsole();

        Person newPerson = new Person(name);
        println("NEW USER: \"" + name + "\" : Please write the weekly schedule. Formatting is shown below.");
        setPrinterColor(ConsoleColors.PURPLE);
        println("\tEnter each day of the week using the first three letters of the day you are entering. (For example, \"mon\").\n\tYou will then be directed to a submenu to fill out the details of that day. When finished, enter \"done\".");
        setPrinterColor(RESET);

        boolean commandsBeingEntered = true;
        while(commandsBeingEntered) {
            String day = getInputFromConsole();
            while (!isValidDay(day) || day.equals("done")) {
                if(day.equals(CANCEL) || day.equals("done")) {commandsBeingEntered = false; break;}
                println("The day you entered was not valid. Please enter a valid day using at least the first three letters of the day.");
                day = getInputFromConsole();
            }
            if(day.equals("done") || day.equals(CANCEL)) {commandsBeingEntered = false; continue;}

            println("ADD DAY SCHEDULE: \"" + getFullDay(day).toUpperCase() + "\" : Enter unavailable times separated by commas, " +
                    "with the start and end time\nwritten in military time (0hrs-23hrs) separated by a hyphen.\nIf you'd like to set the " +
                    "night hours of the day available or unavailable, use \"?night=true\" or \"?night=false\". Example: 10-14,6-7,?night=false,16-18");

            String unavailableTimes = getInputFromConsole();
            String[] unavailableTimeInputs = unavailableTimes.split(",");

            int i = 0;
            boolean error = false;
            while (i < unavailableTimeInputs.length && !error && commandsBeingEntered) {
                String cmdInput = unavailableTimeInputs[i];
                if (cmdInput.contains("-")) {
                    // command segment is an unavailable time
                    String startTime = cmdInput.split("-")[0];
                    String endTime = cmdInput.split("-")[1];
                    newPerson   .getSchedule()
                                .getWeeklySchedule()[getDayIndex(day)]
                                .writeUnavailableTimes(Integer.valueOf(startTime),Integer.valueOf(endTime));
                } else if (cmdInput.contains("?")) {
                    // command segment is a data operator
                    if (cmdInput.contains("night")) {
                        if (cmdInput.contains("true")) {
                            newPerson.getSchedule().setAllNightsAvailable();
                        } else {
                            newPerson.getSchedule().setAllNightsUnavailable();
                        }
                    }
                } else if (cmdInput.contains("done")) {
                    commandsBeingEntered = false;
                    break;
                } else {
                    error = true;
                    println("ERROR: Commands have been inputted incorrectly. Please try again...");
                    break;
                }
                i++;
            }
        }

        println("COMPLETED WEEKLY SCHEDULE SETUP. Now enter additional unavailable days.");
        setPrinterColor(PURPLE);
        println("\tPlease input additional unavailable days separated by commas, with the formatting of the dates being MM/DD/YYYY");

        boolean dateError = false;
        do {
            String additionalDatesInput = getInputFromConsole();
            if(additionalDatesInput.equals("done")){
                break;
            }
            String[] individualDateInput = additionalDatesInput.split(",");
            for (String da : individualDateInput){
                String[] ddmmyyyy = da.split("/");
                if(ddmmyyyy.length != 3) {
                    println("ERROR: You did not input a valid date formatted in DD/MM/YYYY separated by commas. Please try again...");
                    dateError = true;
                    break;
                } else {
                    newPerson.getSchedule().addUnavailableDay(Integer.valueOf(ddmmyyyy[0]), Integer.valueOf(ddmmyyyy[1]), Integer.valueOf(ddmmyyyy[2]));
                }
            }
        } while (dateError);

        resetPrinter();
        print("\nFINISHED SETTING UP PERSON \"" + name + "\"... ");

        // add the person to the list
        people.add(newPerson);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        print("Successfully added new person to directory.\n");
    }

    private void viewPerson(){
        println("VIEW PERSON: Please enter the index of the person you'd like to view.");
        setPrinterColor(PURPLE);
        int i = 0;
        for (Person p : people){
            println("\t[" + i++ + "] : " + p.getName());
        }
        resetPrinter();

        String index = getInputFromConsole();
        while(Integer.valueOf(index)>=people.size()){
            println("ERROR: That was not a valid index. Try again...");
            index = getInputFromConsole();
        }

        people.get(Integer.valueOf(index)).printSchedule();
        resetPrinter();

        println("Task completed successfully.\n");
    }

    private void editPerson(){
        resetPrinter();
        println("EDIT PERSON: Please enter the index of the person you'd like to edit.");
        setPrinterColor(PURPLE);
        int i = 0;
        for (Person p : people){
            println("\t[" +i+ "] : " + p.getName());
        }
        resetPrinter();

        String index = getInputFromConsole();
        while(Integer.valueOf(index)>=people.size()){
            println("ERROR: That was not a valid index. Try again...");
            index = getInputFromConsole();
        }

        int indexOfEdit = Integer.valueOf(index);
        Person person = people.get(indexOfEdit);

        println("EDIT PERSON: \"" + person.getName() + "\" : Enter the letter corresponding to the action you'd like to perform.");
        setPrinterColor(ConsoleColors.PURPLE);
        println("\t(a) Edit Name\t(b) Edit Schedule\t(c) Delete\t(d) Exit Menu");
        resetPrinter();

        String optionInput = getInputFromConsole();
        while(!optionInput.toUpperCase().equals("A")&&!optionInput.toUpperCase().equals("B")&&!optionInput.toUpperCase().equals("C")){
            println("ERROR: LETTER ENTERED WAS NOT ACCEPTED. Try again...");
            optionInput = getInputFromConsole();
        }

        char option = optionInput.toUpperCase().charAt(0);
        switch (option){
            // new name
            case 'A' -> {
                println("EDIT NAME \"" + person.getName() + "\" : Enter new name...");
                String newName = getInputFromConsole();
                people.get(indexOfEdit).setName(newName);
                println("Successfully changed name of person to \"" + newName + ".\"");
            }

            // edit schedule
            case 'B' -> {
                println("EDIT SCHEDULE \"" + person.getName() + "\" : Enter one of the options from below...");
                setPrinterColor(ConsoleColors.PURPLE);
                println("\t(a) Erase current schedule and setup a new one \n\t(b) Add new week time parameters\t(c) Erase additional unavailable dates and add new ones");
                resetPrinter();

                String subchoice = getInputFromConsole();
                while(!(subchoice.toLowerCase().equals("a") || subchoice.toLowerCase().equals("b") || subchoice.toLowerCase().equals("c"))){
                    System.out.println("ERROR: That was not a valid choice. Try again...");
                    subchoice = getInputFromConsole();
                }

                char choice = subchoice.toUpperCase().charAt(0);
                switch (choice){
                    // erase current schedule and enter a new one
                    case 'A' -> {
                        Schedule newSchedule = new Schedule();
                        println("\tEnter each day of the week using the first three letters of the day you are entering. (For example, \"mon\").\n\tYou will then be directed to a submenu to fill out the details of that day. When finished, enter \"done\".");
                        setPrinterColor(RESET);

                        boolean commandsBeingEntered = true;
                        while(commandsBeingEntered) {
                            String day = getInputFromConsole();
                            while (!isValidDay(day) || day.equals("done")) {
                                if(day.equals(CANCEL) || day.equals("done")) {commandsBeingEntered = false; break;}
                                println("The day you entered was not valid. Please enter a valid day using at least the first three letters of the day.");
                                day = getInputFromConsole();
                            }
                            if(day.equals("done") || day.equals(CANCEL)) {commandsBeingEntered = false; continue;}

                            println("ADD DAY SCHEDULE: \"" + getFullDay(day).toUpperCase() + "\" : Enter unavailable times separated by commas, " +
                                    "with the start and end time\nwritten in military time (0hrs-23hrs) separated by a hyphen.\nIf you'd like to set the " +
                                    "night hours of the day available or unavailable, use \"?night=true\" or \"?night=false\". Example: 10-14,6-7,?night=false,16-18");

                            String unavailableTimes = getInputFromConsole();
                            String[] unavailableTimeInputs = unavailableTimes.split(",");

                            int j = 0;
                            boolean error = false;
                            while (j < unavailableTimeInputs.length && !error && commandsBeingEntered) {
                                String cmdInput = unavailableTimeInputs[j];
                                if (cmdInput.contains("-")) {
                                    // command segment is an unavailable time
                                    String startTime = cmdInput.split("-")[0];
                                    String endTime = cmdInput.split("-")[1];
                                    newSchedule
                                            .getWeeklySchedule()[getDayIndex(day)]
                                            .writeUnavailableTimes(Integer.valueOf(startTime),Integer.valueOf(endTime));
                                } else if (cmdInput.contains("?")) {
                                    // command segment is a data operator
                                    if (cmdInput.contains("night")) {
                                        if (cmdInput.contains("true")) {
                                            newSchedule.setAllNightsAvailable();
                                        } else {
                                            newSchedule.setAllNightsUnavailable();
                                        }
                                    }
                                } else if (cmdInput.contains("done")) {
                                    commandsBeingEntered = false;
                                    break;
                                } else {
                                    error = true;
                                    println("ERROR: Commands have been inputted incorrectly. Please try again...");
                                    break;
                                }
                                j++;
                            }
                        }

                        println("COMPLETED WEEKLY SCHEDULE SETUP. Now enter additional unavailable days.");
                        setPrinterColor(PURPLE);
                        println("\tPlease input additional unavailable days separated by commas, with the formatting of the dates being MM/DD/YYYY");

                        boolean dateError = false;
                        do {
                            String additionalDatesInput = getInputFromConsole();
                            if(additionalDatesInput.equals("done")){
                                break;
                            }
                            String[] individualDateInput = additionalDatesInput.split(",");
                            for (String da : individualDateInput){
                                String[] ddmmyyyy = da.split("/");
                                if(ddmmyyyy.length != 3) {
                                    println("ERROR: You did not input a valid date formatted in DD/MM/YYYY separated by commas. Please try again...");
                                    dateError = true;
                                    break;
                                } else {
                                    newSchedule.addUnavailableDay(Integer.valueOf(ddmmyyyy[0]), Integer.valueOf(ddmmyyyy[1]), Integer.valueOf(ddmmyyyy[2]));
                                }
                            }
                        } while (dateError);

                        resetPrinter();
                        print("\nFINISHED SCHEDULE SET-UP...");

                        // override the new schedule over the old one (replacing the old one)
                        people.get(indexOfEdit).setSchedule(newSchedule);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        print("Successfully edited schedule and wrote to directory.\n");
                    }

                    // add new week time parameters
                    case 'B' -> {

                        boolean commandsBeingEntered = true;
                        while(commandsBeingEntered){
                            String day = getInputFromConsole();
                            while (!isValidDay(day) || day.equals("done")) {
                                if(day.equals(CANCEL) || day.equals("done")) {commandsBeingEntered = false; break;}
                                println("The day you entered was not valid. Please enter a valid day using at least the first three letters of the day.");
                                day = getInputFromConsole();
                            }
                            if(day.equals("done") || day.equals(CANCEL)) {commandsBeingEntered = false; continue;}

                            println("ADD DAY SCHEDULE: \"" + getFullDay(day).toUpperCase() + "\" : Enter unavailable times separated by commas, " +
                                    "with the start and end time\nwritten in military time (0hrs-23hrs) separated by a hyphen.\nIf you'd like to set the " +
                                    "night hours of the day available or unavailable, use \"?night=true\" or \"?night=false\". Example: 10-14,6-7,?night=false,16-18");

                            String unavailableTimes = getInputFromConsole();
                            String[] unavailableTimeInputs = unavailableTimes.split(",");

                            int j = 0;
                            boolean error = false;
                            while (j < unavailableTimeInputs.length && !error && commandsBeingEntered) {
                                String cmdInput = unavailableTimeInputs[j];
                                if (cmdInput.contains("-")) {
                                    // command segment is an unavailable time
                                    String startTime = cmdInput.split("-")[0];
                                    String endTime = cmdInput.split("-")[1];
                                    people.get(indexOfEdit).getSchedule()
                                            .getWeeklySchedule()[getDayIndex(day)]
                                            .writeUnavailableTimes(Integer.valueOf(startTime),Integer.valueOf(endTime));
                                } else if (cmdInput.contains("?")) {
                                    // command segment is a data operator
                                    if (cmdInput.contains("night")) {
                                        if (cmdInput.contains("true")) {
                                            people.get(indexOfEdit).getSchedule().setAllNightsAvailable();
                                        } else {
                                            people.get(indexOfEdit).getSchedule().setAllNightsUnavailable();
                                        }
                                    }
                                } else if (cmdInput.contains("done")) {
                                    commandsBeingEntered = false;
                                    break;
                                } else {
                                    error = true;
                                    println("ERROR: Commands have been inputted incorrectly. Please try again...");
                                    break;
                                }
                                j++;
                            }
                        }

                    }

                    case 'C' -> {
                        // erase additional unavailable dates and add new ones
                        people.get(indexOfEdit).getSchedule().getUnavailableDays().clear();
                        setPrinterColor(PURPLE);
                        println("\tPlease input additional unavailable days separated by commas, with the formatting of the dates being MM/DD/YYYY");

                        boolean dateError = false;
                        do {
                            String additionalDatesInput = getInputFromConsole();
                            if(additionalDatesInput.equals("done")){
                                break;
                            }
                            String[] individualDateInput = additionalDatesInput.split(",");
                            for (String da : individualDateInput){
                                String[] ddmmyyyy = da.split("/");
                                if(ddmmyyyy.length != 3) {
                                    println("ERROR: You did not input a valid date formatted in DD/MM/YYYY separated by commas. Please try again...");
                                    dateError = true;
                                    break;
                                } else {
                                    people.get(indexOfEdit).getSchedule().addUnavailableDay(Integer.valueOf(ddmmyyyy[0]), Integer.valueOf(ddmmyyyy[1]), Integer.valueOf(ddmmyyyy[2]));
                                }
                            }
                        } while (dateError);

                        resetPrinter();
                    }
                }

            }

            case 'C' -> {
                setPrinterColor(YELLOW);
                println("ARE YOU SURE YOU WANT TO DELETE THIS PERSON? THIS ACTION CANNOT BE UNDONE.\nEnter \"confirm\" to continue, or \"cancel\" to cancel the operation.");
                resetPrinter();

                String subchoice = getInputFromConsole();
                while(!(subchoice.toLowerCase().equals("confirm") || subchoice.toLowerCase().equals("cancel"))){
                    System.out.println("ERROR: That was not a valid choice. Try again...");
                    subchoice = getInputFromConsole();
                }

                if(subchoice.toLowerCase().equals("confirm")){
                    // delete the person being edited
                    println("\tDELETING PERSON...");
                    people.remove(indexOfEdit);
                    println("\tTask completed successfully.");
                } else {
                    // cancel and continue
                    println("DELETE OPERATION CANCELED.");
                }
            }
        }
    }

    private void exportDirectory(){

    }

    private void importDirectory(){

    }

    private void quit(){
        setPrinterColor(ConsoleColors.WHITE);
        println("STOPPING PROCESS");
        System.exit(0);
    }

    // endregion

    // region Data Management Methods

    public static boolean isValidDay(String s){
        s = s.toUpperCase();

        if(s.contains("MON")) return true;
        if(s.contains("TUE")) return true;
        if(s.contains("WED")) return true;
        if(s.contains("THU")) return true;
        if(s.contains("FRI")) return true;
        if(s.contains("SAT")) return true;
        if(s.contains("SUN")) return true;

        return false;
    }

    public static int getDayIndex(String s){
        s = s.toUpperCase();

        if(s.contains("MON")) return 1;
        if(s.contains("TUE")) return 2;
        if(s.contains("WED")) return 3;
        if(s.contains("THU")) return 4;
        if(s.contains("FRI")) return 5;
        if(s.contains("SAT")) return 6;
        if(s.contains("SUN")) return 0;

        return -1;
    }

    public static String getFullDay(String s){
        s = s.toUpperCase();

        if(s.contains("MON")) return "Monday";
        if(s.contains("TUE")) return "Tuesday";
        if(s.contains("WED")) return "Wednesday";
        if(s.contains("THU")) return "Thursday";
        if(s.contains("FRI")) return "Friday";
        if(s.contains("SAT")) return "Saturday";
        if(s.contains("SUN")) return "Sunday";

        return null;
    }

    public static boolean listContains(ArrayList<Person> list, String keyName){
        for(Person person : list){
            if(person.getName().equals(keyName) || person.getName().contains(keyName)) return true;
        }
        return false;
    }

    // endregion

    // region Console Interaction Methods

    @Contract(pure = true)
    private void read(String input){
        switch (input.toLowerCase()) {

            case "/help" -> help();

            case "/editperson" -> editPerson();

            case "/viewperson" -> viewPerson();


            case "/addperson" -> addPerson();

            case "/quit" -> quit();

            case "/export" -> exportDirectory();

            case "/import" -> importDirectory();

            default -> {
                setPrinterColor(ConsoleColors.RED);
                println("COMMAND \"" + input + "\" NOT FOUND. TRY USING /help FOR A VIEW OF ALL COMMANDS");
                resetPrinter();
            }
        }
    }

    private String getInputFromConsole(){
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        // Reading data using readLine
        String input = null;
        try {
            input = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return input;
    }

    public static void print(String s){
        System.out.print(s);
    }

    public static void println(String s){
        System.out.println(s);
    }

    //endregion

    public static void main(String[] args) {
        new ScheduleFinder().start();
    }

}
