package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Person person = new Person();

        Scanner scnr = new Scanner(System.in);

        boolean exit = false; 
        
        while (!exit) {

            System.out.println("Please input which function you would like to run:");
            System.out.println("---------------------------------------------------------------");
            System.out.println("1. addPerson");
            System.out.println("2. updatePersonalDetails");
            System.out.println("3. addDemeritPoints\n");
            System.out.println("4. Exit");
            
            System.out.print("Input: ");
            String input = scnr.nextLine();
            
            switch (input) {
                case "1" -> person.addPerson();
                case "2" -> person.updatePersonalDetails();
                case "3" -> person.addDemeritPoints();
                case "4" -> exit = true;
                default -> System.out.println("Please enter a valid input.");
            }
            System.out.println("---------------------------------------------------------------\n");
        }
    }
}