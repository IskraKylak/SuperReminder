package com.example.fanvlad.superremindergood;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceHelper {
    // ключ доступа
    public static final String SPLASH_IS_INVISIBLE = "spalash_is_invisible";

    private static PreferenceHelper instance;

    private Context context;

    private SharedPreferences preferences;// хранилище

    private PreferenceHelper (){
    }
    // получаем обьект инстанс или создаем его
    public static PreferenceHelper getInstance (){
        if (instance == null)
        {
            instance = new PreferenceHelper();
        }
        return instance;
    }

    public void init (Context context)
    {
        this.context = context;
        // название настрое к и модификатор настроек
        preferences =  context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    public void putBoolean (String key, boolean value){
        // вносит изменения в настройки
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        // изменения вступили в силу
        editor.apply();
    }

    public boolean getBoolean(String key)
    {
        return preferences.getBoolean(key, false);
    }
}
