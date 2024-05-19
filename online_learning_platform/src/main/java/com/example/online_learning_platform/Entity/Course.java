package com.example.online_learning_platform.Entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "instructor_id", referencedColumnName = "id")
    private Instructor instructor;

    @Column(name = "name")
    private String name;

    @Column(name = "duration")
    @Temporal(TemporalType.DATE)
    private Date duration; 

    @Column(name = "category")
    private String category;

    @Column(name = "rating")
    private double rating = 0.0;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "enrolled_students")
    private int enrolledStudents = 0;

    @Column(name = "sum_of_ratings")
    private double sum_of_ratings=0.0;
    @Column(name = "num_of_ratings")
    private double num_of_ratings=0.0;


    @Column(name = "status", columnDefinition = "boolean default false")
    private boolean status; 

   
    
    @ElementCollection
    private List<String> reviews;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getsumOfRatings() {
        return sum_of_ratings  ;
    }

    public void setsumOfRatings(double sum_of_ratings  ) {
        this.sum_of_ratings   = sum_of_ratings  ;
    }

    public double getnumOfRatings() {
        return num_of_ratings;
    }

    public void setnumOfRatings(double num_of_ratings) {
        this.num_of_ratings = num_of_ratings;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(int enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
