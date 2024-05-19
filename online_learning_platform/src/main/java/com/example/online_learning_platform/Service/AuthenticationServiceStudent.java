
package com.example.online_learning_platform.Service;

import com.example.online_learning_platform.Repository.CourseRepository;
import com.example.online_learning_platform.Entity.Course;

import com.example.online_learning_platform.Entity.Student;
import com.example.online_learning_platform.Repository.StudentRepository;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.online_learning_platform.JWTUtil;

@Service
public class AuthenticationServiceStudent {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;

    public String signUp(Student student) {
        // Check if the email is already registered
        if (studentRepository.findByEmail(student.getEmail()) != null) {
            return "Email already registered";
        }

        // Save the new student to the database
        studentRepository.save(student);

        return "Signup successful";
    }

    public String signIn(Student student) {
        // Check if the email exists in the database
        Student existingStudent = studentRepository.findByEmail(student.getEmail());
        if (existingStudent == null) {
            return "User not found";
        }

        // Check if the password matches
        if (!existingStudent.getPassword().equals(student.getPassword())) {
            return "Incorrect password";
        }

        // Get the ID of the existing student
        long studentId = existingStudent.getId();

        // Generate JWT token with email and ID
        JWTUtil token = new JWTUtil();
        String jwtToken = token.generateToken(student.getEmail(), studentId);

        return jwtToken;
    }


     public List<Course> viewAllCourses(String token)
     {
        JWTUtil jwtUtil = new JWTUtil();
        DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
        String email = decodedJWT.getClaim("email").asString();

        // If the email is null or empty, the token is invalid
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }

        // Fetch the instructor from the database based on the email
        Student existingStudent = studentRepository.findByEmail(email);

        if (existingStudent == null) {
            throw new IllegalArgumentException("Student not found");
        }
        
     

        // Fetch courses for the instructor
        List<Course> courses = courseRepository.findAll();


           Iterator<Course> iterator = courses.iterator();

        while (iterator.hasNext()) {
            Course c= iterator.next();
            // Compare course's duration with current date
            if (!c.isStatus()) {
                // Remove the course from the list
                iterator.remove();
              
            }
        }

    
       

