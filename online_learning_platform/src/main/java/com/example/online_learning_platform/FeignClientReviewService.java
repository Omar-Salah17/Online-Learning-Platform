package com.example.online_learning_platform;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.online_learning_platform.Entity.TestCenter;

@FeignClient(value = "testcenter" ,url = "http://localhost:8181")

public interface FeignClientReviewService {
    
    @PostMapping("/demo2-1.0-SNAPSHOT/api/testcenters/signup")
    String getAllReviewForCourse(@RequestBody TestCenter testCenter);
}