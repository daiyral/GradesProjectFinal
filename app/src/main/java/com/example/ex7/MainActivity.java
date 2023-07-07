package com.example.ex7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements MyGradesFrag.myGradesFragListener, AllCoursesFrag.AllCoursesFragListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    .addToBackStack("BBB")
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
                    .addToBackStack("BBB")
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
                .addToBackStack("BBB")
                .commit();
    }
    

    @Override
    protected void onPause() {
        super.onPause();
        CoursesModel viewModel = new ViewModelProvider(this).get(CoursesModel.class);
        //viewModel.setSPCountries(this);
    }
}