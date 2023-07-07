package com.example.ex7;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.ArraySet;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class GradesModel extends AndroidViewModel {
    MutableLiveData<ArrayList<Course>> courseLiveData;
    MutableLiveData<Integer> itemSelectedLive;
    Integer itemSelected;

    ArrayList<Course> courseList = new ArrayList<>();

    public GradesModel(Application app) {
        super(app);
        this.courseLiveData = new MutableLiveData<>();
        this.itemSelectedLive = new MutableLiveData<>();
        initCourseList(app);
        this.itemSelected = RecyclerView.NO_POSITION;
        this.itemSelectedLive.setValue(this.itemSelected);
    }


    public void initCourseList(Application app){
        Context context =  app.getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> courses = prefs.getStringSet("my_courses", new HashSet<String>());
        if (!courses.isEmpty()) {
            for (String course : courses) {
                String[] courseDetails = course.split(";");
                Course courseObj = new Course(courseDetails[0], courseDetails[1], 0, Float.parseFloat(courseDetails[2]));
                courseList.add(courseObj);
            }
        }
            courseLiveData.setValue(courseList);
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

