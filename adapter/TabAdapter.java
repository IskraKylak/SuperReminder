package com.example.fanvlad.superremindergood.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.example.fanvlad.superremindergood.fragment.CurrentTaskFragment;
import com.example.fanvlad.superremindergood.fragment.DoneTaskFragment;

/**
 * Created by Vlad on 30.09.2015.
 */

// берет данные из источника и размещает их в списек (адаптер)
public class TabAdapter extends FragmentStatePagerAdapter {
    // хранит количество вкладок
    private int numberOfTabs;

    public static final int CURRENT_TASK_FRAGMENT_POSITION = 0;
    public static final int DONE_TASK_FRAGMENT_POSITION = 1;
    // для того что бы обьекты не создавались при каждом вызове гет итем
    private CurrentTaskFragment currentTaskFragment;
    private DoneTaskFragment doneTaskFragment;
    // новые обьекты будем вызывать при помощи конструктора
    public TabAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
        currentTaskFragment = new CurrentTaskFragment();
        doneTaskFragment = new DoneTaskFragment();
    }

    @Override
    public Fragment getItem(int i) {
        // возвращает разные фрагменты в зависимосте от значения переменной i
        switch (i)
        {
            case 0:
                return  currentTaskFragment;
            case 1:
                return  doneTaskFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
