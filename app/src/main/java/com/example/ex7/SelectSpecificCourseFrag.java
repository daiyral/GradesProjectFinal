package com.example.ex7;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;


public class SelectSpecificCourseFrag extends Fragment {
    private CoursesModel viewModel;
    private Course position = null;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.course_detail_frag, container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView courseDetails = view.findViewById(R.id.detail_text);
        viewModel = new ViewModelProvider(requireActivity()).get(CoursesModel.class);
        viewModel.getCourseLiveData().observe(getActivity(), new Observer<ArrayList<Course>>() {
            @Override
            public void onChanged(ArrayList<Course> courses) {

                if (position != null && !courses.contains(position))
                    courseDetails.setText("");
            }
        });
        viewModel.getItemSelected().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer >= 0){
                    position = viewModel.getCourse(integer);
                    if (position != null)
                        courseDetails.setText(position.getDescription());
                    else
                        courseDetails.setText("");
                }
            }
        });
    }
}
