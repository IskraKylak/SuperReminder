package com.example.fanvlad.superremindergood.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.fanvlad.superremindergood.MainActivity;
import com.example.fanvlad.superremindergood.MyApplication;
import com.example.fanvlad.superremindergood.R;

/**
 * Created by Vlad on 28.10.2015.
 */
// Notification - оповещения о событиях
//BroadcastReceiver - приемник широковещательных сообщений( компонент андроид
    // получает внешнее событие от каких либо приложений и реагирует на них )
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // заголовок
        String  title = intent.getStringExtra("title");
        //time_stamp время создания задачи
        long timeStamp = intent.getLongExtra("time_stamp", 0);
        // цвет приоритета задачи
        int color = intent.getIntExtra("color", 0);
        // запускает главное активити при нажатии на нотификацию
        Intent resultIntent = new Intent(context, MainActivity.class);
        // если активити видимо
        if(MyApplication.isActivityVisible()){
            // присваевае м значение интента которое поступило на вход
            // не будет пересоздавать активити
            resultIntent = intent;
        }
        // стартует новое активити если приложение не видимо
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // pendingIntent - позволяет запустить хранящийся в нем интент от имени того приложения
        // а также с теми полномочиями с которими этот пдинтент создавался
        //
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) timeStamp,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // при построение Notification - оповещения, будем использовать заголово задачи и заголово приложения
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("SuperReminderGood");
        builder.setContentText(title);
        // используем цвет иконки нашей задачи
        builder.setColor(context.getResources().getColor(color));
        // еще одна иконка
        builder.setSmallIcon(R.drawable.ic_check_white_48dp);
        // какие свойства уведомления будут использованы от системы по умолчанию
        builder.setDefaults(Notification.DEFAULT_ALL);
        // отдаем методу наш пд интент
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        // уведомление отменяется при нажатии на него пользователем
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        // отвечает за уведомления пользователя
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        // указуем время и заголовок
        notificationManager.notify((int)timeStamp,notification);
        // не забываем прописать наш ресивер в файле манифесте

    }
}
