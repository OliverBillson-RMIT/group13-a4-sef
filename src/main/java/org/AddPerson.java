package org;

import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddPerson {

    private String personID;
    private String firstName;
    private String lastName;
    private String address;
    private String birthdate;
    private HashMap<Date, Integer> demeritPoints; // Hold demerit points and offense day.
    private boolean isSuspended;

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

    public static boolean AddPersonFunc(String personId, String firstName, String lastName, String address,
            String birthdate) {

        // We return this value, and use it to check if our details are valid.
        boolean isValid; 

        boolean isLengthInvalid = false;
        // This is how we set our 'isValid'. If > 0, we have an issue, thus details are invalid. 
        int issuesCount = 0;

        /*
         * Valdidate - Condition 1
         */
        // personId Length must be 10.
        if (personId.length() != 10) {
            isLengthInvalid = true;

            issuesCount++;
            System.out.println("personId not valid: personId must be 10 characters long.");
        }

        // Don't check the rest if our length is invalid, we may have index OOB issues.
        if (!isLengthInvalid) {

            // Chars 0-1 must be nums between 2-9.
            for (int i = 0; i < 2; i++) { // Check indexes 0 and 1.

                // We take each character as a substring (not a char), and convert them to int.
                String strNumber = personId.substring(i, i + 1);
                int num = convertStrToInt(strNumber);

                // First two characters must be between 2-9 (inclusive)
                if (num < 2 || num > 9) {
                    issuesCount++;
                    System.out.println("personId not valid: Character " + (i + 1) + " must be a number between 2 and 9.");
                }
            }

            // Check index 3 - 8 for special characters.
            /* 
            * Our pattern checks for:
            *   punctuation characters, - (\p{punct}) These chars are: !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
            *   At least 2 times - {2,}
            */ 
            Pattern p = Pattern.compile("[\\p{Punct}]{2,}");
            Matcher m = p.matcher(personId.substring(2, 8)); // We create a substring from index 3-8 (inclusive)

            // If no match is found:
            if (!m.find()) {
                issuesCount++;
                System.out.println("personId not valid: There must be at least 2 special characters between characters 3 and 8.");
            }
        }

        /*
         * Valdidate - Condition 2
         */
        

        isValid = issuesCount == 0;
        System.out.println("\n\n------------------------------------------------");
        System.out.println("Total issues: " + issuesCount);
        System.out.println("Valid?: " + isValid);

        return isValid;
    }

    public static void main(String[] args) {
        AddPersonFunc("1234567890", "Julian", "Bashir", "Somewher", "04/20/02");
    }
}
/*
 * 1. addPerson function. 
 * This method stores information about a person in a TXT file. 
 * However, the following conditions should be considered when adding persons. 
 * If the Person's information meets the below conditions and any other
 * conditions you may want to consider,
 * the information should be inserted into a TXT file, and the addPerson
 * function should return true. 
 * Otherwise, the information should not be inserted into the TXT file, and the
 * addPerson function should return false.
 * 
 * Condition 1: personID should be exactly 10 characters long;
 * the first two characters should be numbers between 2 and 9,
 * there should be at least two special characters between characters 3 and 8,
 * and the last two characters should be uppercase letters (A-Z). Example: *
 * "56s_d%&fAB"
 * 
 * Condition 2: The address of the Person should follow the following format:
 * Street Number|Street|City|State|Country. 
 * The State should be only Victoria.
 * Example: 32|Highland Street|Melbourne|Victoria|Australia.
 * 
 * Condition 3: The format of the birthdate of the person should follow the
 * following format: DD-MM-YYYY. Example: 15-11-1990
 */