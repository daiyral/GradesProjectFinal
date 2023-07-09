package com.example.ex7;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.HashSet;
import java.util.Set;


public class CourseNotificationService extends Service {
    public static final int CHANNEL_ID_INT = 1;

    private HashSet<String> old_course_list=null;
    NotificationManager mNotiMgr;
    Notification.Builder mNotifyBuilder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        initForeground();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        Context context = CourseNotificationService.this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent localIntent = new Intent();
                localIntent.setAction("Counter");

                while (true) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Set<String> myCourses = sharedPreferences.getStringSet("my_courses", new HashSet<String>());

                    if (old_course_list == null) {
                        old_course_list = new HashSet<String>();
                        old_course_list.addAll(myCourses);
                    } else {
                        for (String course : myCourses) {
                            if (!old_course_list.contains(course)) {
                                // New course added
                                old_course_list.add(course);
                                String course_name = course.split(";")[0];
                                String course_grade = course.split(";")[3];
                                // Send notification
                                updateNotification("New course added to your list: " + course_name + ", grade: " + course_grade);
                            } else if (!myCourses.contains(course)) {
                                // Course removed
                                old_course_list.remove(course);
                                String course_name = course.split(";")[0];
                                String course_grade = course.split(";")[3];
                                // Send notification
                                updateNotification("Course removed from your list: " + course_name);
                            }
                        }

                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return START_NOT_STICKY;
    }


    private void initForeground(){
        String CHANNEL_ID = "my_channel_01";
        if (mNotiMgr==null )
            mNotiMgr= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "My main channel",
                NotificationManager.IMPORTANCE_DEFAULT);
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                .createNotificationChannel(channel);

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mNotifyBuilder = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("If a new grade is added a notification will appear here")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent);
        startForeground(CHANNEL_ID_INT, updateNotification(""));
    }

    public Notification updateNotification(String details){
        mNotifyBuilder.setContentText(details).setOnlyAlertOnce(false);
        Notification noti = mNotifyBuilder.build();
        noti.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        mNotiMgr.notify(CHANNEL_ID_INT, noti);
        return noti;
    }
}
