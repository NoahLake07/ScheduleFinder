package com.schedulefinder.time;

public class Conversion {

    public static int time(String time){
        time = time.toUpperCase();
        if(time.equals("12AM")) return 0;
        if(time.equals("1AM")) return 1;
        if(time.equals("2AM")) return 2;
        if(time.equals("3AM")) return 3;
        if(time.equals("4AM")) return 4;
        if(time.equals("5AM")) return 5;
        if(time.equals("6AM")) return 6;
        if(time.equals("7AM")) return 7;
        if(time.equals("8AM")) return 8;
        if(time.equals("9AM")) return 9;
        if(time.equals("10AM")) return 10;
        if(time.equals("11AM")) return 11;
        if(time.equals("12PM")) return 12;
        if(time.equals("1PM")) return 13;
        if(time.equals("2PM")) return 14;
        if(time.equals("3PM")) return 15;
        if(time.equals("4PM")) return 16;
        if(time.equals("5PM")) return 17;
        if(time.equals("6PM")) return 18;
        if(time.equals("7PM")) return 19;
        if(time.equals("8PM")) return 20;
        if(time.equals("9PM")) return 21;
        if(time.equals("10PM")) return 22;
        if(time.equals("11PM")) return 23;
        return -1;
    }

    public static String time(int hr){
        if(hr == 12) return "12PM";

        if(hr<=11){
            return hr + "AM";
        } else {
            return hr-12 + "PM";
        }
    }

    public static String day(int index){
        if(index == 0) return "SUN";
        if(index == 1) return "MON";
        if(index == 2) return "TUE";
        if(index == 3) return "WED";
        if(index == 4) return "THU";
        if(index == 5) return "FRI";
        if(index == 6) return "SAT";
        return "ERR";
    }

}
