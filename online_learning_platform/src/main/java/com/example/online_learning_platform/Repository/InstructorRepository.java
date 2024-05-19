package com.example.online_learning_platform.Repository;

import com.example.online_learning_platform.Entity.Instructor;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    // Define custom methods if needed
    Optional<Instructor> findByEmail(String email);
    @SuppressWarnings({ "nullness", "null" })
        List<Instructor> findAll();
        

   
}
