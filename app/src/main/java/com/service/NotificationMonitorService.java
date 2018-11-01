package com.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import static com.massky.sraum.MainfragmentActivity.SRAUM_IS_DOWN_LOAD;

/**
 * Created by zhu on 2018/9/4.
 */

@SuppressLint("NewApi")
public class NotificationMonitorService extends NotificationListenerService {
    // 在收到消息时触发
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {


        // TODO Auto-generated method stub
        Bundle extras = sbn.getNotification().extras;
        // 获取接收消息APP的包名
        String notificationPkg = sbn.getPackageName();
        // 获取接收消息的抬头
        String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
        // 获取接收消息的内容
        String notificationText = extras.getString(Notification.EXTRA_TEXT);
        Log.i("XSL_Test", "Notification posted " + notificationTitle + " & " + notificationText);
//        Toast.makeText(NotificationMonitorService.this, "show_content:" +
//                        "Notification posted " + notificationTitle + " & " + notificationText
//                , Toast.LENGTH_LONG).show();

        switch (notificationTitle) {
            case "sraum正在下载":
                Intent intent = new Intent();
                intent.putExtra("apk_down","sraum正在下载");
                intent.setAction(SRAUM_IS_DOWN_LOAD);
                sendBroadcast(intent);
                break;
        }
    }

    // 在删除消息时触发
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // TODO Auto-generated method stub
        Bundle extras = sbn.getNotification().extras;
        // 获取接收消息APP的包名
        String notificationPkg = sbn.getPackageName();
        // 获取接收消息的抬头
        String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
        // 获取接收消息的内容
        String notificationText = extras.getString(Notification.EXTRA_TEXT);
        Log.i("XSL_Test", "Notification removed " + notificationTitle + " & " + notificationText);
//        Toast.makeText(NotificationMonitorService.this, "show_content:" +
//                        "Notification removed " + notificationTitle + " & " + notificationText
//                , Toast.LENGTH_LONG).show();
    }


//    @Override
//    public void onDestroy() {
//        requestRebind(new ComponentName(this, NotificationMonitorService.class));
//        super.onDestroy();
//    }
}
