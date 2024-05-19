package com.example.online_learning_platform.Controller;

import com.example.online_learning_platform.Entity.Student;
import com.example.online_learning_platform.Entity.TestCenter;
import com.example.online_learning_platform.Entity.exam;
import com.example.online_learning_platform.Entity.exam_result;
import com.example.online_learning_platform.Repository.StudentRepository;
import com.example.online_learning_platform.Repository.CourseRepository;
import com.example.online_learning_platform.Repository.InstructorRepository;
import com.example.online_learning_platform.Entity.Instructor;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.online_learning_platform.JWTUtil;

import com.example.online_learning_platform.Entity.Course;
import com.example.online_learning_platform.Service.AuthenticationServiceStudent;



import com.example.online_learning_platform.Service.AuthenticationServiceInstructor;


import java.util.List;

import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AuthenticationController {


    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private InstructorRepository instructorRepository;
    
   
   @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AuthenticationServiceStudent authenticationService;
    @Autowired
    private AuthenticationServiceInstructor authenticationServiceInstructor ;

    
 


    @Autowired
    private CourseRepository courseRepository;

    @PostMapping("/Student/signup")
    public String signup(@RequestBody Student newStudent) {
        return authenticationService.signUp(newStudent);
    }
    @PostMapping("/Student/signin")
    public String signin(@RequestBody Student Student) {
        return authenticationService.signIn(Student);
    }
    @GetMapping("/Student/ViweAllCourses")
    public List<Course> viewAllCourses(@RequestHeader("Authorization") String token) {
        return authenticationService.viewAllCourses(token);
    }
    @GetMapping("/Student/ViweUpdateCourses")
    public List<Course> ViweUpdateCourses(@RequestHeader("Authorization") String token) {
        return authenticationService.viewupdateCourses(token);
    }

    @GetMapping("/Student/SearchByName")
    public List<Course> searchCoursesByName(@RequestParam String name, @RequestHeader("Authorization") String token) {
        return authenticationService.searchByName(name, token);
    }


    @GetMapping("/Student/SearchByCategory")
    public List<Course> searchCoursesByCategoryStudent(@RequestParam String category,@RequestHeader("Authorization") String token) {
        return authenticationService.searchCoursesByCategory(category,token);
    }

    @GetMapping("/Student/sortedByRating")
    public List<Course> getCoursesSortedByRatingstudent(@RequestHeader("Authorization") String token) {
        return authenticationService.searchCoursesSortedByRatings(token);
    }




    @PostMapping("/Student/enroll")
    public String enrollStudent(@RequestParam Long studentId, @RequestParam Long courseId, @RequestHeader("Authorization") String token) {
         JWTUtil jwtUtil = new JWTUtil();
        DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
        String email = decodedJWT.getClaim("email").asString();

        
        // If the email is null or empty, the token is invalid
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }

            // Fetch the instructor from the database based on the email
         Student existingStudent = studentRepository.findByEmail(email);
            
         if (existingStudent==null) {
            throw new IllegalArgumentException("Student not found");
        }

    
        String enrollmentServiceUrl = "http://localhost:8091/enroll?studentId=" + studentId + "&courseId=" + courseId; // Replace with the actual URL of the EnrollmentService
        String result = restTemplate.postForObject(enrollmentServiceUrl, null, String.class);
        return result;
      
    }
  

     @PostMapping("/Student/cancelEnrollment")
    public String cancelEnrollment(@RequestParam Long enrollmentId, @RequestHeader("Authorization") String token) {
        JWTUtil jwtUtil = new JWTUtil();
        DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
        String email = decodedJWT.getClaim("email").asString();
        
        System.out.println(enrollmentId);
        // If the email is null or empty, the token is invalid
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }

            // Fetch the instructor from the database based on the email
         Student existingStudent = studentRepository.findByEmail(email);
            
         if (existingStudent==null) {
            throw new IllegalArgumentException("Student not found");
        }

        // Cancel enrollment
       
        String enrollmentServiceUrl = "http://localhost:8091/cancel?enrollmentId=" + enrollmentId ; // Replace with the actual URL of the EnrollmentService
        String result = restTemplate.postForObject(enrollmentServiceUrl, null, String.class);
        return result;
      
    }

 @GetMapping("/Student/AcceptiningEnrollment")
 public String AcceptiningEnrollment(@RequestParam Long studentId, @RequestHeader("Authorization") String token) {
    JWTUtil jwtUtil = new JWTUtil();
    DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
    String email = decodedJWT.getClaim("email").asString();
    
    // If the email is null or empty, the token is invalid
    if (email == null || email.isEmpty()) {
        throw new IllegalArgumentException("Invalid token");
    }

        // Fetch the instructor from the database based on the email
     Student existingStudent = studentRepository.findByEmail(email);
        
     if (existingStudent==null) {
        throw new IllegalArgumentException("Student not found");
    }

     String enrollmentServiceUrl = "http://localhost:8091/AcceptingEnrollment?studentId=" + studentId ; // Replace with the actual URL of the EnrollmentService
     String result = restTemplate.getForObject(enrollmentServiceUrl, String.class); 
     return result;
 }
    @PostMapping("/Student/Rate")
    public String Rate(@RequestParam double rate,@RequestParam Long courseId,@RequestHeader("Authorization") String token ){
        JWTUtil jwtUtil = new JWTUtil();
        DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
        String email = decodedJWT.getClaim("email").asString();
        
        // If the email is null or empty, the token is invalid
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }
    
            // Fetch the instructor from the database based on the email
         Student existingStudent = studentRepository.findByEmail(email);
            
         if (existingStudent==null) {
            throw new IllegalArgumentException("Student not found");
        }
        authenticationService.RattingCourse(rate, courseId);

        return "Ratting is Done";
        
    }


    @PostMapping("/Student/Review")
    public String ReviewCourse(@RequestBody Course course,@RequestParam Long courseId,@RequestHeader("Authorization") String token ){
        JWTUtil jwtUtil = new JWTUtil();
        DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
        String email = decodedJWT.getClaim("email").asString();
        
        // If the email is null or empty, the token is invalid
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }
    
            // Fetch the instructor from the database based on the email
         Student existingStudent = studentRepository.findByEmail(email);
            
         if (existingStudent==null) {
            throw new IllegalArgumentException("Student not found");
        }
            
       
       String f= authenticationService.appendReviewToCourse(course, courseId);

        return f;
        
    }


    


    @PostMapping("/Instructor/signup")
    public String signup(@RequestBody Instructor NewInstructor) {
        return authenticationServiceInstructor.signup(NewInstructor);
    }
    @PostMapping("/Instructor/signin")
    public String signin(@RequestBody Instructor Instructor) {
        return authenticationServiceInstructor.signIn(Instructor);
    }
   @PostMapping("/Instructor/AddCourse")
    public String Createourse(@RequestBody Course course, @RequestHeader("Authorization") String token) {
        return authenticationServiceInstructor.createCourse(course, token);
    }

    @GetMapping("/Instructor/ViweCourses")
    public List<Course> viewCourses(@RequestHeader("Authorization") String token) {
        return authenticationServiceInstructor.viewCourses(token);
    }
 
   @GetMapping("/Instructor/SearchByNameAndInstructorId")
    public List<Course> searchCoursesByNameAndInstructorId(@RequestParam String name, @RequestHeader("Authorization") String token) {
        return authenticationServiceInstructor.searchByNameAndInstructorId(name, token);
    }

  @GetMapping("/Instructor/SearchByCategory")
    public List<Course> searchCoursesByCategory(@RequestParam String category,@RequestHeader("Authorization") String token) {
        return authenticationServiceInstructor.searchCoursesByCategory(category,token);
    }

    @GetMapping("/Instructor/sortedByRating")
    public List<Course> getCoursesSortedByRating(@RequestHeader("Authorization") String token) {
        return authenticationServiceInstructor.searchCoursesSortedByRatings(token);
    }


 @GetMapping("/Instructor/PendingEnrollment")
    public String PendingEnrollmen(@RequestParam Long courseId, @RequestHeader("Authorization") String token) {
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

        String enrollmentServiceUrl = "http://localhost:8091/PendingEnrollment?courseId=" + courseId ; // Replace with the actual URL of the EnrollmentService
        String result = restTemplate.getForObject(enrollmentServiceUrl, String.class); 
        return result;
    }


   @PutMapping("/Instructor/UpdateEnrollment")
    public String updateEnrollment(@RequestParam Long enrollmentId,@RequestParam Long courseId, @RequestParam boolean status, @RequestHeader("Authorization") String token) {
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

         // Fetch the course from the database based on the courseId
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalCourse.isEmpty()) {
            throw new IllegalArgumentException("Course not found");
        }

        Course course = optionalCourse.get();
        if (status && course.getCapacity() > course.getEnrolledStudents()) 
        {
            course.setEnrolledStudents(course.getEnrolledStudents() + 1);
            courseRepository.save(course);
            
        }
        else{
            return "Enrollment updated Faild No Capacity";

        }

        // Make a PUT request to the EnrollmentService to update the enrollment status
        String enrollmentServiceUrl = "http://localhost:8091/UpdateEnrollment?enrollmentId=" + enrollmentId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Boolean> requestEntity = new HttpEntity<>(status, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                enrollmentServiceUrl,
                HttpMethod.PUT,
                requestEntity,
                String.class);

        // Check if the response is successful
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return "Enrollment status updated successfully";
        } else {
            // Handle error response
            throw new RuntimeException("Failed to update enrollment status");
        }
    }





    @PostMapping("/Admin/signin")
    public String signinAdmin(@RequestBody Student Student) {
        return authenticationService.signIn(Student);
    }

    //Admin
    @GetMapping("/Admin/Viewstudentaccounts")
    public List<Student> viewAccountStudents( @RequestHeader("Authorization") String token){
        JWTUtil jwtUtil = new JWTUtil();
        DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
        String email = decodedJWT.getClaim("email").asString();

        
        // If the email is null or empty, the token is invalid
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }

            // Fetch the instructor from the database based on the email
         Student existingStudent = studentRepository.findByEmail(email);
            
         if (existingStudent==null) {
            throw new IllegalArgumentException("Admin not found");
        }

        authenticationService.getAllAccounts();



       return  authenticationService.getAllAccounts();
    }



    @GetMapping("/Admin/ViewInstructoraccounts")
    public List<Instructor> viewAccountInstructor( @RequestHeader("Authorization") String token){
       
       
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
            throw new IllegalArgumentException("Admin not found");
        }
       return authenticationServiceInstructor.getAllAccounts();
    }


    @PutMapping("/Admin/EditUserAccount")
    public String Edituseraccount(@RequestBody Student Student,@RequestParam Long studentId,@RequestHeader("Authorization") String token){


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
            throw new IllegalArgumentException("Admin not found");
        }


      String edit= authenticationService.editAccount(Student, studentId);

        return edit;

    }


    @PutMapping("/Admin/EditInstructorAccount")
    public String EditInstructorAccount(@RequestBody Instructor instructor,@RequestParam Long instructorid,@RequestHeader("Authorization") String token){


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
            throw new IllegalArgumentException("Admin not found");
        }


      String edit= authenticationServiceInstructor.editAccount(instructor, instructorid);

        return edit;

    }

  
    @GetMapping("/Admin/GetAllCourses")
    public List<Course> GetAllCourses(@RequestHeader("Authorization") String token){


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
            throw new IllegalArgumentException("Admin not found");
        }


       List<Course> courses=authenticationServiceInstructor.GetAllcourse();

        return courses;

    }


    @GetMapping("/Admin/GetAllNotPublishCourses")
    public List<Course> GetNotPublishCourses(@RequestHeader("Authorization") String token){


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
            throw new IllegalArgumentException("Admin not found");
        }


       List<Course> courses=authenticationServiceInstructor.GetNotPublishCourses();

        return courses;

    }




    @PutMapping("/Admin/PublishCourse")
    public String AcceptCoursePublish(@RequestParam long courseId,@RequestParam Boolean status,@RequestHeader("Authorization") String token)
    {
        
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
            throw new IllegalArgumentException("Admin not found");
        }

        return authenticationServiceInstructor.AcceptCoursePublish(courseId, status);


    }
    @PutMapping("/Admin/EditCourse")
    public String EditCourse(@RequestBody Course course,@RequestParam long courseId,@RequestHeader("Authorization") String token){

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
            throw new IllegalArgumentException("Admin not found");
        }

        return authenticationServiceInstructor.EditCourse(course,courseId);
    }
    
    @DeleteMapping("/Admin/RemoveCourse")
    public String RemoveCourse(@RequestParam long courseId,@RequestHeader("Authorization") String token){

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
            throw new IllegalArgumentException("Admin not found");
        }

        return authenticationServiceInstructor.Remove(courseId);
    }

    @GetMapping("/Admin/getcoursespopularity")
    public List<Course> checkthecoursespopularity(@RequestHeader("Authorization") String token){

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
            throw new IllegalArgumentException("Admin not found");
        }

        return authenticationServiceInstructor.checkTheCoursesPopularity();
    }


    @GetMapping("/Admin/getcoursespopularityByrating")
    public List<Course> getcoursespopularityByrating(@RequestHeader("Authorization") String token){

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
            throw new IllegalArgumentException("Admin not found");
        }

        return authenticationServiceInstructor.checkTheCoursesPopularityByrating();
    }
      

   

