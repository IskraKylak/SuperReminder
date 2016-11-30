package com.example.fanvlad.superremindergood.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.fanvlad.superremindergood.database.DBHelper;
import com.example.fanvlad.superremindergood.model.ModelTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad on 28.10.2015.
 */
// востанавливает оповещение после перезагрузки устройства
public class AlarmSetter extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        // создали экземпляры классов
        DBHelper dbHelper = new DBHelper(context);

        AlarmHelper.getInstance().init(context);
        AlarmHelper alarmHelper = AlarmHelper.getInstance();


        List<ModelTask> tasks = new ArrayList<>();
        // ВЫБОРКА ПО СТАТУСУ, масивы сторок с статусами ( текущая и прошедшая), сортировка по дате
        tasks.addAll(dbHelper.query().getTasks(DBHelper.SELECTION_STATUS + " OR "
                + DBHelper.SELECTION_STATUS, new String[]{Integer.toString(ModelTask.STATUS_CURRENT),
                Integer.toString(ModelTask.STATUS_OVERDUE)},DBHelper.TASK_DATE_COLUMN));
        // устанавливаем напоминание
        for(ModelTask task : tasks){
            // дата задачи не равна 0
            if(task.getDate() != 0) {
                alarmHelper.setAlarm(task);
            }

        }
        // не забывае м добавить разрешение в манифесте

    }
}
