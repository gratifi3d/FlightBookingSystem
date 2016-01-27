/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Vector;
import javax.ejb.Remote;

/**
 *
 * @author Farhan
 */
@Remote
public interface FlightBookingSystemManagerRemote {

    // UserEntity methods
    public void addUser(String userName, String password, String contactNumber, String emailAddress); // Add user
    public boolean verifyUser(String userName); // Check if user exists
    public int deleteUser(String userName); // Delete user
    public void updateUserProfile(String userName, String email, String contactNumber);
    public void updateUserPassword(String userName, String password);
    
    // FlightEntity methods
    public void addFlight(String flightNumber, String departureCity, String arrivalCity, String aircraftType, int totalSeats);
    public boolean verifyFlight(String flightNumber); // Check if flight exists
    public int updateFlight(String flightNumber);
    public int updateFlightPartial(String flightNumber, String aircraftType, int totalSeats);
    public int deleteFlight(String flightNumber);

    // ScheduleEntity methods
    public void addSchedule(String flightNumber, String departureTime, String arrivalTime, int price, int availableSeats);
    public boolean verifySchedule(String flightNumber, String departureTime);
    public void updateSchedule(String flightNumber, String departureDate, String departureTime, String arrivalTime, int price);
    public void updateSchedulePartial(String flightNumber, String departureDate, int price);
    public boolean existSchedule(String flightNumber, String departureDate);
    public boolean bookedSchedule(String flightNumber, String departureDate);
    public void deleteSchedule(String flightNumber, String departureDate);

    // List booking methods
    public ArrayList<Vector> getBookings();
        
    // FlightBookingSystemManager methods
    public void remove(); 

    // Website methods
    public boolean verifyLogin(String userName, String password);

    public ArrayList<String> getUserInfo(String userName);

    public String encryptData(String data) throws NoSuchAlgorithmException;

    public ArrayList<Vector> getSchedules();

    public ArrayList<Vector> getFlights();

    public ArrayList<Vector> processRequests();

    public void updateRequestStatus(Long id, String status);

    public void updateRequestComments(Long id, String comments);

    public ArrayList<Vector> listMatchedFlights(String departureDate, String departureCity, String arrivalCity, String numberPassengers);

    public ArrayList<Vector> listOneStops(String departureDate, String departureCity, String arrivalCity, String numberPassengers);

    public void addPassenger(String passportNumber, String name, String gender, String dateOfBirth);

    public ArrayList<Vector> getPassengers();

    public void addBooking(ArrayList<ArrayList<String>> passengerList, String userName, String bookedFlightSchedule);

    public ArrayList<ArrayList<String>> bookingsMade(String userName);

    public void makePayment(Long bookingNum, String cardHolderName, String cardType, String cardNumber);

    public ArrayList<Vector> showAllWebBookings(String userName);

    public ArrayList<ArrayList<String>> getRequests(String userName);

    public boolean anyUserExists();














}
