/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightbookingsystemclient;

import ejb.FlightBookingSystemManagerRemote;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;
import javax.ejb.EJB;

/**
 *
 * @author Farhan
 */
public class Main {
    @EJB
    private static FlightBookingSystemManagerRemote flightBookingSystemManager;

    // Create new instance of Main
    public Main() {
    }
    
    public static void main(String[] args) {
        Main client = new Main();
        client.populateData();
        client.execute(args);
        flightBookingSystemManager.remove();
    }
    
    // Test code to add user
    public void execute(String[] args) {
        try {
            String choice = "";
            while(!choice.equals("0")) { 
                System.out.println("******************************************");
                System.out.println("Welcome to the Flight Booking System Admin");
                System.out.println("******************************************");
                
                System.out.println("*** User Menu ***");
                System.out.print("1a. Add a User.  ");
                System.out.println("1b. Remove a User.");
                
                System.out.println("*** Flight Menu ***");
                System.out.print("2a. Add Flight.  ");
                System.out.print("2b. Update Flight.  ");
                System.out.println("2c. Delete Flight.");
                
                System.out.println("*** Schedule Menu ***");
                System.out.print("3a. Add Schedule.  ");
                System.out.print("3b. Update Schedule.  ");
                System.out.println("3c. Delete Schedule.");
                
                System.out.println("*** View Menu ***");
                System.out.print("4a. View Bookings.  ");
                System.out.print("4b. View Schedules.  ");
                System.out.println("4c. View Flights.");
                
                System.out.println("*** Requests Menu ***");
                System.out.println("5. Process Requests.");
                
                System.out.println("*** Enter 0 to Exit ***");

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("\nEnter choice: ");
                choice = br.readLine();
                
                if(choice.equals("1a"))
                    displayAddUser();
                else if(choice.equals("1b"))
                    displayDeleteUser();
                else if(choice.equals("2a"))
                    displayAddFlight();
                else if(choice.equals("2b"))
                    displayUpdateFlight();
                else if(choice.equals("2c"))
                    displayDeleteFlight();
                else if(choice.equals("3a"))
                    displayAddSchedule();
                else if(choice.equals("3b"))
                    displayUpdateSchedule();
                else if(choice.equals("3c"))
                    displayDeleteSchedule();
                else if(choice.equals("4a"))
                    displayBookings();
                else if(choice.equals("4b"))
                    displaySchedules();
                else if(choice.equals("4c"))
                    displayFlights();
                else if(choice.equals("5"))
                    displayProcessRequests();
                else if(choice.equals("0"))
                    return;
                else
                    System.out.println("\nInvalid Choice\n");
                } // end while
            
            } catch(Exception ex) {
                System.err.println("Caught an unexpected exception!");
                ex.printStackTrace();
        }
    }
    
    // Add user
    private void displayAddUser() { 
        try {
            System.out.println("\n*** Add a User ***");
            
            // Verify if user exists
            String userName = getString("Username", null);
            if(flightBookingSystemManager.verifyUser(userName)) {
                System.out.println("\nUser " + userName + " exists!\n");
            }
            // If user does not exist, create user
            else {  
                String password = getString("Password", null);
                String contactNumber = getString("Contact No.", null);
                String emailAddress = getString("Email Address", null);
                flightBookingSystemManager.addUser(userName, password, contactNumber, emailAddress);
                System.out.println("\nUser " + userName + " added successfully.\n");
            }
            
        } catch(Exception ex) {
            System.out.println("Caught an unexpected exception because " + ex.getMessage() + "\n");
        }
    }
    
    // Delete user
    private void displayDeleteUser() {
        try {
            System.out.println("\n*** Remove a User ***");
            
            String userName = getString("Username", null);
            
            if(flightBookingSystemManager.deleteUser(userName) == 0)
                System.out.println("\nUser " + userName + " deleted successfully.\n");
            else if(flightBookingSystemManager.deleteUser(userName) == 1)
                System.out.println("\nUser " + userName + " is associated with a booking and a request!\n");
            else if(flightBookingSystemManager.deleteUser(userName) == 2)
                System.out.println("\nUser " + userName + " is associated with a booking!\n");
            else if(flightBookingSystemManager.deleteUser(userName) == 3)
                System.out.println("\nUser " + userName + " is associated with a request!\n");
            else
                System.out.println("\nUser " + userName + " does not exist!\n");
            
        } catch(Exception ex) {
            System.out.println("Caught an unexpected exception because " + ex.getMessage() + "\n");
        }
    }
    
