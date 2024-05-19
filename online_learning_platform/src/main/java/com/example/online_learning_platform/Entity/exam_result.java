package com.example.online_learning_platform.Entity;

import java.util.List;

public class exam_result {




    private long exam_id;


    private Long studentId;


    private int grade;

    // Constructors, getters, and setters
    public exam_result() {
    }



    public long getExam_id() {
        return exam_id;
    }

    public void setExam_id(long exam_id) {
        this.exam_id = exam_id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
