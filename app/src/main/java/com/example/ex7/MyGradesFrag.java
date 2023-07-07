package com.example.ex7;

//import android.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MyGradesFrag extends Fragment{
    private myGradesFragListener listener;
    private GradesModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        try{
            this.listener = (myGradesFragListener)context;
        }catch(ClassCastException e){
            throw new ClassCastException("the class " +
                    context.getClass().getName() +
                    " must implements the interface 'myGradesFragListener'");
        }
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.course_frag, container,false);
    }

    @Override
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvCourses = (RecyclerView) view.findViewById(R.id.courseRecycler);
        viewModel = new ViewModelProvider(requireActivity()).get(GradesModel.class);
        GradeAdapter adapter = new GradeAdapter(view.getContext(), getActivity(), viewModel, this.listener);
        rvCourses.setAdapter(adapter);
        rvCourses.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public interface myGradesFragListener{
        void viewGradeInformation(Course course);
    }

}
