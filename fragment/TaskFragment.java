package com.example.fanvlad.superremindergood.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.fanvlad.superremindergood.MainActivity;
import com.example.fanvlad.superremindergood.R;
import com.example.fanvlad.superremindergood.adapter.TaskAdapter;
import com.example.fanvlad.superremindergood.alarm.AlarmHelper;
import com.example.fanvlad.superremindergood.model.Item;
import com.example.fanvlad.superremindergood.model.ModelTask;

/**
 * Created by Vlad on 07.10.2015.
 */
// обьеденяет каренттаск и данетаск фрагмент
public abstract class TaskFragment extends Fragment {

    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;

    protected TaskAdapter adapter;

    public MainActivity activity;

    public AlarmHelper alarmHelper;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getActivity() != null){
            activity = (MainActivity) getActivity();
        }

        alarmHelper = AlarmHelper.getInstance();

        addTaskFromDB();
    }

    // добавляет задачи
    public void addTask (ModelTask newTask, boolean saveToDB){
        int position = -1;
        // добавляються по дате
        for (int i = 0; i < adapter.getItemCount(); i++){
            if(adapter.getItem(i).isTask()){
                ModelTask task = (ModelTask) adapter.getItem(i);
                if(newTask.getDate() < task.getDate()){
                    position = i;
                    // прерывание цыкла при нахождении елемета с большей датой
                    break;
                }
            }
        }
        // не один елемент из списка не больше нового, новый элемент элемент будет добавлятся позицией ниже
        if(position != -1){
            adapter.addItem(position, newTask);
        }else
        {
            adapter.addItem(newTask);
        }

        if (saveToDB){
            activity.dbHelper.saveTask(newTask);
        }
    }

    // реализация вызова диалога - удаления таска
    public void removeTaskDialog(final int location){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setMessage(R.string.dialog_removing_message);

        Item item = adapter.getItem(location);

        if(item.isTask()) {

            ModelTask removingTask = (ModelTask) item;

            final long timeStamp = removingTask.getTimeStamp();
            final boolean[] isRemoved = {false};

            dialogBuilder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    adapter.removeItem(location);
                    isRemoved[0] = true;
                    // снекбар, выдвигающаяся панель снизу
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinator),
                            R.string.removed, Snackbar.LENGTH_LONG);
                    snackbar.setAction(R.string.dialog_cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addTask(activity.dbHelper.query().getTask(timeStamp), false);
                            isRemoved[0] = false;
                        }
                    });
                    // вешаем на снек бар слушатель он аттач... через гетвиев
                    snackbar.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                        // 2 метода которые относятся к жизненому цыклу снекбара,
                        // когда появляется
                        @Override
                        public void onViewAttachedToWindow(View v) {

                        }
                        // когда исчезает
                        @Override
                        public void onViewDetachedFromWindow(View v) {
                            // окончательное удаление
                            if(isRemoved[0]){
                                alarmHelper.removeAlarm(timeStamp);
                                activity.dbHelper.removeTask(timeStamp);
                            }
                        }
                    });
                    snackbar.show();

                    dialog.dismiss();
                }
            });

            dialogBuilder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }

        dialogBuilder.show();
    }
    // найти таск
    public abstract void findTasks(String title);

    public abstract void addTaskFromDB();

    public abstract void moveTask(ModelTask task);
}
