package com.example.fanvlad.superremindergood.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fanvlad.superremindergood.R;
import com.example.fanvlad.superremindergood.fragment.CurrentTaskFragment;
import com.example.fanvlad.superremindergood.fragment.TaskFragment;
import com.example.fanvlad.superremindergood.fragment.Utils;
import com.example.fanvlad.superremindergood.model.Item;
import com.example.fanvlad.superremindergood.model.ModelTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vlad on 04.10.2015.
 */
public class CurrentTasksAdapter extends TaskAdapter {



    private static final int TYPE_TASK = 0;
    private static final int TYPE_SEPARATOR = 1;

    public CurrentTasksAdapter(CurrentTaskFragment taskFragment) {
        super(taskFragment);
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        switch (viewType){
            case TYPE_TASK:
                // получаем елемент модел таск и инициализируем их
                View v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.model_task, viewGroup, false);
                TextView title = (TextView) v.findViewById(R.id.tvTaskTitle);
                TextView date = (TextView) v.findViewById(R.id.tvTaskDate);
                CircleImageView priority = (CircleImageView) v.findViewById(R.id.cvTaskPriority);

                return new TaskViewHolder(v, title ,date, priority);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final Item item = items.get(position);

        if(item.isTask()){
            // активируем возможность нажатия таска
            viewHolder.itemView.setEnabled(true);
            final ModelTask task = (ModelTask) item;
            final TaskViewHolder taskViewHolder = (TaskViewHolder) viewHolder;

            final View itemView = taskViewHolder.itemView;
            final Resources resources = itemView.getResources();

            taskViewHolder.title.setText(task.getTitle());
            if(task.getDate() != 0) {
                taskViewHolder.date.setText(Utils.getFullDate(task.getDate()));
            }else{
                taskViewHolder.date.setText(null);
            }

            itemView.setVisibility(View.VISIBLE);
            taskViewHolder.priority.setEnabled(true);



            taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_default_material_light));
            taskViewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_default_material_light));
            taskViewHolder.priority.setColorFilter(resources.getColor(task.getPriorityColor()));
            taskViewHolder.priority.setImageResource(R.drawable.ic_checkbox_blank_circle_white_48dp);
            // добавим слушатель обьекту itemView
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // хендлер нужен для того что бы срабтала рипл анимация( что бы она успела сроботать до того как сработает диалог)
                    Handler handler = new Handler();
                    // задержка
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getTaskFragment().removeTaskDialog(taskViewHolder.getLayoutPosition());
                        }
                    }, 1000);

                    return true;
                }
            });


            // слушатель который по клику на картинке меняет стутс задачи (делает выполненой)
            taskViewHolder.priority.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // многократное нажатие будут блокироватся ( можно будет нажать на задача 1 раз)
                    taskViewHolder.priority.setEnabled(false);
                    task.setStatus(ModelTask.STATUS_DONE);
                    // обновления статуса
                    getTaskFragment().activity.dbHelper.update().status(task.getTimeStamp(), ModelTask.STATUS_DONE);



                    taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_disabled_material_light));
                    taskViewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_disabled_material_light));
                    taskViewHolder.priority.setColorFilter(resources.getColor(task.getPriorityColor()));
                    // добавляем анимацию
                    ObjectAnimator flipIn = ObjectAnimator.ofFloat(taskViewHolder.priority, "rotationY", -180f, 0f);
                    // присвоим аниматору слушатель
                    flipIn.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }
                        // выполняется по окончанию анимации
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // статус задачи выполнен
                            if(task.getStatus() == ModelTask.STATUS_DONE){
                                taskViewHolder.priority.setImageResource(R.drawable.ic_check_circle_white_48dp);
                                // перемещает елемент в сторону на расстояние равной его длине
                                ObjectAnimator translationX = ObjectAnimator.ofFloat(itemView,
                                        "translationX",0f, itemView.getWidth());
                                // возвращает елемент списка в исходное состаяние
                                ObjectAnimator translationXBack = ObjectAnimator.ofFloat(itemView,
                                        "translationX", itemView.getWidth(), 0f);
                                // слушатель для аниматора траслешинХ
                                translationX.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }
                                    // делаем елемент невидимым что б удаленный елемент не отображался
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        itemView.setVisibility(View.GONE);
                                        getTaskFragment().moveTask(task);
                                        removeItem(taskViewHolder.getLayoutPosition());
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });
                                // запуск анимаций в определенной последовательности
                                AnimatorSet translationSet = new AnimatorSet ();
                                translationSet.play(translationX).before(translationXBack);
                                translationSet.start();
                            }

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    flipIn.start();

                }
            });
        }
    }

    // в зависимосте от позиции возвращает таск или сепаратор
    @Override
    public int getItemViewType(int position) {
        if(getItem(position).isTask()){
            return TYPE_TASK;
        }else
        {
            return TYPE_SEPARATOR;
        }
    }

}
