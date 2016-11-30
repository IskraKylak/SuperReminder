package com.example.fanvlad.superremindergood;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.Toast;

import com.example.fanvlad.superremindergood.adapter.TabAdapter;
import com.example.fanvlad.superremindergood.alarm.AlarmHelper;
import com.example.fanvlad.superremindergood.database.DBHelper;
import com.example.fanvlad.superremindergood.dialog.AddingTaskDialogFragment;
import com.example.fanvlad.superremindergood.fragment.CurrentTaskFragment;
import com.example.fanvlad.superremindergood.fragment.DoneTaskFragment;
import com.example.fanvlad.superremindergood.fragment.SplashFragment;
import com.example.fanvlad.superremindergood.fragment.TaskFragment;
import com.example.fanvlad.superremindergood.model.ModelTask;

public class MainActivity extends AppCompatActivity implements AddingTaskDialogFragment.AddingTaskListener,
        CurrentTaskFragment.OnTaskDoneListener, DoneTaskFragment.OnTaskRestoreListener{
    // позволяет найти фрагмент по ид findFragmentById(int id)
    FragmentManager fragmentManager;

    PreferenceHelper preferenceHelper;

    TabAdapter tabAdapter;

    TaskFragment currentTaskFragment;
    TaskFragment doneTaskFragment;
    // переменная для поиска
    SearchView searchView;

    public DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceHelper.getInstance().init(getApplicationContext());
        preferenceHelper = PreferenceHelper.getInstance();
        // инициализируем алар хелпер
        AlarmHelper.getInstance().init(getApplicationContext());

        dbHelper = new DBHelper(getApplicationContext());

        fragmentManager = getFragmentManager();

        runSplash();

        setUI();
    }
    // переопределили методы видимости активити
    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem splashItem = menu.findItem(R.id.action_splash);
        splashItem.setChecked(preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_splash) {
            item.setChecked(!item.isChecked());//меняет значения флага меню на противоположный
            // сохроняем состояние флага в PreferenceHelper
            preferenceHelper.putBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE, item.isChecked());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void runSplash() {
        // проверяет значение флага
        if(!preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE)) {
            SplashFragment splashFragment = new SplashFragment();
            // запустили сплешфрагмент в транзакции
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, splashFragment)// заменяет 1 фрагмант на другой
                    .addToBackStack(null)
                    .commit();
        }
    }
    // отвечает за пользовательський интерфейс
    private void setUI (){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        if(toolbar != null)
        {
            // установим цвет текста
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            // подключаем тулбар
            setSupportActionBar(toolbar);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        // создаем вкладку и присвоим ей текст(в сет текст строковые ресурсы string.xml)
        tabLayout.addTab(tabLayout.newTab().setText(R.string.current_task));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.done_task));
        // создаем обьект вьюпагер и найдем макет для него
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        tabAdapter = new TabAdapter(fragmentManager,2);
        // присвоим адаптер вьюпейджеру
        viewPager.setAdapter(tabAdapter);
        // установим вьюпейджеру слушатель на события смены вкладок
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // слушатель обьекта таб лейаут
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            // определяет что таб выбран
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            //таб не выбран
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            // выбран рание выбраный таб
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        currentTaskFragment = (CurrentTaskFragment) tabAdapter.getItem(TabAdapter.CURRENT_TASK_FRAGMENT_POSITION);
        doneTaskFragment = (DoneTaskFragment) tabAdapter.getItem(TabAdapter.DONE_TASK_FRAGMENT_POSITION);
        // инициализация переменной поиска
        searchView = (SearchView) findViewById(R.id.search_view);
        // добавляем слушатель для поиска
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            // реализация поиска
            @Override
            public boolean onQueryTextChange(String newText) {
                currentTaskFragment.findTasks(newText);
                doneTaskFragment.findTasks(newText);
                return false;
            }
        });

        // инициализируем флатинг актион.. и повесим на него слушатель
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment addingTaskDialogFragment = new AddingTaskDialogFragment();
                addingTaskDialogFragment.show(fragmentManager,"AddingTaskDialogFragment");
            }
        });
    }

    @Override
    public void onTaskAdded(ModelTask newTask) {
        currentTaskFragment.addTask(newTask, true);
    }

    @Override
    public void onTaskAddingCancel() {
        Toast.makeText(this,"Task adding Cancel", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTaskDone(ModelTask task) {
        doneTaskFragment.addTask(task, false);
    }

    @Override
    public void onTaskRestore(ModelTask task) {
        currentTaskFragment.addTask(task, false);
    }
}
