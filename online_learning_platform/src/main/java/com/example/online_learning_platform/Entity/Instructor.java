package com.example.online_learning_platform.Entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Instructor")
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username")
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "bio")
    private String bio;

    @Column(name = "affiliation")
    private String affiliation;

    @Column(name = "years_of_experience")
    private int years_of_experience;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<Course> courses;

    
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setyears_of_experience(int years_of_experience) {
        this.years_of_experience = years_of_experience;
    }

    public int getyears_of_experience() {
        return years_of_experience;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

  

}