    // Add flight
    private void displayAddFlight() {
        try {
            System.out.println("\n*** Add a Flight ***");
            
            String flightNumber = getString("Flight Number", null);
            // Verify if flight exists
            if(flightBookingSystemManager.verifyFlight(flightNumber)) {
                System.out.println("\nFlight " + flightNumber + " exists!\n");
            }
            // If flight does not exist, create flight
            else {
                String departureCity = getString("Departure City", null);
                String arrivalCity = getString("Arrival City", null);
                String aircraftType = getString("Aircraft Type", null);
                int totalSeats = Integer.parseInt(getString("Total Seats", null));
                flightBookingSystemManager.addFlight(flightNumber, departureCity, arrivalCity, aircraftType, totalSeats);
                System.out.println("\nFlight " + flightNumber + " added successfully.\n");
            }
            
        } catch(Exception ex) {
            System.out.println("Caught an unexpected exception because " + ex.getMessage() + "\n");
        }
    }
      
    private void displayUpdateFlight() {
        try {
            System.out.println("\n*** Update a flight ***");
            
            String flightNumber = getString("Flight Number", null);

            // If flight and schedule exists
            if(flightBookingSystemManager.updateFlight(flightNumber) == 0) {
                String aircraftType = getString("Aircraft Type", null);
                int totalSeats = Integer.parseInt(getString("Total Seats", null));
                // Update flight
                if(flightBookingSystemManager.updateFlightPartial(flightNumber, aircraftType, totalSeats) == 0) {
                    System.out.println("\nFailed to update: No. seats updated less than total no. of booked seats!\n");
                }
                else {
                    System.out.println("\nFlight " + flightNumber + " updated!\n");
                }
            }
            // If flight exists but schedule does not exist
            else if(flightBookingSystemManager.updateFlight(flightNumber) == 1) {
                String newFlightNumber = getString("Flight Number", null);
                String departureCity = getString("Departure City", null);
                String arrivalCity = getString("Arrival City", null);
                String aircraftType = getString("Aircraft Type", null);
                int totalSeats = Integer.parseInt(getString("Total Seats", null)); 
                // Update flight
                int dummy;
                dummy = flightBookingSystemManager.deleteFlight(flightNumber);
                flightBookingSystemManager.addFlight(newFlightNumber, departureCity, arrivalCity, aircraftType, totalSeats);
                System.out.println("\nFlight " + flightNumber + " updated!\n");                
            }
            // If flight does not exist
            else
            System.out.println("\nFlight " + flightNumber + " does not exist!\n");
            
        } catch(Exception ex) {
            System.out.println("Caught an unexpected exception because " + ex.getMessage() + "\n");
        }
    }
    
    public void displayDeleteFlight() {
        try {
            System.out.println("\n*** Delete a flight ***");
            
            String flightNumber = getString("Flight Number", null);
            
            if(flightBookingSystemManager.deleteFlight(flightNumber) == 0) {
                System.out.println("\nFlight " + flightNumber + " deleted!\n");
            }
            else if(flightBookingSystemManager.deleteFlight(flightNumber) == 1) {
                System.out.println("\nFlight " + flightNumber + " is associated with a schedule\n");
            }
            else {
                System.out.println("\nFlight " + flightNumber + " does not exist!\n");
            }
        } catch(Exception ex) {
            System.out.println("Caught an unexpected exception because " + ex.getMessage() + "\n");
        }
    }
    
