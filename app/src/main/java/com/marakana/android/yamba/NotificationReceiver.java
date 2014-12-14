package com.marakana.android.yamba;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    public static final int NOTIFICATION_ID = 42;

    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int count = intent.getIntExtra("count", 0);

        PendingIntent operation = PendingIntent.getActivity(context, -1, new Intent(context, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
        // 获得通知服务
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(context)
                .setContentTitle("New tweets!")
                .setContentText("You've got " + count + " new tweets")
                .setSmallIcon(android.R.drawable.sym_action_email)
                .setContentIntent(operation) // 设置一个PendingIntent
                .setAutoCancel(true)
                .getNotification();
        notification.defaults = Notification.DEFAULT_SOUND; // 发出默认声音
        notificationManager.notify(NOTIFICATION_ID, notification); // 第一个参数为自定义的通知唯一标识
    }
}
