package com.example.online_learning_platform.Repository;


import com.example.online_learning_platform.Entity.Student;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Define custom methods if needed
    Student findByEmail(String email);
    Student findByid(Long id);
    
    @SuppressWarnings({ "nullness", "null" })
    List<Student> findAll();

}
