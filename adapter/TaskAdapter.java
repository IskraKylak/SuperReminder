package com.example.fanvlad.superremindergood.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.fanvlad.superremindergood.fragment.TaskFragment;
import com.example.fanvlad.superremindergood.model.Item;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vlad on 07.10.2015.
 */
// об.еденим каренттаск и донетаск адаптер
public abstract class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Item> items;

    TaskFragment taskFragment;
    // инициализируем масив
    public TaskAdapter(TaskFragment taskFragment) {
        this.taskFragment = taskFragment;
        items = new ArrayList<>();
    }

    public Item getItem(int position) {
        return items.get(position);
    }

    // добавляет пункты списка
    public void addItem(Item item) {
        items.add(item);
        // сообщаем о добавлении нового елемента в список ( в конец списка)
        notifyItemInserted(getItemCount() - 1);
    }

    // добавляет пункты списка но с другим параметрами ( в определенную позицию)
    public void addItem(int location, Item item) {
        items.add(location, item);
        notifyItemInserted(location);
    }

    public void removeItem(int location) {
        // передаваемоя позиция больше равна  0
        if (location >= 0 && location <= getItemCount() - 1) {
            items.remove(location);
            notifyItemRemoved(location);
        }
    }

    public void removeAllItems(){
        if(getItemCount()!=0){
            items = new ArrayList<>();
            notifyDataSetChanged();

        }
    }

    // возвращает размер масива елементов списка
    @Override
    public int getItemCount() {
        return items.size();
    }
    // виден всем классам в пакете
    protected class TaskViewHolder extends RecyclerView.ViewHolder {
        // заголовок и дата
        protected TextView title;
        protected TextView date;
        protected CircleImageView priority;

        public TaskViewHolder(View itemView, TextView title, TextView date, CircleImageView priority) {
            super(itemView);
            this.date = date;
            this.title = title;
            this.priority = priority;
        }
    }

    public TaskFragment getTaskFragment() {
        return taskFragment;
    }
}
