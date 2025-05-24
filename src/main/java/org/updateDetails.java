package org;

import java.util.Date;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.Period;

public class updateDetails {
    private String personID;
    private String firstName;
    private String lastName;
    private String address;
    private String birthdate;
    private HashMap<Date, Integer> demeritPoints; // Hold demerit points and offense day.
    private boolean isSuspended;

    public boolean updatePersonalDetails(String personID, String firstName, String lastName, String address, String birthdate) {

        boolean changeAddress = false;
        boolean changeBirthDate = false;

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

        if(changeAddress) {
            this.address = address;
        }

        return false;
    }
}
