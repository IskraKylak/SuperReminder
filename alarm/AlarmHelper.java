package com.example.fanvlad.superremindergood.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.fanvlad.superremindergood.model.ModelTask;

/**
 * Created by Vlad on 28.10.2015.
 */
// сингл тон
public class AlarmHelper {

    private static AlarmHelper instance;
    private Context context;
    private AlarmManager alarmManager;
    // создает обьект аларм хелпер в случае его отстутсвия
    public static AlarmHelper getInstance(){
        if (instance == null){
            instance = new AlarmHelper();
        }
        return instance;
    }
    // инициализирует алармменеджер
    public void init(Context context){
        this.context = context;
        alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
    }
    // создает ресивер и передает ему данные заголовок, время задачи и цвет приоритета
    public void setAlarm(ModelTask task){
        Intent intent = new Intent(context,AlarmReceiver.class);
        intent.putExtra("title",task.getTitle());
        intent.putExtra("time_stamp",task.getTimeStamp());
        intent.putExtra("color", task.getPriorityColor());
        // используем его метод getBroadcast для доступа к ресиверу,
        // PendingIntent.FLAG_UPDATE_CURRENT -  указует если пдинтент существует то новый не буде создаватся
        // будет испл. текущий но с обновленными данными
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
                (int) task.getTimeStamp(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        // передаем менеджеру время пробуждения устройства полную дату задачи, И ПД ИНТЕНТ
        alarmManager.set(AlarmManager.RTC_WAKEUP, task.getDate(),pendingIntent);
    }
    // удаляет задачу по тиместамп временисозданию
    public void removeAlarm(long taskTimeStamp){
        Intent intent = new Intent(context, AlarmReceiver.class);
        //
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context , (int) taskTimeStamp,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // отменяем оповищение <uses-permission android:name="android.permission.VIBRATE"/>
        //
        alarmManager.cancel(pendingIntent);
    }
}