//////////////////////////////////////////////////////////////////////////////////////////////////////////
  
    @PostMapping("/Admin/createAccounttestcenter")
    public ResponseEntity<String> createAccounttestcenter(@RequestBody TestCenter testcenter,@RequestHeader("Authorization") String token) {



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
            throw new IllegalArgumentException("Admin not found");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TestCenter> requestEntity = new HttpEntity<>(testcenter, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:8181/demo2-1.0-SNAPSHOT/api/testcenters/signup",
                HttpMethod.POST,
                requestEntity,
                String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // If the response status is OK, return the response body
            return ResponseEntity.ok(responseEntity.getBody());
        } else {
            // If the response status is not OK, return an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create test center");
        }

    }
         
    
   


    @GetMapping("/TestCenter/GetTestCenterByname")
    public ResponseEntity<TestCenter> GetTestCenterByname(@RequestParam String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        ResponseEntity<TestCenter> responseEntity = restTemplate.exchange(
                "http://localhost:8181/demo2-1.0-SNAPSHOT/api/testcenters/getinfobyname?name=" + name,
                HttpMethod.GET,
                null,
                TestCenter.class);
    
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // If the response status is OK, return the response body
            return ResponseEntity.ok(responseEntity.getBody());
        } else {
            // If the response status is not OK, return an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    
    @PostMapping("/TestCenter/signin")
    public ResponseEntity<String> signin(@RequestBody TestCenter testcenter) {
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        HttpEntity<TestCenter> requestEntity = new HttpEntity<>(testcenter, headers);
    
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:8181/demo2-1.0-SNAPSHOT/api/testcenters/signin",
                HttpMethod.POST,
                requestEntity,
                String.class);
    
        // Check if the response status is OK
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // If the response status is OK, return the response body
            return ResponseEntity.ok(responseEntity.getBody());
        } else {
            // If the response status is not OK, return the response body
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        }
    }
    


    @GetMapping("/TestCenter/GetTestCenterByid")
    public ResponseEntity<TestCenter> GetTestCenterByid(@RequestParam long id, @RequestHeader("Authorization") String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token); // Set the Authorization header with the token
    
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
    
        ResponseEntity<TestCenter> responseEntity = restTemplate.exchange(
                "http://localhost:8181/demo2-1.0-SNAPSHOT/api/testcenters/getinfobyid?id=" + id,
                HttpMethod.GET,
                requestEntity, // Pass the headers with the token
                TestCenter.class);
    
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // If the response status is OK, return the response body
            return ResponseEntity.ok(responseEntity.getBody());
        } else {
            // If the response status is not OK, return an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @PutMapping("/TestCenter/updatecenterbyid")
    public ResponseEntity<TestCenter> updatecenterbyid(@RequestParam long id, @RequestBody TestCenter testcenter, @RequestHeader("Authorization") String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token); // Set the Authorization header with the token
    
        HttpEntity<TestCenter> requestEntity = new HttpEntity<>(testcenter, headers);
    
        ResponseEntity<TestCenter> responseEntity = restTemplate.exchange(
                "http://localhost:8181/demo2-1.0-SNAPSHOT/api/testcenters/updatecenterbyid?id=" + id,
                HttpMethod.PUT,
                requestEntity,
                TestCenter.class);
    
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // If the response status is OK, return the response body
            return ResponseEntity.ok(responseEntity.getBody());
        } else {
            // If the response status is not OK, return an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/TestCenter/addExam")
    public ResponseEntity<String> addExam(@RequestBody exam Exam, @RequestHeader("Authorization") String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token); // Set the Authorization header with the token
    
        HttpEntity<exam> requestEntity = new HttpEntity<>(Exam, headers);
    
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:8181/demo2-1.0-SNAPSHOT/api/exams/addExam",
                HttpMethod.POST,
                requestEntity,
                String.class);
    
        if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
            // If the response status is CREATED, return the response body
            return ResponseEntity.ok(responseEntity.getBody());
        } else {
            // If the response status is not CREATED, return an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add exam");
        }
    }
    



    @GetMapping("/Student/getexambyname")
    public ResponseEntity<List<exam>> getexambyname(@RequestParam String name,@RequestHeader("Authorization") String token) {

        JWTUtil jwtUtil = new JWTUtil();
            DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
            String email = decodedJWT.getClaim("email").asString();
            
            // If the email is null or empty, the token is invalid
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("Invalid token");
            }
        
                // Fetch the instructor from the database based on the email
            Student existingStudent = studentRepository.findByEmail(email);
                
            if (existingStudent==null) {
                throw new IllegalArgumentException("Student not found");
            }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Corrected URI to be absolute
        String uri = "http://localhost:8181/demo2-1.0-SNAPSHOT/api/exams/getexambyname?name=" + name;

        ResponseEntity<List<exam>> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<exam>>() {}); // Corrected response type to List<exam>

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // If the response status is OK, return the response body
            return ResponseEntity.ok(responseEntity.getBody());
        } else {
            // If the response status is not OK, return an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }





    @GetMapping("/Student/getexambylocation")
    public ResponseEntity<List<exam>> getexambylocation(@RequestParam String location,@RequestHeader("Authorization") String token) {

        JWTUtil jwtUtil = new JWTUtil();
        DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
        String email = decodedJWT.getClaim("email").asString();
        
        // If the email is null or empty, the token is invalid
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }

            // Fetch the instructor from the database based on the email
        Student existingStudent = studentRepository.findByEmail(email);
            
        if (existingStudent==null) {
            throw new IllegalArgumentException("Student not found");
        }
            
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Corrected URI to be absolute
        String uri = "http://localhost:8181/demo2-1.0-SNAPSHOT/api/exams/getexambylocation?location=" + location;

        ResponseEntity<List<exam>> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<exam>>() {}); // Corrected response type to List<exam>

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // If the response status is OK, return the response body
            return ResponseEntity.ok(responseEntity.getBody());
        } else {
            // If the response status is not OK, return an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @GetMapping("/Student/gettestcenterbylocation")
    public ResponseEntity<List<TestCenter>> gettestceterbylocation(@RequestParam String location,@RequestHeader("Authorization") String token) {


        JWTUtil jwtUtil = new JWTUtil();
        DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
        String email = decodedJWT.getClaim("email").asString();
        
        // If the email is null or empty, the token is invalid
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }
    
            // Fetch the instructor from the database based on the email
        Student existingStudent = studentRepository.findByEmail(email);
            
        if (existingStudent==null) {
            throw new IllegalArgumentException("Student not found");
        }
            
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Corrected URI to be absolute
        String uri = "http://localhost:8181/demo2-1.0-SNAPSHOT/api/testcenters/getinfobyLocation?location=" + location;

        ResponseEntity<List<TestCenter>> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TestCenter>>() {}); // Corrected response type to List<exam>

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // If the response status is OK, return the response body
            return ResponseEntity.ok(responseEntity.getBody());
        } else {
            // If the response status is not OK, return an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    

   
    

    @GetMapping("/TestCenter/gettestcenterexam")
    public ResponseEntity<List<exam>> getexamallname(@RequestParam long testcenter_id, @RequestHeader("Authorization") String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token); // Set the Authorization header with the token
    
        // Corrected URI to be absolute
        String uri = "http://localhost:8181/demo2-1.0-SNAPSHOT/api/testcenters/gettestcenterexams?testcenter_id=" + testcenter_id;
    
        ResponseEntity<List<exam>> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(headers), // Pass the headers with the token
                new ParameterizedTypeReference<List<exam>>() {});
    
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // If the response status is OK, return the response body
            return ResponseEntity.ok(responseEntity.getBody());
        } else {
            // If the response status is not OK, return an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


//http://localhost:8181/demo2-1.0-SNAPSHOT/api/exam_results/addstudenttoexam


    @PostMapping("/Student/addstudenttoexam")
    public  ResponseEntity<String>  addstudenttoexam(@RequestBody exam_result examR,@RequestHeader("Authorization") String token) {

        JWTUtil jwtUtil = new JWTUtil();
        DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
        String email = decodedJWT.getClaim("email").asString();
        
        // If the email is null or empty, the token is invalid
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }

            // Fetch the instructor from the database based on the email
        Student existingStudent = studentRepository.findByEmail(email);
            
        if (existingStudent==null) {
            throw new IllegalArgumentException("Student not found");
        }

        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<exam_result> requestEntity = new HttpEntity<>(examR, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:8181/demo2-1.0-SNAPSHOT/api/exam_results/addstudenttoexam",
                HttpMethod.POST,
                requestEntity,
                String.class);

        if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
            // If the response status is CREATED, return the response body
            return ResponseEntity.ok(responseEntity.getBody());
        } else {
            // If the response status is not CREATED, return an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add exam");
        }
    }

