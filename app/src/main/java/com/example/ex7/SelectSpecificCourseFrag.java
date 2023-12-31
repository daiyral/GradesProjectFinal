package com.example.ex7;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextWatcher;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class SelectSpecificCourseFrag extends Fragment {
    private SelectSpecificCourseFragListener listener;
    private String courseName = null;
    private String courseDescription = null;
    private Float courseGrade = null;

    private Float courseCredit = null;

    private String creator;


    @Override
    public void onAttach(@NonNull Context context) {
        try{
            this.listener = (SelectSpecificCourseFragListener)context;
        }catch(ClassCastException e){
            throw new ClassCastException("the class " +
                    context.getClass().getName() +
                    " must implements the interface 'SelectSpecificCourseFragListener'");
        }
        super.onAttach(context);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            // Get the menu items you want to show/hide
            MenuItem addCourseMenuItem = menu.findItem(R.id.add_course);

            // Update the visibility of menu items
            addCourseMenuItem.setVisible(false);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_course_frag, container, false);
    }

    public String getCreator() {
        return creator;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView courseName = view.findViewById(R.id.courseName);
        TextView courseDescription = view.findViewById(R.id.courseDescription);
        EditText gradeInput = view.findViewById(R.id.gradeInput);
        Button addGradeButton = view.findViewById(R.id.addGradeButton);
        Bundle args = getArguments();
        if (args != null) {
            courseDescription.setVisibility(View.VISIBLE);
            gradeInput.setVisibility(View.VISIBLE);
            addGradeButton.setVisibility(View.VISIBLE);
            this.creator = args.getString("creator");
            this.courseDescription = args.getString("description");
            this.courseName = args.getString("name");
            this.courseGrade = args.getFloat("grade");
            this.courseCredit = args.getFloat("credit");
            if (this.courseGrade != 0.0) {
                gradeInput.setText(this.courseGrade.toString());
            }
            courseName.setText(this.courseName);
            courseDescription.setText(this.courseDescription);
            gradeInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                public void afterTextChanged(android.text.Editable s) {
                    if (s.length() > 0)  {
                        try {
                            float grade = Float.parseFloat(s.toString());
                            if (grade >= 0 && grade <= 100) {
                                courseGrade = grade;
                            }
                            else{
                                courseGrade = null;
                                Toast.makeText(getActivity(), "Please add grade between 0 and 100", Toast.LENGTH_SHORT).show();
                            }
                        }catch(NumberFormatException e){
                            courseGrade = null;
                            Toast.makeText(getActivity(), "Please input numbers only", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
            });

            addGradeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addGradeClick(v);
                }
            });
        }
        else{
            courseDescription.setVisibility(View.GONE);
            gradeInput.setVisibility(View.GONE);
            addGradeButton.setVisibility(View.GONE);
            courseName.setText("Please Select Course");
        }


        super.onViewCreated(view, savedInstanceState);
    }
    public interface SelectSpecificCourseFragListener {
        void updateMyGradeList(Course course);
        void removeCourseFromMyGradeList(String courseName);
    }
    public void addGradeClick(View view) {
        if(courseGrade == null){
            return;
        }
        int i=0;
        Context context = getContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Course new_course = new Course(courseName, courseDescription, courseCredit, courseGrade);
        Set<String> myCourses = sharedPreferences.getStringSet("my_courses", new HashSet<String>());
        for (String course : myCourses) {
            String[] courseInfo = course.split(";");
            if (courseInfo[0].equals(courseName)) {
                myCourses.remove(course);
                this.listener.removeCourseFromMyGradeList(courseName);
                break;
            }
            i++;
        }
        String newCourse = courseName + ";" + courseDescription + ";" + courseCredit + ";" + courseGrade;
        Set<String> newCourseSet = new HashSet<String>();
        newCourseSet.addAll(myCourses);
        newCourseSet.add(newCourse);
        editor.putStringSet("my_courses", newCourseSet);
        editor.apply();
        this.listener.updateMyGradeList(new_course);
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().popBackStack();
        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }
}
