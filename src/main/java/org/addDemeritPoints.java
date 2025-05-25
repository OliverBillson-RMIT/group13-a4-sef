package org;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.List;
import java.util.ArrayList;

public class addDemeritPoints {

    private String personID;
    private String birthdate;
    private boolean isSuspended = false;

    public addDemeritPoints(String id, String bday) {
        this.personID = id;
        this.birthdate = bday;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public String addDemeritPoints(String offenseDate, int points) {
        if (points < 1 || points > 6) return "Failed";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate offenseDay, birthDay;
      try {
            offenseDay = LocalDate.parse(offenseDate, formatter);
            birthDay = LocalDate.parse(birthdate, formatter);
        } catch (DateTimeParseException e) {
            return "Failed";
        }

        int age = Period.between(birthDay, LocalDate.now()).getYears();
        int totalRecentPoints = 0;
        String fileName = "demerits.txt";
        String newEntry = personID + "|" + offenseDate + "|" + points;

        try {
            File file = new File(fileName);
            if (!file.exists()) file.createNewFile();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 3 && parts[0].equals(personID)) {
                        LocalDate pastDate = LocalDate.parse(parts[1], formatter);
                        if (pastDate.isAfter(LocalDate.now().minusYears(2))) {
                            totalRecentPoints += Integer.parseInt(parts[2]);
                        }
                    }
                }
            }
        } catch (IOException | DateTimeParseException | NumberFormatException e) {
            return "Failed";
        }

        totalRecentPoints += points;

        if ((age < 21 && totalRecentPoints > 6) || (age >= 21 && totalRecentPoints > 12)) {
            isSuspended = true;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(newEntry);
            writer.newLine();
        } catch (IOException e) {
            return "Failed";
        }

        return "Success";
    }

    public static void main(String[] args) {
        addDemeritPoints person = new addDemeritPoints("56s_d%&fAB", "01-01-2006");
        String result = person.addDemeritPoints("25-05-2024", 4);
        System.out.println("Result: " + result);
        System.out.println("Suspended? " + person.isSuspended());
    }
}
