package com.example.online_learning_platform.Service;

import com.example.online_learning_platform.Entity.Instructor;
import com.example.online_learning_platform.Repository.InstructorRepository;


import com.example.online_learning_platform.Repository.CourseRepository;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.example.online_learning_platform.Entity.Course;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.online_learning_platform.JWTUtil;

import java.util.Iterator;
import java.util.List;
@Service
public class AuthenticationServiceInstructor {

    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private CourseRepository courseRepository;

    public String signup(Instructor newInstructor) {
        if (newInstructor == null) {
            return "Invalid input";
        }
    
        String email = newInstructor.getEmail();
        if (email == null || email.isEmpty()) {
            return "Email cannot be empty";
        }
    
        // Check if an instructor with the provided email already exists
        Optional<Instructor> existingInstructor = instructorRepository.findByEmail(email);
        if (existingInstructor.isPresent()) {
            return "Email already exists";
        }
    
        try {
            // Save the new instructor
            instructorRepository.save(newInstructor);
            return "Signup successful";
        } catch (Exception e) {
            // Handle any unexpected exceptions during save operation
            return "An error occurred during signup";
        }
    }
    
   

    public String signIn(Instructor instructor) {
        // Find instructor by email
        Optional<Instructor> optionalInstructor = instructorRepository.findByEmail(instructor.getEmail());
    
        // Check if instructor exists
        if (optionalInstructor.isEmpty()) {
            return "User not found";
        }
    
        // Extract instructor from Optional
        Instructor existingInstructor = optionalInstructor.get();
    
        // Check if the password matches
        if (!existingInstructor.getPassword().equals(instructor.getPassword())) {
            return "Incorrect password";
        }
    
        // Generate JWT token
        JWTUtil token = new JWTUtil();
        String generatedToken = token.generateToken(instructor.getEmail(), existingInstructor.getId());
    
        return generatedToken;
    }
    


 
    public String createCourse(Course course, String token) {
     // Instantiate JWTUtil to validate the token
       JWTUtil jwtUtil = new JWTUtil();
        DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
        String email = decodedJWT.getClaim("email").asString();
     //   long id = decodedJWT.getClaim("id").asLong();
       
       
   
   

        // If the email is null or empty, the token is invalid
        if (email == null || email.isEmpty()) {
            return "Invalid token";
        }

        // Fetch the instructor from the database based on the email
        Optional<Instructor> optionalInstructor = instructorRepository.findByEmail(email);

        if (optionalInstructor.isEmpty()) {
            return "Instructor not found";
        }

        Instructor instructor = optionalInstructor.get();

        // Set the fetched instructor as the instructor of the course
        course.setInstructor(instructor);

        // Save the course object to the database
        courseRepository.save(course);

        // Return a success message or any appropriate response
        return "Course created successfully";
    }



    public List<Course> viewCourses( String token) 
    {
        JWTUtil jwtUtil = new JWTUtil();
        DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
        String email = decodedJWT.getClaim("email").asString();
        long id = decodedJWT.getClaim("id").asLong();
        
        // If the email is null or empty, the token is invalid
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }
    
        // Fetch the instructor from the database based on the email
        Optional<Instructor> optionalInstructor = instructorRepository.findByEmail(email);
        
        if (optionalInstructor.isEmpty()) {
            throw new IllegalArgumentException("Instructor not found");
        }
    
        // Fetch courses for the instructor
        List<Course> courses = courseRepository.findByInstructorId(id);
    
