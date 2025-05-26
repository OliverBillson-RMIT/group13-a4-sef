package org;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public static boolean AddPersonFunc(String personID, String firstName, String lastName, String address,
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
        if (addresses.length != 5) {
            issuesCount++;
            System.out.println(
                    "Address is not valid: Address must be in the correct format. E.g. Example: 32|Highland Street|Melbourne|Victoria|Australia.");
        }

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
    }

    public static void main(String[] args) {
        System.out.println(
                (AddPersonFunc("2234??7890", "Julian", "Bashir", "32A|Highland Street|Melbourne|Victoria|Australia",
                        "22-02-2005")));
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