package com.example.ex7;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private final Context context;
    private final CoursesModel viewModel;
    private ArrayList<Course> courseList = new ArrayList<>();

    private AllCoursesFrag.AllCoursesFragListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private boolean isSelected;

    public CourseAdapter(Context context, FragmentActivity activity, CoursesModel viewModel, AllCoursesFrag.AllCoursesFragListener listener) {
        this.viewModel = viewModel;
        this.context = context;
        this.listener = listener;
        viewModel.getCourseLiveData().observe(activity, new Observer<ArrayList<Course>>() {
            @Override
            public void onChanged(ArrayList<Course> courses) {
                setCoursesList(courses);
            }
        });
    }

    @NonNull
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View courseView = inflater.inflate(R.layout.course_row, parent, false);

        return new ViewHolder(courseView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.ViewHolder holder, int position) {
        Course course = courseList.get(position);
        this.selectedPosition = this.viewModel.getPosition();
        if (this.selectedPosition == position)
            holder.itemView.setBackgroundResource(R.color.white);
        else
            holder.itemView.setBackgroundResource(R.color.transparent);
        holder.setCourse(position, course);

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public void setCoursesList(ArrayList<Course> coursesList){
        this.courseList = coursesList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseName;
        private final TextView description;
        private final TextView creditPoints;
        //private final TextView grade;
        private final View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = (TextView) itemView.findViewById(R.id.course);
            description = (TextView) itemView.findViewById(R.id.description);
            creditPoints = (TextView) itemView.findViewById(R.id.creditPoints);
            this.view = itemView;
        }

        public void setCourse(int position, Course course){
            this.courseName.setText(course.getName());
            this.creditPoints.setText(Float.toString(course.getCredit()));
            if (course.getDescription().length() > 50) {
                this.description.setText(course.getDescription().substring(0, 50) + "...");
            }
            else {
                this.description.setText(course.getDescription());
            }
            
            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.setItemSelected(position);
                    listener.clickCourseToAddGrade(course);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
