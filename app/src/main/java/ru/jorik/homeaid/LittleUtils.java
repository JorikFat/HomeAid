package ru.jorik.homeaid;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 111 on 27.07.2017.
 */

//Костыли для разработки
    //Я знаю что этот класс вредит работе приложения и постоянно весит в памяти. Забейте. Потом исправлю
    //// TODO: 28.07.2017 раскидать методы по другим классам
public class LittleUtils {

    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

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
