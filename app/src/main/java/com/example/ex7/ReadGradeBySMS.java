package com.example.ex7;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReadGradeBySMS extends BroadcastReceiver {
    Course new_course = null;
    private SmsListener listener;

    public ReadGradeBySMS(SmsListener listener) {
        this.listener = listener;
    }
    //MUST RECIEVE SMS WITH structure for example :" Recived new grade from information center 80 on Algebra "
    @Override
        public void onReceive(Context context, Intent intent) {
            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            SmsMessage message = messages[0];
            if(message!=null){
                String senderNumber = message.getDisplayOriginatingAddress();
                String contentSMS = message.getDisplayMessageBody();
                if (senderNumber.equals("braude")){
                    try {
                        String[] splitSMS = contentSMS.split("information center")[1].split("on");
                        String courseName = splitSMS[1];
                        String courseGrade = splitSMS[0];
                        getCourseFromFireBase(context, courseName, Float.parseFloat(courseGrade));
                    }
                    catch (Exception e){
//                        Toast.makeText(context, "SMS from: " + senderNumber + "\nMessage: " + contentSMS, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

    public void addGradeToSP(Context context, Course new_course){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int i=0;
        Set<String> myCourses = sharedPreferences.getStringSet("my_courses", new HashSet<String>());
        for (String course : myCourses) {
            String[] courseInfo = course.split(";");
            if (courseInfo[0].equals(new_course.getName())) {
                myCourses.remove(course);
                this.listener.removeCourseFromMyGradeList(i);
                break;
            }
            i++;
        }
        String newCourse = new_course.getName() + ";" + new_course.getDescription() + ";" + new_course.getCredit() + ";" + new_course.getGrade();
        Set<String> newCourseSet = new HashSet<String>();
        newCourseSet.addAll(myCourses);
        newCourseSet.add(newCourse);
        editor.putStringSet("my_courses", newCourseSet);
        editor.apply();


    }
    private void getCourseFromFireBase(Context context, String courseName, Float courseGrade){
        DatabaseReference dbCourses = FirebaseDatabase.getInstance().getReference("courses");
        dbCourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    Course course = courseSnapshot.getValue(Course.class);
                    if (course.getName().trim().toLowerCase(Locale.ROOT).equals(courseName.trim().toLowerCase(Locale.ROOT)))
                    {
                        new_course = new Course(courseName.trim(), course.getDescription(), course.getCredit(), courseGrade);
                        addGradeToSP(context, new_course);
                        listener.addCourseBySms(new_course);
                        break;

                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public interface SmsListener {
        public void addCourseBySms(Course course);
        public void removeCourseFromMyGradeList(int idx);
    }

}


