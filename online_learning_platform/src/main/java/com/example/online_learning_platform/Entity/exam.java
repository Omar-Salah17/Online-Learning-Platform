package com.example.online_learning_platform.Entity;

public class exam  {
   

   
    private String name;

   
    private int duration;



   
    private long testcenter_id;


 
    private String availableDates;




   
    private String location;




    public String getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(String availableDates) {
        this.availableDates = availableDates;
    }




    // Constructors, getters, and setters
    public exam() {
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getTestcenter_id() {
        return testcenter_id;
    }

    public void setTestcenter_id(long testcenter_id) {
        this.testcenter_id = testcenter_id;
    }


}
