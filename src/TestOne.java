import com.schedulefinder.ConsoleColors;
import com.schedulefinder.person.Person;
import com.schedulefinder.time.Conversion;
import com.schedulefinder.time.Day;
import static com.schedulefinder.time.Conversion.time;

public class TestOne {

    Person noah = new Person("Noah");

    public void setupNoahSchedule(){
        Day sun = new Day();
        sun.setUnavailable(); // entire day unavailable

        Day mon = new Day();
        mon.writeUnavailableTimes(time("6AM"),time("12PM")); // mtb training

        Day tues = new Day();
        tues.writeUnavailableTimes(time("9AM"),time("12PM")); // wmr coaching
        tues.writeUnavailableTimes(time("6PM"),time("8PM")); // mtb practice

        Day wed = new Day();
        wed.writeUnavailableTimes(time("9AM"),time("12PM")); // wmr training

        Day thur = new Day();
        thur.writeUnavailableTimes(time("9AM"),time("12PM")); // wmr coaching
        thur.writeUnavailableTimes(time("6PM"),time("8PM")); // mtb practice

        Day fri = new Day();

        Day sat = new Day();

        noah.getSchedule().setWeek(new Day[]{sun,mon,tues,wed,thur,fri,sat});
        noah.getSchedule().setAllNightsUnavailable();

        noah.getSchedule().addUnavailableDay(6,20,2023);
        noah.getSchedule().addUnavailableDay(6,24,2023);
        noah.getSchedule().addUnavailableDay(7,18,2023);
        noah.getSchedule().addUnavailableDay(7,19,2023);
        noah.getSchedule().addUnavailableDay(7,20,2023);
        noah.getSchedule().addUnavailableDay(7,22,2023);
        noah.getSchedule().addUnavailableDay(8,12,2023);
    }

    public void printNoahSchedule(Person person){
        System.out.println("\t\t"+person.getName().toUpperCase()+"'S WEEKLY SCHEDULE");
        System.out.print("\t\t");
        for (int hour = 1; hour <= 24; hour++) {
            System.out.print(hour-1 + "\t");
        }

        System.out.println();
        for (int day = 0; day < 7; day++) {
            System.out.print(Conversion.day(day)+":\t");
            for (int hour = 1; hour <= 24; hour++) {
                boolean isHrAvailable = person.getSchedule().getDayOfWeek(day).isHourAvailable(hour);
                if(isHrAvailable){
                    System.out.print(ConsoleColors.GREEN);
                    System.out.print("O\t");
                    System.out.print(ConsoleColors.RESET);
                } else {
                    System.out.print(ConsoleColors.RED);
                    System.out.print("X\t");
                    System.out.print(ConsoleColors.RESET);
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        TestOne x = new TestOne();

        x.setupNoahSchedule();
        x.printNoahSchedule(x.noah);
        System.out.println("ADDITIONAL UNAVAILABLE DAYS:\t"+x.noah.getSchedule().getUnavailableDays());
    }
}