package com.hlub.dev.demodonghobaothuc;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class AlarmReceiver extends BroadcastReceiver {


    private String CHANNEL_ID = "CHANEL";
    private int notificationId = 99;
    private String ACTION_SNOOZE = "ACTION";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Receiver", "Hello");

        //Nhân kq
        String chuoi = intent.getExtras().getString("extra");
        Log.e("Key ", chuoi);

        //->truyền cho SERVICE
        Intent myIntent = new Intent(context, Music.class);
        myIntent.putExtra("extra", chuoi);

        //từ android 8.0 bị hạn chế

        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(myIntent);
        } else {
            context.startService(myIntent);
        }

        createNotificationChannel(context);
        noti(context);

    }

//    public void noti(Context context) {
//        //TRUYỀN EXTRA ->TẮT NHẠC
//        Intent intentMusic = new Intent(context, Music.class);
//        intentMusic.putExtra("extra", "off");
//        PendingIntent ptMusic =
//                PendingIntent.getBroadcast(context, 0, intentMusic, 0);
//
//        //CHUYỂN VỀ MÀN HÌNH
//        Intent intentMain = new Intent(context, MainActivity.class);
//        PendingIntent ptMain =
//                PendingIntent.getBroadcast(context, 0, intentMain, 0);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle("My notification")
//                .setContentText("Hello World!")
//                .setContentIntent(ptMain)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"))
//                .addAction(R.drawable.ic_launcher_background, "abc",
//                        ptMusic);
//
//        //hiển thị thông báo
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//
//
//        notificationManager.notify(notificationId, mBuilder.build());
//
//
//    }


    public void noti(Context context) {
        Intent notificationIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);

        Notification notification = builder.setContentTitle("Demo App Notification")
                .setContentText("New Notification From Demo App..")
                .setTicker("New Message Alert!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
            builder.addAction(R.drawable.ic_launcher_foreground,"abc",pendingIntent);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "NotificationDemo",
                    IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notification);
    }


    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
