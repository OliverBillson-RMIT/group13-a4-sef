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
        if (points < 1 || points > 6) {
            return "Failed";
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate offense;
        try {
            offense = LocalDate.parse(offenseDate, fmt);
        } catch (DateTimeParseException e) {
            return "Failed";
        }

        LocalDate dob;
        try {
            dob = LocalDate.parse(birthdate, fmt);
        } catch (DateTimeParseException e) {
            return "Failed";
        }

        int age = Period.between(dob, LocalDate.now()).getYears();

        int recentPoints = 0;
        List<String> fileLines = new ArrayList<>();
        String fileName = "demerits.txt";
        String newLine = personID + "|" + offenseDate + "|" + points;

        try {
            File file = new File(fileName);
            if (!file.exists()) file.createNewFile();

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                fileLines.add(line);
                String[] parts = line.split("\\|");
                if (parts.length == 3 && parts[0].equals(personID)) {
                    LocalDate prevDate = LocalDate.parse(parts[1], fmt);
                    if (prevDate.isAfter(LocalDate.now().minusYears(2))) {
                        recentPoints += Integer.parseInt(parts[2]);
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            return "Failed";
        }

        recentPoints += points;

        if ((age < 21 && recentPoints > 6) || (age >= 21 && recentPoints > 12)) {
            isSuspended = true;
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
            bw.write(newLine);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            return "Failed";
        }

        return "Success";
 
    }
    public static void main(String[] args) {
    addDemeritPoints p = new addDemeritPoints("56s_d%&fAB", "01-01-2006");

    String result = p.addDemeritPoints("25-05-2024", 4);
    System.out.println("Result: " + result);
    System.out.println("Suspended? " + p.isSuspended());
}

}
