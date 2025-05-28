package org;

import java.util.Date;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.Period;

public class sakina {
    private String personID;
    private String firstName;
    private String lastName;
    private String address;
    private String birthdate;
    private HashMap<Date, Integer> demeritPoints; // Hold demerit points and offense day.
    private boolean isSuspended;

    //need to add Oliver's addPerson checks aswell 
    //need to also modify to change txt file based on updates 
    public boolean updatePersonalDetails(String personID, String firstName, String lastName, String address, String birthdate) {

        boolean changeAddress = false;
        boolean changeBirthDate = false;
        boolean changeID = false;
        boolean updateMade = false;

        LocalDate currDate = LocalDate.now();
        String[] words = birthdate.split("\\s+");

        String d = words[0];
        String m = words[1];
        String y = words[2];

        int day = Integer.parseInt(d);
        int month = Integer.parseInt(m);
        int year = Integer.parseInt(y);

        LocalDate date = LocalDate.of(day, month, year);
        Period age = Period.between(date, currDate);
        

        if(age.getYears() > 18) {
            changeAddress = true;
        }

        if(personID.charAt(0) % 2 != 0) {
            changeID = true;
        }

        //condition 1
        if(!changeBirthDate && changeAddress) {
            this.address = address;
            updateMade = true;
        }

        //condition 3
        if(!changeBirthDate && changeID) {
            this.personID = personID;
            updateMade = true;
        }

        //condition 2
        //add proper check to see if birthday is looking to be updated 
        if (birthdate != null) {
            this.birthdate = birthdate;
            changeBirthDate = true;
        }

        return updateMade;
    }
}
