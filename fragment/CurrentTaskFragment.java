package com.example.fanvlad.superremindergood.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fanvlad.superremindergood.R;
import com.example.fanvlad.superremindergood.adapter.CurrentTasksAdapter;
import com.example.fanvlad.superremindergood.database.DBHelper;
import com.example.fanvlad.superremindergood.model.ModelTask;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentTaskFragment extends TaskFragment {




    public CurrentTaskFragment() {
        // Required empty public constructor
    }

    OnTaskDoneListener onTaskDoneListener;

    public interface OnTaskDoneListener {
        void onTaskDone(ModelTask task);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onTaskDoneListener = (OnTaskDoneListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnTaskDoneListner");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // что б получить доступ к поиску вью
        View rootView = inflater.inflate(R.layout.fragment_current_task, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvCurrentTasks);
        // будем использовать контекст через ллменеджер
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        adapter = new CurrentTasksAdapter(this);
        recyclerView.setAdapter(adapter);
        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void findTasks(String title) {
        // удаляем задачу
        adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList<>();
        // ВЫБОРКА ПО СТАТУСУ, масивы сторок с статусами ( текущая и прошедшая), сортировка по дате
        // "%" + title + "%" - производит поис по части слова т. е. ( 1 -> коля1)
        tasks.addAll(activity.dbHelper.query().getTasks(DBHelper.SELECTION_LIKE_TITLE + " AND "
                + DBHelper.SELECTION_STATUS + " OR " + DBHelper.SELECTION_STATUS,
                new String[]{"%" + title + "%", Integer.toString(ModelTask.STATUS_CURRENT),
                Integer.toString(ModelTask.STATUS_OVERDUE)},DBHelper.TASK_DATE_COLUMN));
        for (int i = 0; i < tasks.size(); i++ ){
            addTask(tasks.get(i), false);
        }
    }

    @Override
    public void addTaskFromDB() {
        // удаляем задачу
        adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList<>();
        // ВЫБОРКА ПО СТАТУСУ, масивы сторок с статусами ( текущая и прошедшая), сортировка по дате
        tasks.addAll(activity.dbHelper.query().getTasks(DBHelper.SELECTION_STATUS + " OR "
                + DBHelper.SELECTION_STATUS, new String[]{Integer.toString(ModelTask.STATUS_CURRENT),
                Integer.toString(ModelTask.STATUS_OVERDUE)},DBHelper.TASK_DATE_COLUMN));
        for (int i = 0; i < tasks.size(); i++ ){
            addTask(tasks.get(i), false);
        }
    }

    @Override
    public void moveTask(ModelTask task) {
        alarmHelper.removeAlarm(task.getTimeStamp());

        // слушатель на выполненые задачи
        onTaskDoneListener.onTaskDone(task);
    }
}
