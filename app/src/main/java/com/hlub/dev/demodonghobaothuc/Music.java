package com.hlub.dev.demodonghobaothuc;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class Music extends Service {
    MediaPlayer mediaPlayer;
    int id;
    private static final int NOTIFICATION_ID = 99;
    private static final String CHANNEL_ID = "com.singhajit.notificationDemo.channelId";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String nhanKey = intent.getExtras().getString("extra");
        Log.e("Nhận key ", nhanKey);

        if (nhanKey.equals("on")) {
            id = 1;
        } else if (nhanKey.equals("off")) {
            id = 0;
        }

        if (id == 1) {
            mediaPlayer = MediaPlayer.create(this, R.raw.toichocogaido);
            mediaPlayer.start();
            id = 0;
            runAsForeground();
        } else {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }


        return START_NOT_STICKY;
    }

    private void runAsForeground() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText(getString(R.string.isRecording))
                .setContentIntent(pendingIntent).build();

        startForeground(NOTIFICATION_ID, notification);

    }


}
