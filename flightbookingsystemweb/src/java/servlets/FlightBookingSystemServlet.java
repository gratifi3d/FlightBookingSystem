package servlets;

import ejb.FlightBookingSystemManagerRemote;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Vector;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Farhan
 */
public class FlightBookingSystemServlet extends HttpServlet {
    @EJB
    private FlightBookingSystemManagerRemote flightBookingSystemManager;
    private ArrayList data = null;
    private String loginUserName = "";
    private String bookingFlightSchedule = "";
    
    public void init() {
        System.out.println("FlightBookingSystemServlet: init()");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        System.out.println("FlightBookingSystemServlet: processRequest()");
        try (PrintWriter out = response.getWriter()) {
                       
            RequestDispatcher dispatcher;
            ServletContext servletContext = getServletContext();
            
            String page = request.getPathInfo();
            page = page.substring(1);
            
            // Index page
            if("index".equals(page)) {
                System.out.println("FlightBookingSystemServlet: index.jsp loaded");
                if(isLoggedIn()) {
                    request.setAttribute("userInfo", getUserData(loginUserName));
                    String name = loginUserName;
                    request.setAttribute("userName", name);
                    page = "menu";
                }
            }
            // To process login
            else if("loginUser".equals(page)) {
                System.out.println("FlightBookingSystemServlet: User login executed");
                if(loginUser(request)) {
                    request.setAttribute("userInfo", getUserData(loginUserName));
                    page = "menu";
                }
                else {
                    System.out.println("FlightBookingSystemServlet: User not logged in");
                    page = "index";
                }
            }
            // Logged in page
            else if("menu".equals(page)) {
                System.out.println("FlightBookingSystemServlet: Menu.jsp loaded");
                if(isLoggedIn()) {
                    String name = loginUserName;
                    request.setAttribute("userName", name);
                }
                else {
                    System.out.println("FlightBookingSystemServlet: User not logged in");
                    page = "index";
                }
            }
            // Process logout
            else if("logout".equals(page)) {
                System.out.println("FlightBookingSystemServlet: Logout executed");
                if(isLoggedIn()) {
                    System.out.println("FlightBookingSystemServlet: Logout successful");
                    loginUserName = "";
                    page = "index";
                }
                else
                    System.out.println("FlightBookingSystemServlet: Logout unsuccessful");
            }
            // Registration page
            else if("register".equals(page)) {
                System.out.println("FlightBookingSystemServlet: Register.jsp loaded");
            }
            // After successful registration page
            else if("registerStatus".equals(page)) {
                System.out.println("FlightBookingSystemServlet: User registration executed");
                // Invoke method to register user
                if(registerUser(request)) {
                    page = "registerStatus";
                }
                else {
                    page = "register";
                }
            }
            // Update password page
            else if("updatePassword".equals(page)) {
                if(isLoggedIn()) {
                System.out.println("FlightBookingSystemServlet: UpdatePassword.jsp loaded");
                }
                else {
                    System.out.println("FlightBookingSystemServlet: User not logged in");
                    page = "index";
                }
            }
            
            // To process update of password
            else if("changePassword".equals(page)) {
                System.out.println("FlightBookingSystemServlet: Password change executed");
                if(isLoggedIn()) {
                    if(updatePassword(request)) {
                        System.out.println("FlightBookingSystemServlet: Password update successful");
                        page = "changePassword";
                    }
                    else {
                        System.out.println("FlightBookingSystemServlet: Password failed to update");
                        page = "updatePassword";
                    }
                }
                else {
                    System.out.println("FlightBookingSystemServlet: User not logged in");
                    page = "index";
                }
            }
            // Update profile page
            else if("updateProfile".equals(page)) {
                if(isLoggedIn()) {
                System.out.println("FlightBookingSystemServlet: UpdateProfile.jsp loaded");
                }
                else {
                    System.out.println("FlightBookingSystemServlet: User not logged in");
                    page = "index";
                }
            }
            // To process profile update
            else if("changeProfile".equals(page)) {
                if(isLoggedIn()) {
                    updateProfile(request);
                    System.out.println("FlightBookingSystemServlet: Profile update successful");
                    page = "changeProfile";
                }
                else {
                    System.out.println("FlightBookingSystemServlet: User not logged in");
                    page = "index";
                }
            }
            // Search flights page
            else if("searchFlights".equals(page)) {
                if(isLoggedIn()) {
                    System.out.println("FlightBookingSystemServlet: SearchFlights.jsp loaded");
                }
                else {
                    System.out.println("FlightBookingSystemServlet: User not logged in");
                    page = "index";
                }
            }
            // To process flight search
            else if("requestSearchFlights".equals(page)) {
                if(isLoggedIn()) {
                    System.out.println("FlightBookingSystemServlet: RequestSearchFlights.jsp loaded"); 
                    System.out.println("FlightBookingSystemServlet: Flight search executed"); 
                    // Set ArrayList<Vector> as an attribute
                    request.setAttribute("numberPassengers", getNumPassengers(request));
                    request.setAttribute("flights", getRequestedFlights(request));
                    request.setAttribute("oneStops", getOneStops(request));
                }
                else {
                    System.out.println("FlightBookingSystemServlet: User not logged in");
                    page = "index";
                }
            }
            
            // Passenger info adding page
            else if("passengerInfo".equals(page)) {
                if(isLoggedIn()) {
                    System.out.println("FlightBookingSystemServlet: PassengerInfo.jsp loaded"); 
                    request.setAttribute("numberPassengers", getNumPassengers(request));
                    bookingFlightSchedule = getSelectedSchedule(request);
                }
                else {
                    System.out.println("FlightBookingSystemServlet: User not logged in");
                    page = "index";
                }
            }
            
            else if("confirmedBooking".equals(page)) {
                if(isLoggedIn()) {
                    System.out.println("FlightBookingSystemServlet: ConfirmedBooking.jsp loaded"); 
                    createBooking(request);
                }
                else {
                    System.out.println("FlightBookingSystemServlet: User not logged in");
                    page = "index"; 
                }
            }
            
            else if("payment".equals(page)) {
                if(isLoggedIn()) {
                    System.out.println("FlightBookingSystemServlet: Payment.jsp loaded"); 
                    if(getBookings() != null) {
                        request.setAttribute("userBookings", getBookings());
                    }
                }
                else {
                    System.out.println("FlightBookingSystemServlet: User not logged in");
                    page = "index";  
                }
            }
            
            else if("viewRequests".equals(page)) {
                if(isLoggedIn()) {
                    System.out.println("FlightBookingSystemServlet: ViewRequests.jsp loaded"); 
                    if(getRequests() != null)
                    request.setAttribute("requests", getRequests());
                }
                else {
                    System.out.println("FlightBookingSystemServlet: User not logged in");
                    page = "index";   
                }
            }
            
            else if("makePayment".equals(page)) {
                if(isLoggedIn()) {
                    System.out.println("FlightBookingSystemServlet: MakePayment.jsp loaded"); 
                    request.setAttribute("bookingNum", request.getParameter("bookingNum"));
                }
                else {
                    System.out.println("FlightBookingSystemServlet: User not logged in");
                    page = "index";  
                }
            }
            
            else if("confirmedPayment".equals(page)) {
                if(isLoggedIn()) {
                    System.out.println("FlightBookingSystemServlet: ConfirmedPayment.jsp loaded"); 
                    Long bookingNum = Long.parseLong(request.getParameter("bookingNum"));
                    String cardHolderName = request.getParameter("cardHolderName");
                    String cardType = request.getParameter("cardType");
                    String cardNumber = request.getParameter("cardNumber");
                    payBooking(bookingNum, cardHolderName, cardType, cardNumber);
                }
                else {
                    System.out.println("FlightBookingSystemServlet: User not logged in");
                    page = "index";  
                }
            }            
            
            else if("viewBookings".equals(page)) {
                if(isLoggedIn()) {
                    System.out.println("FlightBookingSystemServlet: ViewBookings.jsp loaded"); 
                    if(showAllWebBookings() != null)
                    request.setAttribute("allBookings", showAllWebBookings()); // ArrayList<Vector>
                }
                else {
                    System.out.println("FlightBookingSystemServlet: User not logged in");
                    page = "index"; 
                }
            }
            

                    
            // Error page
            else {
                page = "error";
                System.out.println("FlightBookingSystemServlet: Error.jsp loaded");
            }
            
            // Dispatcher
            dispatcher = servletContext.getNamedDispatcher(page);
            if(dispatcher == null) {
                dispatcher = servletContext.getNamedDispatcher("Error");
            }
            dispatcher.forward(request, response);
            
            
            
        } catch(Exception e) {
            log("Exception in FlightBookingSystemServlet.processRequest()");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("FlightBookingSystemServlet: doGet()");
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("FlightBookingSystemServlet: doPost()");
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    @Override
    public void destroy() {
        System.out.println("FlightBookingSystemServlet: destroy()");
    }
    
    // Log a user in
    private boolean loginUser(HttpServletRequest request) {
        System.out.println("FlightBookingSystemServlet: loginUser()");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password"); 
        if(flightBookingSystemManager.verifyLogin(userName, password)) {
            loginUserName = userName;
            return true;
        }
        return false;
    }
    
    // Check if user is already logged in
    private boolean isLoggedIn() {
        System.out.println("FlightBookingSystemServlet: isLoggedIn()");
        if(loginUserName.equals(""))
            return false;
        return true;
    }
    
    
    // Get the user data (userName, password, contact, email)
    private ArrayList<String> getUserData(String userName) {
        System.out.println("FlightBookingSystemServlet: getUserData()");
        ArrayList<String> info = flightBookingSystemManager.getUserInfo(userName);
        return info;
    }
    
    // Register a user
    private boolean registerUser(HttpServletRequest request) throws NoSuchAlgorithmException {
        System.out.println("FlightBookingSystemServlet: registerUser()");
        ArrayList list = new ArrayList();
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String contactNumber = request.getParameter("contactNumber");
        String email = request.getParameter("email");
        // If password matches
        if(confirmPassword.equals(password)) {
            // Verify if user exists before
            if(!flightBookingSystemManager.verifyUser(userName)) {     
                flightBookingSystemManager.addUser(userName, password, contactNumber, email);
                System.out.println("FlightBookingSystemServlet: User registration successful");
                return true;
            }
        }
        System.out.println("FlightBookingSystemServlet: User registration unsuccessful");
        return false;
    }
    
    // Update a user profile
    private void updateProfile(HttpServletRequest request) {
        System.out.println("FlightBookingSystemServlet: updateProfile()");
        String newEmail = request.getParameter("newEmail");
        String newContactNumber = request.getParameter("newContactNumber");
        flightBookingSystemManager.updateUserProfile(loginUserName, newEmail, newContactNumber);
    }
    
    private boolean updatePassword(HttpServletRequest request) throws NoSuchAlgorithmException {
        System.out.println("FlightBookingSystemServlet: updatePassword()");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmNewPassword = request.getParameter("confirmNewPassword");
        
        // Encrypt oldPassword to MD5
        oldPassword = flightBookingSystemManager.encryptData(oldPassword);
        
        // Check if old password match and new passwords match
        if(oldPassword.equals(getUserData(loginUserName).get(1))) {
            if(newPassword.equals(confirmNewPassword)) {
                flightBookingSystemManager.updateUserPassword(loginUserName, newPassword);
                return true;
            }
        }
        return false;
    }
    
    private ArrayList<Vector> getRequestedFlights(HttpServletRequest request) {
        System.out.println("FlightBookingSystemServlet: getRequestedFlights()");
        String departureDate = request.getParameter("departureDate");
        String departureCity = request.getParameter("departureCity");
        String arrivalCity = request.getParameter("arrivalCity");
        String numberPassengers = request.getParameter("numberPassengers");
        
        ArrayList<Vector> requestedFlights = flightBookingSystemManager.listMatchedFlights(departureDate, departureCity, arrivalCity, numberPassengers);

        return requestedFlights;
    }

    private ArrayList<Vector> getOneStops(HttpServletRequest request) {
        System.out.println("FlightBookingSystemServlet: getOneStops()");
        String departureDate = request.getParameter("departureDate");
        String departureCity = request.getParameter("departureCity");
        String arrivalCity = request.getParameter("arrivalCity");
        String numberPassengers = request.getParameter("numberPassengers");
        
        ArrayList<Vector> oneStops = flightBookingSystemManager.listOneStops(departureDate, departureCity, arrivalCity, numberPassengers);
        
        return oneStops;
    }    
    
    // Return no. of passengers from SearchFlights.jsp
    private int getNumPassengers(HttpServletRequest request) {
        System.out.println("FlightBookingSystemServlet: getNumPassengers()");
        return Integer.parseInt(request.getParameter("numberPassengers"));
    }
    
    private String getSelectedSchedule(HttpServletRequest request) {
        System.out.println("FlightBookingSystemServlet: getSelectedSchedule()");
        return request.getParameter("flightData");
    }
    
    // Create a booking
    private void createBooking(HttpServletRequest request) {
        System.out.println("FlightBookingSystemServlet: createBooking()");
        int numPassengers = Integer.parseInt(request.getParameter("numberPassengers"));
        String userName = loginUserName;
        
        // Put all the data in the forms into the proper arraylist
        ArrayList<ArrayList<String>> allPassengerInfo = new ArrayList<ArrayList<String>>();
        for(int i=0; i<numPassengers; i++) {
            ArrayList<String> passengerInfo = new ArrayList<String>();
            String name = request.getParameter("name" + Integer.toString(i));
            String passportNum = request.getParameter("passportNum" + Integer.toString(i));
            String gender = request.getParameter("gender" + Integer.toString(i));
            String dateOfBirth = request.getParameter("dateOfBirth" + Integer.toString(i));
            passengerInfo.add(name);
            passengerInfo.add(passportNum);
            passengerInfo.add(gender);
            passengerInfo.add(dateOfBirth);
            allPassengerInfo.add(passengerInfo);
        }
        flightBookingSystemManager.addBooking(allPassengerInfo, userName, bookingFlightSchedule);
                
    }
    
    private ArrayList<ArrayList<String>> getBookings() { 
        System.out.println("FlightBookingSystemServlet: getBookings()");
        ArrayList<ArrayList<String>> userBookings = new ArrayList<ArrayList<String>>();
        userBookings = flightBookingSystemManager.bookingsMade(loginUserName);
        return userBookings;
    }
    
    private void payBooking(Long bookingNum, String cardHolderName, String cardType, String cardNumber) {
        System.out.println("FlightBookingSystemServlet: payBooking()");
        flightBookingSystemManager.makePayment(bookingNum, cardHolderName, cardType, cardNumber);
    }
    
    private ArrayList<Vector> showAllWebBookings() {
        System.out.println("FlightBookingSystemServlet: showAllWebBookings()");
        ArrayList<Vector> showAllWebBookings = new ArrayList<Vector>();
        showAllWebBookings = flightBookingSystemManager.showAllWebBookings(loginUserName);
        return showAllWebBookings;
    }
    
    private ArrayList<ArrayList<String>> getRequests() {
        System.out.println("FlightBookingSystemServlet: getRequests()");
        return flightBookingSystemManager.getRequests(loginUserName);
    }
    
    
} // End of code