//http://localhost:8181/demo2-1.0-SNAPSHOT/api/exam_results/updateExamResult?id=3

    @PutMapping("/TestCenter/updateexamgrade")
    public ResponseEntity<exam_result> updateexamgrade(@RequestParam long id, @RequestBody exam_result er,@RequestHeader("Authorization") String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);
       
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Corrected URL to match the intended functionality
        String url = "http://localhost:8181/demo2-1.0-SNAPSHOT/api/exam_results/updateExamResult?id=" + id;

        // Corrected response type to exam_result.class
        ResponseEntity<exam_result> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(er, headers),
                exam_result.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // If the response status is OK, return the response body
            return ResponseEntity.ok(responseEntity.getBody());
        } else {
            // If the response status is not OK, return an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//http://localhost:8181/demo2-1.0-SNAPSHOT/api/exam_results/getExamResultByStudentid?Studentid=1


    @GetMapping("/TestCenter/getexambystudentid")
    public ResponseEntity<List<exam_result>> getexambystudentid(@RequestParam long Studentid, @RequestHeader("Authorization") String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token); // Set the Authorization header with the token
    
        // Corrected URI to be absolute
        String uri = "http://localhost:8181/demo2-1.0-SNAPSHOT/api/exam_results/getExamResultByStudentidintestcenter?Studentid=" + Studentid;
    
        ResponseEntity<List<exam_result>> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(headers), // Pass the headers with the token
                new ParameterizedTypeReference<List<exam_result>>() {});
    
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // If the response status is OK, return the response body
            return ResponseEntity.ok(responseEntity.getBody());
        } else {
            // If the response status is not OK, return an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/Student/viewhistory")
    public ResponseEntity<List<exam_result>> viewhistory(@RequestParam long Studentid,@RequestHeader("Authorization") String token) {


        JWTUtil jwtUtil = new JWTUtil();
        DecodedJWT decodedJWT = jwtUtil.decodeToken(token);
        String email = decodedJWT.getClaim("email").asString();
        
        // If the email is null or empty, the token is invalid
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }
    
            // Fetch the instructor from the database based on the email
        Student existingStudent = studentRepository.findByEmail(email);
            
        if (existingStudent==null) {
            throw new IllegalArgumentException("Student not found");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Corrected URI to be absolute
        String uri = "http://localhost:8181/demo2-1.0-SNAPSHOT/api/exam_results/getExamResultByStudentid?Studentid=" + Studentid;

        ResponseEntity<List<exam_result>> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<exam_result>>() {}); // Corrected response type to List<exam>

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // If the response status is OK, return the response body
            return ResponseEntity.ok(responseEntity.getBody());
        } else {
            // If the response status is not OK, return an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }







}









