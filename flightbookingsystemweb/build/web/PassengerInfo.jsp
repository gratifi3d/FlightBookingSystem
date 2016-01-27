<%-- 
    Document   : PassengerInfo
    Created on : Mar 24, 2015, 2:38:04 PM
    Author     : User
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FBS | Passenger Entry</title>
        <link href="../js/libs/twitter-bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">
        <link href="../js/libs/twitter-bootstrap/css/index.css" rel="stylesheet" media="screen">
    </head>
    <body>
        <nav class="nav">
            <button type="button" class="btn btn-default" onClick="location.href='updatePassword'">Update Password</button>
            <button type="button" class="btn btn-default" onClick="location.href='updateProfile'">Update Profile</button>
            <button type="button" class="btn btn-default" onClick="location.href='searchFlights'">Search Flights</button>
            <button type="button" class="btn btn-default" onClick="location.href='payment'">Payment</button>
            <button type="button" class="btn btn-default" onClick="location.href='viewBookings'">View Bookings</button>
            <button type="button" class="btn btn-default" onClick="location.href='viewRequests'">View Requests</button>
            <button type="button" class="btn btn-default" onClick="location.href='logout'">Logout</button>
        </nav>
        
                <form class="form-signin" action="confirmedBooking" method="POST">
            <h2 style="text-align: center;">Passenger Information</h2>
        
            <%
            for(int i=0; i<Integer.parseInt(request.getParameter("numberPassengers")); i++) {
                // Incremental variables for each form
            String name = "name" + Integer.toString(i);
            String passportNum = "passportNum" + Integer.toString(i);
            String gender = "gender" + Integer.toString(i);
            String dateOfBirth = "dateOfBirth" + Integer.toString(i);
        %>
        <div>
            <h3 class="form-signin-heading text-center">Passenger <%= i+1 %></h3>
            <label for="inputName" class="sr-only">Name</label>
            <input type="text" name="<%=name%>" id="inputName" class="form-control" placeholder="Name" required autofocus>
            <label for="inputPassportNum" class="sr-only">Passport No. </label>
            <input type="text" name="<%=passportNum%>" id="inputPassportNum" class="form-control" placeholder="Passport No." required>
            <label for="inputGender" class="sr-only">Gender</label>
            <input type="text" name="<%=gender%>" id="inputGender" class="form-control" placeholder="Gender" required>
            <label for="inputDateOfBirth" class="sr-only">Date of Birth</label>
            <input type="text" name="<%=dateOfBirth%>" id="inputDateOfBirth" class="form-control" placeholder="Date of Birth (DD/MM/YYYY)" required>
            <br/>
        </div>
        <%
            }
        %>
        <input type="hidden" name="numberPassengers" value="<%= request.getParameter("numberPassengers") %>" />
        <input type="hidden" name="flights" value="<%= request.getParameter("flights") %>" />
        <button class="btn btn-primary btn-block" type="submit">Confirm Booking</button>
        </form>
    </body>
</html>
