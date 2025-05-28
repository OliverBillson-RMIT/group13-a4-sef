package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Person {
    
    private String personID;
    private String firstName;
    private String lastName;
    private String address;
    private String birthdate;
    private HashMap<Date, Integer> demeritPoints; // Hold demerit points and offense day.
    private boolean isSuspended;

    public boolean addPerson(String personID, String firstName, String lastName, String address,
            String birthdate) {

        boolean isLengthInvalid = false;
        // This is how we set our 'isValid'. If > 0, we have an issue, thus details are
        // invalid.
        int issuesCount = 0;

        /*
         * Valdidate - Condition 1 (personID)
         */
        // personID Length must be 10.
        if (personID.length() != 10) {
            isLengthInvalid = true;

            issuesCount++;
            System.out.println("personID is not valid: personID must be 10 characters long.");
        }

        // Don't check the rest if our length is invalid, we may have index OOB issues.
        if (!isLengthInvalid) {

            // Chars 0-1 must be nums between 2-9.
            for (int i = 0; i < 2; i++) { // Check indexes 0 and 1.

                // We take each character as a substring (not a char), and convert them to int.
                String strNumber = personID.substring(i, i + 1);
                int num = convertStrToInt(strNumber);

                // First two characters must be between 2-9 (inclusive)
                if (num < 2 || num > 9) {
                    issuesCount++;
                    System.out.println(
                            "personID is not valid: Character " + (i + 1) + " must be a number between 2 and 9.");
                }
            }

            // Check index 3 - 8 for special characters.
            /*
             * Our pattern checks for:
             * punctuation characters, - (\p{punct}) These chars are:
             * !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
             * At least 2 times - {2,}
             */
            Pattern p = Pattern.compile("[\\p{Punct}]{2,}");
            Matcher m = p.matcher(personID.substring(2, 8)); // We create a substring from index 3-8 (inclusive)

            // If no match is found:
            if (!m.find()) {
                issuesCount++;
                System.out.println(
                        "personID is not valid: There must be at least 2 special characters between characters 3 and 8.");
            }
        }

        /*
         * Valdidate - Condition 2 (address)
         */
        // We can use a regex to split up the string per |.
        String regex = "[\\|]";
        String[] addresses = address.split(regex);

        // Check we have the correct number of elements in address (5 for num, street,
        // city, state, country)
        boolean addressLengthWrong = false; // If length is wrong, don't do remaining address checks incase of OOB exceptions.
        if (addresses.length != 5) {
            addressLengthWrong = true;
            issuesCount++;
            System.out.println(
                    "Address is not valid: Address must be in the correct format. E.g. Example: 32|Highland Street|Melbourne|Victoria|Australia.");
        }

        // Skip checks, if wrong length to avoid OOB exceptions.
        if (!addressLengthWrong) {
            // Check address number. If can't conver to number, something is invalid.
            String addressNumber = addresses[0];
            if (convertStrToInt(addressNumber) == -1) {

                // This handle an address number like 33A. If indexes 0 - length-1 is number we
                // may have a "33A" address
                Pattern p = Pattern.compile("^[0-9]+[A-Za-z]$");
                Matcher m = p.matcher(addressNumber);
                if (!m.find()) {
                    System.out.println("Address is not valid: Address number is not a valid number.");
                    issuesCount++;
                }
            }

            // Check state is vic.
            if (!(addresses[3].equalsIgnoreCase("victoria"))) {
                issuesCount++;
                System.out.println("Address is not valid: State is not \"Victoria\".");
            }

            // Check country is aud.
            if (!(addresses[4].equalsIgnoreCase("australia"))) {
                issuesCount++;
                System.out.println("Address is not valid: Country is not \"Australia\".");
            }

        }

        /*
         * Valdidate - Condition 3 (date)
         */

        // Try parse date. If fail, date is likely invalid.
        try {
            // Parse date and match format.
            @SuppressWarnings("unused")
            LocalDate parsedDate = LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (DateTimeParseException e) {
            issuesCount++;
            System.out.println("Birthdate is not valid: Birthdate is not in the format DD-MM-YYYY.");
        }

        // We return this value, and use it to check if our details are valid.
        boolean isValid = issuesCount == 0;

        /*
         * Write details to file:
         */
        // Only write to file if our checks are valid. 
        if (isValid) {

            // Try open a new file, if fail, print to console. 
            try (FileWriter myWriter = new FileWriter(personID + "-details.txt")) {
                myWriter.write(personID + "," + firstName + "," + lastName + "," + address + "," + birthdate);
                myWriter.close();
            } catch (IOException e) {
                System.out.println("Details could not be saved to file.");
                isValid = false;
            }
        }

        return isValid;
    } // | addPerson

    
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

    /*
     * Helper functions
     */

    /**
     * Helper function to convert a string to an int.
     * Will return -1 if input is not a proper integer, or if input is invalid.
     * 
     * @param str String to convert to int
     * @return An int. If valid, returns integer representation of str. If invalid,
     *         return -1.
     */
    public static int convertStrToInt(String str) {
        int output;
        try {
            output = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            output = -1;
        }
        return output;
    }
}
