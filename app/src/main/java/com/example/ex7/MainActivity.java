package com.example.ex7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Notification;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements MyGradesFrag.myGradesFragListener, AllCoursesFrag.AllCoursesFragListener,ReadGradeBySMS.SmsListener, SelectSpecificCourseFrag.SelectSpecificCourseFragListener {
    private final int READ_SMS_CODE = 1;
    private final int RECEIVE_SMS_CODE = 2;
    private ReadGradeBySMS smsReceiver;
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private static final int FOREGROUND_PERMISSION_REQUEST_CODE = 100;
    private static final String FOREGROUND_SERVICE_PERMISSION = "android.permission.FOREGROUND_SERVICE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions(Manifest.permission.RECEIVE_SMS,RECEIVE_SMS_CODE, "granted RECEIVE_SMS permission");
        smsReceiver = new ReadGradeBySMS(this);

        // Create an intent filter for SMS_RECEIVED_ACTION
        IntentFilter intentFilter = new IntentFilter(SMS_RECEIVED_ACTION);
        // Register the BroadcastReceiver
        registerReceiver(smsReceiver, intentFilter);
        //startService();
        getSupportActionBar().setTitle("MD Grades");
        setContentView(R.layout.activity_main);
        SelectSpecificCourseFrag fragB = (SelectSpecificCourseFrag) getSupportFragmentManager().findFragmentByTag("FRAGB");
        if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)){
            if (fragB != null) {
                getSupportFragmentManager().beginTransaction()
                        .show(fragB)
                        .commit();
            }
            else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentDetailContainerView, SelectSpecificCourseFrag.class,null, "FRAGB")
                        .commit();
            }
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, CourseNotificationService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    private void checkPermissions(String permission, int requestCode, String toastTest){
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] { permission }, requestCode);
        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MyPreferences prefFrag = (MyPreferences) getSupportFragmentManager().findFragmentByTag("prefFrag");
        if (prefFrag != null)
            return true;
        if (item.getItemId() == R.id.settings) {
            showPreferences();
            return true;
        }
        if(item.getItemId() == R.id.add_course){
            showAllCourses();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void clickCourseToAddGrade(Course course){
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            Bundle args = new Bundle();
            args.putString("name", course.getName());
            args.putString("description", course.getDescription());
            args.putFloat("grade", course.getGrade());
            args.putFloat("credit", course.getCredit());
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragContainer, SelectSpecificCourseFrag.class, args,"FRAGB")
                    .addToBackStack("DDD")
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    @Override
    public void viewGradeInformation(Course course){
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            Bundle args = new Bundle();
            args.putString("name", course.getName());
            args.putString("description", course.getDescription());
            args.putFloat("grade", course.getGrade());
            args.putFloat("credit", course.getCredit());
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragContainer, SelectSpecificCourseFrag.class, args,"FRAGB")
                    .addToBackStack("AAA")
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    private void showAllCourses() {
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragContainer, AllCoursesFrag.class, null,"allCoursesFrag")
                .addToBackStack("BBB")
                .commit();
    }

    private void showPreferences() {
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .add(android.R.id.content, new MyPreferences(), "prefFrag")
                .addToBackStack("CCC")
                .commit();
    }
    

    @Override
    protected void onPause() {
        super.onPause();
        CoursesModel viewModel = new ViewModelProvider(this).get(CoursesModel.class);
        //viewModel.setSPCountries(this);
    }


    @Override
    public void updateMyGradeList(Course course) {
        GradesModel viewModel = new ViewModelProvider(this).get(GradesModel.class);
        viewModel.addCourse(course);
    }

    @Override
    public void removeCourseFromMyGradeList(int idx) {
        GradesModel viewModel = new ViewModelProvider(this).get(GradesModel.class);
        viewModel.removeCourse(idx);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister the BroadcastReceiver when the activity is destroyed
        unregisterReceiver(smsReceiver);
       // stopService();
    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, CourseNotificationService.class);
        stopService(serviceIntent);
    }
    @Override
    public void addCourseBySms(Course course) {
        GradesModel viewModel = new ViewModelProvider(this).get(GradesModel.class);
        viewModel.addCourse(course);
//        Toast.makeText(this, "Course added successfully", Toast.LENGTH_SHORT).show();
    }
}