    public void displayAddSchedule() {
        try {
            System.out.println("\n*** Add a schedule ***");
            
            String flightNumber = getString("Flight Number", null);
            // Check if flight exists
            if(!flightBookingSystemManager.verifyFlight(flightNumber)) {
                System.out.println("\nFlight " + flightNumber + " does not exist!\n");
            }
            else {
                System.out.println("\n*** Enter Schedule Details ***");
                String departureTime = getString("Departure Time (DD/MM/YYYY HH:MM)", null);
                // Check if same day schedule exists
                if(flightBookingSystemManager.verifySchedule(flightNumber, departureTime)) {
                    System.out.println("\nFailed to add: existing flight scheduled on the same day!\n");
                }
                else {
                    // If everything is well
                    String arrivalTime = getString("Arrival Time (DD/MM/YYYY HH:MM)", null);
                    int price = Integer.parseInt(getString("Price", null));
                    int availableSeats = Integer.parseInt(getString("Available Seats", null));
                    flightBookingSystemManager.addSchedule(flightNumber, departureTime, arrivalTime, price, availableSeats); 
                    System.out.println("\nSchedule added to flight " + flightNumber + "\n");     
                }
            }
            
        } catch(Exception ex) {
            System.out.println("Caught an unexpected exception because " + ex.getMessage() + "\n");
        }
    }
    
    public void displayUpdateSchedule() {
       try {
            System.out.println("\n*** Update a schedule ***");
            
            String flightNumber = getString("Flight Number", null);
            // Check if flight exists
            if(!flightBookingSystemManager.verifyFlight(flightNumber)) {
                System.out.println("\nFlight " + flightNumber + " does not exist!\n");
                return;
            }
            
            String departureDate = getString("Departure Date (DD/MM/YYYY)", null);
            
            // Check if schedule exists
            if(!flightBookingSystemManager.existSchedule(flightNumber, departureDate))
                System.out.println("\nSchedule on " + flightNumber + " does not exist!\n");
            else {
                // If schedule is associated with a booking
                if(flightBookingSystemManager.bookedSchedule(flightNumber, departureDate)) {
                    int price = Integer.parseInt(getString("Price", null));
                    flightBookingSystemManager.updateSchedulePartial(flightNumber, departureDate, price);
                    System.out.println("\nSchedule updated!\n");
                }
                // If schedule is not associated with a booking
                else {
                    String departureTime = getString("Departure Time (DD/MM/YYYY HH:MM)", null);
                    // If date does not conflict
                    if(!flightBookingSystemManager.verifySchedule(flightNumber, departureTime)) {
                        String arrivalTime = getString("Arrival Time (DD/MM/YYYY HH:MM)", null);
                        int price = Integer.parseInt(getString("Price", null));
                        flightBookingSystemManager.updateSchedule(flightNumber, departureDate, departureTime, arrivalTime, price);  
                        System.out.println("\nSchedule updated!\n");
                    }
                    // If date conflicts
                    else
                        System.out.println("\nFailed to add schedule: a schedule exists on the same day!\n");
                }
            }
       
       } catch(Exception ex) {
            System.out.println("Caught an unexpected exception because " + ex.getMessage() + "\n");
       }
    }
    
    public void displayDeleteSchedule() {
        try {
            System.out.println("\n*** Delete a schedule ***");
            
            String flightNumber = getString("Flight Number", null);
            // Check if flight exists
            if(!flightBookingSystemManager.verifyFlight(flightNumber)) {
                System.out.println("\nFlight " + flightNumber + " does not exist!\n");
                return;
            }
            
            String departureDate = getString("Departure Date (DD/MM/YYYY)", null);
            
            // Check if schedule exists
            if(!flightBookingSystemManager.existSchedule(flightNumber, departureDate))
                System.out.println("\nSchedule on " + flightNumber + " does not exist!\n");
            else {
                if(!flightBookingSystemManager.bookedSchedule(flightNumber, departureDate)) {
                    flightBookingSystemManager.deleteSchedule(flightNumber, departureDate);
                    System.out.println("\nSchedule deleted!\n");
                }
                else {
                    System.out.println("\nSchedule cannot be deleted: booking exists!\n");
                }
            }
        } catch(Exception ex) {
            System.out.println("Caught an unexpected exception because " + ex.getMessage() + "\n");
        }
    }
    
