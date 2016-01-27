<%-- 
    Document   : ViewBookings
    Created on : Mar 11, 2015, 9:06:33 PM
    Author     : User
--%>

<%@page import="java.util.Vector"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FBS | View Bookings</title>
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
        <h2 style="text-align: center; margin-top: 40px;">All Bookings</h2>
        <%
            ArrayList<Vector> allBookings = (ArrayList<Vector>)request.getAttribute("allBookings");
            int counter = 1;
            for(Object o: allBookings) {
            Vector im = (Vector)o;
            
            %> 
            <h3 style="text-align: center">* Booking # <%=counter%> *</h3>
            <div class="form-signin">
            <p>Booking No.: <%= im.get(0) %></p>
            <p>Booking Time: <%= im.get(1) %></p>
            <p>Total Amount: <%= im.get(2) %></p>
            <p>Payment Status: <%= im.get(3) %></p>
            </br>
            
            <%
            
            ArrayList<ArrayList<String>> scheds = (ArrayList<ArrayList<String>>)im.get(4);
            int scheduleCounter = 1;
            for(Object a: scheds) {
                ArrayList<String> z = (ArrayList)a;
            %>    
            
            <h4>Flight # <%=scheduleCounter%></h4>
            <p>Flight No.: <%= z.get(0) %></p>
            <p>Departure City <%= z.get(1) %></p>
            <p>Arrival City: <%= z.get(2) %></p>
            <p>Departure Time: <%= z.get(3) %></p>
            <p>Arrival Time: <%= z.get(4) %></p>
            <p>Aircraft Type: <%= z.get(5) %></p>
            </br>
            <%  
            scheduleCounter++;
            }
            %>
            
            <%
            ArrayList<ArrayList<String>> pass = (ArrayList<ArrayList<String>>)im.get(5);
            int passengerCounter = 1;
            for(Object b: pass) {
                ArrayList<String> l = (ArrayList)b;
            %>    
            
            <h4>Passenger # <%=passengerCounter%></h4>
            <p>Passport No.: <%= l.get(0) %></p>
            <p>Name: <%= l.get(1) %></p>
            <p>Gender: <%= l.get(2) %></p>
            <p>Date of Birth: <%= l.get(3) %></p>
            </br>
            <%  
            passengerCounter++;
            }
            %> 
            </div>      
            <%
            counter++;
            }
            %>
    </body>
</html>
