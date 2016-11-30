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
import com.example.fanvlad.superremindergood.adapter.DoneTaskAdapter;
import com.example.fanvlad.superremindergood.database.DBHelper;
import com.example.fanvlad.superremindergood.model.ModelTask;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoneTaskFragment extends TaskFragment {




    public DoneTaskFragment() {
        // Required empty public constructor
    }

    OnTaskRestoreListener onTaskRestoreListener;

    public interface OnTaskRestoreListener {
        void onTaskRestore(ModelTask task);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            onTaskRestoreListener = (OnTaskRestoreListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    "must implement OnTaskRestoreListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_done_task, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvDoneTasks);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DoneTaskAdapter(this);
        recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return rootView;
    }
    // см. в СurrentTask..
    @Override
    public void findTasks(String title) {
        adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(activity.dbHelper.query().getTasks(DBHelper.SELECTION_LIKE_TITLE + " AND "
                        + DBHelper.SELECTION_STATUS , new String[]{"%" + title + "%",
                Integer.toString(ModelTask.STATUS_DONE)},DBHelper.TASK_DATE_COLUMN));
        for (int i = 0; i < tasks.size(); i++ ){
            addTask(tasks.get(i), false);
        }
    }

    @Override
    public void addTaskFromDB() {
        adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(activity.dbHelper.query().getTasks(DBHelper.SELECTION_STATUS ,
                new String[]{Integer.toString(ModelTask.STATUS_DONE)},DBHelper.TASK_DATE_COLUMN));
        for (int i = 0; i < tasks.size(); i++ ){
            addTask(tasks.get(i), false);
        }
    }

    @Override
    public void moveTask(ModelTask task) {
        if(task.getDate() != 0){
            alarmHelper.setAlarm(task);
        }
        onTaskRestoreListener.onTaskRestore(task);
    }
}