    // Display view bookings
    public void displayBookings() {
        try {
            System.out.println("\n*** List Bookings ***");
            int counter = 1;
            ArrayList<Vector> allBookings = flightBookingSystemManager.getBookings();
            if(allBookings.isEmpty()) {
                System.out.println("No bookings made!");
            }
            for(Object o: allBookings) {
                Vector p = (Vector)o;
                
                // Check if payment has been made in booking
                    String paymentStatus = "";
                    if(p.get(8) == null)
                        paymentStatus = "unpaid";
                    else
                        paymentStatus = "paid";
                    
                // Print booking information
                System.out.println("\n* Booking " + counter + " *");
                System.out.println("Booking Number: " + p.get(0));
                System.out.println("Booking Time: " + p.get(1) + "\n");

                // Print user info
                System.out.println("User name: " + p.get(4));
                System.out.println("Contact No.: " + p.get(5));
                System.out.println("Email: " + p.get(6) + "\n");
                
                // Print all associated flight schedules (loop)
                ArrayList<ArrayList<String>> allFlightSche = (ArrayList<ArrayList<String>>) p.get(3);
                for(int i=0; i<allFlightSche.size(); i++) {
                        System.out.println("Flight " + (i+1) + " Number: " + allFlightSche.get(i).get(0));
                        System.out.println("Flight " + (i+1) + " Departure Time: " + allFlightSche.get(i).get(1) + "\n");
                }
                
                // Print all associated passengers
                ArrayList<ArrayList<String>> allPass = (ArrayList<ArrayList<String>>) p.get(7);
                for(int i=0; i<allPass.size(); i++) {
                        System.out.println("Passenger " + (i+1) + " Name: " + allPass.get(i).get(1));
                        System.out.println("Passenger " + (i+1) + " Passport No.: " + allPass.get(i).get(0));
                        System.out.println("Passenger " + (i+1) + " Gender: " + allPass.get(i).get(2));
                        System.out.println("Passenger " + (i+1) + " Date of Birth: " + allPass.get(i).get(3) + "\n");
                }
                
                // Print payment info
                System.out.println("Total Amount: " + p.get(2));
                System.out.println("Payment Status: " + paymentStatus + "\n");
                
                // Print payment details
                if(p.get(8) != null) {
                    System.out.println("Payment Time: " + p.get(8));
                    System.out.println("Card Type: " + p.get(9));
                    System.out.println("Card Number: " + p.get(10));
                    System.out.println("Card Holder Name: " + p.get(11) + "\n");
                }
                
                counter++;
            }
           
        } catch(Exception ex) {
           System.out.println("Caught an unexpected exception because " + ex.getMessage() + "\n");
        }
    }
    
    // Display view schedules
    public void displaySchedules() {
        try {
            System.out.println("\n*** List Schedules ***");
            int counter = 1;
            for (Object o: flightBookingSystemManager.getSchedules()) {
                Vector im = (Vector)o;
                System.out.println("\n* Flight Schedule " + counter + " *");
                System.out.println("Flight number: " + im.get(0));
                System.out.println("Departure date: " + im.get(1));
                System.out.println("Arrival date: " + im.get(2));
                System.out.println("Available seats: " + im.get(3));
                System.out.println("Price: $" + im.get(4));
                counter++;
            }
                System.out.println("\n");
        } catch(Exception ex) {
           System.out.println("Caught an unexpected exception because " + ex.getMessage() + "\n");
        }
    }
    
    public void displayFlights() {
        try {
            System.out.println("\n*** List Flights ***");
            int counter = 1;
            for (Object o: flightBookingSystemManager.getFlights()) {
                Vector im = (Vector)o;
                System.out.println("\n* Flight " + counter + " *");
                System.out.println("Flight number: " + im.get(0));
                System.out.println("Departure city: " + im.get(1));
                System.out.println("Arrival city: " + im.get(2));
                System.out.println("Total seats: " + im.get(3));
                System.out.println("Aircraft type: " + im.get(4));
                counter++;
            }
                System.out.println("\n");
        } catch(Exception ex) {
           System.out.println("Caught an unexpected exception because " + ex.getMessage() + "\n");
        }        
    }
            
