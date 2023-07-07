package com.example.ex7;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Course {
    private String name;
    private String description;
    private float credit;
    private float grade;

    public Course(String name, String description, float credit, float grade) {
        this.name = name;
        this.description = description;
        this.credit = credit;
        this.grade = grade;
    }
    public Course(){

    }

    public float getGrade() {
        return grade;
    }
    
    public void setGrade(float grade) {
        this.grade = grade;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }
}