        return courses;
    }


    public List<Course> viewupdateCourses(String token) {
        JWTUtil jwtUtil = new JWTUtil();
        DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
        String email = decodedJWT.getClaim("email").asString();
    
        // If the email is null or empty, the token is invalid
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }
    
        // Fetch the student from the database based on the email
        Student existingStudent = studentRepository.findByEmail(email);
    
        if (existingStudent == null) {
            throw new IllegalArgumentException("Student not found");
        }
    
        // Fetch courses
        List<Course> courses = courseRepository.findAll();

        
    
        // Get current date
        Date currentDate = new Date();
    
        // Iterate through the list of courses
        Iterator<Course> iterator = courses.iterator();
        while (iterator.hasNext()) {
            Course course = iterator.next();
            // Compare course's duration with current date
            if (course.getDuration().before(currentDate)||!course.isStatus()) {
                // Remove the course from the list
                iterator.remove();
              
            }
        }
     
      

    
        return courses;
    }


    public List<Course> searchByName(String name, String token) {
    JWTUtil jwtUtil = new JWTUtil();
    DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
    String email = decodedJWT.getClaim("email").asString();

    // If the email is null or empty, the token is invalid
    if (email == null || email.isEmpty()) {
        throw new IllegalArgumentException("Invalid token");
    }

    // Fetch the student from the database based on the email
    Student existingStudent = studentRepository.findByEmail(email);

    if (existingStudent == null) {
        throw new IllegalArgumentException("Student not found");
    }

      // Create a Specification for case-insensitive search by name
      Specification<Course> spec = (Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
        // Use criteria builder to create a case-insensitive search condition
        Predicate predicate = criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")), // Convert name to lowercase for comparison
                "%" + name.toLowerCase() + "%" // Convert the search term to lowercase for comparison
        );
        return predicate;
    };

    // Perform the search using the created Specification
        List<Course> courses = courseRepository.findAll(spec);

        Iterator<Course> iterator = courses.iterator();

        while (iterator.hasNext()) {
            Course c= iterator.next();
            // Compare course's duration with current date
            if (!c.isStatus()) {
                // Remove the course from the list
                iterator.remove();
            
            }
        }


    return courses;
}
 

    public List<Course> searchCoursesByCategory(String category, String token) {
        JWTUtil jwtUtil = new JWTUtil();
        DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
        String email = decodedJWT.getClaim("email").asString();

        // If the email is null or empty, the token is invalid
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }

        // Fetch the instructor from the database based on the email
        Student existingStudent = studentRepository.findByEmail(email);

        if (existingStudent == null) {
            throw new IllegalArgumentException("Student not found");
        }
      // Create a Specification for case-insensitive search by name
      Specification<Course> spec = (Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
        // Use criteria builder to create a case-insensitive search condition
        Predicate predicate = criteriaBuilder.like(
                criteriaBuilder.lower(root.get("category")), // Convert name to lowercase for comparison
                "%" + category.toLowerCase() + "%" // Convert the search term to lowercase for comparison
        );
        return predicate;
    };
        

        List<Course> courses = courseRepository.findAll(spec);

        Iterator<Course> iterator = courses.iterator();

        while (iterator.hasNext()) {
            Course c= iterator.next();
            // Compare course's duration with current date
            if (!c.isStatus()) {
                // Remove the course from the list
                iterator.remove();
            
            }
        }

       
        return courses;
    }


    public List<Course> searchCoursesSortedByRatings(String token) {

        JWTUtil jwtUtil = new JWTUtil();
        DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
        String email = decodedJWT.getClaim("email").asString();

        // If the email is null or empty, the token is invalid
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }

        // Fetch the instructor from the database based on the email
        Student existingStudent = studentRepository.findByEmail(email);

        if (existingStudent == null) {
            throw new IllegalArgumentException("Student not found");
        }
        List<Course>courses=courseRepository.findByOrderByRatingDesc();

        Iterator<Course> iterator = courses.iterator();

        while (iterator.hasNext()) {
            Course c= iterator.next();
            // Compare course's duration with current date
            if (!c.isStatus()) {
                // Remove the course from the list
                iterator.remove();
              
            }
        }


        return  courses;
    }

    
    public String RattingCourse(double Rate, Long id) {

        Optional<Course> optionalCourse = courseRepository.findById(id);
        Course course = optionalCourse.get();
        double numOfRatings = course.getnumOfRatings();
        numOfRatings++;
        course.setnumOfRatings(numOfRatings);
        double SumOfRatings = course.getsumOfRatings();
        course.setsumOfRatings(SumOfRatings + Rate);

        course.setRating((SumOfRatings + Rate) / numOfRatings);
        courseRepository.save(course);

        return "Ratting Course";
    }

    public String appendReviewToCourse(Course course, Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course existingCourse = optionalCourse.get();
            List<String> existingReviews = existingCourse.getReviews();
            List<String> newReviews = course.getReviews();
            
            // Append new reviews to existing reviews
            existingReviews.addAll(newReviews);
            
            existingCourse.setReviews(existingReviews);
            
            courseRepository.save(existingCourse);
            return "Reviews appended to Course with ID: " + id;
        } else {
            return "Course not found with ID: " + id;
        }
    }
    
    public List<Student> getAllAccounts() 
    {
      
        List<Student> student=studentRepository.findAll();


        Iterator<Student> iterator = student.iterator();

        while (iterator.hasNext()) {
            Student s = iterator.next();
            // Compare course's duration with current date
            if ("Admin".equals(s.getUsername())) {

                
                // Remove the course from the list
                iterator.remove();
              
            }
        }

        return student;

    }

    public String editAccount(Student updatedStudent, Long studentId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
    
        if (optionalStudent.isPresent()) {
            
            Student existingStudent = optionalStudent.get();
            
            // Update the existing student's information with the provided updated student object
           existingStudent.setEmail(updatedStudent.getEmail());
           existingStudent.setUsername(updatedStudent.getUsername());
           existingStudent.setAffiliation(updatedStudent.getAffiliation());
           existingStudent.setBio(updatedStudent.getBio());
           existingStudent.setPassword(updatedStudent.getPassword());
           
    
            // Save the updated student object to the database
            studentRepository.save(existingStudent);
    
            return "Student information updated successfully";
        } else {
            return "Student not found with ID: " + studentId;
        }
    }
    
    

 


}




