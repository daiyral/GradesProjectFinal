package com.example.ex7;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class SelectSpecificCourseFrag extends Fragment {
    private CoursesModel viewModel;
    private Course position = null;
    private String courseName = null;
    private String courseDescription = null;
    private Float courseGrade = null;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_course_frag, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            this.courseDescription = args.getString("description");
            this.courseName = args.getString("name");
            this.courseGrade = args.getFloat("grade");
            TextView courseName = view.findViewById(R.id.courseName);
            TextView courseDescription = view.findViewById(R.id.courseDescription);
            courseName.setText(this.courseName);
            courseDescription.setText(this.courseDescription);
        }
        EditText gradeInput = view.findViewById(R.id.gradeInput);
        gradeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void afterTextChanged(android.text.Editable s) {
                if (s.length() > 0) {
                    float grade = Float.parseFloat(s.toString());
                    if (grade >= 0 && grade <= 100) {
                        courseGrade = grade;
                    }
                }
            }
        });
        Button addGradeButton = view.findViewById(R.id.addGradeButton);
        addGradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGradeClick(v);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public void addGradeClick(View view) {
        Context context = getContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> myCourses = sharedPreferences.getStringSet("my_courses", new HashSet<String>());
        String newCourse = courseName + ";" + courseDescription + ";" + courseGrade  ;
        myCourses.add(newCourse);
        editor.putStringSet("my_courses", myCourses);
        editor.apply();
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().popBackStack();

    }
}
