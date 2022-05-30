package com.example.android_alarmservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button btnStartTimer;
    Calendar dateTime = Calendar.getInstance();

    private final TimePickerDialog.OnTimeSetListener onTimeSetListener =
            (view, hourOfDay, minute) -> {
                // получаем из календаря время и устанавливаем на это время будильник
                dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dateTime.set(Calendar.MINUTE, minute);
                setAlarm();
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartTimer = findViewById(R.id.btnStartTimer);

        // устанавливаем значения календаря на определенное время
        btnStartTimer.setOnClickListener(view -> new TimePickerDialog(MainActivity.this,
                onTimeSetListener,
                dateTime.get(Calendar.HOUR_OF_DAY),
                dateTime.get(Calendar.MINUTE),
                true)
                .show());
    }

    private void setAlarm() {
        long timerMillis = dateTime.getTimeInMillis();
        long currentMillis = System.currentTimeMillis();
        if (timerMillis < currentMillis) {
            long dayMillis = 24 * 60 * 60 * 1000;
            timerMillis += dayMillis;
        }

        Intent alarm = new Intent(MainActivity.this, Alarm.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timerMillis,
                PendingIntent.getBroadcast(getApplicationContext(), 0, alarm, 0));
    }
}