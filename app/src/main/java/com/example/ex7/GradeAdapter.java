package com.example.ex7;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder> {
    private final Context context;
    private final GradesModel viewModel;
    private ArrayList<Course> courseList = new ArrayList<>();

    private float gradeAvg=0;
    private float totalCredits=0;

    private MyGradesFrag.myGradesFragListener listener;

    private int selectedPosition = RecyclerView.NO_POSITION;
    private boolean isSelected;


    public GradeAdapter(Context context, FragmentActivity activity, GradesModel viewModel, MyGradesFrag.myGradesFragListener listener) {
        this.viewModel = viewModel;
        this.context = context;
        this.listener = listener;

        viewModel.getCourseLiveData().observe(activity, new Observer<ArrayList<Course>>() {
            @Override
            public void onChanged(ArrayList<Course> courses) {
                setCoursesList(courses);
                updateAvg(context);
            }
        });
    }


    @NonNull
    @Override
    public GradeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View courseView = inflater.inflate(R.layout.course_row, parent, false);
        return new ViewHolder(courseView);
    }

    public float getGradeAvg() {
        return gradeAvg;
    }

    public float getTotalCredits() {
        return totalCredits;
    }

    @Override
    public void onBindViewHolder(@NonNull GradeAdapter.ViewHolder holder, int position) {
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

    public void setCoursesList(ArrayList<Course> coursesList) {
        this.courseList = coursesList;
        notifyDataSetChanged();
        updateEmptyView();
    }
    private void updateEmptyView(){
        TextView empty_list = ((FragmentActivity) context).findViewById(R.id.empty_state_text);
        RecyclerView recyclerView = ((FragmentActivity) context).findViewById(R.id.courseRecycler);
        if (empty_list != null && recyclerView != null)
        {
            if (getItemCount() == 0) {
                empty_list.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                empty_list.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }

    }
    public void updateAvg(Context context){
        float totalGradeCreditSum = 0;
        float totalCreditSum = 0;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> courses = prefs.getStringSet("my_courses", new HashSet<String>());

        if (!courses.isEmpty()) {
            for (String course : courses) {
                String[] courseDetails = course.split(";");
                float credit = Float.parseFloat(courseDetails[2]);
                float grade = Float.parseFloat(courseDetails[3]);
                totalCreditSum += credit;
                totalGradeCreditSum += credit * grade;
            }
            this.gradeAvg = totalGradeCreditSum / totalCreditSum;
            this.totalCredits = totalCreditSum;
        }
        else{
            this.gradeAvg=0;
            this.totalCredits=0;
        }

        TextView avgView = ((FragmentActivity) context).findViewById(R.id.gradeAvgView);
        TextView creditsView = ((FragmentActivity) context).findViewById(R.id.creditSum);
        if(avgView!=null && creditsView!=null){
            String avg, credits;
            avg = String.format("%.2f", this.gradeAvg);
            credits = String.format("%.2f", this.totalCredits);
            avgView.setText(avgView.getText().toString().split(":")[0] + ": " + avg);
            creditsView.setText(creditsView.getText().toString().split(":")[0] + ": " + credits);
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseName;
        private final TextView description;
        private final TextView creditPoints;

        private final TextView grade;
        private final View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = (TextView) itemView.findViewById(R.id.course);
            description = (TextView) itemView.findViewById(R.id.description);
            creditPoints = (TextView) itemView.findViewById(R.id.creditPoints);
            grade = (TextView) itemView.findViewById(R.id.grade);
            this.view = itemView;
        }

        public void setCourse(int position, Course course) {
            this.courseName.setText(course.getName());
            if (course.getDescription().length() > 40) {
                this.description.setText(course.getDescription().substring(0, 40) + "...");
            } else {
                this.description.setText(course.getDescription());
            }
            this.creditPoints.setText(Float.toString(course.getCredit()));
            this.grade.setText(Float.toString(course.getGrade()));
            this.view.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Delete course")
                            .setMessage("Are you sure you want to delete this course?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    Set<String> myCourses = sharedPreferences.getStringSet("my_courses", new HashSet<String>());
                                    Set<String> newCourseSet = new HashSet<String>();
                                    newCourseSet.addAll(myCourses);
                                    for (String course1 : newCourseSet) {
                                        String[] courseInfo = course1.split(";");
                                        if (courseInfo[0].equals(course.getName())) {
                                            newCourseSet.remove(course1);
                                            break;
                                        }
                                    }
                                    editor.putStringSet("my_courses", newCourseSet);
                                    editor.apply();
                                    viewModel.removeCourse(position);
                                    listener.viewGradeInformation(null);
                                    selectedPosition = RecyclerView.NO_POSITION;
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Handle Cancel button click
                                    // Perform any actions or logic here
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
            });
            View.OnClickListener handleClick = new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    viewModel.setItemSelected(position);
                    listener.viewGradeInformation(courseList.get(position));
                    notifyDataSetChanged();
                }
            };
            this.view.setOnClickListener(handleClick);

        }
    }
}