    public void displayProcessRequests() {
        try {
            String choice = "";
            
            while(!choice.equals("0")) {
                
            System.out.println("\n*** List Requests ***");
            int counter = 1;
            if(flightBookingSystemManager.processRequests().isEmpty()) {
                System.out.println("No requests made!");
            }
            for (Object o: flightBookingSystemManager.processRequests()) {
                Vector im = (Vector)o;
                if(!im.get(3).equals("processed")) {
                    System.out.println("\n* Request " + counter + " *");
                    System.out.println("Request Id: " + im.get(0));
                    System.out.println("Time received: " + im.get(1));
                    System.out.println("Content: " + im.get(2));
                    System.out.println("Status: " + im.get(3));
                    System.out.println("Comments: " + im.get(4));
                    counter++;
                }
            }
                System.out.println("\n");
                
                // Update requests menu
                System.out.println("1. Update request status  ");
                System.out.println("2. Add comments");
                System.out.println("* Enter 0 to return to menu *\n");

                // Get choice
                choice = getString("Choice:", null);
                
                // Change status
                if(choice.equals("1")) {
                    String id = getString("Request Id", null);
                    String status = getString("Status", null);
                    flightBookingSystemManager.updateRequestStatus(Long.parseLong(id), status);
                }
                
                // Change comments
                if(choice.equals("2")) {
                    String id = getString("Request Id", null);
                    String comments = getString("Comments", null);
                    flightBookingSystemManager.updateRequestComments(Long.parseLong(id), comments);
                }
                
            } // end of while
                
        } catch(Exception ex) {
           System.out.println("Caught an unexpected exception because " + ex.getMessage() + "\n");
        }   
    }
    
    // Helper function
    public String getString(String attrName, String oldValue){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String stringValue = null;
        
        try {
            while(true) {
                System.out.print("Enter " + attrName + (oldValue==null?"":"(" + oldValue + ")") + " : ");
                stringValue = br.readLine();
                if(stringValue.length() != 0) {
                    break;
                } else if (stringValue.length() == 0 && oldValue != null) {
                    stringValue = oldValue;
                    break;
                }
                System.out.println("\nInvalid " + attrName + "...\n");
            }
        } catch(Exception ex) {
            System.out.println("\nSystem Error Message: " + ex.getMessage() + "\n");
        }
        return stringValue.trim();
    }
 
