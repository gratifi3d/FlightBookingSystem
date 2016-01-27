/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Farhan
 */
@Stateless
public class FlightBookingSystemManager implements FlightBookingSystemManagerRemote {
    @PersistenceContext
    EntityManager em;
    
    private UserEntity userEntity;
    private BookingEntity bookingEntity;
    private PaymentEntity paymentEntity;
    private PassengerEntity passengerEntity;
    private ScheduleEntity scheduleEntity;
    private FlightEntity flightEntity;
    private RequestEntity requestEntity;
    
    private Collection<BookingEntity> bookings;
    private Collection<RequestEntity> requests;
    private Collection<ScheduleEntity> schedules;

    public FlightBookingSystemManager() {
    }

    //*** UserEntity methods ***
    
    // Add user method
    @Override
    public void addUser(String userName, String password, String contactNumber, String emailAddress) {
        System.out.println("FlightBookingSystemManager: addUser()");
        userEntity = new UserEntity();
        try {
            userEntity.create(userName, password, contactNumber, emailAddress);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(FlightBookingSystemManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        bookingEntity = new BookingEntity();
        requestEntity = new RequestEntity();
        bookings = new ArrayList<BookingEntity>();
        requests = new ArrayList<RequestEntity>();
        // Create booking and request tables
        bookingEntity.setUserBooking(userEntity);
        requestEntity.setUserRequest(userEntity);
        userEntity.setBookings(bookings);
        userEntity.setRequests(requests);
        em.persist(userEntity);
    }
    
    
    // addUser helper method to verify if user exists
    @Override
    public boolean verifyUser(String userName) {
        System.out.println("FlightBookingSystemManager: verifyUser()");
        userEntity = em.find(UserEntity.class, userName);
        return userEntity != null; // Returns true if user exists
    }
    
    @Override
    public boolean anyUserExists() {
        System.out.println("FlightBookingSystemManager: anyUserExists");
        Query q = em.createQuery("SELECT p FROM UserData p");
        int counter = 0;
        for(Object o: q.getResultList()) {
            counter++;
        }
        // TEST
        System.out.println("TESTING POPULATE NO.: " + counter);
        if(counter == 0)
            return false; // no user exists
        else
            return true; // user exists
    }
    
    // Update user profile method
    @Override
    public void updateUserProfile(String userName, String email, String contactNumber) {
        System.out.println("FlightBookingSystemManager: updateUserProfile()");
        if(verifyUser(userName)) {
        userEntity.setEmailAddress(email);
        userEntity.setContactNumber(contactNumber);
        }
    }
    
    // Update user password method
    @Override
    public void updateUserPassword(String userName, String password) {
        System.out.println("FlightBookingSystemManager: updateUserPassword()");
        if(verifyUser(userName)) {
            try {
                userEntity.setPassword(password);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(FlightBookingSystemManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
        
    // Delete user method
    @Override
    public int deleteUser(String userName) {
        System.out.println("FlightBookingSystemManager: deleteUser()");
       // Check if user exists
       userEntity = em.find(UserEntity.class, userName);
       // If user is not associated with any bookings or requests
       if(userEntity != null && userEntity.getBookings().isEmpty() && userEntity.getRequests().isEmpty()) {
           em.remove(userEntity);
           System.out.println("\nUser " + userName + " deleted!\n");
           return 0;
       }
       // If user is associated with at least a booking and a request
       else if (userEntity != null && userEntity.getBookings().size() > 0 && userEntity.getRequests().size() > 0) {
           System.out.println("\nUser " + userName + " is associated with a booking and a request!\n");
           return 1;
       }
       // If user is associated with at least a booking 
       else if (userEntity != null && userEntity.getBookings().size() > 0) {
           System.out.println("\nUser " + userName + " is associated with a booking!\n");
           return 2;
       }
       // If user is associated with at least a request
       else if (userEntity != null && userEntity.getRequests().size() > 0) {
           System.out.println("\nUser " + userName + " is associated with a request!\n");
           return 3;
       }
       // If user does not exist at all
       else {
           System.out.println("\nUser " + userName + " does not exist!\n");   
           return 4;
       }
    }
    
    // *** FlightEntity methods ***
    
    @Override
    public void addFlight(String flightNumber, String departureCity, String arrivalCity, String aircraftType, int totalSeats) {
        System.out.println("FlightBookingSystemManager: addFlight()");
        flightEntity = new FlightEntity();
        scheduleEntity = new ScheduleEntity();
        flightEntity.create(flightNumber, departureCity, arrivalCity, aircraftType, totalSeats);
        schedules = new ArrayList<ScheduleEntity>();
        scheduleEntity.setFlightSchedule(flightEntity);
        flightEntity.setSchedules(schedules);
        em.persist(flightEntity);
    }
    
    // Helper method for addFlight
    @Override
    public boolean verifyFlight(String flightNumber) {
        System.out.println("FlightBookingSystemManager: verifyFlight()");
        flightEntity = em.find(FlightEntity.class, flightNumber);
        return flightEntity != null; // Returns true if flight exists
    }
    
    @Override
    public int updateFlight(String flightNumber) {
        System.out.println("FlightBookingSystemManager: updateFlight()");
        // Check if flight exists
        flightEntity = em.find(FlightEntity.class, flightNumber);
        // If flight exists
        if(flightEntity != null) {
            if(flightEntity.getSchedules().size() > 0)
                return 0;
            else
                return 1;
        }
        else {
            System.out.println("\nFlight " + flightNumber + " does not exist!\n");
            return 2;
        }
    }
    
    // Helper method for updateFlight
    @Override
    public int updateFlightPartial(String flightNumber, String aircraftType, int totalSeats) {
        System.out.println("FlightBookingSystemManager: updateFlightPartial()");
        flightEntity = em.find(FlightEntity.class, flightNumber);
        // Check if total no. of updated seats < total no. of booked seats
        ArrayList<ScheduleEntity> sch = new ArrayList<ScheduleEntity>(flightEntity.getSchedules());
        int seatsAdded = totalSeats - flightEntity.getTotalSeats();
        int availableSeats;
        int remainingSeats;
        // Iterate through all schedules and get available number of seats from each schedule
        for(int i=0; i<sch.size(); i++) {
            scheduleEntity = new ScheduleEntity();
            scheduleEntity = sch.get(i);
            availableSeats = scheduleEntity.getAvailableSeats();
            // Difference in number of seat changes (can be +ve or -ve);
            remainingSeats = seatsAdded + availableSeats;
            System.out.println(remainingSeats);
            // If new total no. of seats < total no. of booked seats, return an error message
            if(remainingSeats < 0)
                return 0;
            else {
                // Update number of available seats in schedule
                sch.get(i).setAvailableSeats(remainingSeats);
            }
       }
        // If all is well, update flight information
        flightEntity.setAircraftType(aircraftType);
        flightEntity.setTotalSeats(totalSeats);
        return 1;
    }
    
    @Override
    public int deleteFlight(String flightNumber) {
        System.out.println("FlightBookingSystemManager: deleteFlight()");
        flightEntity = em.find(FlightEntity.class, flightNumber);
        if(flightEntity != null && flightEntity.getSchedules().isEmpty()) {
            em.remove(flightEntity);
            System.out.println("\nFlight " + flightNumber + " deleted!\n");
            return 0;
        }
        else if(flightEntity != null && flightEntity.getSchedules().size() > 0) {
            System.out.println("\nFlight " + flightNumber + " is associated with a schedule\n");
            return 1;
        }
        else {
            System.out.println("\nFlight " + flightNumber + " does not exist!\n");
            return 2;
        }
    }
    
    // *** Schedule methods ***
    
    // Add schedule
    @Override
    public void addSchedule(String flightNumber, String departureTime, String arrivalTime, int price, int availableSeats) {
        System.out.println("FlightBookingSystemManager: addSchedule()");
        flightEntity = em.find(FlightEntity.class, flightNumber);
        ArrayList<ScheduleEntity> sch = new ArrayList<ScheduleEntity>(flightEntity.getSchedules());
        scheduleEntity = new ScheduleEntity();
        scheduleEntity.create(departureTime, arrivalTime, price, availableSeats, false);
        scheduleEntity.setFlightSchedule(flightEntity);
        sch.add(scheduleEntity);
        flightEntity.setSchedules(sch);
        System.out.println("\nSchedule added to flight " + flightNumber + "\n");
    }
    
    // addSchedule helper method to verify date conflict
    @Override
    public boolean verifySchedule(String flightNumber, String departureTime) {
        System.out.println("FlightBookingSystemManager: verifySchedule()");
        flightEntity = em.find(FlightEntity.class, flightNumber);
        // Check association with specific flight number
        ArrayList<ScheduleEntity> sch = new ArrayList<ScheduleEntity>(flightEntity.getSchedules());
        // Check if date to be added conflicts with current schedules
        for(int i=0; i<sch.size(); i++) {
            // Get only date from schedule
            String dept1 = departureTime.substring(0, 10); // From input
            String dept2 = sch.get(i).getDepartureTime().substring(0, 10); // From schedule
            if(dept1.equals(dept2)) {
                System.out.println("\nFailed to add schedule: a schedule exists on the same day!\n");
                return true;
            }
        }
        return false;
    }
    
    // Update schedule
    @Override
    public void updateSchedule(String flightNumber, String departureDate, String departureTime, String arrivalTime, int price) {
        System.out.println("FlightBookingSystemManager: updateSchedule()");
        flightEntity = em.find(FlightEntity.class, flightNumber);
        // Check association with specific flight number
        ArrayList<ScheduleEntity> sch = new ArrayList<ScheduleEntity>(flightEntity.getSchedules());
        // Check if date to be added conflicts with current schedules
        for(int i=0; i<sch.size(); i++) {
            // Get only date from schedule
            String dept = sch.get(i).getDepartureTime().substring(0, 10); // From schedule
            if(dept.equals(departureDate)) {
                scheduleEntity = new ScheduleEntity();
                scheduleEntity = sch.get(i);
                scheduleEntity.setDepartureTime(departureTime);
                scheduleEntity.setArrivalTime(arrivalTime);
                scheduleEntity.setPrice(price);
                System.out.println("\nSchedule successfully updated!\n");
            }
        }     
    }
    
    // Update price only in schedule when booking exists
    @Override
    public void updateSchedulePartial(String flightNumber, String departureDate, int price) {
        System.out.println("FlightBookingSystemManager: updateSchedulePartial()");
        flightEntity = em.find(FlightEntity.class, flightNumber);
        // Check association with specific flight number
        ArrayList<ScheduleEntity> sch = new ArrayList<ScheduleEntity>(flightEntity.getSchedules());
        // Check if date to be added conflicts with current schedules
        for(int i=0; i<sch.size(); i++) {
            // Get only date from schedule
            String dept = sch.get(i).getDepartureTime().substring(0, 10); // From schedule
            if(departureDate.equals(dept)) {
                scheduleEntity = new ScheduleEntity();
                scheduleEntity = sch.get(i);
                scheduleEntity.setPrice(price);
                System.out.println("\nSchedule successfully updated!\n");
            }
        } 
    }
            
    // updateSchedule helper to check if booking exists
    @Override
    public boolean existSchedule(String flightNumber, String departureDate) {
        System.out.println("FlightBookingSystemManager: existSchedule()");
        flightEntity = em.find(FlightEntity.class, flightNumber);
        // Check association with specific flight number
        ArrayList<ScheduleEntity> sch = new ArrayList<ScheduleEntity>(flightEntity.getSchedules());
        // Check if date to be added conflicts with current schedules
        for(int i=0; i<sch.size(); i++) {
            // Get only date from schedule
            String dept = sch.get(i).getDepartureTime().substring(0, 10); // Get date only from schedule
            System.out.println(dept.length());
            if(dept.equals(departureDate)) {
                System.out.println("\nSchedule found!\n");
                return true; // Schedule exists
            }
        }
        System.out.println("\nSchedule not found!\n");
        return false; // Schedule does not exist
    }
    
    // Check if schedule is booked or not
    @Override
    public boolean bookedSchedule(String flightNumber, String departureDate) {
        System.out.println("FlightBookingSystemManager: bookedSchedule()");
        flightEntity = em.find(FlightEntity.class, flightNumber);
        scheduleEntity = new ScheduleEntity();
        // Check association with specific flight number
        ArrayList<ScheduleEntity> sch = new ArrayList<ScheduleEntity>(flightEntity.getSchedules());
        // Check if date to be added conflicts with current schedules
        for(int i=0; i<sch.size(); i++) {
            // Get only date from schedule
            String dept = sch.get(i).getDepartureTime().substring(0, 10); // From schedule
            if(departureDate.equals(dept)) {
                scheduleEntity = sch.get(i);
                if(scheduleEntity.isHasBooking()) {
                    System.out.println("\nBooking exists!\n");
                    return true; // Booking exists
                }
                else {
                    System.out.println("\nBooking does not exist!\n");
                    return false; // Booking does not exist!
                }
            }
        }
        System.out.println("\nBooking does not exist!\n");
        return false; // Booking does not exist
    }
    
    // Delete schedule
    @Override
    public void deleteSchedule(String flightNumber, String departureDate) {
        System.out.println("FlightBookingSystemManager: deleteSchedule()");
        flightEntity = em.find(FlightEntity.class, flightNumber);
        // Check association with specific flight number
        ArrayList<ScheduleEntity> sch = new ArrayList<ScheduleEntity>(flightEntity.getSchedules());
        // Check if date to be added conflicts with current schedules
        for(int i=0; i<sch.size(); i++) {
            // Get only date from schedule
            String dept = sch.get(i).getDepartureTime().substring(0, 10); // From schedule
            if(departureDate.equals(dept)) {
                scheduleEntity = new ScheduleEntity();
                scheduleEntity = sch.get(i);
                em.remove(scheduleEntity);
            }
        }
    }
    
    // *** Passenger methods ***
    
    @Override
    public void addPassenger(String passportNumber, String name, String gender, String dateOfBirth) {
        System.out.println("FlightBookingSystemManager: addPassenger()");
        passengerEntity = new PassengerEntity();
        passengerEntity.create(passportNumber, name, gender, dateOfBirth);
        em.persist(passengerEntity);
    }
    
    // *** Booking methods ***
    
    // Add booking but not yet linked with other entities
    @Override
    public void addBooking(ArrayList<ArrayList<String>> passengerList, String userName, String bookedFlightSchedule) {
        System.out.println("FlightBookingSystemManager: addBooking()");
        // Initialise
        bookingEntity = new BookingEntity();
        userEntity = new UserEntity();
        paymentEntity = new PaymentEntity();
        
        // Create booking entity first
        int totalAmount = 0;
        bookingEntity.create(totalAmount);
                
        // Link payment but it is empty for now.
        bookingEntity.setBookingPayment(paymentEntity);
        
        // Link user to booking entity
        userEntity = em.find(UserEntity.class, userName);
        bookingEntity.setUserBooking(userEntity);

        // Link passengers
        // Convert arraylist to an arraylist of passender entities
        ArrayList<PassengerEntity> passList = new ArrayList<PassengerEntity>();
        for(int i=0; i<passengerList.size(); i++) {
            passengerEntity = new PassengerEntity();
            ArrayList<String> temp = passengerList.get(i);
            String name = temp.get(0);
            String passportNum = temp.get(1);
            String gender = temp.get(2);
            String dateOfBirth = temp.get(3);
            passengerEntity.create(passportNum, name, gender, dateOfBirth);
            passList.add(passengerEntity);
        }
        bookingEntity.setPassengers(passList); // DONE
        // Link schedules
        // The the schedule iD or iDs
        String scheduleId = bookedFlightSchedule.trim();
        // If it is a one-way route
        if(scheduleId.contains(" ")) {
            String[] ids = scheduleId.split("\\s+");
            // Create the scheduleEntity arraylist for one-way flight
            ArrayList<ScheduleEntity> sch1 = new ArrayList<ScheduleEntity>();
            for(int i=0; i<ids.length; i++) {
                scheduleEntity = new ScheduleEntity();
                scheduleEntity = em.find(ScheduleEntity.class, Long.parseLong(ids[i]));
                // Minus off the avail seats
                scheduleEntity.setAvailableSeats(scheduleEntity.getAvailableSeats()-passengerList.size());
                totalAmount += scheduleEntity.getPrice();
                sch1.add(scheduleEntity);
            }
            bookingEntity.setSchedules(sch1);
        }
        // If it is a direct route
        else {
            // Create the scheduleEntity arraylist for direct flight
            scheduleEntity = new ScheduleEntity();
            scheduleEntity = em.find(ScheduleEntity.class, Long.parseLong(scheduleId));
            // Minus off the avail seats
            scheduleEntity.setAvailableSeats(scheduleEntity.getAvailableSeats()-passengerList.size());           
            ArrayList<ScheduleEntity> sch2 = new ArrayList<ScheduleEntity>();
            totalAmount += scheduleEntity.getPrice();
            sch2.add(scheduleEntity);
            bookingEntity.setSchedules(sch2);
        } // DONE  
        bookingEntity.setTotalAmount(totalAmount * passengerList.size());
        em.persist(bookingEntity); // Should this be at the end?
        em.merge(userEntity);
        em.refresh(userEntity);
        em.flush();
    }

    
    // *** View methods ***
    
    @Override
    public ArrayList<Vector> getBookings() {
        System.out.println("FlightBookingSystemManager: getBookings()");
        Query q = em.createQuery("SELECT p FROM BookingData p");
        ArrayList<Vector> entityList = new ArrayList();
        for(Object o: q.getResultList()) {
            BookingEntity p = (BookingEntity)o;
            
            // Add booking information
            Vector bookingInfo = new Vector();
            bookingInfo.add(p.getId()); // 0
            bookingInfo.add(p.getBookingTime()); // 1
            bookingInfo.add(p.getTotalAmount()); // 2
            
            // Add flight schedule ArrayList **
            Collection<ScheduleEntity> sche = p.getSchedules();
            ArrayList<ArrayList<String>> allSche = new ArrayList();
            for(Object b: sche) {
                ScheduleEntity x = (ScheduleEntity)b;
                ArrayList<String> passStr = new ArrayList();
                passStr.add(x.getFlightSchedule().getFlightNumber()); // 0
                passStr.add(x.getDepartureTime()); // 1
                allSche.add(passStr);
            }
            bookingInfo.add(allSche); // 3
            
            // Add user information
            bookingInfo.add(p.getUserBooking().getUserName()); // 4
            bookingInfo.add(p.getUserBooking().getContactNumber()); // 5
            bookingInfo.add(p.getUserBooking().getEmailAddress()); // 6
            
            // Add passengers ArrayList **
            Collection<PassengerEntity> pass = p.getPassengers();
            ArrayList<ArrayList<String>> allPass = new ArrayList();
            for(Object a: pass) {
                PassengerEntity x = (PassengerEntity)a;
                ArrayList<String> passStr = new ArrayList();
                passStr.add(x.getPassportNumber()); // 0 
                passStr.add(x.getName()); // 1
                passStr.add(x.getGender()); // 2
                passStr.add(x.getDateOfBirth()); // 3
                allPass.add(passStr);
            }     
            bookingInfo.add(allPass); // 7
            
            // Add payment information
            // Check if payment exists
            if(p.getBookingPayment() != null) {
                bookingInfo.add(p.getBookingPayment().getPaymentTime()); // 8
                bookingInfo.add(p.getBookingPayment().getCardType()); // 9
                bookingInfo.add(p.getBookingPayment().getCardNumber()); // 10
                bookingInfo.add(p.getBookingPayment().getCardHolderName()); // 11
            }
                entityList.add(bookingInfo);
        }
        return entityList;
    }
    
    // Get bookings made
    @Override
    public ArrayList<ArrayList<String>> bookingsMade(String userName) {
        System.out.println("FlightBookingSystemManager: bookingsMade()");
        userEntity = em.find(UserEntity.class, userName);
        ArrayList<ArrayList<String>> userBookings = new ArrayList<ArrayList<String>>();
        Collection<BookingEntity> bookingsMade = userEntity.getBookings();
        
        for(Object o: bookingsMade) {
            ArrayList<String> temp = new ArrayList<String>();
            BookingEntity b = (BookingEntity) o;
            String bookingNum = b.getId().toString();
            String bookingTime = b.getBookingTime();
            
            temp.add(bookingNum); // 0 BOOKING NUMBER
            temp.add(bookingTime); // 1 BOOKING TIME
            
            // Check if one way or direct flight
            ArrayList<ScheduleEntity> sch = new ArrayList<ScheduleEntity>(b.getSchedules());
            if(sch.size() == 2) {
                String dept = sch.get(0).getFlightSchedule().getDepartureCity();
                String transfer = sch.get(0).getFlightSchedule().getArrivalCity();
                String arr = sch.get(1).getFlightSchedule().getArrivalCity();
                String route = dept + " -> " + transfer + " -> " + arr;
                temp.add(route); // 2 ONE WAY ROUTE
            }
            else {
                String dept = sch.get(0).getFlightSchedule().getDepartureCity();
                String arr = sch.get(0).getFlightSchedule().getArrivalCity();
                String route = dept + " -> " + arr;  
                temp.add(route); // 2 DIRECT ROUTE
            }
            
            String numPassengers = Integer.toString(b.getPassengers().size());
            temp.add(numPassengers); // 3 NUMBER PASSENGERS
            
            String totalAmount = Integer.toString(b.getTotalAmount());
            temp.add(totalAmount); // 4 TOTAL AMOUNT

            // if empty, add not paid, if not empty, add paid
            if(b.getBookingPayment().getCardHolderName() == null)
                temp.add("unpaid"); // 5 UNPAID
            else
                temp.add("paid"); // 5 PAID

            userBookings.add(temp);
        }
        return userBookings;
    }
  
    // showAllBookings
    @Override
    public ArrayList<Vector> showAllWebBookings(String userName) {
        System.out.println("FlightBookingSystemManager: showAllWebBookings()");
        userEntity = em.find(UserEntity.class, userName);
        ArrayList<Vector> entityList = new ArrayList();
        Collection<BookingEntity> books = userEntity.getBookings();
        for(Object o: books) {
            BookingEntity p = (BookingEntity)o;
            
            // Add booking information
            Vector bookingInfo = new Vector();
            bookingInfo.add(p.getId()); // 0
            bookingInfo.add(p.getBookingTime()); // 1
            bookingInfo.add(p.getTotalAmount()); // 2
            if(p.getBookingPayment().getCardHolderName() != null)
                bookingInfo.add("paid"); // 3
            else
                bookingInfo.add("unpaid"); // 3
            
            // Add flight schedule ArrayList **
            Collection<ScheduleEntity> sche = p.getSchedules();
            ArrayList<ArrayList<String>> allSche = new ArrayList();
            for(Object b: sche) {
                ScheduleEntity x = (ScheduleEntity)b;
                ArrayList<String> passStr = new ArrayList();
                passStr.add(x.getFlightSchedule().getFlightNumber()); // 0
                passStr.add(x.getFlightSchedule().getDepartureCity()); // 1
                passStr.add(x.getFlightSchedule().getArrivalCity()); // 2
                passStr.add(x.getDepartureTime()); // 3
                passStr.add(x.getArrivalTime()); // 4
                passStr.add(x.getFlightSchedule().getAircraftType()); // 5
                allSche.add(passStr);
            }
            bookingInfo.add(allSche); // 4 ArrArrStr
            
            // Add passengers ArrayList **
            Collection<PassengerEntity> pass = p.getPassengers();
            ArrayList<ArrayList<String>> allPass = new ArrayList();
            for(Object a: pass) {
                PassengerEntity x = (PassengerEntity)a;
                ArrayList<String> passStr = new ArrayList();
                passStr.add(x.getPassportNumber()); // 0 
                passStr.add(x.getName()); // 1
                passStr.add(x.getGender()); // 2
                passStr.add(x.getDateOfBirth()); // 3
                allPass.add(passStr);
            }     
            bookingInfo.add(allPass); // 5 ArrArrStr

                entityList.add(bookingInfo);
        }
        return entityList;
    }
    
    
    @Override
    public ArrayList<Vector> getPassengers() {
        System.out.println("FlightBookingSystemManager: getPassengers()");
        Query q = em.createQuery("SELECT m FROM PassengerData m");
        ArrayList<Vector> pass = new ArrayList();
        for (Object o: q.getResultList()) {
            PassengerEntity m = (PassengerEntity) o;
            
            Vector im = new Vector();
            im.add(m.getPassportNumber()); // 0
            im.add(m.getName()); // 1
            im.add(m.getGender()); // 2
            im.add(m.getDateOfBirth()); // 3
            
            pass.add(im);
        }
        return pass;    
    }
    
    
    @Override
    public ArrayList<Vector> getSchedules() {
        System.out.println("FlightBookingSystemManager: getSchedules()");
        Query q = em.createQuery("SELECT m FROM ScheduleData m");
        ArrayList<Vector> sch = new ArrayList();
        for (Object o: q.getResultList()) {
            ScheduleEntity m = (ScheduleEntity) o;
            
            Vector im = new Vector();
            im.add(m.getFlightSchedule().getFlightNumber()); // 0
            im.add(m.getDepartureTime()); // 1
            im.add(m.getArrivalTime()); // 2
            im.add(m.getAvailableSeats()); // 3
            im.add(m.getPrice()); // 4
            
            sch.add(im);
        }
        return sch;
    }
    
    @Override
    public ArrayList<Vector> getFlights() {
        System.out.println("FlightBookingSystemManager: getFlights()");
        Query q = em.createQuery("SELECT m FROM FlightData m");
        ArrayList<Vector> flights = new ArrayList();
        for (Object o: q.getResultList()) {
            FlightEntity m = (FlightEntity) o;
            
            Vector im = new Vector();
            im.add(m.getFlightNumber()); // 0
            im.add(m.getDepartureCity()); // 1
            im.add(m.getArrivalCity()); // 2
            im.add(m.getTotalSeats()); // 3
            im.add(m.getAircraftType()); // 4
            
            flights.add(im);
        }
        return flights;    
    }
    
    // Request methods
    @Override
    public ArrayList<Vector> processRequests() {
        System.out.println("FlightBookingSystemManager: processRequests()");
        Query q = em.createQuery("SELECT m FROM RequestData m");
        ArrayList<Vector> req = new ArrayList();
        for (Object o: q.getResultList()) {
            RequestEntity m = (RequestEntity) o;
            
            Vector im = new Vector();
            im.add(m.getId()); // 0
            im.add(m.getTime()); // 1
            im.add(m.getContent()); // 2
            im.add(m.getStatus()); // 3
            im.add(m.getComment()); // 4
            
            req.add(im);
        }
        return req; 
    }
    
    @Override
    public void updateRequestStatus(Long id, String status) {
        System.out.println("FlightBookingSystemManager: updateRequestStatus()");
        requestEntity = em.find(RequestEntity.class, id);
        requestEntity.setStatus(status);
    }
    
    @Override
    public void updateRequestComments(Long id, String comments) {
        System.out.println("FlightBookingSystemManager: updateRequestComments()");
        requestEntity = em.find(RequestEntity.class, id);
        requestEntity.setComment(comments);
    }
    
    // *** Website methods ***
    
    // Check if login user and password are correct
    @Override
    public boolean verifyLogin(String userName, String password) {
        System.out.println("FlightBookingSystemManager: verifyLogin()");
            // Check if user exists
            userEntity = em.find(UserEntity.class, userName);
            if(userEntity != null) {
                try {
                    // Check if password matches
                    if(userEntity.getPassword().equals(encryptData(password))) {
                        return true;
                    }
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(FlightBookingSystemManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        return false;
    }
    
    @Override
    public ArrayList<String> getUserInfo(String userName) {
        System.out.println("FlightBookingSystemManager: getUserInfo()");
        userEntity = em.find(UserEntity.class, userName);
        if(userEntity != null) {
            ArrayList<String> userInfo = new ArrayList<String>();
            userInfo.add(userEntity.getUserName()); // 0
            userInfo.add(userEntity.getPassword()); // 1
            userInfo.add(userEntity.getContactNumber()); // 2
            userInfo.add(userEntity.getEmailAddress()); // 3
            return userInfo;
        }
        return null;
    }
    
    // List of matched flights from web query
    @Override
    public ArrayList<Vector> listMatchedFlights(String departureDate, String departureCity, String arrivalCity, String numberPassengers) {
        System.out.println("FlightBookingSystemManager: listMatchedFlights()");
        Query q = em.createQuery("SELECT m FROM ScheduleData m");
        ArrayList<Vector> req = new ArrayList();
        // Scan through all data
        for (Object o: q.getResultList()) {
            ScheduleEntity m = (ScheduleEntity) o;
            Vector im = new Vector();
            
            // If departure date input is equal
            String tempDepartureTime = m.getDepartureTime().substring(0, 10);
            String tempDepartureCity = m.getFlightSchedule().getDepartureCity();
            String tempArrivalCity = m.getFlightSchedule().getArrivalCity();
            int tempAvailableSeats = m.getAvailableSeats();
            
            // Check if valid
            if(departureDate.equals(tempDepartureTime)) {
                if(departureCity.equals(tempDepartureCity)) {
                    if(arrivalCity.equals(tempArrivalCity)) {
                        if(tempAvailableSeats >= Integer.parseInt(numberPassengers)) {
                            String specialDept = departureCity + " " + m.getDepartureTime();
                            String specialArr = arrivalCity + " " + m.getArrivalTime(); 
                            String specialPrice = "SGD " + m.getPrice() + " / passenger";
                            im.add(m.getFlightSchedule().getFlightNumber()); // 0
                            im.add(specialDept); // 1
                            im.add(specialArr); // 2
                            im.add(specialPrice); // 3
                            im.add(m.getPrice()); // 4
                            im.add(numberPassengers); // 5
                            im.add(m.getId()); // 6
                            req.add(im);
                        }
                    }
                }
            }
        }
        return req; // Return the ArrayList<Vector>  
    }
    
    // List of one stop flights from web query
    public ArrayList<Vector> listOneStops(String departureDate, String departureCity, String arrivalCity, String numberPassengers) {
        System.out.println("FlightBookingSystemManager: listOneStops()");
        Query q = em.createQuery("SELECT m FROM ScheduleData m");
        
        ArrayList<Vector> linkedDepartures = new ArrayList();
        ArrayList<Vector> linkedArrivals = new ArrayList();
        ArrayList<Vector> oneStopFlights = new ArrayList();
        ArrayList<Vector> linkedDeparturesFull = new ArrayList();
        ArrayList<Vector> linkedArrivalsFull = new ArrayList();
        
        // Scan through all data in scheduledata
        for (Object o: q.getResultList()) {
            ScheduleEntity m = (ScheduleEntity) o;
            Vector im = new Vector();
            
            // If departure date input is equal
            String tempDepartureTime = m.getDepartureTime().substring(0, 10);
            String tempDepartureCity = m.getFlightSchedule().getDepartureCity();
            String tempArrivalCity = m.getFlightSchedule().getArrivalCity();
            int tempAvailableSeats = m.getAvailableSeats();
                           
            // Add flight to ArrayList linkedDepartures if arrival city DONT MATCH, date match, departure city match, seats avail.
            if(departureDate.equals(tempDepartureTime)) {
                if(departureCity.equals(tempDepartureCity)) {
                    if(!arrivalCity.equals(tempArrivalCity)) {
                        if(tempAvailableSeats >= Integer.parseInt(numberPassengers)) {
                            String specialDept = tempDepartureCity + " " + m.getDepartureTime();
                            String specialArr = tempArrivalCity + " " + m.getArrivalTime(); 
                            String specialPrice = "SGD " + m.getPrice() + " / passenger";
                            im.add(m.getFlightSchedule().getFlightNumber()); // 0
                            im.add(tempDepartureCity); // 1
                            im.add(m.getDepartureTime()); // 2
                            im.add(tempArrivalCity); // 3
                            im.add(m.getArrivalTime()); // 4
                            im.add(m.getPrice()); // 5
                            im.add(numberPassengers); // 6
                            im.add(m.getId()); // 7
                        im.add(specialDept); // 8
                        im.add(specialArr); // 9
                        im.add(specialPrice); // 10
                            
                            linkedDepartures.add(im);
                        }
                    }
                }
            }
            
            // Add flight to ArrayList linkedArrivals if dept city DONT MATCH, arrival city match, seats avail
            if(!departureCity.equals(tempDepartureCity)) {
                if(arrivalCity.equals(tempArrivalCity)) {
                    if(tempAvailableSeats >= Integer.parseInt(numberPassengers)) {
                        String specialDept = tempDepartureCity + " " + m.getDepartureTime();
                        String specialArr = tempArrivalCity + " " + m.getArrivalTime(); 
                        String specialPrice = "SGD " + m.getPrice() + " / passenger";
                        
                            im.add(m.getFlightSchedule().getFlightNumber()); // 0
                            im.add(tempDepartureCity); // 1
                            im.add(m.getDepartureTime()); // 2
                            im.add(tempArrivalCity); // 3
                            im.add(m.getArrivalTime()); // 4
                            im.add(m.getPrice()); // 5
                            im.add(numberPassengers); // 6
                            im.add(m.getId()); // 7
                        im.add(specialDept); // 8
                        im.add(specialArr); // 9
                        im.add(specialPrice); // 10
                            
                        linkedArrivals.add(im);
                    }
                }
            }
        }
        
        // Important: arrival of 1st flight must equal to departure of 2nd flight
        
        for(Object flightOne: linkedDepartures) {
            Vector one = (Vector)flightOne;
            for(Object flightTwo: linkedArrivals) {
                Vector two = (Vector)flightTwo;
                // Remove flight if not equal
                String firstFlightArrival = one.get(3).toString();
                String secondFlightDeparture = two.get(1).toString();
                if(firstFlightArrival.equals(secondFlightDeparture)) {
                    linkedArrivalsFull.add(two);
                    linkedDeparturesFull.add(one);
                }
            }
        }
        
        // Now there will be 2 ArrayLists. Find matching one stop flights
            System.out.println("LinkedDeptFull size: " + linkedDeparturesFull.size());   
            System.out.println("LinkedArrFull size: " + linkedArrivalsFull.size());                  
        for(Object a: linkedDeparturesFull) {
            Vector first = (Vector)a;          
            for(Object b: linkedArrivalsFull) {
                Vector second = (Vector)b;
                // Compare timings, must be >= 30 mins difference
                // Get timings from first(arrTime) and second(deptTime) first
                String firstArrTime = first.get(4).toString();
                firstArrTime = firstArrTime.substring(firstArrTime.length()-5);
                int timeArrFirst = Integer.parseInt(firstArrTime.substring(0, 2)) * 60;
                int timeArrSecond = Integer.parseInt(firstArrTime.substring(3));
                int timeArrTotal = timeArrFirst + timeArrSecond;

                String secondDeptTime = second.get(2).toString();
                secondDeptTime = secondDeptTime.substring(secondDeptTime.length()-5);  
                int timeDeptFirst = Integer.parseInt(secondDeptTime.substring(0, 2)) * 60;
                int timeDeptSecond = Integer.parseInt(secondDeptTime.substring(3));
                int timeDeptTotal = timeDeptFirst + timeDeptSecond;
                
                int diff = timeDeptTotal - timeArrTotal;             
                
                // Compare then if success, add to oneStopFlights ArrayList
                if(diff >= 30) {
                    // If incoming arrial = outgoing departure
                    if(first.get(3).toString().equals(second.get(1).toString())) {
                    Vector link = new Vector();
                    link.add(first);
                    link.add(second);
                    oneStopFlights.add(link);
                    System.out.println("SIZEEE: " + oneStopFlights.size());
                    }
                }
            }
        }
        // Return the ArrayList<Vector>  
        return oneStopFlights;
    }
    
    // Make payment
    
    @Override
    public void makePayment(Long bookingNum, String cardHolderName, String cardType, String cardNumber) {
        System.out.println("FlightBookingSystemManager: makePayment()");
        bookingEntity = em.find(BookingEntity.class, bookingNum);
        paymentEntity = new PaymentEntity();
        paymentEntity.create(cardType, cardNumber, cardHolderName);
        bookingEntity.setBookingPayment(paymentEntity);
    }
    
    // Send request list to web
    @Override
    public ArrayList<ArrayList<String>> getRequests(String userName) {
        System.out.println("FlightBookingSystemManager: getRequests()");
        userEntity = em.find(UserEntity.class, userName);
        Collection<RequestEntity> tempList = userEntity.getRequests();
        ArrayList<ArrayList<String>> reqList = new ArrayList<ArrayList<String>>();
        
        for(Object o: tempList) {
            RequestEntity r = (RequestEntity)o;
            ArrayList<String> temp = new ArrayList<String>();
            
            temp.add(r.getContent()); // 0
            temp.add(r.getStatus()); // 1
            temp.add(r.getComment()); // 2
            reqList.add(temp);
            
        }
        return reqList;
    }
      
    //  MD5 encryption helper
    /**
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
        @Override
    public String encryptData(String data) throws NoSuchAlgorithmException {
    // Convert password that was input into MD5 for verification
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes());
            
            byte byteData[] = md.digest();
            
            //convert the byte to hex format method 1
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            
            return sb.toString();
    }
    
    
    // *** FightBookingSystemManager methods ***
    @Remove
    @Override
    public void remove() {
        System.out.println("FlightBookingSystemManager:remove()");
    }
   
}