        return courses;
    }
   

    public List<Course> searchByNameAndInstructorId(String name, String token) {
        JWTUtil jwtUtil = new JWTUtil();
        DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
        String email = decodedJWT.getClaim("email").asString();
        
        // If the email is null or empty, the token is invalid
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }
    
        // Fetch the instructor from the database based on the email
        Optional<Instructor> optionalInstructor = instructorRepository.findByEmail(email);
        
        if (optionalInstructor.isEmpty()) {
            throw new IllegalArgumentException("Instructor not found");
        }
    
        Instructor instructor = optionalInstructor.get();

        // Fetch courses for the instructor by name and instructor ID
        List<Course> courses = courseRepository.findByNameAndInstructorId(name, instructor.getId());
    
        
         // Check if courses are found
    if (courses.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No courses found with the given name and instructor ID");
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
        Optional<Instructor> optionalInstructor = instructorRepository.findByEmail(email);
        
        if (optionalInstructor.isEmpty()) {
            throw new IllegalArgumentException("Instructor not found");
        }
    
      List<Course> courses = courseRepository.findByCategory(category);
      if (courses.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No courses found with the given name and instructor ID");
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
        Optional<Instructor> optionalInstructor = instructorRepository.findByEmail(email);
        
        if (optionalInstructor.isEmpty()) {
            throw new IllegalArgumentException("Instructor not found");
        }
    



        return courseRepository.findByOrderByRatingDesc();
    }


    public List<Instructor> getAllAccounts() 
    {
      
        List<Instructor> instructor=instructorRepository.findAll();


        Iterator<Instructor> iterator = instructor.iterator();

        while (iterator.hasNext()) {
            Instructor s = iterator.next();
            // Compare course's duration with current date
            if ("Admin".equals(s.getUsername())) {


                // Remove the course from the list
                iterator.remove();
              
            }
        }

        return instructor;

    }



  public String editAccount(Instructor updatedInstructor, Long InstructorId) {
        Optional<Instructor> optionalinstructor = instructorRepository.findById(InstructorId);
    
        if (optionalinstructor.isPresent()) {
            
            Instructor existing = optionalinstructor.get();
            
            // Update the existing student's information with the provided updated student object
           existing.setEmail(updatedInstructor.getEmail());
           existing.setUsername(updatedInstructor.getUsername());
           existing.setAffiliation(updatedInstructor.getAffiliation());
           existing.setBio(updatedInstructor.getBio());
           existing.setPassword(updatedInstructor.getPassword());
           existing.setyears_of_experience(updatedInstructor.getyears_of_experience());
           
    
            // Save the updated student object to the database
            instructorRepository.save(existing);
    
            return "Instructor information updated successfully";
        } else {
            return "Instructor not found with ID: " + InstructorId;
        }
    }
    


    public List<Course> GetAllcourse() {

      
        List<Course>course = courseRepository.findAll();

        return course ;
    }



    public List<Course> GetNotPublishCourses() {

      
        List<Course>course = courseRepository.findAll();
        Iterator<Course> iterator = course.iterator();

        while (iterator.hasNext()) {
            Course c= iterator.next();
            // Compare course's duration with current date
            if (c.isStatus()) {
                // Remove the course from the list
                iterator.remove();
              
            }
        }

        return course ;
    }


   
    public String AcceptCoursePublish(Long courseId,Boolean status)
    {

        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        // Check if the course exists
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            
            // Update the status of the course
            course.setStatus(status);
            
            // Save the changes to the database
            courseRepository.save(course);
           
            return "Course publication status updated successfully";
        } else {
            return "Course not found with ID: " + courseId;
        }
    }

    
    public String EditCourse(Course course,Long courseId){

        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        // Check if the course exists
        if (optionalCourse.isPresent()) {
            Course c = optionalCourse.get();
            
            c.setCapacity(course.getCapacity());
            c.setCategory(course.getCategory());
            c.setDuration(course.getDuration());
            c.setName(course.getName());
            c.setEnrolledStudents(course.getEnrolledStudents());

            // Save the changes to the database
            courseRepository.save(c);
           
            return "Course Edit successfully";
        } else {
            return "Course not found with ID: " + courseId;
        }
        

    }


    public String Remove(Long courseId){

        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        // Check if the course exists
        if (optionalCourse.isPresent()) {
            Course c = optionalCourse.get();
            
            courseRepository.delete(c);
           
            return "Course Removed successfully";
        } else {
            return "Course not found with ID: " + courseId;
        }

    }



    public List<Course> checkTheCoursesPopularity() {

        List<Course> courses = courseRepository.findAll();
        Iterator<Course> iterator = courses.iterator();
        int maxEnrolledStudents = Integer.MIN_VALUE;
        Course mostPopularCourse = null;
    
        while (iterator.hasNext()) {
            Course course = iterator.next();
            int enrolledStudents = course.getEnrolledStudents();//1
            
            // Compare course's enrolled students with the current maximum
            if (enrolledStudents > maxEnrolledStudents) {
                // Update the maximum number of enrolled students
                maxEnrolledStudents = enrolledStudents;
                // Set the most popular course to the current course
                mostPopularCourse = course;
            }
        }
    
        // Clear the courses list and add the most popular course
         courses.clear();
        if (mostPopularCourse != null) {
            courses.add(mostPopularCourse);
        }
    
        return courses;
    }
    

    public List<Course> checkTheCoursesPopularityByrating() {

        List<Course> courses = courseRepository.findAll();
        Iterator<Course> iterator = courses.iterator();

        double maxrating = Integer.MIN_VALUE;
        Course mostPopularCourse = null;
    
        while (iterator.hasNext()) {
            Course course = iterator.next();
            double rate = course.getRating();//1
            
            // Compare course's enrolled students with the current maximum
            if (rate > maxrating) {
                // Update the maximum number of enrolled students
                maxrating = rate;
                // Set the most popular course to the current course
                mostPopularCourse = course;
            }
        }
    
        // Clear the courses list and add the most popular course
         courses.clear();
        if (mostPopularCourse != null) {
            courses.add(mostPopularCourse);
        }
    
        return courses;
    }
    

}
