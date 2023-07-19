package com.example.ex7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements MyGradesFrag.myGradesFragListener, AllCoursesFrag.AllCoursesFragListener,ReadGradeBySMS.SmsListener, SelectSpecificCourseFrag.SelectSpecificCourseFragListener {
    private final int READ_SMS_CODE = 1;
    private final int RECEIVE_SMS_CODE = 2;
    private ReadGradeBySMS smsReceiver;
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private FrameLayout fragmentContainer;
    private FrameLayout fragmentContainer1;
    private FrameLayout fragmentContainer2;



    @Override
    protected void attachBaseContext(Context base) {
        // Load the selected language preference from SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(base);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String selectedLanguage = sharedPreferences.getString("selected_language", "en");

        Locale locale = new Locale(selectedLanguage);

        // Update the locale
        Locale newLocale = new Locale(selectedLanguage);
        Locale.setDefault(newLocale);

        // Apply the new locale configuration
        Configuration configuration = new Configuration();
        configuration.setLocale(newLocale);
        base = base.createConfigurationContext(configuration);

        super.attachBaseContext(base);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions(Manifest.permission.RECEIVE_SMS,RECEIVE_SMS_CODE, "granted RECEIVE_SMS permission");
        smsReceiver = new ReadGradeBySMS(this);
        // Create an intent filter for SMS_RECEIVED_ACTION
        IntentFilter intentFilter = new IntentFilter(SMS_RECEIVED_ACTION);
        // Register the BroadcastReceiver
        registerReceiver(smsReceiver, intentFilter);
        startService();
        getSupportActionBar().setTitle(R.string.title);
        setContentView(R.layout.activity_main);
        fragmentContainer = findViewById(R.id.fragmentContainer_portrait1);
        fragmentContainer1 = findViewById(R.id.fragmentContainer1_land);
        fragmentContainer2 = findViewById(R.id.fragmentContainer2_land);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape orientation
            showTwoFragments(savedInstanceState);
        } else {
            // Portrait orientation
            showOneFragment(savedInstanceState);
        }
    }
    private void showOneFragment(Bundle bundle) {
        Fragment fragment = null;
        String activeFragment = "MyGradesFrag";
        if (bundle != null) {
            if (bundle.containsKey("activeFragment_landscape")) {
                activeFragment = bundle.getString("activeFragment_landscape");
            }
        }
        if (activeFragment.equals("MyGradesFrag")) {
            fragment = new MyGradesFrag();
        } else if (activeFragment.equals("AllCoursesFrag")) {
            fragment = new AllCoursesFrag();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer_portrait1, fragment)
                .commit();
    }

    private void showTwoFragments(Bundle bundle) {
        Fragment fragment1 = null;
        String activeFragment = "MyGradesFrag";
        if (bundle != null) {
            if (bundle.containsKey("activeFragment_portrait")) {
                activeFragment = bundle.getString("activeFragment_portrait");
            }
        }
        if(activeFragment.equals("MyGradesFrag")){
            fragment1 = new MyGradesFrag();
//            if(((MyGradesFrag) fragment1).getModel().getItemSelected() != RecyclerView.NO_POSITION){
//
//            };
        }else if(activeFragment.equals("AllCoursesFrag")){
            fragment1 = new AllCoursesFrag();
        }

        SelectSpecificCourseFrag fragment2 = new SelectSpecificCourseFrag();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer1_land, fragment1)
                .replace(R.id.fragmentContainer2_land, fragment2)
                .commit();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Fragment fragment1 = fragmentManager.findFragmentById(R.id.fragmentContainer1_land);
            String activeFragmentString = "";
            if (fragment1 instanceof MyGradesFrag) {
                activeFragmentString = "MyGradesFrag";
            } else if (fragment1 instanceof AllCoursesFrag) {
                activeFragmentString = "AllCoursesFrag";
            }
            savedInstanceState.putString("activeFragment_landscape", activeFragmentString);
        } else {
            // Portrait orientation
            Fragment fragment1 = fragmentManager.findFragmentById(R.id.fragmentContainer_portrait1);
            String activeFragmentString = "";
            if (fragment1 instanceof MyGradesFrag) {
                activeFragmentString = "MyGradesFrag";
            } else if (fragment1 instanceof SelectSpecificCourseFrag) {
                if(((SelectSpecificCourseFrag) fragment1).getCreator().equals("MyGradesFrag"))
                    activeFragmentString = "MyGradesFrag";
                else
                    activeFragmentString = "AllCoursesFrag";
            } else if (fragment1 instanceof AllCoursesFrag) {
                activeFragmentString = "AllCoursesFrag";
            }
            savedInstanceState.putString("activeFragment_portrait", activeFragmentString);
        }
        super.onSaveInstanceState(savedInstanceState);

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
        int id = item.getItemId();
        if (id == R.id.action_preference) {
            // Handle preference menu item click
            // Open preferences activity or show a dialog
            showPreferences();
            return true;
        } else if (id == R.id.action_exit) {
            Log.i("menu exit item", "menu exit item");
            DialogFragment newFragment = DialogFrag.newInstance();
            newFragment.show(getSupportFragmentManager(),"dialog");
            return true;
        } else if(item.getItemId() == R.id.add_course){
           showAllCourses();
           return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void clickCourseToAddGrade(Course course){
        int container = 0;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            container = R.id.fragmentContainer_portrait1;
        }
        else{
            container = R.id.fragmentContainer2_land;
        }
        Bundle args = new Bundle();
        args.putString("creator", "AllCoursesFrag");
        args.putString("name", course.getName());
        args.putString("description", course.getDescription());
        args.putFloat("grade", course.getGrade());
        args.putFloat("credit", course.getCredit());
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(container, SelectSpecificCourseFrag.class, args,"FRAGB")
//                .addToBackStack("DDD")
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void viewGradeInformation(Course course){
        int container = 0;
        Bundle args = null;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            if(course == null) return;
            container = R.id.fragmentContainer_portrait1;

        }
        else{
            container = R.id.fragmentContainer2_land;
        }
        if (course != null){
            args = new Bundle();
            args.putString("creator", "MyGradesFrag");
            args.putString("name", course.getName());
            args.putString("description", course.getDescription());
            args.putFloat("grade", course.getGrade());
            args.putFloat("credit", course.getCredit());
        }

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(container, SelectSpecificCourseFrag.class, args,"FRAGB")
                .addToBackStack("AAA")
                .commit();

    }

    private void showAllCourses() {
        int container = 0;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            container = R.id.fragmentContainer_portrait1;
        }
        else{
            container = R.id.fragmentContainer1_land;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(container, AllCoursesFrag.class, null,"allCoursesFrag")
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
    }


    @Override
    public void updateMyGradeList(Course course) {
        GradesModel viewModel = new ViewModelProvider(this).get(GradesModel.class);
        viewModel.addCourse(course);
    }

    @Override
    public void removeCourseFromMyGradeList(String courseName) {
        GradesModel viewModel = new ViewModelProvider(this).get(GradesModel.class);
        viewModel.removeCourseByName(courseName);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the BroadcastReceiver when the activity is destroyed
        unregisterReceiver(smsReceiver);
        stopService();
    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, CourseNotificationService.class);
        stopService(serviceIntent);
    }
    @Override
    public void addCourseBySms(Course course) {
        GradesModel viewModel = new ViewModelProvider(this).get(GradesModel.class);
        viewModel.addCourse(course);
    }
}