    // *** CODE TO POPULATE DATA INTO THE DATABASE IF IT IS EMPTY. ***
    public void populateData() {
        try {
            // IF NO DATA EXISTS (no user in particular)
            if(!flightBookingSystemManager.anyUserExists()) {
                System.out.println("Populating data...");
                
            // CREATE 5 USERS FIRST
                flightBookingSystemManager.addUser("Bob", "abc123", "98765432", "bob@gmail.com");
                flightBookingSystemManager.addUser("Tom", "hello", "87654321", "tom@gmail.com");
                flightBookingSystemManager.addUser("Roy", "password", "55556666", "roy@gmail.com");
                flightBookingSystemManager.addUser("Farhan", "secret", "12345678", "farhan@gmail.com");
                flightBookingSystemManager.addUser("Dino", "animal", "00000000", "dino@gmail.com");
                
            // CREATE 10 FLIGHTS
                flightBookingSystemManager.addFlight("SQ1111" , "Singapore" , "Sydney", "Airbus A340-300", 295);
                flightBookingSystemManager.addFlight("SQ2222" , "Singapore" , "Yunnan", "Airbus A340-500", 372);
                flightBookingSystemManager.addFlight("SQ9999" , "Singapore" , "India", "Airbus A380-700", 525);
                
                flightBookingSystemManager.addFlight("CI3333" , "Yunnan" , "Japan", "Airbus A380-800", 853);
                flightBookingSystemManager.addFlight("CI4444" , "Yunnan" , "India", "Boeing 777-200", 440);
                flightBookingSystemManager.addFlight("CI3040" , "Yunnan" , "Canada", "Boeing 747-8", 700);
                
                flightBookingSystemManager.addFlight("QF5555" , "Sydney" , "Russia", "Airbus A380-900", 900);
                flightBookingSystemManager.addFlight("QF6666" , "Sydney" , "Seoul", "Boeing 777-300", 550);
                flightBookingSystemManager.addFlight("QF5060" , "Sydney" , "India", "Boeing 747-400", 624);
                
                flightBookingSystemManager.addFlight("AI7080" , "India" , "Yunnan", "Airbus A340-300", 295);
                flightBookingSystemManager.addFlight("AI7777" , "India" , "Canada", "Boeing 747-400", 624);
                flightBookingSystemManager.addFlight("AI8888" , "India" , "Sydney", "Boeing 747-8", 700);
                
            // CREATE 2 SCHEDULES PER FLIGHT
                flightBookingSystemManager.addSchedule("SQ1111", "01/03/2015 09:00", "01/03/2015 11:00", 1000, 295);
                flightBookingSystemManager.addSchedule("SQ1111", "02/03/2015 09:00", "02/03/2015 11:00", 1000, 295);
                flightBookingSystemManager.addSchedule("SQ2222", "01/03/2015 09:00", "01/03/2015 13:00", 900, 372);
                flightBookingSystemManager.addSchedule("SQ2222", "02/03/2015 09:00", "02/03/2015 13:00", 900, 372);
                flightBookingSystemManager.addSchedule("SQ9999", "01/03/2015 09:00", "01/03/2015 12:00", 700, 525);
                flightBookingSystemManager.addSchedule("SQ9999", "02/03/2015 09:00", "02/03/2015 12:00", 700, 525);
                
                flightBookingSystemManager.addSchedule("CI3333", "01/03/2015 14:00", "01/03/2015 16:00", 1200, 853);
                flightBookingSystemManager.addSchedule("CI3333", "02/03/2015 14:00", "02/03/2015 16:00", 1200, 853);
                flightBookingSystemManager.addSchedule("CI4444", "01/03/2015 14:00", "01/03/2015 17:00", 500, 440);
                flightBookingSystemManager.addSchedule("CI4444", "02/03/2015 14:00", "02/03/2015 17:00", 500, 440);
                flightBookingSystemManager.addSchedule("CI3040", "01/03/2015 14:00", "01/03/2015 18:00", 850, 700);
                flightBookingSystemManager.addSchedule("CI3040", "02/03/2015 14:00", "02/03/2015 18:00", 850, 700);
                
                flightBookingSystemManager.addSchedule("QF5555", "01/03/2015 13:00", "01/03/2015 16:00", 2100, 900);
                flightBookingSystemManager.addSchedule("QF5555", "02/03/2015 13:00", "02/03/2015 16:00", 2100, 900);
                flightBookingSystemManager.addSchedule("QF6666", "01/03/2015 13:00", "01/03/2015 17:00", 1800, 550);
                flightBookingSystemManager.addSchedule("QF6666", "02/03/2015 13:00", "02/03/2015 17:00", 1800, 550);
                flightBookingSystemManager.addSchedule("QF5060", "01/03/2015 13:00", "01/03/2015 18:00", 1500, 624);
                flightBookingSystemManager.addSchedule("QF5060", "02/03/2015 13:00", "02/03/2015 18:00", 1500, 624);

                flightBookingSystemManager.addSchedule("AI7080", "01/03/2015 15:00", "01/03/2015 20:00", 1450, 295);
                flightBookingSystemManager.addSchedule("AI7080", "02/03/2015 15:00", "02/03/2015 20:00", 1450, 295);
                flightBookingSystemManager.addSchedule("AI7777", "01/03/2015 15:00", "01/03/2015 21:00", 1200, 624);
                flightBookingSystemManager.addSchedule("AI7777", "02/03/2015 15:00", "02/03/2015 21:00", 1200, 624);
                flightBookingSystemManager.addSchedule("AI8888", "01/03/2015 15:00", "01/03/2015 23:00", 1350, 700);
                flightBookingSystemManager.addSchedule("AI8888", "02/03/2015 15:00", "02/03/2015 23:00", 1350, 700);
                
            } // end of if
            
        } catch(Exception ex) {
            System.err.println("Caught an unexpected exception!");
            ex.printStackTrace();
        }
    }
    
}
