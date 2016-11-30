package com.example.fanvlad.superremindergood.model;

import com.example.fanvlad.superremindergood.R;

import java.util.Date;

/**
 * Created by Vlad on 04.10.2015.
 */
// для тасков
    // клас задачи
public class ModelTask implements Item {
    // приоритеты и статусы задач
    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int PRIORITY_HIGH = 2;
    // масив приоритетов заполненый
    public static final String[] PRIORITY_LEVELS = {"Low Priority", "Normal Priority", "High Priority"};


    // статусы для разделения задач в списке по срокам и формирования запросов в базу данных
    public static final int STATUS_OVERDUE = 0;
    public static final int STATUS_CURRENT = 1;
    public static final int STATUS_DONE = 2;

    private String title;
    private long date;
    private int priority;
    private int status;
    private long timeStamp;

    // статус со значением -1 создаеться
    public ModelTask(){
        this.status = -1;
        this.timeStamp = new Date().getTime();
    }

    public ModelTask (String title, long date, int priority, int status, long timeStamp )
    {
        this.title = title;
        this.date = date;
        this.priority = priority;
        this.status = status;
        this.timeStamp = timeStamp;

    }


    // приоритет для цвета
    public int getPriorityColor(){
        switch (getPriority()){

            case PRIORITY_HIGH:
                if(getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE){
                    return R.color.priority_high;
                }else{
                    return R.color.priority_high_selected;
                }
            case PRIORITY_NORMAL:
                if(getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE){
                    return R.color.priority_normal;
                }else{
                    return R.color.priority_normal_selected;
                }

            case PRIORITY_LOW:
                if(getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE){
                    return R.color.priority_low;
                }else {
                    return R.color.priority_low_selected;
                }
            default: return 0;
        }
    }

    // гетеры и сетеры для приорити и статус
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean isTask() {
        return true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
