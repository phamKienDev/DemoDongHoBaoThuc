package com.hlub.dev.demodonghobaothuc;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static com.hlub.dev.demodonghobaothuc.App.CHANNEL_1_ID;

public class MainActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button btnHenGio;
    private Button btnDungLai;
    private TextView tvTime;
    AlarmReceiver alarmReceiver;
    AlarmManager alarmManager;
    Calendar calendar;
    PendingIntent pendingIntent;
    PendingIntent pendingNotifi;
    private String CHANNEL_ID = "CHANEL";
    private int notificationId = 99;
    private String ACTION_SNOOZE = "ACTION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhxa();

        calendar = Calendar.getInstance();
        //cho phép truy cập vào hệ thống báo động
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        final Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);

        btnHenGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());

                int gio = timePicker.getCurrentHour();
                int phut = timePicker.getCurrentMinute();

                String hh = String.valueOf(gio);
                String mm = String.valueOf(phut);
                if (gio > 12) {
                    hh = String.valueOf(gio - 12);
                }
                if (phut < 10) {
                    mm = "0" + mm;
                }

                intent.putExtra("extra", "on");

                pendingIntent = PendingIntent.getBroadcast(
                        MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                //KÍCH HOẠT
                //trên version 19 thì mới có thêm function setExact với độ chính xác cao hơn
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager
                            .setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager
                            .set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }

                tvTime.setText("Đặt giờ " + hh + ":" + mm);
            }
        });
        btnDungLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTime.setText("Dừng lại");
                intent.putExtra("extra", "off");
                sendBroadcast(intent);

            }
        });
    }


    void anhxa() {
        timePicker = findViewById(R.id.timePicker);
        btnHenGio = findViewById(R.id.btnHenGio);
        btnDungLai = findViewById(R.id.btnDungLai);
        tvTime = findViewById(R.id.tvTime);
    }
}
