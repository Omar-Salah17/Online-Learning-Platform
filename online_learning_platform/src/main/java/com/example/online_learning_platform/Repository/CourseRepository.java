package com.example.online_learning_platform.Repository;


import com.example.online_learning_platform.Entity.Course;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    // Custom query method to find courses by category
    List<Course> findByCategory(String category);


    Course findByid(long id); 


    List<Course> findByName(String name);

  // Custom query method to find courses by name and instructor id
    List<Course> findByNameAndInstructorId(String name, Long instructorId);

    // Custom query method to find courses by instructor id
    List<Course> findByInstructorId(Long instructorId);
    
    // Custom query method to find courses by rating greater than a specified value
    List<Course> findByRatingGreaterThan(double rating);
    
    List<Course> findByOrderByRatingDesc();
    // Add more custom query methods as needed


    List<Course> findAll(Specification<Course> spec);


    List<Course> findByCategoryIgnoreCase(String category);


  

   


   

//List <Course>findCourses();
    
}
