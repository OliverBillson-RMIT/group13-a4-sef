package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Person {

    private String personID;
    private String firstName;
    private String lastName;
    private String address;
    private String birthdate;
    private HashMap<LocalDate, Integer> demeritPoints = new HashMap<>(); // Hold demerit points and offense day.
    private boolean isSuspended = false;

    public boolean isSuspended() {
        return isSuspended;
    }

    public boolean addPerson(String personID, String firstName, String lastName, String address,
            String birthdate) {

        // Validation is handled by a function. Input our detials, and returns the
        // number of issues.
        // If our issue count is not 0, we have problems, and dont write details to
        // file.
        boolean isValid = validatePersonDetails(personID, firstName, lastName, address, birthdate) == 0;

        /*
         * Write details to file:
         */
        // Only write to file if our checks are valid.
        if (isValid) {

            // Our details are valid, we can save our details to person obj.
            this.personID = personID;
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.birthdate = birthdate;

            // Try open a new file, if fail, print to console.
            try (FileWriter writer = new FileWriter(personID + "-details.txt")) { // our file name is
                                                                                  // {personID}-details.txt, so each
                                                                                  // file should be unique.
                // We write as a comma seperate file
                writer.write(personID + "," + firstName + "," + lastName + "," + address + "," + birthdate);
                // Close to save file.
                writer.close();
            } catch (IOException e) {
                System.out.println("Details could not be saved to file.");
                isValid = false;
            }
        }

        return isValid;
    } // | addPerson

    public boolean updatePersonalDetails(String newPersonID, String newFirstName, String newLastName, String newAddress,
            String newBirthdate) {
        boolean changeAddress = false;
        boolean changeID = true;
        boolean updateMade = false;
        boolean canChangeOtherDetails = true;

        // Validation from Add Person:
        boolean isValid = 0 == validatePersonDetails(newPersonID, newFirstName, newLastName, newAddress, newBirthdate);

        if (!isValid) {
            return false;
        }

        LocalDate currDate = LocalDate.now();
        String[] words = newBirthdate.trim().split("\\-");

        if (words.length != 3) {
            return false;
        }

        int day = Integer.parseInt(words[0]);
        int month = Integer.parseInt(words[1]);
        int year = Integer.parseInt(words[2]);

        LocalDate newDate = LocalDate.of(year, month, day);
        Period age = Period.between(newDate, currDate);

        // check if address can be changed
        if (age.getYears() < 18) {
            changeAddress = false;
        }

        // check if ID can be changed
        char firstChar = newPersonID.charAt(0);
        if (Character.isDigit(firstChar) && (firstChar - '0') % 2 == 0) {
            changeID = false;
        }

        // check if birthday is changing
        boolean isBirthdateChanging = !this.birthdate.equals(newBirthdate);
        if (isBirthdateChanging) {
            canChangeOtherDetails = false; // if birthdate changes, no other details should be able to
        }

        if (isBirthdateChanging && canChangeOtherDetails) {
            return false; // birthday changing but other details also changing is not allowed

        }

        // check what we can update whilst following the set rules
        if (isBirthdateChanging) {
            this.birthdate = newBirthdate;
            updateMade = true;

        } else {
            if (!this.firstName.equals(newFirstName)) {
                this.firstName = newFirstName;
                updateMade = true;
            }
            if (!this.lastName.equals(newLastName)) {
                this.lastName = newLastName;
                updateMade = true;
            }
            if (!this.address.equals(newAddress) && changeAddress) {
                this.address = newAddress;
                updateMade = true;
            }
            if (!this.personID.equals(newPersonID) && changeID) {
                this.personID = newPersonID;
                updateMade = true;
            }
        }

        // only write to file if update is allowed
        if (updateMade) {
            try (FileWriter writer = new FileWriter(personID + "-updated-details.txt")) {
                writer.write(this.personID + "," + this.firstName + "," + this.lastName + "," + this.address + ","
                        + this.birthdate + "\n");
            } catch (IOException e) {
                System.out.println("Failed updating details: " + e.getMessage());
                return false;
            }

        }

        return updateMade;
    }

    public String addDemeritPoints(String offenseDate, int points) {
        // Defensive checks
        if (this.personID == null || this.birthdate == null)
            return "Failed";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate offenseDay;
        try {
            offenseDay = LocalDate.parse(offenseDate, formatter);
        } catch (DateTimeParseException e) {
            return "Failed";
        }
        if (points < 1 || points > 6)
            return "Failed";

        // Parse birthdate and calculate age
        LocalDate birthDay = LocalDate.parse(this.birthdate, formatter);
        int age = Period.between(birthDay, LocalDate.now()).getYears();

        // Load all past demerits for this person from file into HashMap
        loadDemeritsFromFile();

        // Add new offense to HashMap
        demeritPoints.put(offenseDay, points);

        // Calculate total points in last 2 years
        int totalPoints = 0;
        LocalDate now = LocalDate.now();
        for (Map.Entry<LocalDate, Integer> entry : demeritPoints.entrySet()) {
            if (!entry.getKey().isBefore(now.minusYears(2))) {
                totalPoints += entry.getValue();
            }
        }

        // Suspension logic
        if ((age < 21 && totalPoints > 6) || (age >= 21 && totalPoints > 12)) {
            isSuspended = true;
        }

        // Append new offense to file
        String fileName = "demerits.txt";
        String toWrite = personID + "|" + offenseDate + "|" + points;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(toWrite);
            writer.newLine();
        } catch (IOException e) {
            return "Failed";
        }

        return "Success";
    }

    // Loads all demerits for this person from the file into the HashMap
    private void loadDemeritsFromFile() {
        demeritPoints.clear(); // Clear previous data
        String fileName = "demerits.txt";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String row;
            while ((row = reader.readLine()) != null) {
                String[] parts = row.split("\\|");
                if (parts.length == 3 && parts[0].equals(personID)) {
                    LocalDate date = LocalDate.parse(parts[1], formatter);
                    int pts = Integer.parseInt(parts[2]);
                    demeritPoints.put(date, pts);
                }
            }
        } catch (IOException | DateTimeParseException | NumberFormatException e) {
            // Ignore if file not found or lines are malformed
        }
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

    /**
     * Takes person details, and checks them against various conditions. If any fail
     * validation, return the number of issues.
     * 
     * @param personID
     * @param firstName
     * @param lastName
     * @param address
     * @param birthdate
     * @return issueCount, Integer. Counts the number of issues with inputs. TO
     *         check if no issues were found.
     */
    public static int validatePersonDetails(String personID, String firstName, String lastName, String address,
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
        boolean addressLengthWrong = false; // If length is wrong, don't do remaining address checks incase of OOB
                                            // exceptions.
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

        return issuesCount;
    }
}