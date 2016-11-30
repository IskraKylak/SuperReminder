package com.example.fanvlad.superremindergood.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.fanvlad.superremindergood.model.ModelTask;

/**
 * Created by Vlad on 24.10.2015.
 */
public class DBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "reminder_database";

    // отдельная таблица в которой будут такие поля как ниже
    public static final String TASKS_TABLE = "task_table";

    // поля таблицы
    public static final String TASK_TITLE_COLUMN = "task_title";
    public static final String TASK_DATE_COLUMN = "task_date";
    public static final String TASK_PRIORITY_COLUMN = "task_priority";
    public static final String TASK_STATUS_COLUMN = "task_status";
    public static final String TASK_TIME_STAMP_COLUMN = "task_time_stamp";

    // скл запрос для создания таблицы( идентификатор типа INTEGER PRIMARY KEY AUTOINCREMENT, столбец заголовка с типотекс который не может быть пустым
    // дата с типом лонг, приоритет с типом интежер, статус с типом интеджер,
    //TASK_TIME_STAMP_COLUMN будет создаваться при каждои создании нового task, за основу будет брать текущую дату, это
    // будет ключ для доступа к текущему таску, два task создать с одинаковой датой и время будет не возможно
    private static final String TASKS_TABLE_CREATE_SCRIPT = "CREATE TABLE "
            + TASKS_TABLE + " (" + BaseColumns._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK_TITLE_COLUMN + " TEXT NOT NULL, "
            + TASK_DATE_COLUMN + " LONG, " + TASK_PRIORITY_COLUMN + " INTEGER, "
            + TASK_STATUS_COLUMN + " INTEGER, " + TASK_TIME_STAMP_COLUMN + " LONG);";

    // константа для выборки, ВЫБОРКА ПО СТАТУСУ
    public static final String SELECTION_STATUS = DBHelper.TASK_STATUS_COLUMN + " = ?";
    // еще одна константа для выборки
    public static final String SELECTION_TIME_STAMP = TASK_TIME_STAMP_COLUMN + " = ?";
    // ищем похожие по заголовкам записи
    public static final String SELECTION_LIKE_TITLE = TASK_TITLE_COLUMN + " LIKE ?";

    // переменные для доступа DBQueryManager и DBUpdateManager
    private DBQueryManager queryManager;
    private DBUpdateManager updateManager;


    // инициализируем переменные для доступа
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        queryManager = new DBQueryManager(getReadableDatabase());
        updateManager = new DBUpdateManager(getWritableDatabase());
    }
    // создаем таблицу
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TASKS_TABLE_CREATE_SCRIPT);
    }
    // удаляем таблицу, при помощи DROP TABLE и пересоздавать onCreate(db);
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TASKS_TABLE);
        onCreate(db);
    }
    // сохраняет task
    public void saveTask (ModelTask task){
        ContentValues newValues = new ContentValues();

        newValues.put(TASK_TITLE_COLUMN, task.getTitle());
        newValues.put(TASK_DATE_COLUMN, task.getDate());
        newValues.put(TASK_STATUS_COLUMN, task.getStatus());
        newValues.put(TASK_PRIORITY_COLUMN, task.getPriority());
        newValues.put(TASK_TIME_STAMP_COLUMN, task.getTimeStamp());

        getWritableDatabase().insert(TASKS_TABLE, null, newValues);

    }
    // методи для доступа к этим классам DBQueryManager и DBUpdateManager
    public DBQueryManager query(){
        return queryManager;
    }

    public DBUpdateManager update(){
       return updateManager;
    }

    //Отправляет в базу данных запрос на удаление тасков
    public void removeTask(long timeStamp){
        getWritableDatabase().delete(TASKS_TABLE, SELECTION_TIME_STAMP, new String[]{Long.toString(timeStamp)});
    }
}
