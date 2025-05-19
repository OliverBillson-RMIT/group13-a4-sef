package org.example;

import java.util.Date;
import java.util.HashMap;


public class Person {
    
    private String personID;
    private String firstName;
    private String lastName;
    private String address;
    private String birthdate;
    private HashMap<Date, Integer> demeritPoints; // Hold demerit points and offense day.
    private boolean isSuspended;

    public boolean addPerson() {
        // TODO: Implement ME!
        // Don't forget to create your own file, don't edit this file!!
        // We will combine our functions later!
        System.out.println("addPerson");
        return true;
    }
    
    public boolean updatePersonalDetails() {
        // TODO: Implement ME!
        // Don't forget to create your own file, don't edit this file!!
        // We will combine our functions later!
        System.out.println("updatePersonalDetails");
        return true;
    }
    
    public String addDemeritPoints() {
        // TODO: Implement ME!
        // Don't forget to create your own file, don't edit this file!!
        // We will combine our functions later!
        System.out.println("addDemeritPoints");
        return "Success";
    }
}
