import com.schedulefinder.ConsoleColors;
import com.schedulefinder.person.Person;
import org.jetbrains.annotations.Contract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
                "\t>\tview person - /viewperson\n");
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
                println("The day you entered was not valid. Please enter a valid day using at least the first three letters of the day.");
                day = getInputFromConsole();
                if(day.equals(CANCEL)) {commandsBeingEntered = false; break;}
            }
            if(day.equals("done")) {commandsBeingEntered = false; continue;}
            if(day.equals(CANCEL)) {commandsBeingEntered = false; continue; }
            println("EDIT DAY SCHEDULE: \"" + getFullDay(day).toUpperCase() + "\" : Enter unavailable times separated by commas, " +
                    "with the start and end time\nwritten in military time (0hrs-23hrs) separated by a hyphen.\nIf you'd like to set the " +
                    "night hours of the day available or unavailable, use \"?night=true\" or \"?night=false\". Example: 10-14,6-7,?night=false,16-18");

            String unavailableTimes = getInputFromConsole();
            String[] unavailableTimeInputs = unavailableTimes.split(",");
            int i = 0;
            boolean error = false;
            while (i < unavailableTimeInputs.length || error == true) {
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
                    error = true;
                    break;
                } else {
                    error = true;
                    println("ERROR: Commands have been inputted incorrectly. Please try again...");
                    break;
                }
            }
        }

        // TODO add additional unavailable dates setup

        print("\nFINISHED SETTING UP PERSON \"" + name + "\"... ");

        // add the person to the list
        people.add(new Person(name));
        print("Successfully added new person to directory.\n");
    }

    private void editPerson(){
        resetPrinter();
        println("EDIT PERSON: Please enter full name of person to edit.");

        String name = getInputFromConsole();
        while(listContains(people,name)) {
            println("ERROR: PERSON NOT FOUND. PLEASE ENTER THE EXACT NAME OF THE PERSON YOU WANT TO EDIT.");
            name = getInputFromConsole();
            println("Searching directory...");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        println("EDIT PERSON: \"" + name + "\" : Enter the letter corresponding to the action you'd like to perform.");
        setPrinterColor(ConsoleColors.PURPLE);
        println("\t(a) Edit Name\t(b) Edit Schedule\t(c) Delete");
        resetPrinter();

        String optionInput = getInputFromConsole();
        while(!optionInput.toUpperCase().equals("A")||!optionInput.toUpperCase().equals("B")||!optionInput.toUpperCase().equals("C")){
            println("ERROR: LETTER ENTERED WAS NOT ACCEPTED. Try again...");
            optionInput = getInputFromConsole();
        }
        char option = optionInput.toUpperCase().charAt(0);
        switch (option){
            case 'A' -> {
                println("EDIT NAME \"" + name + "\" : Enter new name...");
                String newName = getInputFromConsole();
                // TODO override old name using the index of the person in the directory
            }

            case 'B' -> {
                println("EDIT SCHEDULE \"" + name + "\" : Enter one of the options from below...");
                setPrinterColor(ConsoleColors.PURPLE);
                println("\t(a) Erase current schedule and setup a new one \n\t(b) Add new time parameters\t(c) Delete");
                resetPrinter();
                // TODO evaluate choice and perform desired action
            }

            case 'C' -> {

            }
        }
    }

    private void quit(){
        setPrinterColor(ConsoleColors.WHITE);
        println("STOPPING PROCESS");
        System.exit(1);
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
        for(Person element : list){
            if(element.getName().equals(keyName)) return true;
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


            case "/addperson" -> addPerson();

            case "/quit" -> quit();

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
