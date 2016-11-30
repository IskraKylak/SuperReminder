package com.example.fanvlad.superremindergood.fragment;

import java.text.SimpleDateFormat;

/**
 * Created by Vlad on 02.10.2015.
 */
// для получения даты из календаря в текстовом виде
public class Utils {

    public static String getDate(long date) {
        // создаем обьек дайт формат класса сипл дейтформат
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        return dateFormat.format(date);
    }

    public static String getTime(long time){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(time);
    }
    // возвращает полную дату
    public static String  getFullDate(long date){
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");
        return fullDateFormat.format(date);
    }
}
