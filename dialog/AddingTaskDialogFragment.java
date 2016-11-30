package com.example.fanvlad.superremindergood.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.fanvlad.superremindergood.R;
import com.example.fanvlad.superremindergood.alarm.AlarmHelper;
import com.example.fanvlad.superremindergood.fragment.Utils;
import com.example.fanvlad.superremindergood.model.ModelTask;

import java.util.Calendar;


/**
 * Created by Vlad on 02.10.2015.
 */
public class AddingTaskDialogFragment extends DialogFragment{
    // стркутура патерна наблюдатель
    // для получения доступа к диалогу из активити  нужно определить слушатель
    // вызовем эти методы в кнопках ок и кансел (слушателях)
    public interface AddingTaskListener {
        void onTaskAdded(ModelTask newTask);
        void onTaskAddingCancel();
    }
    // экземпляр слушателя эдингтаск( в блоке тру качь присвоим активити)
    private AddingTaskListener addingTaskListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // проверяем что активити реализует интерфейс иначе исключение
        try{
            addingTaskListener = (AddingTaskListener)activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement AddingTaskListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // создаем обьект класса алертдайлокбилдер
        final AlertDialog.Builder builder = new AlertDialog.Builder (getActivity());
        // находи строку по ид и установим заголовок окна методом сет тайтл
        builder.setTitle(R.string.dialog_title);
        // для работы макета с диалогом
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // находим макет диалога и все елементы в нем
        View container = inflater.inflate(R.layout.dialog_task, null);
        //елементы едит текст можно получить через текст инпут лейаут
        final TextInputLayout tilTitle = (TextInputLayout)container.findViewById(R.id.tilDialogTaskTitle);
        final EditText etTitle = tilTitle.getEditText();

        TextInputLayout tilDate = (TextInputLayout)container.findViewById(R.id.tilDialogTaskDate);
        final EditText etDate = tilDate.getEditText();

        TextInputLayout tilTime = (TextInputLayout)container.findViewById(R.id.tilDialogTaskTime);
        final EditText etTime = tilTime.getEditText();
        // списоко приоритетов
        Spinner spPriority = (Spinner) container.findViewById(R.id.spDialogTaskPrioryty);


        // присвоим заголовку методом сет хин (подсказки) поучаем ссылку на ресурс методом гетресурсес
        tilTitle.setHint(getResources().getString(R.string.task_title));
        tilDate.setHint(getResources().getString(R.string.task_date));
        tilTime.setHint(getResources().getString(R.string.task_time));

        builder.setView(container);

        final ModelTask task = new ModelTask();

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, ModelTask.PRIORITY_LEVELS);

        // присвоим адаптер спинеру
        spPriority.setAdapter(priorityAdapter);
        // слушатель
        spPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // устанавлюем приоитет используя позитион
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                task.setPriority(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // возвращает текуещее ремя
        final Calendar calendar = Calendar.getInstance();
        // добавили 1 час что б срабатывал через час если не указана время
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);


        // слушатели для полей тайм пикер дайлог и дейт пикер дайлог
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // добавляем символ пробела при вводе
                if (etDate.length() == 0) {
                    etDate.setText(" ");
                }
                // создаем обьект дейт пикер фрагмент
                DialogFragment datePickerFragment = new DatePickerFragment() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        etDate.setText(Utils.getDate(calendar.getTimeInMillis()));
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        etDate.setText(null);
                    }
                };
                // отображаем диалог методом шов
                datePickerFragment.show(getFragmentManager(), "DatePickerFragment");
            }
        });
        // все тоже для окна выбора времени
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etTime.length() == 0) {
                    etTime.setText(" ");
                }
                DialogFragment timePickerFragment = new TimePickerFragment() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        etTime.setText(Utils.getTime(calendar.getTimeInMillis()));
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        etTime.setText(null);
                    }
                };
                // паказуем
                timePickerFragment.show(getFragmentManager(), "TimePickerFragment");
            }
        });
        // кнопки подтверждения
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // присваем заголовок слушателю кнопки таск
                task.setTitle(etTitle.getText().toString());
                task.setStatus(ModelTask.STATUS_CURRENT);
                //если дата и время не равны 0 то устанвлюем в таск
                if(etDate.length() != 0 || etTime.length() != 0){
                    task.setDate(calendar.getTimeInMillis());
                    // создаем обьект аларм хелпер
                    AlarmHelper alarmHelper = AlarmHelper.getInstance();
                    alarmHelper.setAlarm(task);
                    dialog.dismiss();
                }
                task.setStatus(ModelTask.STATUS_CURRENT);
                addingTaskListener.onTaskAdded(task);
                dialog.dismiss();
            }
        });
        // кнопка отмены
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // вызываем метод созданого интерфейса
                addingTaskListener.onTaskAddingCancel();
                dialog.cancel();
            }
        });
        // устанавлюем слушатель на событие отображения диалога
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener(){
            @Override
            public void onShow(DialogInterface dialog) {
                final Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                if(etTitle.length() == 0){
                    positiveButton.setEnabled(false);
                    tilTitle.setError(getResources().getString(R.string.dialog_error_empty_title));
                }
                // слушатель на событие изменнения текста
                etTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }
                    // проверяем длину текста 0  блокируем кнопку
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s.length() == 0) {
                            positiveButton.setEnabled(false);
                            tilTitle.setError(getResources().getString(R.string.dialog_error_empty_title));
                        }else{
                            positiveButton.setEnabled(true);
                            // отключаем отображение ошибки
                            tilTitle.setErrorEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });

        return alertDialog;
    }
}
