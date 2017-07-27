package ru.jorik.homeaid;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by 111 on 27.07.2017.
 */

//Костыли для разработки
public class LittleUtils {

    //работа с Calendar
    public static Date getToday(){
        return Calendar.getInstance().getTime();
    }

    public static Calendar getCustomCalendar(int year, int month, int day){
        Calendar rCalendar = Calendar.getInstance();
        rCalendar.set(year, month, day);
        return rCalendar;
    }
}
