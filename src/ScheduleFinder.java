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

    @Contract(pure = true)
    private void read(String input){
        switch (input.toLowerCase()) {

            case "/help" -> {
                setPrinterColor(ConsoleColors.PURPLE);
                print("\tCOMMAND GUIDE\n");
                print("\t>\thelp command - /help\n" +
                        "\t>\tadd person - /addperson\n" +
                        "\t>\tedit person - /editperson");
                setPrinterColor(ConsoleColors.RESET);
                println("");
            }

            case "/editperson" -> {
                resetPrinter();
                println("EDIT PERSON: Please enter full name of person to edit.");

                String name = getInputFromConsole();
                while(!people.contains(name)) {
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
                //TODO implementation to edit profile
            }

            case "/addperson" -> {
                setPrinterColor(ConsoleColors.RESET);
                println("ADD NEW PERSON: Enter the full name of the person you'd like to add.");
                String name = getInputFromConsole();

                people.add(new Person(name));
                println("NEW USER CREATED: \"" + name + "\" : Please write the weekly schedule. Formatting is shown below.");
                setPrinterColor(ConsoleColors.PURPLE);
                println("\tEnter each day of the week using the first three letters of the day you are entering. (For example, \"mon\").\n\tYou will then be directed to a submenu to fill out the details of that day. When finished, enter \"done\".");
                setPrinterColor(RESET);

                String day = getInputFromConsole();
                while(!isValidDay(day)){
                    println("The day you entered was not valid. Please enter a valid day using at least the first three letters of the day.");
                    day = getInputFromConsole();
                }
                println("EDIT WEEKDAY SCHEDULE: \"" + getFullDay(day).toUpperCase() + "\" : Enter unavailable times separated by commas, " +
                        "with the start and end time\nwritten in military time (0hrs-23hrs) separated by a hyphen.\nIf you'd like to set the " +
                        "night hours of the day available or unavailable, use \"?night=true\" or \"?night=false\". Example: 10-14,6-7,?night=false,16-18");

            }

            case "/quit" -> {
                setPrinterColor(ConsoleColors.WHITE);
                println("STOPPING PROCESS");
                System.exit(1);
            }

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

    public static void print(String s){
        System.out.print(s);
    }

    public static void println(String s){
        System.out.println(s);
    }

    public static void main(String[] args) {
        new ScheduleFinder().start();
    }

}
