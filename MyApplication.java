package com.example.fanvlad.superremindergood;

import android.app.Application;

/**
 * Created by Vlad on 28.10.2015.
 */
//что б ресивер не запускал активити если она уже запущена
    // этот калс с методом возвращающий значение видимости активити
public class MyApplication extends Application{

    private static boolean activityVisible;
    // возвращает булиан
    public static boolean isActivityVisible(){
        return activityVisible;
    }
    // вкл.
    public static void  activityResumed(){
        activityVisible = true;
    }
    // выкл.
    public static void activityPaused(){
        activityVisible = false;
    }

}
