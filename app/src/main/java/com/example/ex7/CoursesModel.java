package com.example.ex7;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CoursesModel extends AndroidViewModel {
    MutableLiveData<ArrayList<Course>> courseLiveData;
    MutableLiveData<Integer> itemSelectedLive;
    Integer itemSelected;
    private DatabaseReference dbCourses;
    ArrayList<Course> courseList = new ArrayList<>();


    public CoursesModel(Application app) {
        super(app);
        this.courseLiveData = new MutableLiveData<>();
        this.itemSelectedLive = new MutableLiveData<>();
        initCourseList(app);
        this.itemSelected = RecyclerView.NO_POSITION;
        this.itemSelectedLive.setValue(this.itemSelected);
    }

    public void initCourseList(Application app){
        Context context =  app.getApplicationContext();
        dbCourses = FirebaseDatabase.getInstance().getReference("courses");
        dbCourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    Course course = courseSnapshot.getValue(Course.class);
                    courseList.add(course);
                }
                courseLiveData.setValue(courseList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public MutableLiveData<ArrayList<Course>> getCourseLiveData() {
        return this.courseLiveData;
    }

    public MutableLiveData<Integer> getItemSelected() {
        return this.itemSelectedLive;
    }

    public void setItemSelected(int position){
        this.itemSelected = position;
        this.itemSelectedLive.setValue(this.itemSelected);
    }

    public int getPosition(){
        return this.itemSelected;
    }

    public void removeCourse(int position){
        courseList.remove(position);
        courseLiveData.setValue(courseList);
    }

    public Course getCourse(int position){
        return this.courseList.get(position);
    }
}

