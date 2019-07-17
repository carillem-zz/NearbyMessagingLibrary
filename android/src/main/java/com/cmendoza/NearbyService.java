package com.cmendoza;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

public class NearbyService extends Service
{
    public static final String FOREGROUND = "com.cmendoza.nearbymessaging.NearbyService";
    public static final String APP_NAME = "Nearby Messaging Example";

    private NotificationCompat.Builder build = null;
    private String CHANNEL_ID = "NRBYMSG_CHNL";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        createNotificationChannel();
        build = createNotificationCompat();

        startDefaultNotification();

        Nearby.getMessagesClient(this).handleIntent(intent, new MessageListener()
        {
            @Override
            public void onFound(Message msg)
            {
                String message = NearbyMessage.readSubscribedMessage(msg).getMessage();

                Log.i("NearbyMessaging found:", message);
            }

            @Override
            public void onLost(Message message)
            {
                Log.i("NearbyMessaging bg msg:", "lost");
            }
        });

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    private void startDefaultNotification()
    {
        int NOTIFICATION_ID = 572834;

        String packageName = getApplicationContext().getPackageName();
        Intent openApp = getPackageManager().getLaunchIntentForPackage(packageName);
        PendingIntent redirectToApp = PendingIntent.getActivity(getApplicationContext(),
                1000,
                openApp,
                PendingIntent.FLAG_UPDATE_CURRENT);

        build.setSmallIcon(android.R.mipmap.sym_def_app_icon)
                .setContentTitle(APP_NAME)
                .setContentText("Nearby running in background")
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(redirectToApp);

        Notification defaultNotif = build.build();

        startForeground(NOTIFICATION_ID, defaultNotif);

    }

    private NotificationCompat.Builder createNotificationCompat()
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        return builder;
    }

    private void createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Nearby Messaging";
            String description = "Running in background";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
