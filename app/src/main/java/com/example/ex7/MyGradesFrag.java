package com.example.ex7;

//import android.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MyGradesFrag extends Fragment {
    private myGradesFragListener listener;
    private GradesModel viewModel;
    private TextView emptyStateText;
    private RecyclerView recyclerView;

    public GradeAdapter adapter;

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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // Get the menu items you want to show/hide
        MenuItem addCourseMenuItem = menu.findItem(R.id.add_course);

        // Update the visibility of menu items
        addCourseMenuItem.setVisible(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_frag, container, false);
        emptyStateText = view.findViewById(R.id.empty_state_text);
        recyclerView = view.findViewById(R.id.courseRecycler);

        return view;
    }

    @Override
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvCourses = (RecyclerView) view.findViewById(R.id.courseRecycler);
        viewModel = new ViewModelProvider(requireActivity()).get(GradesModel.class);
        adapter = new GradeAdapter(view.getContext(), getActivity(), viewModel, this.listener);
        rvCourses.setAdapter(adapter);
        rvCourses.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        RecyclerView rvCourses = getView().findViewById(R.id.courseRecycler);
        TextView emptyStateText = getView().findViewById(R.id.empty_state_text);
        if (adapter != null && adapter.getItemCount() == 0) {
            rvCourses.setVisibility(View.GONE);
            emptyStateText.setVisibility(View.VISIBLE);
        } else {
            rvCourses.setVisibility(View.VISIBLE);
            emptyStateText.setVisibility(View.GONE);
        }
    }
    public GradeAdapter getAdapter(){
        return this.adapter;
    }
    public interface myGradesFragListener{
        void viewGradeInformation(Course course);
    }

}
