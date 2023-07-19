package com.example.ex7;

//import android.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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


public class AllCoursesFrag extends Fragment{
	private AllCoursesFragListener listener;
	private CoursesModel viewModel;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	@Override
	public void onAttach(@NonNull Context context) {
		try{
			this.listener = (AllCoursesFragListener)context;
		}catch(ClassCastException e){
			throw new ClassCastException("the class " +
					context.getClass().getName() +
					" must implements the interface 'AllCoursesFragListener'");
		}
		super.onAttach(context);
	}
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		// Get the menu items you want to show/hide
		MenuItem addCourseMenuItem = menu.findItem(R.id.add_course);

		// Update the visibility of menu items
		addCourseMenuItem.setVisible(false);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.course_frag, container, false);
		TextView avg_text = view.findViewById(R.id.gradeAvgView);
		TextView credits_text = view.findViewById(R.id.creditSum);
		avg_text.setVisibility(View.GONE); // Show the TextView
		credits_text.setVisibility(View.GONE); // Show the TextView
		return view;
	}

	@Override
	public void onViewCreated(View view,@Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RecyclerView rvCourses = (RecyclerView) view.findViewById(R.id.courseRecycler);
		viewModel = new ViewModelProvider(requireActivity()).get(CoursesModel.class);
		CourseAdapter adapter = new CourseAdapter(view.getContext(), getActivity(), viewModel, this.listener);
		rvCourses.setAdapter(adapter);
		rvCourses.setLayoutManager(new LinearLayoutManager(view.getContext()));
	}

	public interface AllCoursesFragListener{
		void clickCourseToAddGrade(Course course);
	}

}
